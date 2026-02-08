# Repositorio para proyecto de Acceso a Datos

## 🚀 Descripción del Proyecto

Aplicación **Java 17** para gestión de reservas de aulas usando **Amazon DynamoDB** (NoSQL).  
Implementa patrones **DAO**, **Service** y **menú interactivo** con logging profesional **SLF4J**.

## 📁 Estructura del Proyecto
````
src
\---main
    \---java
        \---g6
            \---dynamodb
                |   Dictionary.java
                |
                +---DAO
                |       AulaDAO.java
                |       ReservaDAO.java
                |       UsuarioDAO.java
                |
                +---Model
                |       Aula.java
                |       Reserva.java
                |       Test.java
                |       Usuario.java
                |
                +---Service
                |       AulaService.java
                |       ReservaService.java
                |       UsuarioService.java
                |
                +---Style
                |       Menu.java
                |
                \---Util
                        AWSClient.java
                        AWSClient_SinAnotaciones.java
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


## ✅ Funcionalidades de la Aplicación


| **1**  | **Gestionar USUARIOS** Crear / Buscar / Borrar | ✅ |

| **2**  | **Gestionar AULAS** ID/Nombre/Capacidad/Edificio | ✅ |

| **3**  | **Gestionar RESERVAS** UUID/Fechas/Validación automática | ✅ |

| **0**  | **SALIR** Cerrar aplicación limpia | ✅ |


📚 Autores
Grupo 6 - DAM 2 - 2026
- Mario Garcia
- Mateo Ayarra
- Samuel Cobreros
- Zacaria Daghri
