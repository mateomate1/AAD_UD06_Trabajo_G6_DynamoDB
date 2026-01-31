package g6.dynamodb.Model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Usuario")
public class Usuario {
    String id;
    String nombre;
}
