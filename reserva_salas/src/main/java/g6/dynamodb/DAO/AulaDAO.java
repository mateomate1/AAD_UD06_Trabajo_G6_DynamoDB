package g6.dynamodb.DAO;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import g6.dynamodb.Model.Aula;

public class AulaDAO {
    private final DynamoDBMapper mapper;

    public AulaDAO(AmazonDynamoDB dynamoDB) {
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    public Aula findById(String id) {
        return mapper.load(Aula.class, id);
    }

    public void save(Aula a) {
        mapper.save(a);
    }

    public void delete(Aula a) {
        mapper.delete(a);
    }

}
