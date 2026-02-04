package g6.dynamodb.Model;

import java.time.LocalDateTime;

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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
    public Integer getnPersonas() {
        return nPersonas;
    }
    public void setnPersonas(Integer nPersonas) {
        this.nPersonas = nPersonas;
    }
    public Aula getAula() {
        return aula;
    }
    public void setAula(Aula aula) {
        this.aula = aula;
    }
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
