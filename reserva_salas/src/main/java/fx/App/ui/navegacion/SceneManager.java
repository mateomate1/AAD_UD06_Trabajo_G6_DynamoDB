package fx.App.ui.navegacion;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Clase responsable de gestionar la navegación entre escenas en la aplicación.
 * Proporciona un método estático para cambiar la escena actual a una nueva escena basada en un archivo FXML.
 */
public class SceneManager {

    private static Stage stage;

    // Método para establecer la referencia al Stage principal
    public static void setStage(Stage primerStage) {
        stage = primerStage;
    }

    /**
     * Cambia la escena actual a la especificada por el nombre del archivo FXML.
     * @param fxml El nombre del archivo FXML (sin la extensión) que se encuentra en el paquete "fxml".
     */
    public static void cambioScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(
                SceneManager.class.getResource("/fxml/" + fxml + ".fxml")
            );
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
