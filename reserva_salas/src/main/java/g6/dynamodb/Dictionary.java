package g6.dynamodb;

public class Dictionary {
    public enum Tablas {
        USUARIOS("Usuarios"),
        AULAS("Aulas"),
        RESERVAS("Reservas");

        private final String nombre;

        Tablas(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public enum Estado {
        PENDIENTE("Pendiente"),
        ACEPTADA("Aceptada"),
        RECHAZADA("Rechazada");

        private final String valor;

        Estado(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        @Override
        public String toString() {
            return valor;
        }
    }

}
