package g6.dynamodb.DAO;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import g6.dynamodb.Model.Reserva;

/**
 * DAO para la entidad {@link Reserva} usando {@link DynamoDBMapper}.
 * 
 * Centraliza las operaciones básicas de acceso a datos para reservas (lectura, escritura
 * y borrado) sobre la tabla correspondiente en DynamoDB.[web:46][web:47][web:57]
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class ReservaDAO {
    private final DynamoDBMapper mapper;

    /**
     * Crea una nueva instancia de ReservaDAO con el cliente DynamoDB indicado.
     * 
     * @param dynamoDB cliente {@link AmazonDynamoDB} configurado para la aplicación
     */
    public ReservaDAO(AmazonDynamoDB dynamoDB){
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    /**
     * Busca una reserva por su identificador.
     * 
     * @param id identificador de la reserva
     * @return instancia de {@link Reserva} o null si no existe
     */
    public Reserva findById(String id){
        return mapper.load(Reserva.class, id);
    }

    /**
     * Guarda o actualiza una reserva en la tabla de DynamoDB.
     * 
     * @param r instancia de {@link Reserva} a persistir
     */
    public void save(Reserva r){
        mapper.save(r);
    }

    /**
     * Elimina una reserva existente de la tabla de DynamoDB.
     * 
     * @param e instancia de {@link Reserva} a borrar
     */
    public void delete(Reserva e){
        mapper.delete(e);
    }
}
