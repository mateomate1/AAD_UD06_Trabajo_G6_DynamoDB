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

	public AmazonDynamoDB getDB(){
		return this.dynamoDB;
	}

	// METODOS CRUD:
	// ---------------------CREATE---------------------------
	/**
	 * Método de prueba para creación manual de tabla (ejemplo).
	 */
	public void generateTable(String nombreTabla, String nombreAtrClave) {
		CreateTableRequest request = new CreateTableRequest()
				// Nombre de la clase
				.withTableName(nombreTabla)
				// Aqui se define el atributo clave de la tabla
				.withKeySchema(
						new KeySchemaElement(nombreAtrClave, KeyType.HASH))
				// Definicion del atributo clave(el tipo de clave S/N/B)
				.withAttributeDefinitions(
						new AttributeDefinition(nombreAtrClave, ScalarAttributeType.S))
				// Anadir un metodo de pago, en este caso es un metodo dummy
				.withBillingMode(BillingMode.PAY_PER_REQUEST.toString());
		dynamoDB.createTable(request);
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

	// ----------------------READ----------------------------
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
	 * Recupera un Usuario específico por ID fijo "USER1" (método de ejemplo).
	 *
	 * @param id
	 * @param table
	 * @return Usuario encontrado (actualmente retorna null - pendiente
	 *         implementación)
	 */
	public Usuario getItemById(String id, Dictionary.Tablas table) {
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

	/**
	 * Escanea tabla filtrando por nombre usando expresiones nativas.
	 *
	 * @param client cliente DynamoDB
	 * @param tabla  nombre de la tabla
	 * @param nombre valor a buscar en atributo "name"
	 * @return lista de items que coinciden
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

	// ---------------------UPDATE---------------------------
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

	// ---------------------DELETE---------------------------
	public void deleteTable(String tableName) {
		DeleteTableRequest request = new DeleteTableRequest()
				.withTableName(tableName);
		dynamoDB.deleteTable(request);
	}

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
