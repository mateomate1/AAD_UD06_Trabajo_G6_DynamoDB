package g6.dynamodb;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import g6.dynamodb.Util.AWSClient;

public class Main {
    public static void main(String[] args) {
        AWSClient aws;
        try {
            aws = new AWSClient(true);
            aws.listTables().stream().forEach(System.out::println);

            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", new AttributeValue().withS("UserZaca2"));
            item.put("name", new AttributeValue().withN("1234"));

            aws.insertItem(item);
            // aws.scanTable("Usuarios").stream().forEach(System.out::println);

            // aws.generateTable(Test.class);

            // aws.scanTable(Usuario.class).stream().forEach(System.out::println);

            // aws.getItemById();

        } catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}