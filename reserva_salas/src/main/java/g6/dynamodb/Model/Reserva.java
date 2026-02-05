package g6.dynamodb.Model;

import java.util.Date;
import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="reserva")
public class Reserva {

    private String id;
    private Date fechaInicio; // hay que usar date que dynamo no entiende localdatetime
    private Date fechaFin;
    private Integer nPersonas;
    private Aula aula;
    private Usuario usuario;

    public Reserva() {}

    public Reserva(String id, Date fechaInicio, Date fechaFin, Integer nPersonas, Aula aula, Usuario usuario) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nPersonas = nPersonas;
        this.aula = aula;
        this.usuario = usuario;
    }

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "fechaInicio")
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @DynamoDBAttribute(attributeName = "fechaFin")
    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @DynamoDBAttribute(attributeName = "nPersonas")
    public Integer getnPersonas() {
        return nPersonas;
    }

    public void setnPersonas(Integer nPersonas) {
        this.nPersonas = nPersonas;
    }

    @DynamoDBAttribute(attributeName = "aula")
    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    @DynamoDBAttribute(attributeName = "usuario")
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Reserva [id=" + id + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", nPersonas=" + nPersonas + ", aula=" + aula + ", usuario=" + usuario + "]";
    }

}
