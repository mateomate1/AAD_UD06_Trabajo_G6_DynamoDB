package g6.dynamodb.Util;

/**
 * Cliente AWS DynamoDB para operaciones CRUD y gestión de tablas.
 * 
 * Proporciona métodos para conectar con DynamoDB (local/remoto), listar tablas,
 * escanear/buscar items, insertar datos y crear tablas automáticamente desde clases anotadas.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra  
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 0.5
 * @since 0.1
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import g6.dynamodb.Model.Usuario;

/**
 * Cliente AWS DynamoDB para operaciones CRUD y gestión de tablas.
 * 
 * Proporciona métodos para conectar con DynamoDB (local/remoto), listar tablas,
 * escanear/buscar items, insertar datos y crear tablas automáticamente desde clases anotadas.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra  
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 0.5
 * @since 0.1
 */
public class AWSClient {
    public final AmazonDynamoDB dynamoDB;
    public final Properties p = new Properties();
    private final File fichProperties = new File("DynamoDBCredentials.properties");
    private final Logger log = LoggerFactory.getLogger(AWSClient.class);

    /**
     * Inicializa el cliente DynamoDB (local o AWS real).
     * 
     * Carga credenciales desde DynamoDBCredentials.properties. Para modo local usa
     * endpoint específico, para AWS real usa región us-east-1 con credenciales por
     * defecto.
     * 
     * @param local true para DynamoDB Local, false para AWS real
     * @throws FileNotFoundException si no encuentra el archivo de credenciales
     * @throws IOException           si hay error leyendo el archivo de propiedades
     */
    public AWSClient(boolean local) throws FileNotFoundException, IOException {
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
     * Lista todas las tablas disponibles en DynamoDB.
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
     * Recupera un Usuario específico por ID fijo "USER1" (método de ejemplo).
     * 
     * @return Usuario encontrado (actualmente retorna null - pendiente
     *         implementación)
     */
    public Usuario getItemById() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue("USER1"));

        GetItemRequest request = new GetItemRequest()
                .withTableName("Usuarios")
                .withKey(key);

        GetItemResult result = dynamoDB.getItem(request);
        System.out.println(result.getItem());
        Map<String, AttributeValue> salida = result.getItem();

        return null; // Pendiente: convertir AttributeValue a Usuario
    }

    /**
     * Escanea tabla filtrando por nombre usando expresiones nativas.
     * 
     * @param client cliente DynamoDB
     * @param tabla  nombre de la tabla
     * @param nombre valor a buscar en atributo "name"
     * @return lista de items que coinciden
     */
    public List<Map<String, AttributeValue>> scanPorNombre(AmazonDynamoDB client, String tabla, String nombre) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":n", new AttributeValue().withS(nombre));

        ScanRequest request = new ScanRequest()
                .withTableName(tabla)
                .withFilterExpression("name = :n")
                .withExpressionAttributeValues(values);

        return client.scan(request).getItems();
    }

    /**
     * Busca items por atributo usando DynamoDBMapper (genérico).
     * 
     * @param <T>       tipo de la clase modelo anotada
     * @param clazz     clase modelo (ej: Usuario.class)
     * @param attribute nombre del atributo a filtrar
     * @param value     valor a buscar
     * @return lista de objetos que coinciden con el filtro
     */
    public <T> List<T> scanByAttribute(Class<T> clazz, String attribute, String value) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(attribute + " = :v")
                .withExpressionAttributeValues(
                        Map.of(":v", new AttributeValue().withS(value)));

        return mapper.scan(clazz, scanExpression);
    }

    /**
     * Escanea tabla completa con paginación (método nativo).
     * 
     * @param tableName nombre de la tabla
     * @return todos los items de la tabla como mapas de atributos
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

    /**
     * Escanea tabla completa usando DynamoDBMapper.
     * 
     * @param <T> tipo de la clase modelo
     * @param c   clase modelo anotada (ej: Usuario.class)
     * @return lista de objetos de la clase
     */
    public <T> List<T> scanTable(Class<T> c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        DynamoDBScanExpression scanExpresion = new DynamoDBScanExpression();
        return mapper.scan(c, scanExpresion);
    }

    /**
     * Inserta un item en la tabla "Usuarios".
     * 
     * @param item mapa con atributos y valores del item
     */
    public void insertItem(Map<String, AttributeValue> item) {
        PutItemRequest request = new PutItemRequest()
                .withTableName("Usuarios")
                .withItem(item);

        dynamoDB.putItem(request);
    }

    /**
     * Crea tabla automáticamente desde clase modelo anotada.
     * 
     * Configura modo PAY_PER_REQUEST automáticamente.
     * 
     * @param c clase modelo con anotaciones @DynamoDBTable
     */
    public void generateTable(Class<?> c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        CreateTableRequest request = mapper.generateCreateTableRequest(c);

        request.setBillingMode(BillingMode.PAY_PER_REQUEST.toString());
        TableUtils.createTableIfNotExists(dynamoDB, request);
    }

    /**
     * Método de prueba para creación manual de tabla (ejemplo).
     */
    public void test() {
        CreateTableRequest request = new CreateTableRequest()
                .withTableName("Usuarios")
                .withKeySchema(
                        new KeySchemaElement("id", KeyType.HASH))
                .withAttributeDefinitions(
                        new AttributeDefinition("id", ScalarAttributeType.S))
                .withBillingMode(BillingMode.PAY_PER_REQUEST.toString());

        // Ejemplo de items (comentado)
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", new AttributeValue().withS("1"));
        // item.put("name", new AttributeValue().withS("Test"));
    }
}
