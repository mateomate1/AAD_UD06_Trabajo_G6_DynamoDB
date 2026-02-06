package g6.dynamodb.Model;

/**
 * Modelo de entidad para representar usuarios en DynamoDB.
 * 
 * Almacena información básica del usuario: identificador único, nombre y apellidos.
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

@DynamoDBTable(tableName = "Usuarios")
public class Usuario {
    
    private String id;
    private String name;
    private String surname;

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return id del usuario (DynamoDB Hash Key)
     */
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id nuevo identificador único
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return nombre del usuario
     */
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param nombre nuevo nombre del usuario
     */
    public void setName(String nombre) {
        this.name = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     * 
     * @return apellidos del usuario
     */
    @DynamoDBAttribute(attributeName = "surname")
    public String getSurname() {
        return surname;
    }

    /**
     * Establece los apellidos del usuario.
     * 
     * @param surname nuevos apellidos
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Genera representación en String del usuario para debugging/logging.
     * 
     * @return String con todos los campos del usuario
     */
    @Override
    public String toString() {
        return "Usuario [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }
}
