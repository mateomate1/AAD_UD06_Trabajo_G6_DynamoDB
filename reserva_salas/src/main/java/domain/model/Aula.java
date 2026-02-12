package domain.model

/**
 * Entidad Aula para mapeo DynamoDB.
 * 
 * Representa aulas disponibles para reservas con ID unico, nombre/codigo,
 * capacidad maxima y ubicacion/edificio. Mapea a tabla "Aulas".
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Aulas")
public class Aula {

    private String id;
    private String nombre;
    private Integer capacidad;
    private String edificio;

    /**
     * Retorna ID primario del aula (DynamoDB Hash Key).
     * 
     * @return identificador unico del aula
     */
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    /**
     * Establece ID primario del aula.
     * 
     * @param id identificador unico
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna nombre/codigo del aula.
     * 
     * @return codigo como "A-101", "Lab1"
     */
    @DynamoDBAttribute(attributeName = "nombre")
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece nombre/codigo del aula.
     * 
     * @param nombre codigo del aula
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna capacidad maxima del aula.
     * 
     * @return numero maximo de personas
     */
    @DynamoDBAttribute(attributeName = "capacidad")
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * Establece capacidad maxima del aula.
     * 
     * @param capacidad numero maximo personas
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Retorna edificio/ubicacion del aula.
     * 
     * @return edificio o planta
     */
    @DynamoDBAttribute(attributeName = "edificio")
    public String getEdificio() {
        return edificio;
    }

    /**
     * Establece edificio/ubicacion del aula.
     * 
     * @param edificio ubicacion fisica
     */
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    /**
     * Genera representacion String para logging/debug.
     * 
     * @return formato "Aula [id=A-101, nombre=Lab1, ...]"
     */
    @Override
    public String toString() {
        return "Aula [id=" + id + ", nombre=" + nombre + 
               ", capacidad=" + capacidad + ", edificio=" + edificio + "]";
    }
}
