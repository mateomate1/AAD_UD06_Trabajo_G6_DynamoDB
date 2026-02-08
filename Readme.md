# Repositorio para proyecto de Acceso a Datos

## 🚀 Descripción del Proyecto

Aplicación **Java 17** para gestión de reservas de aulas usando **Amazon DynamoDB** (NoSQL).  
Implementa patrones **DAO**, **Service** y **menú interactivo** con logging profesional **SLF4J**.

## 📁 Estructura del Proyecto
````
D:.
├───.vscode
├───ApuntesDynamoDB
│   ├───CREATE
│   └───UPDATE
├───design
├───docker
│   └───dynamodb
├───DynamoDB
│   └───DynamoDBLocal_lib
└───reserva_salas
    ├───src
    │   └───main
    │       ├───java
    │       │   ├───fx
    │       │   │   └───App
    │       │   │       └───ui
    │       │   │           ├───controladores
    │       │   │           └───navegacion
    │       │   └───g6
    │       │       └───dynamodb
    │       │           ├───DAO
    │       │           ├───Model
    │       │           ├───Service
    │       │           ├───Style
    │       │           └───Util
    │       └───resources
    │           ├───css
    │           ├───fxml
    │           └───img
    └───target
        ├───classes
        │   ├───css
        │   ├───fx
        │   │   └───App
        │   │       └───ui
        │   │           ├───controladores
        │   │           └───navegacion
        │   ├───fxml
        │   ├───g6
        │   │   └───dynamodb
        │   │       ├───DAO
        │   │       ├───Model
        │   │       ├───Service
        │   │       ├───Style
        │   │       └───Util
        │   └───img
        ├───maven-status
        │   └───maven-compiler-plugin
        │       └───compile
        │           └───default-compile
        └───test-classes
````
## 📋 Contenido Técnico

| Carpeta     | Funcionalidad |
|-------------|---------------|
| **DAO**     | `UsuarioDAO`, `AulaDAO`, `ReservaDAO` → `save/findById/delete` |
| **Model**   | `Usuario`, `Aula`, `Reserva` → `@DynamoDBTable` |
| **Service** | `ReservaService.crearReserva()` → valida solapamientos |
| **Style**   | `Menu.java` → consola interactiva con switch Java 17 |
| **Util**    | `AWSClient` + `AWSClient_SinAnotaciones` (Low-Level API) |

## 🛠️ Requisitos

```bash
✅ Java 17+
✅ Maven
✅ Docker (DynamoDB Local)
✅ SLF4J + Logback
````

### 🚀 Instalación Rápida
1. Credenciales
Crea src/main/resources/DynamoDBCredentials.properties:

local.accessKeyId=dummy_access_key
local.secretAccessKey=dummy_secret_key
local.region=us-east-1
endpoint=http://localhost:8000

2. DynamoDB Local
bash
cd docker/dynamodb
docker-compose up -d
3. Ejecutar
bash
mvn clean compile exec:java -Dexec.mainClass="g6.dynamodb.Main"

## ✅ Funcionalidades del Menú



| **1**  | **Gestionar USUARIOS** Crear / Buscar / Borrar | ✅ |

| **2**  | **Gestionar AULAS** ID/Nombre/Capacidad/Edificio | ✅ |

| **3**  | **Gestionar RESERVAS** UUID/Fechas/Validación automática | ✅ |

| **0**  | **SALIR** Cerrar aplicación limpia | ✅ |


🧪 Ejemplo de Uso

> Crear Aula: ID=A101, Nombre=A101, Cap=30, Edificio=A
> Crear Reserva: 5 pers, 2026-02-08T10:00→11:00, Aula=A101
> Resultado: ACEPTADA ✓

> Nueva reserva misma aula/horario → RECHAZADA ✗
📊 Logging Profesional
Archivo resources/logback.xml:

text
INFO  | 2026-02-08 | Menu | Tablas: [Usuarios,Aulas,Reservas]
INFO  | Menu | Usuario creado: Juan Perez (ID: USER1)
WARN  | Menu | Aula no encontrada
🔧 AWS Real
En Main.java línea 19:

java
AWSClient aws = new AWSClient(false); // Cloud (false) vs Local (true)
🐛 Troubleshooting
Problema	Solución
FALTA DynamoDBCredentials.properties	Crear archivo properties
localhost:8000 refused	docker-compose up
ClassNotFoundException	mvn clean compile
NoSuchMethodError	Java 17+
🏗️ Arquitectura
text
Main.java → AWSClient → DAOs → Models → Service
         ↓
     generateTable() → [Usuarios,Aulas,Reservas]

📚 Autores
Grupo 6 - DAM 2 - 2026
- Mario Garcia
- Mateo Ayarra
- Samuel Cobreros
- Zacaria Daghri
