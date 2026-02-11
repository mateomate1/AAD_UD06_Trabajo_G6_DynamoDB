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

    private String username;
    private String password;

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return id del usuario (DynamoDB Hash Key)
     */
    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id nuevo identificador único
     */
    public void setUsername(String id) {
        this.username = id;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return nombre del usuario
     */
    @DynamoDBAttribute(attributeName = "name")
    public String getPassword() {
        return password;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param nombre nuevo nombre del usuario
     */
    public void setPassword(String nombre) {
        this.password = nombre;
    }

    

    /**
     * Genera representación en String del usuario para debugging/logging.
     * 
     * @return String con todos los campos del usuario
     */
    @Override
    public String toString() {
        return "Usuario [id=" + username + ", name=" + password + ", surname=" + "]";
    }
}
