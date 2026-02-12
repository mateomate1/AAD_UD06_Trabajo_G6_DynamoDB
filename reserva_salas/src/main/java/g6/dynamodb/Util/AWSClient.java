package g6.dynamodb.Util;

/**
Cliente AWS DynamoDB (High-Level API con DynamoDBMapper).
* Proporciona CRUD generico + creacion tablas automatica desde clases @DynamoDBTable.
* Soporta DynamoDB Local/AWS real via credenciales properties. Centraliza acceso DAOs/Services.
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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class AWSClient {
    public final AmazonDynamoDB dynamoDB;
    public final Properties p = new Properties();
    private final File fichProperties = new File("DynamoDBCredentials.properties");
    private final Logger log = LoggerFactory.getLogger(AWSClient.class);

    /**
     * Constructor principal (local/cloud).
     * Carga DynamoDBCredentials.properties. Local: endpoint custom. Cloud:
     * us-east-1.
     * 
     * @param local true=DynamoDB Local, false=AWS Cloud
     * @throws FileNotFoundException credenciales.properties ausente
     * @throws IOException           error lectura properties
     */
    public AWSClient(boolean local) throws FileNotFoundException, IOException {
        System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
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
     * Cliente DynamoDB configurado.
     * 
     * @return AmazonDynamoDB para DAOs directos
     */
    public AmazonDynamoDB getDynamoDB() {
        return dynamoDB;
    }

    // ==================== CREATE ====================
    /**
     * Crea tabla automatica desde clase @DynamoDBTable.
     * Usa TableUtils + PAY_PER_REQUEST billing.
     * 
     * @param c clase modelo anotada (Usuario.class, Aula.class, etc.)
     */
    public void generateTable(Class<?> c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        CreateTableRequest request = mapper.generateCreateTableRequest(c);
        request.setBillingMode(BillingMode.PAY_PER_REQUEST.toString());
        TableUtils.createTableIfNotExists(dynamoDB, request);
    }

    /**
     * Inserta/actualiza item generico (save).
     * 
     * @param <T>  tipo modelo
     * @param item instancia anotada completa
     */
    public <T> void insertItem(T item) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        mapper.save(item);
    }

    // ==================== READ ====================
    /**
     * Metodo que hace un describe table y si no existe controla la excepcion
     * devolviendo true o false
     * 
     * @param <T>
     * @param dynamoDB
     * @param clazz
     * @return
     */
    public <T> boolean existeTabla(Class<T> clazz) {
        try {
            DynamoDBTable tableAnnotation = clazz.getAnnotation(DynamoDBTable.class);

            if (tableAnnotation == null) {
                return false;
            }

            String nombreTabla = tableAnnotation.tableName();

            DescribeTableRequest request = new DescribeTableRequest().withTableName(nombreTabla);
            dynamoDB.describeTable(request);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean existeTabla(String nombreTabla) {
        try {
            DescribeTableRequest request = new DescribeTableRequest().withTableName(nombreTabla);
            dynamoDB.describeTable(request);
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    /**
     * Lista todas las tablas disponibles en DynamoDB.
     *
     * @return List<String> nombres tablas
     */
    public List<String> listTables() {
        ListTablesResult resultado = dynamoDB.listTables();
        List<String> salida = new ArrayList<>();
        resultado.getTableNames().stream().forEach(salida::add);
        return salida;
    }

    /**
     * Escanea filtrando por atributo (ScanExpression).
     * 
     * @param <T>       tipo modelo
     * @param clazz     clase anotada
     * @param attribute nombre atributo filtrar
     * @param value     valor String buscar
     * @return List<T> items coincidentes
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
    Escanea tabla completa sin filtros.
    * @param <T> tipo modelo
    * @param c clase anotada
    * @return List<T> todos items tabla
    */
    public <T> List<T> scanTable(Class<T> c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(c, scanExpression);
    }

    // ==================== UPDATE ====================
    /**
     * Actualiza item existente (save sobrescribe).
     * 
     * @param <T>  tipo modelo
     * @param item instancia con cambios
     */
    public <T> void updateItem(T item) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        mapper.save(item);
    }

    // ==================== DELETE ====================
    /**
     * Borra item por instancia.
     * 
     * @param <T>  tipo modelo
     * @param item instancia con clave primaria valida
     */
    public <T> void deleteItem(T item) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        mapper.delete(item);
    }

    /**
     * Elimina tabla desde clase modelo.
     * 
     * @param c clase @DynamoDBTable correspondiente
     */
    public void deleteTable(Class<?> c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        DeleteTableRequest request = mapper.generateDeleteTableRequest(c);
        dynamoDB.deleteTable(request);
    }
}
