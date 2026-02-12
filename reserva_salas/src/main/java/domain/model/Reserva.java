package domain.model;

/**
 * Entidad Reserva para mapeo DynamoDB.
 * 
 * Representa reservas completas de aulas con ID, fechas inicio/fin (ISO String),
 * numero personas, aula reservada, usuario propietario y estado (pendiente/confirmada).
 * Mapea a tabla "reserva".
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

import domain.model.Aula;

@DynamoDBTable(tableName = "reserva")
public class Reserva {

    private String id;
    private String fechaInicio;
    private String fechaFin;
    private Integer nPersonas;
    private Aula aula;
    private Usuario usuario;
    private String estado;

    /**
     * Constructor vacio requerido por DynamoDBMapper.
     */
    public Reserva() {
    }

    /**
     * Constructor completo para nueva reserva.
     * 
     * @param id identificador UUID unico
     * @param fechaInicio ISO-8601 String (ej: "2026-02-11T08:00:00")
     * @param fechaFin ISO-8601 String (ej: "2026-02-11T10:00:00")  
     * @param nPersonas cantidad personas (1-50)
     * @param aula aula reservada
     * @param usuario propietario reserva
     */
    public Reserva(String id, String fechaInicio, String fechaFin, Integer nPersonas,
                   Aula aula, Usuario usuario) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nPersonas = nPersonas;
        this.aula = aula;
        this.usuario = usuario;
    }

    /**
     * Retorna ID primario reserva (Hash Key).
     * 
     * @return UUID String
     */
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    /**
     * Establece ID primario reserva.
     * 
     * @param id UUID unico
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna fecha inicio (ISO-8601 String).
     * 
     * @return fechaInicio formateada
     */
    @DynamoDBAttribute(attributeName = "fechaInicio")
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece fecha inicio reserva.
     * 
     * @param fechaInicio ISO-8601 String
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Retorna fecha fin (ISO-8601 String).
     * 
     * @return fechaFin formateada
     */
    @DynamoDBAttribute(attributeName = "fechaFin")
    public String getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece fecha fin reserva.
     * 
     * @param fechaFin ISO-8601 String
     */
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Retorna numero personas.
     * 
     * @return cantidad Integer
     */
    @DynamoDBAttribute(attributeName = "nPersonas")
    public Integer getnPersonas() {
        return nPersonas;
    }

    /**
     * Establece numero personas.
     * 
     * @param nPersonas cantidad (1-50)
     */
    public void setnPersonas(Integer nPersonas) {
        this.nPersonas = nPersonas;
    }

    /**
     * Retorna aula reservada.
     * 
     * @return objeto Aula
     */
    @DynamoDBAttribute(attributeName = "aula")
    public Aula getAula() {
        return aula;
    }

    /**
     * Establece aula reservada.
     * 
     * @param aula objeto Aula valida
     */
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    /**
     * Retorna usuario propietario.
     * 
     * @return objeto Usuario
     */
    @DynamoDBAttribute(attributeName = "usuario")
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece usuario propietario.
     * 
     * @param usuario objeto Usuario valido
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna estado reserva.
     * 
     * @return "pendiente", "confirmada", "cancelada"
     */
    @DynamoDBAttribute(attributeName = "estado_reserva")
    public String getEstado() {
        return estado;
    }

    /**
     * Establece estado reserva.
     * 
     * @param estado nuevo estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Genera representacion String completa.
     * 
     * @return formato legible para logs
     */
    @Override
    public String toString() {
        return "Reserva [id=" + id + ", fechaInicio=" + fechaInicio + 
               ", fechaFin=" + fechaFin + ", nPersonas=" + nPersonas + 
               ", aula=" + aula + ", usuario=" + usuario + ", estado=" + estado + "]";
    }
}
