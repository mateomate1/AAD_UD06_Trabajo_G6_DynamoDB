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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class AWSClient {
    public final AmazonDynamoDB dynamoDB;
    public final Properties p = new Properties();
    private final File fichProperties = new File("DynamoDBCredentials.properties");
    private final Logger log = LoggerFactory.getLogger(AWSClient.class);

    public AWSClient(boolean local) throws FileNotFoundException, IOException {
        p.load(new FileInputStream(fichProperties));
        log.trace("Fichero cargado con exito");

        String accessKey = null;
        String secretKey = null;
        String region = null;
        String endpoint = null;

        if (local) {
            accessKey = p.getProperty("local.accessKeyId");
            secretKey = p.getProperty("local.secretAccessKey");
            region = p.getProperty("local.region");
            endpoint = p.getProperty("endpoint");
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

    public List<String> listTables() {
        ListTablesResult resultado = dynamoDB.listTables();
        List<String> salida = new ArrayList<>();
        resultado.getTableNames().stream().forEach(salida::add);
        return salida;
    }

    public void getItem() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue("USER1"));

        GetItemRequest request = new GetItemRequest()
                .withTableName("Usuarios")
                .withKey(key);

        GetItemResult result = dynamoDB.getItem(request);
        System.out.println(result.getItem());

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

    public void generateTable(Class c) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        CreateTableRequest request = mapper.generateCreateTableRequest(c);

        request.setBillingMode(BillingMode.PAY_PER_REQUEST.toString());
        TableUtils.createTableIfNotExists(dynamoDB, request);
    }

}
