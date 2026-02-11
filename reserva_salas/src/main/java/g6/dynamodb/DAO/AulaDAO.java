package g6.dynamodb.DAO;

import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import g6.dynamodb.Model.Aula;

/**
 * DAO para la entidad {@link Aula} usando {@link DynamoDBMapper}.
 * 
 * Proporciona operaciones básicas de acceso a datos (CRUD simplificado) sobre la tabla de aulas
 * en DynamoDB, aislando la lógica de persistencia del resto de la aplicación.[web:46][web:47][web:57]
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class AulaDAO {
    private final DynamoDBMapper mapper;

    /**
     * Crea una nueva instancia de AulaDAO con el cliente DynamoDB indicado.
     * 
     * @param dynamoDB cliente {@link AmazonDynamoDB} configurado para la aplicación
     */
    public AulaDAO(AmazonDynamoDB dynamoDB) {
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    /**
     * Busca un aula por su identificador.
     * 
     * @param id identificador del aula
     * @return instancia de {@link Aula} o null si no existe
     */
    public Aula findById(String id) {
        return mapper.load(Aula.class, id);
    }

    /**
     * Guarda o actualiza un aula en la tabla de DynamoDB.
     * 
     * @param a instancia de {@link Aula} a persistir
     */
    public void save(Aula a) {
        mapper.save(a);
    }

    /**
     * Elimina un aula existente de la tabla de DynamoDB.
     * 
     * @param a instancia de {@link Aula} a borrar
     */
    public void delete(Aula a) {
        mapper.delete(a);
    }

    public List<Aula> scan(){
        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        List<Aula> aulas = mapper.scan(Aula.class, scan);
        return aulas;
    }

    public List<Aula> scan(DynamoDBScanExpression scan ){
        List<Aula> aulas = mapper.scan(Aula.class, scan);
        return aulas;
    }
}
