package fx.App.ui.controladores;

/**
 * Controlador FXML para la vista de gestion de reservas de aulas.
 * 
 * Integra calendario, lista de aulas disponibles, selector de horas y 
 * navegacion mediante WebView. Gestiona creacion y visualizacion de reservas.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import g6.dynamodb.Style.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;

public class ReservaController {

    @FXML
    private WebView WebViewPReservas;

    @FXML
    private Menu botonAuyudaPReservas;

    @FXML
    private Menu botonInicioPReservas;

    @FXML
    private Button botonReservaPReserva;

    @FXML
    private ComboBox<?> boxHorasPReservas;

    @FXML
    private DatePicker calendarioPReservas;

    @FXML
    private ListView<?> listAulasDisponibles;

    /**
     * Inicializa el controlador despues de cargar FXML.
     * 
     * Configura ComboBox de horas, popula lista de aulas disponibles y 
     * prepara WebView para visualizacion de calendario/reservas.
     */
    @FXML
    private void initialize() {
        // TODO: Configurar ComboBox horas (8:00-22:00)
        // TODO: Cargar aulas disponibles desde DynamoDB
        // TODO: Inicializar WebView con calendario HTML
    }
}
