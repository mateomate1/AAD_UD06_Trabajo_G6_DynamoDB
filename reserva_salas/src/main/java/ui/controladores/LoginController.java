package ui.controladores;

/**
 * Controlador FXML para la vista de login de la aplicacion.
 * 
 * Gestiona la pantalla de inicio de sesion con campos de usuario y contraseña.
 * Permite alternar visibilidad de contraseña y valida las credenciales ingresadas.
 * Realiza navegacion a vista de registro mediante SceneManager.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import app.navegacion.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.UsuarioService;

public class LoginController {

    // --------------------------------------- Declaración de los elementos de la interfaz gráfica que se utilizarán en el controlador. ---------------------------------------
    @FXML
    private Button ButtonLogIn;

    @FXML
    private Button ButtonSingUp;

    @FXML
    private TextField Usuario;

    @FXML
    private PasswordField contrasenia;

    @FXML
    private TextField contraseniaVisible;
    
    @FXML
    private CheckBox checkBoxVerPLogin;

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // --------------------------------------- Métodos que se ejecutan al interactuar con los elementos de la interfaz gráfica. ---------------------------------------------------------
   
    /**
     * Método que se ejecuta al hacer clic en el botón "Log in" para iniciar sesión.
     * @param event Evento que se produce al interactuar con el botón "Log in".* * Valida las credenciales ingresadas utilizando el servicio de usuarios (Usuario
     */
    @FXML
    void IniciarSesion(ActionEvent event) {
        String nombreUsuario = Usuario.getText();
        String contrasena = contrasenia.getText();

       
        if (UsuarioService.loginUsuario(nombreUsuario, contrasena)) {
            SceneManager.cambioScene("pa1App");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de inicio de sesión");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("Nombre de usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.");
            alert.showAndWait();
            
        }
        return;
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Sing up" para registrar un nuevo usuario.
     * @param event
     */
    @FXML
    void RegistrarUsuario(ActionEvent event) {
        SceneManager.cambioScene("singUp");
    }

   /**
    * Método que se ejecuta al inicializar el controlador.
    * Hace que el campo de contraseña visible esté oculto inicialmente
    * y sincroniza su contenido con el campo de contraseña oculto
    * para que ambos campos muestren la misma contraseña cuando se alterna la visibilidad.
    * Y además, agrega un listener al campo de contraseña para validar su contenido en tiempo real 
    * y cambiar el estilo del campo según el resultado de la validación.
    */
    @FXML
    public void initialize() {
        // Inicialmente, el campo de contraseña visible está oculto
        contraseniaVisible.setVisible(false);
        // Sincronizar el contenido del campo de contraseña visible con el campo de contraseña oculto
        contraseniaVisible.textProperty().bindBidirectional(contrasenia.textProperty());


        // Listener de Validación
        contrasenia.textProperty().addListener((observable, oldValue, newValue) -> {
            int resulatadoValidacion = HashUtil.validar(Usuario.getText(), newValue);
            if (resulatadoValidacion == -1) {
                contrasenia.setStyle("-fx-border-color: red;");
                contraseniaVisible.setStyle("-fx-border-color: red;");
            } else if (resulatadoValidacion == 0) {
                contrasenia.setStyle("-fx-border-color: orange;");
                contraseniaVisible.setStyle("-fx-border-color: orange;");
            } else {
                contrasenia.setStyle("-fx-border-color: green;");
                contraseniaVisible.setStyle("-fx-border-color: green;");
            }
        });
    }

    /**
     * Método que permite mostrar u opcultar la contraseña al marcar o desmarcar el CheckBox "checkBoxVerPLogin".
     * @param event Evento que se produce al interactuar con el CheckBox "checkBoxVerPLogin".
     */
    @FXML
    void VerContrasenia(ActionEvent event) {
        if (checkBoxVerPLogin.isSelected()) {
            contrasenia.setVisible(false); // Ocultar el campo de contraseña
            contraseniaVisible.setVisible(true); // Mostrar el campo de contraseña visible
        }else{
            contrasenia.setVisible(true); // Mostrar el campo de contraseña
            contraseniaVisible.setVisible(false); // Ocultar el campo de contraseña visible
        }
    }

   

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}
