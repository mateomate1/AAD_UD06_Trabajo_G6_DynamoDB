package g6.dynamodb;

import java.time.LocalDateTime;

import g6.dynamodb.Model.Reserva;
import g6.dynamodb.Service.ReservaService;
import g6.dynamodb.Util.AWSClient;

public class Main {
    public static void main(String[] args) {
        AWSClient aws;
        try {
            aws = new AWSClient(true);
            // Test t = new Test();
            // t.setClave("clave");
            // aws.insertItem(t);

            // aws.generateTable(Reserva.class);
            // ReservaDAO r = new ReservaDAO(aws.getDynamoDB());
            // Reserva o = r.findById("id");
            // System.out.println(o.toString());

            ReservaService rs = new ReservaService(aws);
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(LocalDateTime.of(2026, 2, 7, 9, 0).toString());
            reserva.setFechaFin(LocalDateTime.of(2026, 2, 7, 10, 20).toString());
            rs.crearReserva(reserva);

            // aws.listTables().stream().forEach(System.out::println);

            // Map<String, AttributeValue> item = new HashMap<>();
            // item.put("id", new AttributeValue().withS("UserZaca2"));
            // item.put("name", new AttributeValue().withN("1234"));

            // aws.insertItem(item);
            // aws.scanTable("Usuarios").stream().forEach(System.out::println);

            // aws.generateTable(Test.class);

            // aws.scanTable(Usuario.class).stream().forEach(System.out::println);

            // aws.getItemById();

            // YYYY-MM-DDTHH:MM:SS
            // LocalDateTime time = LocalDateTime.parse("2026-02-07T12:20");
            // System.out.println(time.toString());
            // time = LocalDateTime.of(2026, 02, 7, 12, 20);
            // System.out.println(time.toString());
        } catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}