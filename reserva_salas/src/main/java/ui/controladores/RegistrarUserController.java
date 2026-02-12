package ui.controladores;

import app.navegacion.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import persistence.dynamodb.AWSClient;
import util.HashUtil;
import service.UsuarioService; 
public class RegistrarUserController {

    @FXML
    private TextField Usuario;

    @FXML
    private Button buttonRewgistrasePRegistro;

    @FXML
    private PasswordField contrasenia;

    @FXML
    private PasswordField contrasenia1;

    @FXML
    private TextField contraseniaVisible1;

    @FXML
    private TextField contraseniaVisible2;

    @FXML
    private CheckBox vBoxPRegistro;

    private  AWSClient awsClient;
    private UsuarioService usuarioService = new UsuarioService(awsClient);

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
            contraseniaVisible1.setVisible(false);
            contraseniaVisible2.setVisible(false);
        // Sincronizar el contenido del campo de contraseña visible con el campo de contraseña oculto
        contraseniaVisible1.textProperty().bindBidirectional(contrasenia.textProperty());
        contraseniaVisible2.textProperty().bindBidirectional(contrasenia1.textProperty());

         // Listener de Validación campo 1
        contrasenia.textProperty().addListener((observable, oldValue, newValue) -> {
            int resulatadoValidacion = HashUtil.validar(Usuario.getText(), newValue);
            if (resulatadoValidacion == -1) {
                contrasenia.setStyle("-fx-border-color: red;");
                contraseniaVisible1.setStyle("-fx-border-color: red;");
            } else if (resulatadoValidacion == 0) {
                contrasenia.setStyle("-fx-border-color: orange;");
                contraseniaVisible1.setStyle("-fx-border-color: orange;");
            } else {
                contrasenia.setStyle("-fx-border-color: green;");
                contraseniaVisible1.setStyle("-fx-border-color: green;");
            }
        });

         // Listener de Validación campo 2
        contrasenia1.textProperty().addListener((observable, oldValue, newValue) -> {
            int resulatadoValidacion = HashUtil.validar(Usuario.getText(), newValue);
            if (resulatadoValidacion == -1) {
                contrasenia1.setStyle("-fx-border-color: red;");
                contraseniaVisible2.setStyle("-fx-border-color: red;");
            } else if (resulatadoValidacion == 0) {
                contrasenia1.setStyle("-fx-border-color: orange;");
                contraseniaVisible2.setStyle("-fx-border-color: orange;");
            } else {
                contrasenia1.setStyle("-fx-border-color: green;");
                contraseniaVisible2.setStyle("-fx-border-color: green;");
            }
        });

    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Sing up" para registrar un nuevo usuario.
     * Verifica que las contraseñas ingresadas en ambos campos coincidan antes de proceder con el registro del usuario.
     * @param event Evento que se produce al interactuar con el botón "Sing up".    
     */
    @FXML
    void RegistrarUsuario(ActionEvent event) {
        String contrasena1 = contrasenia.getText();
        String contrasena2 = contrasenia1.getText();

        // Usuario vacio o contraseña vacía
        if (Usuario.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Registro");
            alert.setHeaderText("Campo de nombre de usuario vacío");
            alert.setContentText("Por favor, ingresa un nombre de usuario.");
            alert.showAndWait();
        }else if (contrasena1.isEmpty() || contrasena2.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Registro");
            alert.setHeaderText("Campos de contraseña vacíos");
            alert.setContentText("Por favor, ingresa una contraseña en ambos campos.");
            alert.showAndWait();    
        }

        // Contraseñas no coinciden
        if (!contrasena1.equals(contrasena2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Registro");
            alert.setHeaderText("Las contraseñas no coinciden");
            alert.setContentText("Por favor, asegúrate de que ambas contraseñas sean iguales.");
            alert.showAndWait();
        }else {
            if(usuarioService.altaUsuario(Usuario.getText(), contrasena1)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro Exitoso");
                alert.setHeaderText("Usuario registrado correctamente");
                alert.setContentText("¡Bienvenido a Ciudad Escolar!");
                alert.showAndWait();
                SceneManager.cambioScene("login");
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de Registro");
                alert.setHeaderText("Error Inesperado");
                alert.setContentText("Por favor, vuelve a intentarlo más tarde o contacta con el soporte si el problema persiste.");
                alert.showAndWait();    
            }
            return;
        }
    }

    /**
     * Método que se ejecuta al hacer clic en el CheckBox para mostrar u ocultar la contraseña.
     * @param event Evento que se produce al interactuar con el CheckBox "vBoxPRegistro".
     */
    @FXML
    void VerContrasenia(ActionEvent event) {
        
     if(vBoxPRegistro.isSelected()){
        // Mostrar la contraseña visible y ocultar la contraseña oculta para el primer campo de contraseña
        contraseniaVisible1.setVisible(true);
        contrasenia.setVisible(false); 

        // Mostrar la contraseña visible y ocultar la contraseña oculta para el segundo campo de contraseña
        contraseniaVisible2.setVisible(true);
        contrasenia1.setVisible(false);

     }else{
        // Ocultar la contraseña visible y mostrar la contraseña oculta para el primer campo de contraseña
        contraseniaVisible1.setVisible(false);
        contrasenia.setVisible(true);

        // Ocultar la contraseña visible y mostrar la contraseña oculta para el segundo campo de contraseña
        contraseniaVisible2.setVisible(false);
        contrasenia1.setVisible(true);
     }

    }
}
