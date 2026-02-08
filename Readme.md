# Repositorio para proyecto de Acceso a Datos

## 🚀 Descripción del Proyecto

Aplicación Java 17 para gestión de reservas de aulas usando **Amazon DynamoDB** (NoSQL).  
Implementa patrones **DAO**, **Service** y **menú interactivo** con logging profesional SLF4J.

## 📁 Estructura del Proyecto

D:.
├───ApuntesDynamoDB/ # Documentación técnica DynamoDB
├───design/ # Diagramas BD y UML
├───docker/dynamodb/ # Docker DynamoDB Local
├───DynamoDB/DynamoDBLocal_lib/ # Librerías DynamoDB Local
└───reserva_salas/ # ✅ APLICACIÓN PRINCIPAL
├───src/
│ └───main/
│ └───java/g6/dynamodb/
│ ├───DAO/ # Data Access Objects (CRUD)
│ ├───Model/ # Entidades (@DynamoDBTable)
│ ├───Service/ # Lógica de negocio
│ ├───Style/ # Menú interactivo
│ ├───Util/ # Cliente AWS + Low-Level API
│ └───Dictionary/ # Enums tablas/estados
└───resources/ # logback.xml + properties
└───target/
├───classes/ # .class compilados
└───test-classes/ # Tests unitarios

text

## 📋 Contenido Técnico

| Carpeta   | Funcionalidad |
|-----------|---------------|
| **DAO**   | `UsuarioDAO`, `AulaDAO`, `ReservaDAO` → `save/findById/delete` |
| **Model** | `Usuario`, `Aula`, `Reserva` → `@DynamoDBTable` |
| **Service**| `ReservaService.crearReserva()` → valida solapamientos |
| **Style** | `Menu.java` → consola interactiva con switch Java 17 |
| **Util**  | `AWSClient` + `AWSClient_SinAnotaciones` (Low-Level API) |

## 🛠️ Requisitos e Instalación

```bash
✅ Java 17+
✅ Maven (mvn clean compile exec:java)
✅ DynamoDB Local (Docker o JAR)
✅ SLF4J + Logback
🚀 Instalación Rápida
Credenciales (obligatorio)
Crea src/main/resources/DynamoDBCredentials.properties:

text
local.accessKeyId=dummy_access_key
local.secretAccessKey=dummy_secret_key
local.region=us-east-1
endpoint=http://localhost:8000
DynamoDB Local (Docker - recomendado)

bash
cd docker/dynamodb
docker-compose up -d
Ejecutar

bash
mvn clean compile exec:java -Dexec.mainClass="g6.dynamodb.Main"
🎮 Menú Interactivo
text
=== SISTEMA DE RESERVAS DE AULAS ===
1. Gestionar USUARIOS    ➕ Crear/Buscar/Borrar
2. Gestionar AULAS       ➕ ID/Nombre/Capacidad/Edificio
3. Gestionar RESERVAS    ➕ UUID/Fechas/Validacion automatica
4-6. Crear tablas        ➕ Usuario/Aula/Reserva
0. SALIR
✅ Funcionalidades Implementadas
Operación	Estado
CRUD Usuario	✅ save/findById/delete
CRUD Aula	✅ save/findById/delete
CRUD Reserva	✅ save/findById/delete
UUID automático	✅ Reservas
Validación fechas	✅ inicio < fin
Detectar solapamientos	✅ Mismo aula
Estados automáticos	✅ ACEPTADA/RECHAZADA
Logging SLF4J	✅ info/warn/error
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
🔧 Configuración AWS Real
En Main.java línea 19:

java
AWSClient aws = new AWSClient(false); // Cloud en lugar de true
🐛 Troubleshooting
Problema	Solución
FALTA DynamoDBCredentials.properties	Crear archivo properties
localhost:8000 refused	docker-compose up
ClassNotFoundException	mvn clean compile
NoSuchMethodError	Java 17+
🏗️ Arquitectura en Capas
text
┌─────────────────┐    ┌──────────────────┐
│   Main.java     │───▶│ AWSClient(true)  │
└─────────────────┘    └──────────────────┘
                          │
                 ┌────────▼────────┐
                 │  generateTable  │
                 └────────┬────────┘
                          │
                 ┌────────────▼────────────┐
                 │ Usuario/Aula/Reserva    │
                 │     @DynamoDBTable      │
                 └────────────┬────────────┘
                          │
                 ┌────────▼────────┐
                 │     DAOs        │
                 │ save/find/delete│
                 └────────┬────────┘
                          │
                 ┌────────▼────────┐
                 │  ReservaService │
                 │  (solapamientos)│
                 └──────────────────┘
📚 Autores
Equipo G6 - DAM UOC 2026

Mario Garcia

Mateo Ayarra

Samuel Cobreros

Zacaria Daghri

Stack Técnico:
Java 17 | DynamoDB v1 Mapper | SLF4J/Logback | Maven
Patrón: DAO + Service + Switch Expressions

