package g6.dynamodb.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import g6.dynamodb.Model.Tablas;
import g6.dynamodb.Model.Usuario;

public class AWSClient_SinAnotaciones {
    public final AmazonDynamoDB dynamoDB;
    public final Properties p = new Properties();
    private final File fichProperties = new File("DynamoDBCredentials.properties");
    private final Logger log = LoggerFactory.getLogger(AWSClient_SinAnotaciones.class);

    /**
     * Inicializar cliente
     * 
     * @param local
     * @throws FileNotFoundException
     * @throws IOException
     */
    public AWSClient_SinAnotaciones(boolean local) throws FileNotFoundException, IOException {
        p.load(new FileInputStream(fichProperties));
        log.trace("Fichero cargado con exito");

        if (local) {
            String accessKey = p.getProperty("local.accessKeyId");
            String secretKey = p.getProperty("local.secretAccessKey");
            String region = p.getProperty("local.region");
            String endpoint = p.getProperty("endpoint");
            this.dynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(
                                    endpoint,
                                    region))
                    .withCredentials(
                            new AWSStaticCredentialsProvider(
                                    new BasicAWSCredentials(accessKey, secretKey)))
                    .build();
        } else {
            this.dynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withRegion("us-east-1")
                    .build();

        }
    }

    /**
     * Listar tablas
     *
     * @return
     */
    public List<String> listTables() {
        ListTablesResult resultado = dynamoDB.listTables();
        List<String> salida = new ArrayList<>();
        resultado.getTableNames().stream().forEach(salida::add);
        return salida;
    }

    /**
     * Recuperar item por id
     */
    public Usuario getItemById(String id, Tablas table) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));

        GetItemRequest request = new GetItemRequest()
                .withTableName(table.toString())
                .withKey(key);
        // Solo sirve para especificar que atributos devolver
        // .withAttributesToGet("name")

        GetItemResult result = dynamoDB.getItem(request);
        System.out.println(result.getItem());
        Map<String, AttributeValue> salida = result.getItem();

        return null;
    }

    public List<Map<String, AttributeValue>> scanByAttribute(AmazonDynamoDB client, String tabla, String nombre) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":n", new AttributeValue().withS(nombre));

        ScanRequest request = new ScanRequest()
                .withTableName(tabla)
                .withFilterExpression("name = :n")
                .withExpressionAttributeValues(values);

        return client.scan(request).getItems();
    }

    public List<java.util.Map<String, AttributeValue>> scanTable(String tableName) {

        ScanRequest request = new ScanRequest()
                .withTableName(tableName);

        ScanResult result = dynamoDB.scan(request);

        List<java.util.Map<String, AttributeValue>> items = new ArrayList<>();
        items.addAll(result.getItems());

        while (result.getLastEvaluatedKey() != null) {
            request = request.withExclusiveStartKey(result.getLastEvaluatedKey());
            result = dynamoDB.scan(request);
            items.addAll(result.getItems());
        }

        return items;
    }

    public void insertItem(Map<String, AttributeValue> item) {
        PutItemRequest request = new PutItemRequest()
                .withTableName("Usuarios")
                .withItem(item);

        dynamoDB.putItem(request);

    }

    public void generateTable() {
        CreateTableRequest request = new CreateTableRequest()
                // Nombre de la clase
                .withTableName("Usuarios")
                // Aqui se define el atributo clave de la tabla
                .withKeySchema(
                        new KeySchemaElement("id", KeyType.HASH))
                // Definicion del atributo clave(el tipo de clave S/N/B)
                .withAttributeDefinitions(
                        new AttributeDefinition("id", ScalarAttributeType.S))
                //
                .withBillingMode(BillingMode.PAY_PER_REQUEST.toString());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", new AttributeValue().withS("1"));
        Map<String, AttributeValue> items = new HashMap<>();
        item.put("id", new AttributeValue().withN("1"));
    }

}
