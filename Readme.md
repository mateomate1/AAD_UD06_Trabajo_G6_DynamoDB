# Repositorio para proyecto de Acceso a Datos

## 🚀 Descripción del Proyecto

Aplicación **Java 17** para gestión de reservas de aulas usando **Amazon DynamoDB** (NoSQL).  
Implementa patrones **DAO**, **Service** y **menú interactivo** con logging profesional **SLF4J**.

## 📁 Estructura del Proyecto

reserva_salas/ # ✅ APLICACIÓN PRINCIPAL
├── src/
│ └── main/
│ └── java/g6/dynamodb/
│ ├── DAO/ # Data Access Objects (CRUD)
│ ├── Model/ # Entidades (@DynamoDBTable)
│ ├── Service/ # Lógica de negocio
│ ├── Style/ # Menú interactivo
│ ├── Util/ # Cliente AWS + Low-Level API
│ └── Dictionary/ # Enums tablas/estados
├── resources/ # logback.xml + properties
├── docker/dynamodb/ # Docker DynamoDB Local
├── design/ # Diagramas BD y UML
└── ApuntesDynamoDB/ # Documentación técnica

text

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

text
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
🎮 Menú Interactivo
text
=== SISTEMA DE RESERVAS DE AULAS ===
1. Gestionar USUARIOS    ➕ Crear/Buscar/Borrar
2. Gestionar AULAS       ➕ ID/Nombre/Capacidad/Edificio
3. Gestionar RESERVAS    ➕ UUID/Fechas/Validación automática
4-6. Crear tablas        ➕ Usuario/Aula/Reserva
0. SALIR
✅ Funcionalidades Implementadas
Operación	Estado
CRUD Usuario	✅
CRUD Aula	✅
CRUD Reserva	✅
UUID automático	✅
Validación fechas	✅
Detectar solapamientos	✅
Estados automáticos	✅
Logging SLF4J	✅
🧪 Ejemplo de Uso
text
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
Equipo G6 - DAM UOC 2026

Mario Garcia

Mateo Ayarra

Samuel Cobreros

Zacaria Daghri

Stack: Java 17 | DynamoDB v1 Mapper | SLF4J/Logback | Maven
Patrón: DAO + Service + Switch Expressions
