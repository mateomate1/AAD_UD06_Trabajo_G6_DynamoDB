package g6.dynamodb.Model;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="reserva")
public class Reserva {

    private String id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer nPersonas;
    private Aula aula;
    private Usuario usuario;

    public Reserva() {}

    public Reserva(String id, LocalDateTime fechaInicio, LocalDateTime fechaFin, Integer nPersonas, Aula aula, Usuario usuario) {
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
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @DynamoDBAttribute(attributeName = "fechaFin")
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
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
