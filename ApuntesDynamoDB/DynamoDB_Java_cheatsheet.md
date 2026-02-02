# Operaciones principales en DynamoDB (AWS SDK Java 1.12)

Este documento resume las **operaciones** mas comunes de **DynamoDB**, diferenciando claramente entre:

- Uso *con* **Clases anotadas** (DynamoMapper)
- Uso *sin* **Clases anotadas** (AmazonWebServiceResult)

---

## 0. Anotaciones para clases java

Estas anotaciones son para **mappear la clase**, por lo que las anotaciones **no van en el atributo**, sino en los **getters**, a excepcion de la anotacion de la propia clase que genera una tabla.

- `@DynamoDBTable`: Indica que es una tabla, debe incluir la subanotacion `(tableName = "NombreTabla")`
- `@DynamoDBHashKey`: Define que un atributo es clave, debe incluir `(attributeName = "id")`, para especificar el nombre del atributo en la base de datos
- `@DynamoDBAttribute`: Define que se trata de un atributo normal, debe ir acompañado de  `(attributeName = "edad")` para definir su nombre en la base de datos

**Ejemplo de clase con anotaciones:**

````java
@DynamoDBTable(tableName = "Usuarios")
public class Usuario {

    private String id;
    private Integer edad;

    // String se convierte en atributo S
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    // Integer se convierte en atributo N
    @DynamoDBAttribute(attributeName = "edad")
    public Integer getEdad() {
        return edad;
    }
}

````

## 1. Crear tabla y sus items

### Sin anottaciones

**Creacion del request para crear la tabla:** Para esto habra que darle nombre a la clase y especificar la clave de la tabla

````java
//Crear el request para crear una tabla
CreateTableRequest request = new CreateTableRequest()
    //Nombre de la tabla
    .withTableName("Usuarios")
    //Aqui se define el atributo clave de la tabla
    .withKeySchema(
        new KeySchemaElement("id", KeyType.HASH)
        //En caso de querer usar varias claves se añaden aqui
        ,new KeySchemaElement("correo", KeyType.HASH)
    )
    //Definicion del atributo clave(el tipo de clave S/N/B)
    .withAttributeDefinitions(
        //Con clave como number
        new AttributeDefinition("id", ScalarAttributeType.N)
        //Con clave como String
        new AttributeDefinition("correo", ScalarAttributeType.S),
    )
````

**Añadir el modo de cobro:**

- **Sin limite de uso:** ajusta el presupuesto al uso y permite una capacidad "infinita"

    ````java
    .withBillingMode(BillingMode.PAY_PER_REQUEST.toString());
    ````

- **Capacidad limitada:** limita el uso de la base de datos y cobra lo mismo se use lo que se use sin permitir sobrepasar dicho limite

    ````java
    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
    ````

**Crear un item:**

- Clave primaria String:

    ````java
    Map<String, AttributeValue> item = new HashMap<>();
    item.put("id", new AttributeValue().withS("XXX"));
    ````

- Clave primaria Number:

    ````java
    Map<String, AttributeValue> item = new HashMap<>();
    item.put("id", new AttributeValue().withN("1"));
    ````

- Clave compuesta:

````java
Map<String, AttributeValue> item = new HashMap<>();
item.put("userId", new AttributeValue().withS("USER1"));
item.put("timestamp", new AttributeValue().withN("123456789"));
````

### Con anottaciones

**Creacion del request de la tabla:**

````java
AmazonDynamoDB dynamoDB = ...;
DynamoDBMapper mapper = new DynamoDBMapper();
        CreateTableRequest request = mapper.generateCreateTableRequest(clase.class);

        request.setBillingMode(BillingMode.PAY_PER_REQUEST.toString());
        TableUtils.createTableIfNotExists(dynamoDB, request);
````

IMPORTANT: Creacion de item para una tabla

## Leer tablas y sus items

**Recuperar las tablas existentes:**

````java
ListTablesResult resultado = dynamoDB.listTables();
resultado.getTableNames()
//Para recuperarlos hay que iterar sobre resultado Ej:
List<String> salida = resultado.getTableNames().stream().forEach(salida::add);
````

### Sin anotaciones

1. **Recuperar un item por clave primaria:**

    ````java
    Map<String, AttributeValue> key = new HashMap<>();
    key.put("id", new AttributeValue("CLAVEaBUSCAR"));

    GetItemRequest request = new GetItemRequest()
            .withTableName("NombreTabla")
            .withKey(key);
    //Esta clase devuelve
    GetItemResult result = dynamoDB.getItem(request);
    Map<String, AttributeValue> salida = result.getItem()
    ````

2. **Recuperar varios items por clave primaria (Bach):**

    ````java
    KeysAndAttributes keys = new KeysAndAttributes()
        .withKeys(
            Map.of("id", new AttributeValue().withS("USER1")),
            Map.of("id", new AttributeValue().withS("USER2"))
        );

    BatchGetItemRequest request = new BatchGetItemRequest()
        .withRequestItems(
            Map.of("Usuarios", keys)
        );

    BatchGetItemResult result = dynamoDB.batchGetItem(request);
    ````

3. **Recuperar todos los elementos de la tabla(Scan):**

    ````java
    ScanRequest request = new ScanRequest()
        .withTableName("Usuarios");
    ScanResult result = dynamoDB.scan(request);

    for (Map<String, AttributeValue> item : result.getItems()) {
        System.out.println(item.get("id").getS());
    }

    ````

4. **Scan con filtro:**

    ````java
    Map<String, AttributeValue> values = new HashMap<>();
    values.put(":n", new AttributeValue().withS("Mateo"));

    ScanRequest request = new ScanRequest()
        .withTableName("Usuarios")
        .withFilterExpression("name = :n")
        .withExpressionAttributeValues(values);

    ScanResult result = dynamoDB.scan(request);
    ````

### Con anotaciones

1. **Recuperar un item por clave primaria:**

    ````java
    Usuario u = mapper.load(Clase.class, "CLAVEaBUSCAR");
    ````

2. **Recuperar varios items por clave primaria (Bach):**

    ````java
    List<Usuario> usuarios = mapper.batchLoad(
    List.of(
        new Usuario() {{ setId("USER1"); }},
        new Usuario() {{ setId("USER2"); }}
    )).get("Usuarios");
    ````

3. **Recuperar todos los elementos de la tabla(Scan):**

    ````java
    DynamoDBScanExpression scan = new DynamoDBScanExpression();
    List<Usuario> usuarios = mapper.scan(Usuario.class, scan);
    ````

4. **Scan con filtro:**
