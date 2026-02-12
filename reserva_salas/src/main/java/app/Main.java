package app;
/**
Main JavaFX Application + Menu TUI fallback.
* Carga inicio.fxml via SceneManager navegacion. CSS styles.css.
* main() ejecuta Menu consola si FX falla, luego launch() GUI.
* 
* @author Mario Garcia
* @author Mateo Ayarra
* @author Samuel Cobreros
* @author Zacaria Daghri
* @version 1.0
* @since 1.0
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.navegacion.SceneManager;
import domain.model.Aula;
import domain.model.Reserva;
import domain.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.dynamodb.AWSClient;

public class Main extends Application {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
    Inicializa GUI principal desde inicio.fxml.
    * Configura SceneManager + title + CSS + show().
    * @param stage escenario JavaFX principal
    * @throws Exception FXML/CSS carga errores
    */
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage); // Navegacion entre escenas
        
        FXMLLoader cargar = new FXMLLoader(
                getClass().getResource("/fxml/inicio.fxml")
        );
        
        Scene scene = new Scene(cargar.load());
        log.info("FXML cargado correctamente");
        stage.setTitle("Reserva de Salas");
        stage.setScene(scene);
        scene.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm()
        );
        
        stage.show();
    }

    /**
    Punto entrada hibrido TUI/GUI.
    * Intenta Menu consola (DynamoDB Local), luego launch() FX.
    * @param args argumentos linea comandos
    */
    public static void main(String[] args) {

        // Intentar ejecutar menú TUI para pruebas DynamoDB Local
        // Si falla (ej. entorno sin consola), continuar con GUI JavaFX
        try {
            AWSClient awsClient = new AWSClient(true); // true = local

            if (!awsClient.existeTabla(Usuario.class)) {
                awsClient.generateTable(Usuario.class);
            }

            if (!awsClient.existeTabla(Aula.class)) {
                awsClient.generateTable(Aula.class);
            }

            if (!awsClient.existeTabla(Reserva.class)) {
                awsClient.generateTable(Reserva.class);
            }
            
            Menu menu = new Menu();
            menu.start();
        } catch (Exception e) {
            log.warn("Algo salio mal: " + e.getMessage());
        }
        
        launch(args);
        log.info("Aplicacion iniciada");
    }
}