
import g6.dynamodb.Model.Test;
import g6.dynamodb.Util.AWSClient;
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

        
        FXMLLoader cargar = new FXMLLoader(
            getClass().getResource("/tu/paquete/app/fxml/login.fxml")
        );

        Scene scene = new Scene(cargar.load());
        stage.setTitle("Reserva de Salas");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        AWSClient aws;
        try {
            aws = new AWSClient(true);
            aws.listTables().stream().forEach(System.out::println);

            // Map<String, AttributeValue> item = new HashMap<>();
            // item.put("id", new AttributeValue("USER3"));
            // item.put("name", new AttributeValue("MARIO"));

            // // aws.insertItem(item);
            // aws.scanTable("Usuarios").stream().forEach(System.out::println);

            aws.generateTable(Test.class);

            // aws.scanTable(Usuario.class).stream().forEach(System.out::println);

            // aws.getItemById();

        } catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            launch(args);
        }
    }

    
}