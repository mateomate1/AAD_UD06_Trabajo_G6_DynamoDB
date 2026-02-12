package persistence.dao

/**
 * DAO para entidad Reserva usando DynamoDBMapper.
 * 
 * Proporciona operaciones **CRUD** completas:
 * - **Crear**: save() para nueva reserva
 * - **Leer**: findById() / scan() para busquedas
 * - **Actualizar**: save() para modificar existente  
 * - **Borrar**: delete() para eliminar
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import domain.model.Reserva;

public class ReservaDAO {
    private final DynamoDBMapper mapper;

    /**
     * Constructor con cliente DynamoDB inyectado.
     * 
     * Inicializa DynamoDBMapper para operaciones Reserva.
     * 
     * @param dynamoDB cliente AmazonDynamoDB activo
     */
    public ReservaDAO(AmazonDynamoDB dynamoDB) {
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    /**
     * Busca reserva por ID primario (hash key).
     * 
     * @param id identificador unico de reserva
     * @return Reserva encontrada o null si no existe
     */
    public Reserva findById(String id) {
        return mapper.load(Reserva.class, id);
    }

    /**
     * Persiste o actualiza reserva en DynamoDB.
     * 
     * Ejecuta operacion UPSERT (save).
     * 
     * @param r instancia Reserva a guardar
     */
    public void save(Reserva r) {
        mapper.save(r);
    }

    /**
     * Elimina reserva especificada de DynamoDB.
     * 
     * Requiere ID primario cargado en objeto.
     * 
     * @param r instancia Reserva a eliminar
     */
    public void delete(Reserva r) {
        mapper.delete(r);
    }

    /**
     * Escanea todas las reservas (scan completo).
     * 
     * Usa expresion por defecto (sin filtros).
     * 
     * @return lista completa de reservas
     */
    public List<Reserva> scan() {
        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        return mapper.scan(Reserva.class, scan);
    }

    /**
     * Escanea reservas con filtros personalizados.
     * 
     * Soporta paginacion y condiciones via ScanExpression.
     * 
     * @param scan expresion de escaneo configurada
     * @return lista de reservas filtradas
     */
    public List<Reserva> scan(DynamoDBScanExpression scan) {
        return mapper.scan(Reserva.class, scan);
    }
}
