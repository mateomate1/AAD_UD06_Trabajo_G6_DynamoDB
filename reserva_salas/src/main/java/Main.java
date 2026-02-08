import fx.App.ui.navegacion.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Método de inicio de la aplicación. Carga la escena inicial desde el archivo FXML y la muestra en el escenario principal.
     * @param stage El escenario principal proporcionado por JavaFX.
    */
    @Override
    public void start(Stage stage) throws Exception {

        SceneManager.setStage(stage); // para que el SceneManager pueda gestionar la navegación entre escenas
        
        FXMLLoader cargar = new FXMLLoader(
            getClass().getResource("/fxml/inicio.fxml")
        );

        Scene scene = new Scene(cargar.load());
        stage.setTitle("Reserva de Salas");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        
        stage.show();
    }
    public static void main(String[] args) {
        
        launch(args);
        
    }

    
}