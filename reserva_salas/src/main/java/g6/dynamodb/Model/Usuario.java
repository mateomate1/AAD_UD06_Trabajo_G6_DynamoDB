package g6.dynamodb.Model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

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
    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    /**
     * Establece el nombre del usuario.
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
     * Genera representación en String del usuario para debugging/logging.
     * 
     * @return String con todos los campos del usuario
     */
    @Override
    public String toString() {
        return "Usuario [Nombre de usuario=" + username + ", Contrasena=" + password + "]";
    }
}
