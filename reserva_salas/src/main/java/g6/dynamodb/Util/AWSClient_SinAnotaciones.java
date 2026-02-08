package g6.dynamodb.Util;

/**
 * Cliente AWS DynamoDB **SIN ANOTACIONES** (Low-Level API).
 * 
 * Implementa operaciones CRUD completas usando directamente la API nativa de DynamoDB
 * (CreateTable, PutItem, GetItem, UpdateItem, DeleteItem, Scan). Útil para casos donde no se
 * pueden usar clases anotadas con @DynamoDBTable o para operaciones muy específicas.
 * 
 * Soporta DynamoDB Local y AWS real mediante credenciales desde properties.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
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
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;

import g6.dynamodb.Dictionary;
import g6.dynamodb.Model.Usuario;

public class AWSClient_SinAnotaciones {
    public final AmazonDynamoDB dynamoDB;
    public final Properties p = new Properties();
    private final File fichProperties = new File("DynamoDBCredentials.properties");
    private final Logger log = LoggerFactory.getLogger(AWSClient_SinAnotaciones.class);

    /**
     * Inicializa el cliente DynamoDB (local o AWS real).
     * 
     * Carga credenciales desde "DynamoDBCredentials.properties". Local usa endpoint específico,
     * AWS real usa us-east-1 con credenciales por defecto del sistema.
     * 
     * @param local true para DynamoDB Local, false para AWS Cloud
     * @throws FileNotFoundException si falta el archivo de credenciales
     * @throws IOException error leyendo propiedades
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
     * Retorna el cliente DynamoDB configurado.
     * 
     * @return instancia de AmazonDynamoDB
     */
    public AmazonDynamoDB getDB(){
        return this.dynamoDB;
    }

    // ==================== CREATE ====================
    /**
     * Crea tabla con clave primaria simple (HASH).
     * 
     * Configura PAY_PER_REQUEST automáticamente.
     * 
     * @param nombreTabla nombre de la nueva tabla
     * @param nombreAtrClave nombre del atributo clave primaria (String)
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
     * Inserta un nuevo item en tabla "Usuarios".
     * 
     * @param item mapa atributo → valor (AttributeValue)
     */
    public void insertItem(Map<String, AttributeValue> item) {
        PutItemRequest request = new PutItemRequest()
                .withTableName("Usuarios")
                .withItem(item);
        dynamoDB.putItem(request);
    }

    // ==================== READ ====================
    /**
     * Lista nombres de todas las tablas.
     * 
     * @return lista de nombres de tablas
     */
    public List<String> listTables() {
        ListTablesResult resultado = dynamoDB.listTables();
        List<String> salida = new ArrayList<>();
        resultado.getTableNames().stream().forEach(salida::add);
        return salida;
    }

    /**
     * Obtiene item por clave primaria.
     * 
     * @param id valor de la clave primaria
     * @param table enumeración Dictionary.Tablas
     * @return mapa de atributos (pendiente conversión a modelo)
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
        return null; // Pendiente: mapper AttributeValue → Usuario
    }

    /**
     * Escanea tabla con filtro por atributo "name".
     * 
     * @param client cliente DynamoDB
     * @param tabla nombre tabla
     * @param nombre valor para filtrar "name"
     * @return items filtrados
     */
    public List<Map<String, AttributeValue>> scanByAttribute(AmazonDynamoDB client, String tabla, String nombre) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":n", new AttributeValue().withS(nombre));
        ScanRequest request = new ScanRequest()
                .withTableName(tabla)
                .withFilterExpression("name = :n")
                .withExpressionAttributeValues(values);
        return client.scan(request).getItems();
    }

    /**
     * Escanea tabla completa con paginación automática.
     * 
     * @param tableName nombre de la tabla
     * @return todos los items como mapas AttributeValue
     */
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

    // ==================== UPDATE ====================
    /**
     * Actualiza un atributo específico de un item.
     * 
     * @param tableName tabla objetivo
     * @param keyName nombre clave primaria
     * @param keyValue valor clave primaria
     * @param attributeName atributo a actualizar
     * @param newValue nuevo valor (AttributeValue)
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
     * Elimina tabla completa.
     * 
     * @param tableName nombre de la tabla a borrar
     */
    public void deleteTable(String tableName) {
        DeleteTableRequest request = new DeleteTableRequest()
                .withTableName(tableName);
        dynamoDB.deleteTable(request);
    }

    /**
     * Elimina item por clave primaria.
     * 
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
