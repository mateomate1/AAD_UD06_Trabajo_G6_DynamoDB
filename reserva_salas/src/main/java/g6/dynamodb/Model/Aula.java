package g6.dynamodb.Model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

public class Aula {

    private String id;
    private Integer tamano;

    public Aula() {}

    public Aula(String id, Integer tamano) {
        this.id = id;
        this.tamano = tamano;
    }

    @DynamoDBAttribute(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "tamano")
    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }

    @Override
    public String toString() {
        return "Aula [id=" + id + ", tamano=" + tamano + "]";
    }

}
