# Operaciones CRUD en DynamoDB

Este fichero pretende ser una brebe guia sobre como utilizar los metodos CRUD por la interfaz de comandos de AWS(AWS CLI), cuyo nombre significa Amazon Web Services – Command Line Interface

## Comandos previos al CRUD

Muchos de los comandos van a requerir json en su contexto, en mi caso no logro hacer que funcione, puede ser por las comillas del json o porque la powershell no recoge bien los json. Sin embargo se puede insertar con un fichero a parte poniendo ***file://*** y la direccion del fichero ya sea relativa o absoluta. Pese a todo, segun el comando ***help*** si es posible introducirlo como json encerrando el json en comillas dobles.

## Create

### Crear tabla

````bash
# Se usan saltos de linea para mayor claridad, el comando debe ocupar una unica linea
aws dynamodb create-table
  --table-name NombreTabla
  --attribute-definitions AttributeName=id,AttributeType=S
  --key-schema AttributeName=id,KeyType=HASH
  --billing-mode PAY_PER_REQUEST
# En caso de usar una DynamoDB en local añadir:
  --endpoint-url http://localhost:8000
````

### Crear item

Este comando inserta un item en una tabla usando un fichero json, pues el json puede dar errores en la terminal

````bash
# Se usan saltos de linea para mayor claridad, el comando debe ocupar una unica linea
aws dynamodb put-item
  --table-name NombreTabla
  --item file://inserts/item.json
# En caso de usar una DynamoDB en local añadir:
  --endpoint-url http://localhost:8000
````

Ejemplo de un json:

````json
// Para ver mas tipos de atributos consultar el fichero "TyposItem.json"
{
    // Atributo obligatorio, no tiene porque llamarse id ni estar compuesto por
    // texto pero es identificativo y si se repite reescribira al anterior
    "id": {
        "S": "USER2"
    },
    "nombre": {
        "S": "Pepe"
    },
    "Edad": {
        "N": 12
    }
}
````

## Read

### Mostrar tabla

Comandos para mostrar las tablas y su información

````bash
aws dynamodb list-tables
# En caso de usar una DynamoDB en local añadir:
  --endpoint-url http://localhost:8000
#El puerto por defecto es el 8000, en caso de haberlo cambiado ya no seria ese el endpoint
````

### Mostrar item

Este comando lee todos los items de una tabla

````bash
# Se usan saltos de linea para mayor claridad, el comando debe ocupar una unica linea
aws dynamodb scan
  --table-name NombreTabla
# En caso de usar una DynamoDB en local añadir:
  --endpoint-url http://localhost:8000
````

**En servidor de AWS:**

## Update

````bash
aws dynamodb update-item
  --table-name Usuarios
  --key file://updates/key.json
  --update-expression "SET atributo = :a"
  --expression-attribute-values file://updates/values.json
````

Es importante que el fichero values.json contenga un contenido tal que:

````json
{
  ":a": { "TYPO": "NUEVO_VALOR" }
}
````

El fichero key.json solo debe contener la clave de esta manera:

````json
{
  "id": { "S": "X"}
}
````

## Delete

### Eliminar Tabla

````bash
aws dynamodb delete-item
  --table-name Usuarios
  --key file://deletes/fichero.json
# En caso de usar una DynamoDB en local añadir:
  --endpoint-url http://localhost:8000
````

### Eliminar Item

````bash
aws dynamodb delete-item
  --table-name Usuarios
  --key file://deletes/fichero.json
# En caso de usar una DynamoDB en local añadir:
  --endpoint-url http://localhost:8000
````

## Resumen

| Operacion      | AWS CLI         | Java           |
| -------------- | --------------- | -------------- |
| Listar tablas  | list-tables     | listTables     |
| Crear tabla    | create-table    | createTable    |
| Insertar item  | put-item        | putItem        |
| Leer item      | get-item / scan | getItem / scan |
| Actualizar     | update-item     | updateItem     |
| Eliminar item  | delete-item     | deleteItem     |
| Eliminar tabla | delete-table    | deleteTable    |
