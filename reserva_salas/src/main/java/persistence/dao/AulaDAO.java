package persistence.dao

/**
 * DAO para entidad Aula usando DynamoDBMapper.
 * 
 * Proporciona operaciones CRUD basicas (save/load/delete/scan) 
 * sobre tabla de aulas en DynamoDB. Aisla logica de persistencia.
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

import domain.model.Aula;

public class AulaDAO {
    private final DynamoDBMapper mapper;

    /**
     * Constructor con cliente DynamoDB inyectado.
     * 
     * Crea DynamoDBMapper configurado para operaciones Aula.
     * 
     * @param dynamoDB cliente AmazonDynamoDB activo
     */
    public AulaDAO(AmazonDynamoDB dynamoDB) {
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    /**
     * Busca aula por ID primario (hash key).
     * 
     * @param id identificador unico del aula
     * @return Aula encontrada o null si no existe
     */
    public Aula findById(String id) {
        return mapper.load(Aula.class, id);
    }

    /**
     * Persiste o actualiza aula en DynamoDB.
     * 
     * Ejecuta operacion UPSERT (save).
     * 
     * @param a instancia Aula a guardar
     */
    public void save(Aula a) {
        mapper.save(a);
    }

    /**
     * Elimina aula especificada de DynamoDB.
     * 
     * Requiere ID primario cargado en objeto.
     * 
     * @param a instancia Aula a eliminar
     */
    public void delete(Aula a) {
        mapper.delete(a);
    }

    /**
     * Escanea todas las aulas (scan completo).
     * 
     * Usa expresion por defecto (sin filtros).
     * 
     * @return lista completa de aulas
     */
    public List<Aula> scan() {
        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        return mapper.scan(Aula.class, scan);
    }

    /**
     * Escanea aulas con filtros personalizados.
     * 
     * Permite paginacion y condiciones via ScanExpression.
     * 
     * @param scan expresion de escaneo configurada
     * @return lista de aulas filtradas
     */
    public List<Aula> scan(DynamoDBScanExpression scan) {
        return mapper.scan(Aula.class, scan);
    }
}
