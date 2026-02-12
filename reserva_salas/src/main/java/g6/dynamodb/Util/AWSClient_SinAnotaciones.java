package g6.dynamodb.Util;

/**
Cliente AWS DynamoDB sin anotaciones (Low-Level API).
* Implementa CRUD completo: CreateTable/PutItem/GetItem/UpdateItem/DeleteItem/Scan.
* Alternativa a DynamoDBMapper para operaciones especificas o testing.
* Soporta DynamoDB Local/AWS real via credenciales properties.
* @author Mario Garcia
* @author Mateo Ayarra
* @author Samuel Cobreros
* @author Zacaria Daghri
* @version 1.0
* @since 1.0
 */
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import g6.dynamodb.Dictionary;
import g6.dynamodb.Model.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AWSClient_SinAnotaciones {
    public final AmazonDynamoDB dynamoDB;
    public final Properties p = new Properties();
    private final File fichProperties = new File("DynamoDBCredentials.properties");
    private final Logger log = LoggerFactory.getLogger(AWSClient_SinAnotaciones.class);

    /**
    Constructor principal (local/cloud).
    * Carga credenciales desde DynamoDBCredentials.properties.
    * Local: endpoint/region especificos. Cloud: us-east-1 default.
    * @param local true=DynamoDB Local, false=AWS Cloud
    * @throws FileNotFoundException archivo credenciales ausente
    * @throws IOException error lectura properties
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
    Acceso al cliente DynamoDB configurado.
    * @return instancia AmazonDynamoDB lista para low-level operations
    */
    public AmazonDynamoDB getDB() {
        return this.dynamoDB;
    }

    // ==================== CREATE ====================
    /**
    Crea tabla con clave primaria HASH simple.
    * Configura PAY_PER_REQUEST billing automatico.
    * @param nombreTabla nombre tabla nueva
    * @param nombreAtrClave atributo clave primaria (String)
    */
    public void generateTable(String nombreTabla, String nombreAtrClave) {
        CreateTableRequest request = new CreateTableRequest()
                .withTableName(nombreTabla)
                .withKeySchema(
                        new KeySchemaElement(nombreAtrClave, KeyType.HASH))
                .withAttributeDefinitions(
                        new AttributeDefinition(nombreAtrClave, ScalarAttributeType.S))
                .withBillingMode(BillingMode.PAY_PER_REQUEST.toString());
        dynamoDB.createTable(request);
    }

    /**
    Inserta item en tabla "Usuarios" (PutItem).
    * @param item mapa atributo=AttributeValue
    */
    public void insertItem(Map<String, AttributeValue> item) {
        PutItemRequest request = new PutItemRequest()
                .withTableName("Usuarios")
                .withItem(item);
        dynamoDB.putItem(request);
    }

    // ==================== READ ====================
    /**
    Lista nombres todas tablas existentes.
    * @return ArrayList nombres tablas
    */
    public List<String> listTables() {
        ListTablesResult resultado = dynamoDB.listTables();
        List<String> salida = new ArrayList<>();
        resultado.getTableNames().stream().forEach(salida::add);
        return salida;
    }

    /**
    Obtiene item por clave primaria (GetItem).
    * Imprime raw result (pendiente mapeo modelo).
    * @param id valor clave primaria
    * @param table tabla Dictionary.Tablas
    * @return Usuario (TODO: implementar mapper AttributeValue->modelo)
    */
    public Usuario getItemById(String id, Dictionary.Tablas table) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));
        GetItemRequest request = new GetItemRequest()
                .withTableName(table.toString())
                .withKey(key);
        GetItemResult result = dynamoDB.getItem(request);
        System.out.println(result.getItem());
        Map<String, AttributeValue> salida = result.getItem();
        return null; // Pendiente: mapper AttributeValue -> Usuario
    }

    /**
    Escanea tabla filtrando por atributo "name".
    * Usa FilterExpression name=:n.
    * @param client cliente DynamoDB
    * @param tabla nombre tabla
    * @param nombre valor filtro "name"
    * @return lista items filtrados como mapas
    */
    public List<Map<String, AttributeValue>> scanByAttribute(
            AmazonDynamoDB client, 
            String tabla, 
            String nombre) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":n", new AttributeValue().withS(nombre));
        ScanRequest request = new ScanRequest()
                .withTableName(tabla)
                .withFilterExpression("name = :n")
                .withExpressionAttributeValues(values);
        return client.scan(request).getItems();
    }

    /**
    Escanea tabla completa con paginacion automatica.
    * Maneja LastEvaluatedKey hasta null.
    * @param tableName nombre tabla
    * @return todos items como List<Map<AttributeValue>>
    */
    public List<Map<String, AttributeValue>> scanTable(String tableName) {
        ScanRequest request = new ScanRequest()
                .withTableName(tableName);
        ScanResult result = dynamoDB.scan(request);
        List<Map<String, AttributeValue>> items = new ArrayList<>();
        items.addAll(result.getItems());
        
        while (result.getLastEvaluatedKey() != null) {
            request = request.withExclusiveStartKey(result.getLastEvaluatedKey());
            result = dynamoDB.scan(request);
            items.addAll(result.getItems());
        }
        return items;
    }

    // ==================== UPDATE ====================
    /**
    Actualiza atributo especifico (UpdateItem).
    * Action PUT sobrescribe valor.
    * @param tableName tabla objetivo
    * @param keyName nombre clave primaria
    * @param keyValue valor clave primaria
    * @param attributeName atributo actualizar
    * @param newValue nuevo valor AttributeValue
    */
    public void updateAttribute(
            String tableName,
            String keyName,
            String keyValue,
            String attributeName,
            AttributeValue newValue) {
        Map<String, AttributeValue> key = Map.of(
                keyName, new AttributeValue().withS(keyValue));
        Map<String, AttributeValueUpdate> updates = Map.of(
                attributeName,
                new AttributeValueUpdate()
                        .withValue(newValue)
                        .withAction(AttributeAction.PUT));
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withAttributeUpdates(updates);
        dynamoDB.updateItem(request);
    }

    // ==================== DELETE ====================
    /**
    Elimina tabla completa (DeleteTable).
    * @param tableName nombre tabla borrar
    */
    public void deleteTable(String tableName) {
        DeleteTableRequest request = new DeleteTableRequest()
                .withTableName(tableName);
        dynamoDB.deleteTable(request);
    }

    /**
    Elimina item por clave primaria (DeleteItem).
    * @param tableName tabla objetivo
    * @param keyName nombre clave primaria
    * @param keyValue valor clave primaria
    */
    public void deleteByKey(
            String tableName,
            String keyName,
            String keyValue) {
        Map<String, AttributeValue> key = Map.of(
                keyName, new AttributeValue().withS(keyValue));
        DeleteItemRequest request = new DeleteItemRequest()
                .withTableName(tableName)
                .withKey(key);
        dynamoDB.deleteItem(request);
    }
}
