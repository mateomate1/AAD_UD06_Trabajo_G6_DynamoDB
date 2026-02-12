package g6.dynamodb;

/**
Diccionario constantes tablas/estados reservas.
* Evita magic strings centralizando nombres DynamoDB y estados ciclo vida.
* Facilita mantenimiento/refactorizacion en DAOs/Services/Model.
* @author Mario Garcia
* @author Mateo Ayarra
* @author Samuel Cobreros
* @author Zacaria Daghri
* @version 1.0
* @since 1.0
 */
public class Dictionary {
    
    /**
    Nombres tablas DynamoDB estandarizadas.
    * USUARIOS/AULAS/RESERVAS para AWSClient_SinAnotaciones.getItemById().
    */
    public enum Tablas {
        /** Tabla usuarios sistema. */
        USUARIOS("Usuarios"),
        /** Tabla aulas disponibles. */
        AULAS("Aulas"),
        /** Tabla reservas aulas. */
        RESERVAS("Reservas");

        private final String nombre;

        Tablas(String nombre) {
            this.nombre = nombre;
        }

        /**
        Nombre tabla para DynamoDB operations.
        * @return String nombre tabla
        */
        @Override
        public String toString() {
            return nombre;
        }
    }

    /**
    Estados ciclo vida reservas.
    * PENDIENTE→ACEPTADA/RECHAZADA via ReservaService validacion automatica.
    */
    public enum Estado {
        /** Reserva creada esperando validacion. */
        PENDIENTE("Pendiente"),
        /** Reserva confirmada sin solapamientos. */
        ACEPTADA("Aceptada"),
        /** Reserva rechazada (solapamiento/fechas invalidas). */
        RECHAZADA("Rechazada");

        private final String valor;

        Estado(String valor) {
            this.valor = valor;
        }

        /**
        Display legible estado.
        * @return String descripcion humana
        */
        public String getValor() {
            return valor;
        }

        /**
        Valor persistencia DynamoDB.
        * @return String para Reserva.estado
        */
        @Override
        public String toString() {
            return valor;
        }
    }
}
