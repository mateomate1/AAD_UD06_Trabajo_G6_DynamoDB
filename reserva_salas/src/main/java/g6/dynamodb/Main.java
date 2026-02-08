package g6.dynamodb;

import g6.dynamodb.Model.Aula;
import g6.dynamodb.Model.Reserva;
import g6.dynamodb.Model.Usuario;
import g6.dynamodb.Style.Menu;
import g6.dynamodb.Util.AWSClient;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal de entrada de la aplicacion de reservas de aulas con DynamoDB.
 * 
 * Inicializa el cliente AWS DynamoDB en modo Local, crea las tablas necesarias
 * (Usuarios, Aulas, Reservas) y lanza el menu interactivo principal.
 * 
 * Maneja excepciones de IO (credenciales/archivos) con logging profesional.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class Main {
    
    /** Logger SLF4J estatico para trazas de la clase principal */
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    /**
     * Punto de entrada principal de la aplicacion.
     * 
     * Flujo de ejecucion:
     * <ol>
     * <li>Crear cliente AWS DynamoDB Local (true)</li>
     * <li>Generar tablas: Usuario, Aula, Reserva</li>
     * <li>Inicializar y ejecutar Menu interactivo</li>
     * </ol>
     * 
     * Captura IOException (credenciales/archivos faltantes) y loguea error.
     * 
     * @param args argumentos de linea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            LOG.info("Iniciando sistema de reservas DynamoDB Local");
            
            // Inicializar cliente DynamoDB Local
            AWSClient aws = new AWSClient(true);
            
            // Crear tablas automaticamente desde modelos anotados
            LOG.debug("Creando tablas...");
            aws.generateTable(Usuario.class);
            aws.generateTable(Aula.class);
            aws.generateTable(Reserva.class);
            LOG.info("Tablas creadas correctamente");

            // Lanzar menu interactivo
            Menu menu = new Menu(aws);
            menu.start();
            
        } catch (IOException e) {
            LOG.error("ERROR [{}]", e.getMessage());
        }
    }
}
