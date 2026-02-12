package ui.controladores;

import java.util.List;
import java.util.Optional;

import app.navegacion.SceneManager;
import domain.model.Aula;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import persistence.dynamodb.AWSClient;
import service.AulaService;



public class ReservasAulasController {

   @FXML
    private MenuItem botonAuyudaPReservasAulas;

    @FXML
    private Button botonReservaPReserva;

    @FXML
    private DatePicker calendarioPReservas;

    @FXML
    private DatePicker calendarioPReservas1;

    @FXML
    private Label capacidadPReservasAulas;

    @FXML
    private ListView<Aula> listAulasDisponibles;

    @FXML
    private Menu menuBarSalirPReservasAulas;

    @FXML
    private Spinner<Integer> numeroPersonasPReservasAulas;

    private  AWSClient awsClient;
    private AulaService aulaService = new AulaService(awsClient);

    private Aula aulaSeleccionada;

    /**
     * Inicializa el controlador cargando las aulas disponibles en la lista.
     */
    @FXML
    public void initialize() {

        // listener para sleccionar aula para hacer reserva, muestra capacidad del aula seleccionada
        listAulasDisponibles.getSelectionModel().selectedItemProperty().addListener((observable, old, aula) -> {
            aulaSeleccionada = aula; // Almacena el aula seleccionada para uso posterior
            if (aula != null) {   
                capacidadPReservasAulas.setText("Capacidad: " + aula.getCapacidad()); // Muestra capacidad del aula seleccionada
            } else { capacidadPReservasAulas.setText("Capacidad: N/A"); }
        });

        // Configura el Spinner para seleccionar número de personas (1-100)
        numeroPersonasPReservasAulas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        // Carga las aulas disponibles al iniciar la pantalla en la ListView
        try {
            List<Aula> aulas = aulaService.ObtenerListaAulas();
            listAulasDisponibles.getItems().clear(); // Limpiar lista antes de cargar
            listAulasDisponibles.setItems(FXCollections.observableArrayList(aulas)); 

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar las aulas: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    void reservarAula(ActionEvent event) {
        if (aulaSeleccionada == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor, seleccione un aula para reservar.").showAndWait();
            return;
        }

        // Aquí se implementaría la lógica para crear una reserva utilizando los datos seleccionados
        // como el aulaSeleccionada, la fecha del calendarioPReservas y la hora del boxHorasPReservas.
        // Se podría mostrar un mensaje de confirmación o error según corresponda.

        new Alert(Alert.AlertType.INFORMATION, "Reserva realizada para el aula: " + aulaSeleccionada.getNombre()).showAndWait();
    }

    /**
     * Maneja la acción de volver al inicio, mostrando una confirmación antes de cambiar de pantalla.
     * @param event evento de clic en el botón de salir
     */
    @FXML
    void SalirInicio(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText("¿Quieres volver al inicio?");
        alert.setContentText("Se cerrará la pantalla actual.");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            SceneManager.cambioScene("inicio");
        }
    }

    @FXML
    void verWebView(ActionEvent event) {

    }

    


}
