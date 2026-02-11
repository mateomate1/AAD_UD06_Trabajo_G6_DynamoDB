package fx.App.ui.navegacion;

/**
 * Gestor centralizado de navegacion entre escenas FXML.
 * 
 * Proporciona metodos estaticos para cambiar escenas dinamicamente
 * usando Stage principal y archivos FXML en /resources/fxml/.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    /**
     * Establece la referencia al Stage principal de la aplicacion.
     * 
     * Debe llamarse en main() despues de crear primaryStage.
     * 
     * @param primerStage Stage principal de JavaFX
     */
    public static void setStage(Stage primerStage) {
        stage = primerStage;
    }

    /**
     * Cambia la escena actual a nueva vista FXML especificada.
     * 
     * Carga /fxml/[nombre].fxml y aplica al Stage actual.
     * Maneja IOException mostrando stack trace en consola.
     * 
     * @param fxml nombre del archivo FXML (sin extension .fxml)
     * @throws IOException si no encuentra archivo FXML
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
