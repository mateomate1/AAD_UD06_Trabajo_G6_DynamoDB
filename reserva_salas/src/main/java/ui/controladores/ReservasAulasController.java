package ui.controladores;

import java.util.List;

import domain.model.Aula;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.web.WebView;
import persistence.dynamodb.AWSClient;
import service.AulaService;



public class ReservasAulasController {

    @FXML
    private WebView WebViewPReservas;

    @FXML
    private Menu botonAuyudaPReservas;

    @FXML
    private Menu botonInicioPReservas;

    @FXML
    private Button botonReservaPReserva;

    @FXML
    private ComboBox<String> boxHorasPReservas;

    @FXML
    private DatePicker calendarioPReservas;

    @FXML
    private ListView<Aula> listAulasDisponibles;

    private  AWSClient awsClient;
    private AulaService aulaService = new AulaService(awsClient);

    /**
     * Inicializa el controlador cargando las aulas disponibles en la lista.
     */
    @FXML
    public void initialize() {
        try {
            List<Aula> aulas = aulaService.ObtenerListaAulas();
            listAulasDisponibles.getItems().clear(); // Limpiar lista antes de cargar
            listAulasDisponibles.setItems(FXCollections.observableArrayList(aulas)); 

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar las aulas: " + e.getMessage()).showAndWait();
        }
        
    }

    @FXML
    void SalirInicio(ActionEvent event) {

    }

    @FXML
    void verWebView(ActionEvent event) {

    }


}
