package g6.dynamodb.DAO;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import g6.dynamodb.Model.Usuario;

public class UsuarioDAO {
    private final DynamoDBMapper mapper;

    public UsuarioDAO(AmazonDynamoDB dynamoDB) {
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    public Usuario findById(String id) {
        return mapper.load(Usuario.class, mapper);
    }

    public void save(Usuario u) {
        mapper.save(u);
    }

    public void delete(Usuario u) {
        mapper.delete(u);
    }
}
