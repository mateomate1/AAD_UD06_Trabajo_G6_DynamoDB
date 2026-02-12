package ui.controladores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import app.navegacion.SceneManager;
import domain.model.Aula;
import domain.model.Reserva;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import persistence.dynamodb.AWSClient;
import service.AulaService;
import service.ReservaService;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;



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
    private AulaService aulaService;

    private Aula aulaSeleccionada;

    /**
     * Inicializa el controlador cargando las aulas disponibles en la lista.
     */
    public ReservasAulasController() throws IOException, FileNotFoundException {
        this.awsClient = new AWSClient(true);
        this.aulaService = new AulaService(awsClient);
    }

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
        // Validación de si hay un aula seleccionada antes de intentar reservar
        if (aulaSeleccionada == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor, seleccione un aula para reservar.").showAndWait();
            return;
        }
        // validar que se haya seleccionado una fechas seleccionadas
        if (calendarioPReservas.getValue() == null || calendarioPReservas1.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor, seleccione las fechas de inicio y fin para la reserva.").showAndWait();
            return;
        }
        // Validar capacidad de personadas no exceda la capacidad del aula seleccionada
        int numeroPersonas = numeroPersonasPReservasAulas.getValue();
        if (aulaSeleccionada.getCapacidad() < numeroPersonas) {
            new Alert(Alert.AlertType.WARNING, "El número de personas excede la capacidad del aula seleccionada.").showAndWait();
            return;
        }

        // validar que la fecha de inicio sea anterior a la fecha de fin
        ReservaService reservaService = new ReservaService(awsClient);
         if (!reservaService.fechasValidas(calendarioPReservas.getValue(), calendarioPReservas1.getValue())) {
            new Alert(Alert.AlertType.WARNING, "La fecha de inicio debe ser anterior a la fecha de fin.").showAndWait();
            return;
        }
        
        Reserva reserva = new Reserva();
        reserva.setAula(aulaSeleccionada);
        reserva.setNPersonas((Integer)numeroPersonas);
        reserva.setFechaInicio(calendarioPReservas.getValue().toString());
        reserva.setFechaFin(calendarioPReservas1.getValue().toString());

        reservaService.crearReserva(reserva);
        


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
        return;
    }

    /**
     * Maneja la acción de mostrar la ayuda, cargando un archivo markdown y mostrándolo en un WebView dentro de una nueva ventana.
     * Con Flexmark se convierte el markdown a HTML para mostrarlo correctamente formateado en el WebView.
     *Si ocurre algún error al cargar el archivo de ayuda, se muestra una alerta con el mensaje de error.
     * @param event evento de clic en el botón de ayuda
     */
    @FXML
    void verWebView(ActionEvent event) {
        try(InputStream is = getClass().getResourceAsStream("/ayuda.md")) {

                    if(is == null){ // en caso de que no se encuentre el archivo
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error de Ayuda");
                        alert.setHeaderText("Archivo de ayuda no encontrado");
                        alert.setContentText("No se pudo cargar el archivo de ayuda. Por favor, asegúrate de que el archivo 'ayuda.md' esté presente en el directorio de recursos.");
                        alert.showAndWait();

                        return;
                    }

                    String cssUrl = getClass().getResource("/css/webviewStyle.css").toExternalForm(); // obtener la URL del archivo CSS para aplicarlo al contenido HTML generado


                    String md = new String(is.readAllBytes()); // leer el contenido del archivo markdown
                    // para convertir markdown a HTML
                    Parser parser = Parser.builder().build(); // se encarga de leer el markdown y convertirlo en una estructura interna AST abstract syntax tree
                    HtmlRenderer renderer = HtmlRenderer.builder().build(); // está parte se encarga de conertir AST en HTML
                    String body = renderer.render(parser.parse(md)); // el resultado se guarda en body


                    // estructura básica de un documento HTML que se usará en el WebView para mostrar el contenido del markdown o en este caso del body (ya convertido a HTML)
                    String html = "<html><head>" +
                        "<link rel='stylesheet' href='" + cssUrl + "'>" +
                        "</head><body>" +
                        body +
                        "</body></html>"  
                    ;

                    WebView navegador = new WebView(); // instancia de WebView para mostrar contenido HTML
                    navegador.getEngine().loadContent(html);  // carga el html creado anteriormente y en la parte de *body* muestra el contenido del markdown convertido a HTML (no carga directamente el body porque falta la estructura HTML básica)
                    
                    Stage ayudaVentana = new Stage(); // nuebva ventana para mostrar la ayuda
                    ayudaVentana.setTitle("Ayuda - DynamoDB Demo"); // título de la ventana de ayuda

                    BorderPane root = new BorderPane(navegador);
                    ayudaVentana.setScene(new Scene(root, 700, 500)); // crear la escena con el WebView
                    ayudaVentana.show(); // mostrar la ventana de ayuda

                    
                    
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de Ayuda");
                    alert.setHeaderText("Error al cargar la ayuda");
                    alert.setContentText("Ocurrió un error al cargar la ayuda: " + ex.getMessage());
                    alert.showAndWait();    
                    return;
                }
    }

    


}
