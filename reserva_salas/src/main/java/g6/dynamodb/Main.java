package g6.dynamodb;



import g6.dynamodb.Model.Test;
import g6.dynamodb.Util.AWSClient;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        // AWSClient aws;
        // try {
        //     aws = new AWSClient(true);
        //     aws.listTables().stream().forEach(System.out::println);

        //     // Map<String, AttributeValue> item = new HashMap<>();
        //     // item.put("id", new AttributeValue("USER3"));
        //     // item.put("name", new AttributeValue("MARIO"));

        //     // // aws.insertItem(item);
        //     // aws.scanTable("Usuarios").stream().forEach(System.out::println);

        //     aws.generateTable(Test.class);

        //     // aws.scanTable(Usuario.class).stream().forEach(System.out::println);

        //     // aws.getItemById();

        // } catch (Exception e){
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        launch(args);
    }

    @Override
    public void start(Stage arg0) throws Exception {
        
        // TODO: Implementar interfaz gráfica y manejarlo por el controlador

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ruta a archivo.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("Reserva de Salas");
        stage.setScene(new Scene(root));
        stage.show();
        

    }
}