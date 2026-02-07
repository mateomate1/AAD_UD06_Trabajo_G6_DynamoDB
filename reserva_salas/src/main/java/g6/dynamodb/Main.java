package g6.dynamodb;

import java.time.LocalDateTime;

import g6.dynamodb.Util.AWSClient;

public class Main {
    public static void main(String[] args) {
        AWSClient aws;
        try {
            aws = new AWSClient(true);
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
            LocalDateTime time = LocalDateTime.parse("2026-02-07T12:20");
            System.out.println(time.toString());
            time = LocalDateTime.of(2026, 02, 7, 12, 20);
            System.out.println(time.toString());
        } catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}