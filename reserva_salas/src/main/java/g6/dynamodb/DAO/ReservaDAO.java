package g6.dynamodb.DAO;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import g6.dynamodb.Model.Reserva;

public class ReservaDAO {
    private final DynamoDBMapper mapper;

    public ReservaDAO(AmazonDynamoDB dynamoDB){
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    public Reserva findById(String id){
        return mapper.load(Reserva.class, id);
    }

    public void save(Reserva r){
        mapper.save(r);
    }

    public void delete(Reserva e){
        mapper.delete(e);
    }

}
