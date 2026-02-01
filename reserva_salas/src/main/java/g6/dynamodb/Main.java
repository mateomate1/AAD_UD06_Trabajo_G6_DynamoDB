package g6.dynamodb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import g6.dynamodb.Model.Test;
import g6.dynamodb.Util.AWSClient;

public class Main {
    public static void main(String[] args) {
        AWSClient aws;
        try {
            aws = new AWSClient(true);
            aws.listTables().stream().forEach(System.out::println);

            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", new AttributeValue("USER3"));
            item.put("name", new AttributeValue("MARIO"));

            // aws.insertItem(item);
            aws.scanTable("Usuarios").stream().forEach(System.out::println);

            aws.generateTable(Test.class);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}