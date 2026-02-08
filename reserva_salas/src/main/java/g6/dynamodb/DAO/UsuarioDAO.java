package g6.dynamodb.DAO;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import g6.dynamodb.Model.Usuario;

/**
 * DAO para la entidad {@link Usuario} usando {@link DynamoDBMapper}.
 * 
 * Encapsula las operaciones de acceso a datos para usuarios, permitiendo cargar, guardar
 * y borrar elementos de la tabla de usuarios en DynamoDB.[web:46][web:47][web:57]
 * 
 * OJO: Se corrige la carga por id para usar el valor de clave primaria en lugar del mapper.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class UsuarioDAO {
    private final DynamoDBMapper mapper;

    /**
     * Crea una nueva instancia de UsuarioDAO con el cliente DynamoDB indicado.
     * 
     * @param dynamoDB cliente {@link AmazonDynamoDB} configurado para la aplicación
     */
    public UsuarioDAO(AmazonDynamoDB dynamoDB) {
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    /**
     * Busca un usuario por su identificador.
     * 
     * @param id identificador del usuario
     * @return instancia de {@link Usuario} o null si no existe
     */
    public Usuario findById(String id) {
        // Corregido: antes usaba mapper como segundo parámetro
        return mapper.load(Usuario.class, id);
    }

    /**
     * Guarda o actualiza un usuario en la tabla de DynamoDB.
     * 
     * @param u instancia de {@link Usuario} a persistir
     */
    public void save(Usuario u) {
        mapper.save(u);
    }

    /**
     * Elimina un usuario existente de la tabla de DynamoDB.
     * 
     * @param u instancia de {@link Usuario} a borrar
     */
    public void delete(Usuario u) {
        mapper.delete(u);
    }
}
