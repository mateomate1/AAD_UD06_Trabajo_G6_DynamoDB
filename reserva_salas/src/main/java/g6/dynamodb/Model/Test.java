package g6.dynamodb.Model;

/**
 * Entidad Test para pruebas DynamoDB.
 * 
 * Modelo simple de prueba con clave primaria y dos atributos String.
 * Mapea a tabla "Test" para validacion rapida de DAOs/mapper.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Test")
public class Test {
    private String clave;
    private String atr1;
    private String atr2;

    /**
     * Retorna clave primaria (Hash Key).
     * 
     * @return identificador unico
     */
    @DynamoDBHashKey(attributeName = "clave")
    public String getClave() {
        return clave;
    }

    /**
     * Establece clave primaria.
     * 
     * @param clave ID unico String
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Retorna primer atributo de prueba.
     * 
     * @return valor atr1
     */
    @DynamoDBAttribute(attributeName = "atr1")
    public String getAtr1() {
        return atr1;
    }

    /**
     * Establece primer atributo.
     * 
     * @param atr1 valor String
     */
    public void setAtr1(String atr1) {
        this.atr1 = atr1;
    }

    /**
     * Retorna segundo atributo de prueba.
     * 
     * @return valor atr2
     */
    @DynamoDBAttribute(attributeName = "atr2")
    public String getAtr2() {
        return atr2;
    }

    /**
     * Establece segundo atributo.
     * 
     * @param atr2 valor String
     */
    public void setAtr2(String atr2) {
        this.atr2 = atr2;
    }

    /**
     * Representacion String para debugging.
     * 
     * @return formato "Test [clave=abc, atr1=val1, atr2=val2]"
     */
    @Override
    public String toString() {
        return "Test [clave=" + clave + ", atr1=" + atr1 + ", atr2=" + atr2 + "]";
    }
}
