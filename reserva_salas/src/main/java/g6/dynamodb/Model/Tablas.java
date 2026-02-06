package g6.dynamodb.Model;

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
