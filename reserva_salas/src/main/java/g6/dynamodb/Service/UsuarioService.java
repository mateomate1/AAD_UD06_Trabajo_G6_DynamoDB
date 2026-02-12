package g6.dynamodb.Service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fx.App.ui.util.HashUtil;
import g6.dynamodb.DAO.UsuarioDAO;
import g6.dynamodb.Model.Usuario;
import g6.dynamodb.Util.AWSClient;

/**
 * Servicio de negocio para gestion de usuarios.
 * 
 * Maneja creacion con username UUID unico automatico y
 * actualizacion validada por existencia. Coordina UsuarioDAO.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class UsuarioService {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioService.class);
    private final AWSClient cliente;

    /**
     * Constructor con cliente AWS inyectado.
     * 
     * @param cliente AWSClient configurado
     */
    public UsuarioService(AWSClient cliente) {
        this.cliente = cliente;
    }

    /**
     * Crea usuario con username UUID unico.
     * 
     * Genera IDs hasta evitar colisiones, asigna a username.
     * 
     * @param u usuario con password (username ignorado)
     * @return usuario persistido con ID unico
     */
    public boolean altaUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(u.getUsername()) == null) {
            dao.save(u);
            return true;
        }
        return false;
    }

    public boolean altaUsuario(String usuario, String contrasena) {
        Usuario u = new Usuario(usuario, contrasena);
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(usuario) == null) {
            dao.save(u);
            return true;
        }
        return false;
    }

    public boolean iniciarSesion(String usuario, String contrasena) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        Usuario u = dao.findById(usuario);
        return u != null && u.getPassword().equals(contrasena);
    }

    /**
     * Actualiza usuario existente.
     * 
     * Valida existencia previa por username.
     * 
     * @param u usuario completo con username valido
     * @return usuario actualizado o null si no existia
     */
    public Usuario actualizarUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(u.getUsername()) == null)
            return null;
        dao.save(u);
        return u;
    }

    /**
     * Valida login de usuario por username y password para poder acceder a la App.
     * Comapra hash de password ingresado con almacenado.
     * 
     * @param username identificador login
     * @param password credencial en texto plano a validar (se convierte en hash
     *                 para comparar)
     * @return true si login exitoso, false si usuario no existe o password no
     *         coincide
     */
    public boolean loginUsuario(String username, String password) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        Usuario u = dao.findById(username);

        try {
            // Codificamosn el password, para comparar hasesh
            String passwordHash = HashUtil.encode(password);

            // Si el usuario existe y el hash de la contraseña coincide, el login es exitoso
            if (u != null && u.getPasswordHash().equals(passwordHash)) {
                return true;
            }

        } catch (Exception e) {
            LOG.error("Error al validar el login del usuario: {}", e.getMessage());
            return false;
        }
        return false;
    }

    public Usuario buscarUsuario(String username) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        return dao.findById(username);
    }

    public boolean deleteUsuario(String username) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        Usuario u = dao.findById(username);
        dao.delete(u);
        return dao.findById(username) == null;
    }

    public boolean deleteUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        String username = u.getUsername();
        dao.delete(u);
        return dao.findById(username) == null;
    }
}
