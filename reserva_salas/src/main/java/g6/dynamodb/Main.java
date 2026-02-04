package g6.dynamodb;

import g6.dynamodb.Model.Test;
import g6.dynamodb.Util.AWSClient;

public class Main {
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
        }
    }
}