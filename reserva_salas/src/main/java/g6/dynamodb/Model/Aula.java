package g6.dynamodb.Model;

/**
 * Modelo de entidad para representar aulas en DynamoDB.
 * 
 * Almacena información de las aulas disponibles para reserva: identificador único,
 * nombre/código del aula, capacidad máxima y ubicación/edificio.
 * Utilizado como parte del sistema de reservas de aulas.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra  
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 0.3
 * @since 0.1
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
     * Obtiene el identificador único del aula.
     * 
     * @return id del aula (DynamoDB Hash Key)
     */
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del aula.
     * 
     * @param id nuevo identificador único
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre/código del aula.
     * 
     * @return nombre del aula (ej: "A-101", "Lab1")
     */
    @DynamoDBAttribute(attributeName = "nombre")
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre/código del aula.
     * 
     * @param nombre nuevo nombre del aula
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la capacidad máxima del aula.
     * 
     * @return capacidad en número de personas
     */
    @DynamoDBAttribute(attributeName = "capacidad")
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * Establece la capacidad máxima del aula.
     * 
     * @param capacidad nueva capacidad máxima
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Obtiene el edificio/ubicación del aula.
     * 
     * @return edificio o ala donde se encuentra (ej: "Edificio A", "Planta 1")
     */
    @DynamoDBAttribute(attributeName = "edificio")
    public String getEdificio() {
        return edificio;
    }

    /**
     * Establece el edificio/ubicación del aula.
     * 
     * @param edificio nuevo edificio/ubicación
     */
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    /**
     * Genera representación en String del aula para debugging/logging.
     * 
     * @return String con todos los campos del aula
     */
    @Override
    public String toString() {
        return "Aula [id=" + id + ", nombre=" + nombre + ", capacidad=" + capacidad + 
               ", edificio=" + edificio + "]";
    }
}
