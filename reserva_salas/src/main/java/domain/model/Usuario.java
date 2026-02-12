package domain.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import util.HashUtil;

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
    private String passwordHash;

    public Usuario() {
    }
    /**
     * Constructor con username y password (texto plano).
     * @param username Nombre del usuario en texto plano
     * @param password  Contraseña del usuario en texto plano, se almacenará como hash.
     */
    public Usuario(String username, String password) {
        this.username = username;
        this.passwordHash = encode(password);
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
     * @return password hashed
     */
    @DynamoDBAttribute(attributeName = "name")
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Cambia el password del usuario. Se espera que el nuevo password ya esté codificado como hash.
     * @param contrasena nuevo hash del password del usuario
     */
    public void setPassword(String contrasena) {
        this.passwordHash  = contrasena;
    }


    /**
     * Codifica el password usando SHA-256 y retorna el hash en formato hexadecimal.
     * verifica que el password no sea ya un hash SHA-256 para evitar almacenar hashes precomputados directamente.
     * Pero solo en calidad de aviso ya que no se puede saber exactamente si el password es un hash o no
     * @param pass contraseña en texto plano a codificar
     * @return hash hexadecimal de la contraseña codificada
     */
    public String encode(String pass) {
        if(HashUtil.esSha256(pass)) 
            throw new IllegalArgumentException("La contraseña proporcionada ya parece ser un hash SHA-256. Se recomienda no almacenar hashes precomputados directamente.");
        return HashUtil.encode(pass);
    }

    /**
     * @param password credencial usuario
     */
    public void setPasswordHash(String password) {
        this.passwordHash = password;
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
        return "Usuario [username=" + username + ", password=" + passwordHash + "]";
    }
}
