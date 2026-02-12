package g6.dynamodb.Model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

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
@DynamoDBTable(tableName = "Usuarios")
public class Usuario {

    private String username;
    private String password;

    public Usuario() {
    }

    public Usuario(String username, String password) {
        this.username = username;
        this.password = encode(password);
    }

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
    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    /**
     * Establece password (texto plano/Hash).
     * 
     * @param contrasena nuevo nombre del usuario
     */
    public void setPassword(String contrasena) {
        this.password = encode(contrasena);
    }

    public String encode(String pass) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                    hexString.append(hex);
                }
                pass = hexString.toString();
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            pass = "FAILED HASH";
        }
        return pass;
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
        return "Usuario [Nombre de usuario=" + username + ", Contrasena=" + password + "]";
    }
}
