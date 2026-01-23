# Repositorio para proyecto de Acceso a Datos

## Contenido

- Design: carpeta que contiene los diagramas de base de datos y UML
- reserva_salas: carpeta que contiene la aplicacion a desarrollar. Esta formado por:
  - src: carpeta que engloba los test y los ficheros propios de la aplicacion
    - main: contiene la logica de negocio y los recursos para la misma
      - Carpeta java, contiene la logica de negocio y conexion a base de datos no sql (DynamoDB)
      - Carpeta resources, contiene elementos necesarios para la logica del negocio(logback y otros)
  - target: carpeta que contiene las clases y los jar a exportar
- Fichero properties: contiene las credenciales de inicio de sesion (opciona:aplicar cifrado)
- Readme: Fichero informativo para el repositorio, contiene informacion del contenido de la app

## Requisitos

- Java 17 o mejor
- Tener instalado la base de datos no sql DynamoDB
