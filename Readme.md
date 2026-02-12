# Repositorio para proyecto de Acceso a Datos

## 🚀 Descripción del Proyecto

Aplicación **Java 17** para gestión de reservas de aulas usando **Amazon DynamoDB** (NoSQL).  
Implementa patrones **DAO**, **Service** y **menú interactivo** con logging profesional **SLF4J**.

## 📁 Estructura del Proyecto

```` bash
src/main/java
│
├── app
│   ├── Main.java
│   ├── Launch.java
│   └── navegacion
│       └── SceneManager.java
│
├── ui
│   └── controladores
│       ├── LoginController.java
│       └── RegistrarUserController.java
│
├── domain
│   └── model
│      ├── Aula.java
│      ├── Reserva.java
│      └── Usuario.java
│
├── service
│   ├── AulaService.java
│   ├── ReservaService.java
│   └── UsuarioService.java
│
├── persistence
│   ├── dao
│   │   ├── AulaDAO.java
│   │   ├── ReservaDAO.java
│   │   └── UsuarioDAO.java
│   │
│   └── dynamodb
│       ├── AWSClient.java
│       └── AWSClientSinAnotaciones.java
│
└── util
    ├── HashUtil.java
    ├── UsersPasswordsData.java
    └── Dictionary.java
````

## 📋 Contenido Técnico

| Carpeta     | Funcionalidad                                                   |
|-------------|-----------------------------------------------------------------|
| **DAO**     | `UsuarioDAO`, `AulaDAO`, `ReservaDAO` → `save/findById/delete`  |
| **Model**   | `Usuario`, `Aula`, `Reserva` → `@DynamoDBTable`                 |
| **Service** | `ReservaService.crearReserva()` → valida solapamientos          |
| **Style**   | `Menu.java` → consola interactiva con switch Java 17            |
| **Util**    | `AWSClient` + `AWSClient_SinAnotaciones` (Low-Level API)        |

## 🛠️ Requisitos

````bash
✅ Java 17+
✅ Maven
✅ Docker (DynamoDB Local)
✅ SLF4J + Logback
````

## ✅ Funcionalidades de la Aplicación

- [x] **1** **Gestionar USUARIOS** Crear / Buscar / Borrar
- [x] **2** **Gestionar AULAS** ID/Nombre/Capacidad/Edificio
- [x] **3** **Gestionar RESERVAS** UUID/Fechas/Validación automática
- [x] **0** **SALIR** Cerrar aplicación limpia

### 📚 Autores

Grupo 6 - DAM 2 - 2026

- Mario Garcia
- Mateo Ayarra
- Samuel Cobreros
- Zacaria Daghri
