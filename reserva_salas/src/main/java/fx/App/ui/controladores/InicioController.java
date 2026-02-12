package fx.App.ui.controladores;

/**
 * Controlador FXML para la vista de inicio de la aplicacion.
 * 
 * Gestiona la pantalla inicial con boton de entrada a la aplicacion.
 * Realiza navegacion a vista de login mediante SceneManager.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import fx.App.ui.navegacion.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class InicioController {

    @FXML
    private Button botonPinicio;

    @FXML
    private Label tituloPInicio;

    /**
     * Maneja accion del boton "Entrar" en vista inicial.
     * 
     * Navega a pantalla de login usando SceneManager.
     * 
     * @param event evento ActionEvent del boton
     */
    @FXML
    void entrarAPP(ActionEvent event) {
        SceneManager.cambioScene("login");
    }
}
