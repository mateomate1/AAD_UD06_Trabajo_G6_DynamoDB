package g6.dynamodb.Model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Test")
public class Test {
    private String clave;
    private String atr1;
    private String atr2;

    @DynamoDBHashKey(attributeName = "clave")
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @DynamoDBAttribute(attributeName = "atr1")
    public String getAtr1() {
        return atr1;
    }

    public void setAtr1(String atr1) {
        this.atr1 = atr1;
    }

    @DynamoDBAttribute(attributeName = "atr2")
    public String getAtr2() {
        return atr2;
    }

    public void setAtr2(String atr2) {
        this.atr2 = atr2;
    }

}
