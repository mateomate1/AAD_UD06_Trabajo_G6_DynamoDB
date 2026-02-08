package g6.dynamodb;

import g6.dynamodb.Model.Aula;
import g6.dynamodb.Model.Reserva;
import g6.dynamodb.Model.Usuario;
import g6.dynamodb.Style.Menu;
import g6.dynamodb.Util.AWSClient;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            AWSClient aws = new AWSClient(true);
            aws.generateTable(Usuario.class);
            aws.generateTable(Aula.class);
            aws.generateTable(Reserva.class);

            Menu menu = new Menu(aws);
            menu.start();

        } catch (IOException e) {
            LOG.error("ERROR [" + e.getMessage() + "]");
        }
    }
}
