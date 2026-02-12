package g6.dynamodb.Style;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g6.dynamodb.DAO.AulaDAO;
import g6.dynamodb.DAO.ReservaDAO;
import g6.dynamodb.Model.Aula;
import g6.dynamodb.Model.Reserva;
import g6.dynamodb.Model.Usuario;
import g6.dynamodb.Service.ReservaService;
import g6.dynamodb.Service.UsuarioService;
import g6.dynamodb.Util.AWSClient;

/**
 * Menu interactivo principal de la aplicacion de reservas de aulas.
 * 
 * Proporciona interfaz de consola con logging SLF4J para operaciones CRUD
 * basicas
 * (crear/buscar/borrar) sobre entidades Usuario, Aula y Reserva usando DAOs y
 * servicio de reservas. Soporta creacion automatica de tablas DynamoDB.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class Menu {
    private final AWSClient aws;
    private final Scanner sc;
    private final UsuarioService usuarioService;
    private final AulaDAO aulaDAO;
    private final ReservaDAO reservaDAO;
    private final ReservaService reservaService;
    private static final Logger log = LoggerFactory.getLogger(Menu.class);

    /**
     * Constructor por defecto con AWS local.
     * Inicializa cliente DynamoDB local, scanner y todos DAOs/Services.
     * 
     * @throws FileNotFoundException si falla carga credenciales
     * @throws IOException           errores IO configuracion AWS
     */
    public Menu() throws FileNotFoundException, IOException {
        this.aws = new AWSClient(true);
        this.sc = new Scanner(System.in);
        this.usuarioService = new UsuarioService(aws);
        this.aulaDAO = new AulaDAO(aws.getDynamoDB());
        this.reservaDAO = new ReservaDAO(aws.getDynamoDB());
        this.reservaService = new ReservaService(aws);
    }

    /**
     * Constructor principal inyectando cliente AWS.
     * Permite testing con mocks o AWS cloud.
     * 
     * @param aws cliente AWS DynamoDB configurado
     */
    public Menu(AWSClient aws) {
        this.aws = aws;
        this.sc = new Scanner(System.in);

        this.usuarioService = new UsuarioService(aws);
        this.aulaDAO = new AulaDAO(aws.getDynamoDB());
        this.reservaDAO = new ReservaDAO(aws.getDynamoDB());
        this.reservaService = new ReservaService(aws);
    }

    /**
     * Inicia bucle principal menu interactivo.
     * Muestra menu, procesa opciones, delega submenus. Sale con opcion 0.
     */
    public void start() {
        log.info("=== SISTEMA DE RESERVAS DE AULAS ===");
        log.info("Tablas cargadas: {}", aws.listTables());
        int opcion = 1;
        while (opcion != 0) {
            try {
                mostrarMenuPrincipal();
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> gestionarUsuarios();
                    case 2 -> gestionarAulas();
                    case 3 -> gestionarReservas();
                    case 4 -> aws.generateTable(Usuario.class);
                    case 5 -> aws.generateTable(Aula.class);
                    case 6 -> aws.generateTable(Reserva.class);
                    case 7 -> inicializar();
                    case 0 -> {
                        log.info("Hasta luego!");
                        return;
                    }
                    default -> log.warn("Opcion invalida");
                }
                pausa();
            } catch (NumberFormatException e) {
                log.warn("No has introducido un numero");
            }
        }
        sc.close();
    }

    private void inicializar() {
        if (aws.existeTabla(Usuario.class))
            aws.deleteTable(Usuario.class);
        aws.generateTable(Usuario.class);

        Usuario u1 = new Usuario("Mateo", "Ayarra123");
        Usuario u2 = new Usuario("Mario", "Garcia123");
        Usuario u3 = new Usuario("Samuel", "Cobreros123");
        Usuario u4 = new Usuario("Zacaria", "Daghri123");

        usuarioService.altaUsuario(u1);
        usuarioService.altaUsuario(u2);
        usuarioService.altaUsuario(u3);
        usuarioService.altaUsuario(u4);

        if (aws.existeTabla(Aula.class))
            aws.deleteTable(Aula.class);
        aws.generateTable(Aula.class);

        if (aws.existeTabla(Reserva.class))
            aws.deleteTable(Reserva.class);
        aws.generateTable(Reserva.class);
    }

    /**
     * Muestra menu principal formateado.
     * Opciones 1-3: CRUD entidades, 4-6: crear tablas, 0: salir.
     */
    private void mostrarMenuPrincipal() {
        log.info("----------------------------------------");
        log.info("1. Gestionar USUARIOS");
        log.info("2. Gestionar AULAS");
        log.info("3. Gestionar RESERVAS");
        log.info("0. SALIR");
        log.info("Selecciona opcion: ");
    }

    /**
     * Gestiona submenu usuarios (crear/buscar/borrar).
     * Compatible con Usuario simplificado (username/password).
     */
    private void gestionarUsuarios() {
        log.info("=== USUARIOS ===");
        log.info("1. Crear nuevo (username/password)");
        log.info("2. Buscar por username");
        log.info("3. Borrar por username");
        log.info("0. Volver");
        log.info("Opcion: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> crearUsuario();
            case 2 -> buscarUsuario();
            case 3 -> borrarUsuario();
            case 4 -> aws.scanTable(Usuario.class);
            default -> log.warn("Opcion invalida");
        }
        pausa();
    }

    /**
     * Crea usuario interactivo (solo username/password reales).
     * Valida campos vacios implicitamente via DAO.
     */
    private void crearUsuario() {
        Usuario u = new Usuario();
        log.info("Username: ");
        u.setUsername(sc.nextLine());
        log.info("Password: ");
        u.setPassword(sc.nextLine());
        usuarioService.altaUsuario(u);
        log.info("Usuario creado: {}", u);
    }

    /**
     * Busca usuario por username mostrando datos.
     * Display password como "nombre" por compatibilidad modelo.
     */
    private void buscarUsuario() {
        log.info("Nombre de usuario: ");
        String username = sc.nextLine();
        Usuario u = usuarioService.buscarUsuario(username);
        if (u != null) {
            log.info("{} {} (ID: {})", u.getPasswordHash(), u.getUsername());
        } else {
            log.warn("Usuario no encontrado");
        }
    }

    /**
     * Borra usuario reconstruyendo por username.
     * Usa delete() DAO con instancia minima.
     */
    private void borrarUsuario() {
        log.info("Username a borrar: ");
        String id = sc.nextLine();
        Usuario u = new Usuario();
        u.setUsername(id);
        usuarioService.deleteUsuario(u.getUsername());
        log.info("Usuario borrado");
    }

    /**
     * Gestiona submenu aulas (crear/buscar/borrar).
     * Captura todos campos Aula: ID/nombre/capacidad/edificio.
     */
    private void gestionarAulas() {
        log.info("=== AULAS ===");
        log.info("1. Crear nuevo");
        log.info("2. Buscar por ID");
        log.info("3. Borrar por ID");
        log.info("0. Volver");
        log.info("Opcion: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> crearAula();
            case 2 -> buscarAula();
            case 3 -> borrarAula();
            default -> log.warn("Opcion invalida");
        }
        pausa();
    }

    /**
     * Crea aula interactiva completa.
     * Solicita ID/nombre/capacidad/edificio via prompts.
     */
    private void crearAula() {
        Aula a = new Aula();
        log.info("ID: ");
        a.setId(sc.nextLine());
        log.info("Nombre/Codigo: ");
        a.setNombre(sc.nextLine());
        log.info("Capacidad: ");
        a.setCapacidad(sc.nextInt());
        sc.nextLine();
        log.info("Edificio: ");
        a.setEdificio(sc.nextLine());
        aulaDAO.save(a);
        log.info("Aula creada: {}", a);
    }

    /**
     * Busca aula por ID con display legible.
     * Formato: "Nombre - Cap: X - Edificio (ID)".
     */
    private void buscarAula() {
        log.info("ID aula: ");
        String id = sc.nextLine();
        Aula a = aulaDAO.findById(id);
        if (a != null) {
            log.info("{} - Cap: {} - {} (ID: {})",
                    a.getNombre(), a.getCapacidad(), a.getEdificio(), a.getId());
        } else {
            log.warn("Aula no encontrada");
        }
    }

    /**
     * Borra aula por ID.
     * Reconstruye instancia minima para delete().
     */
    private void borrarAula() {
        log.info("ID a borrar: ");
        String id = sc.nextLine();
        Aula a = new Aula();
        a.setId(id);
        aulaDAO.delete(a);
        log.info("Aula borrada");
    }

    /**
     * Gestiona submenu de reservas.
     *
     * Opciones: crear reserva, buscar por ID y borrar por ID.
     * Usa ReservaService para validaciones automaticas.
     */
    private void gestionarReservas() {
        log.info("=== RESERVAS ===");
        log.info("1. Crear nuevo");
        log.info("2. Buscar por ID");
        log.info("3. Borrar por ID");
        log.info("0. Volver");
        log.info("Opcion: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> crearReserva();
            case 2 -> buscarReserva();
            case 3 -> borrarReserva();
            default -> log.warn("Opcion invalida");
        }
        pausa();
    }

    /**
     * Crea reserva completa usando ReservaService.
     *
     * Genera UUID automatico, usuario fijo USER1.
     * Valida solapamientos y fechas automaticamente.
     * Usa formato ISO-8601 y aula por ID.
     */
    private void crearReserva() {
        Reserva r = new Reserva();
        r.setId(UUID.randomUUID().toString());
        log.info("Nueva reserva ID: {}", r.getId());
        log.info("N personas: ");
        r.setnPersonas(sc.nextInt());
        sc.nextLine();
        log.info("Fecha inicio (YYYY-MM-DDTHH:mm:ss): ");
        r.setFechaInicio(sc.nextLine());
        log.info("Fecha fin (YYYY-MM-DDTHH:mm:ss): ");
        r.setFechaFin(sc.nextLine());

        Usuario u = new Usuario();
        u.setUsername("USER1");
        r.setUsuario(u);

        Aula a = new Aula();
        log.info("ID Aula: ");
        a.setId(sc.nextLine());
        r.setAula(a);

        Reserva resultado = reservaService.crearReserva(r);
        log.info("Reserva creada: {}", resultado);
    }

    /**
     * Busca reserva por ID con detalles compactos.
     * Muestra fechas/personas/aula/estado.
     */
    private void buscarReserva() {
        log.info("ID reserva: ");
        String id = sc.nextLine();
        Reserva r = reservaDAO.findById(id);
        if (r != null) {
            log.info("{} -> {} | {} pers | {} | {} (ID: {})",
                    r.getFechaInicio(), r.getFechaFin(), r.getnPersonas(),
                    r.getAula().getId(), r.getEstado(), r.getId());
        } else {
            log.warn("Reserva no encontrada");
        }
    }

    /**
     * Borra reserva por ID.
     * Usa delete() directo DAO.
     */
    private void borrarReserva() {
        log.info("ID a borrar: ");
        String id = sc.nextLine();
        Reserva r = new Reserva();
        r.setId(id);
        reservaDAO.delete(r);
        log.info("Reserva borrada");
    }

    /**
     * Pausa esperando Enter usuario.
     * Mejora UX entre operaciones consola.
     */
    private void pausa() {
        log.info("Pulsa Enter para continuar...");
        sc.nextLine();
    }
}
