package g6.dynamodb.Util;

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

public class AWSClient {
    public final AmazonDynamoDB dynamoDB;
    public final Properties p = new Properties();
    private final File fichProperties = new File("DynamoDBCredentials.properties");
    private final Logger log = LoggerFactory.getLogger(AWSClient.class);

    /**
     * Inicializar cliente
     * 
     * @param local
     * @throws FileNotFoundException
     * @throws IOException
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
     * Buscar un item por un atributo, en este caso nombre, de una con anotaciones
     * de dynamodb
     * 
     * @param <T>
     * @param clazz
     * @param attribute
     * @param value
     * @return
     */
    public <T> List<T> scanByAttribute(Class<T> clazz, String attribute, String value) {

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(attribute + " = :v")
                .withExpressionAttributeValues(
                        Map.of(":v", new AttributeValue().withS(value)));

        return mapper.scan(clazz, scanExpression);
    }

    public <T> List<T> scanTable(Class<T> c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        DynamoDBScanExpression scanExpresion = new DynamoDBScanExpression();
        return mapper.scan(c, scanExpresion);
    }

    /**
     * En caso de ya existir la clase el metodo funciona como un update
     * @param usuario
     */
    public void insertItem(Usuario usuario) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        mapper.save(usuario);
    }

    public void generateTable(Class<?> c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        CreateTableRequest request = mapper.generateCreateTableRequest(c);

        request.setBillingMode(BillingMode.PAY_PER_REQUEST.toString());
        TableUtils.createTableIfNotExists(dynamoDB, request);
    }

    

}
