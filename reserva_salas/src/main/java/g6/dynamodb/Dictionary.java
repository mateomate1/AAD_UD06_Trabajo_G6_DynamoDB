package g6.dynamodb;

/**
 * Diccionario de constantes para nombres de tablas y estados de reservas.
 * 
 * Centraliza los valores fijos del sistema evitando "magic strings" en el código.
 * Permite fácil mantenimiento y refactorización de nombres de tablas/estados.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class Dictionary {
    
    /**
     * Enumeración de nombres de tablas DynamoDB.
     * 
     * Proporciona nombres estandarizados para las tablas del sistema de reservas.
     */
    public enum Tablas {
        /** Tabla de usuarios del sistema. */
        USUARIOS("Usuarios"),
        /** Tabla de aulas disponibles. */
        AULAS("Aulas"),
        /** Tabla de reservas de aulas. */
        RESERVAS("Reservas");

        private final String nombre;

        Tablas(String nombre) {
            this.nombre = nombre;
        }

        /**
         * Retorna el nombre real de la tabla en DynamoDB.
         * 
         * @return nombre de tabla como String
         */
        @Override
        public String toString() {
            return nombre;
        }
    }

    /**
     * Enumeración de estados posibles para reservas.
     * 
     * Define el ciclo de vida de una reserva: PENDIENTE → ACEPTADA/RECHAZADA.
     */
    public enum Estado {
        /** Reserva recién creada, esperando validación. */
        PENDIENTE("Pendiente"),
        /** Reserva validada sin solapamientos, confirmada. */
        ACEPTADA("Aceptada"),
        /** Reserva rechazada por fechas inválidas o solapamiento. */
        RECHAZADA("Rechazada");

        private final String valor;

        Estado(String valor) {
            this.valor = valor;
        }

        /**
         * Retorna el valor legible del estado.
         * 
         * @return descripción del estado
         */
        public String getValor() {
            return valor;
        }

        /**
         * Retorna el valor como String para persistencia.
         * 
         * @return valor del estado
         */
        @Override
        public String toString() {
            return valor;
        }
    }
}
