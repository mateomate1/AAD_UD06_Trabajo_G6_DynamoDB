package persistence.dao;

/**
 * DAO para entidad Usuario usando DynamoDBMapper.
 * 
 * Proporciona operaciones CRUD completas:
 * - Crear: save() para nuevo usuario
 * - Leer: findById() por username (hash key)
 * - Actualizar: save() para modificar datos
 * - Borrar: delete() por username
 * 
 * Compatible con modelo Usuario (username/password).
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import domain.model.Usuario;

public class UsuarioDAO {
    private final DynamoDBMapper mapper;

    /**
     * Constructor con cliente DynamoDB inyectado.
     * 
     * Inicializa mapper para operaciones Usuario.
     * 
     * @param dynamoDB cliente AmazonDynamoDB activo
     */
    public UsuarioDAO(AmazonDynamoDB dynamoDB) {
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    /**
     * Carga usuario por username (hash key primaria).
     * 
     * @param id username del usuario
     * @return Usuario encontrado o null
     */
    public Usuario findById(String id) {
        return mapper.load(Usuario.class, id);
    }

    /**
     * Crea o actualiza usuario en DynamoDB (UPSERT).
     * 
     * @param u instancia Usuario completa
     */
    public void save(Usuario u) {
        mapper.save(u);
    }

    /**
     * Borra usuario por username cargado.
     * 
     * @param u instancia Usuario con ID valido
     */
    public void delete(Usuario u) {
        mapper.delete(u);
    }
}
