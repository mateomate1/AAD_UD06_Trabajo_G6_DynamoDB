package g6.dynamodb.Model;

/**
 * Entidad Usuario para mapeo DynamoDB.
 * 
 * Modelo simple con username (Hash Key) y password (atributo name).
 * Compatible con autenticacion basica del sistema reservas.
 * Mapea a tabla "Usuarios".
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

@DynamoDBTable(tableName = "Usuarios")
public class Usuario {

    private String username;
    private String password;

    /**
     * Retorna username (DynamoDB Hash Key primaria).
     * 
     * @return identificador login
     */
    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    /**
     * Establece username primario.
     * 
     * @param username ID login unico
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retorna password mapeado como "name".
     * 
     * @return credencial hashed
     */
    @DynamoDBAttribute(attributeName = "name")
    public String getPassword() {
        return password;
    }

    /**
     * Establece password (texto plano/Hash).
     * 
     * @param password credencial usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Representacion String para logging.
     * 
     * Formato compatible con Menu.toString() usage.
     * 
     * @return "Usuario [username=abc, password=***]"
     */
    @Override
    public String toString() {
        return "Usuario [username=" + username + ", password=" + password + "]";
    }
}
