package g6.dynamodb.Model;

/**
 * Modelo de entidad para representar reservas de aulas en DynamoDB.
 * 
 * Contiene información completa de una reserva: identificador único, fechas de inicio/fin,
 * número de personas, aula reservada y usuario que realiza la reserva.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra  
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 0.3
 * @since 0.1
 */
import java.util.Date;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "reserva")
public class Reserva {

    private String id;
    // Para fechas usaremos Date ya que DynamoDB no soporta LocalDateTime
    private Date fechaInicio;
    private Date fechaFin;
    private Integer nPersonas;
    private Aula aula;
    private Usuario usuario;

    /**
     * Constructor vacío requerido por DynamoDB Mapper.
     */
    public Reserva() {
    }

    /**
     * Constructor completo con todos los parámetros de la reserva.
     * 
     * @param id          identificador único de la reserva (partition key)
     * @param fechaInicio fecha y hora de inicio de la reserva
     * @param fechaFin    fecha y hora de finalización de la reserva
     * @param nPersonas   número de personas que usarán el aula
     * @param aula        objeto Aula reservada
     * @param usuario     objeto Usuario que realiza la reserva
     */
    public Reserva(String id, Date fechaInicio, Date fechaFin, Integer nPersonas,
            Aula aula, Usuario usuario) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nPersonas = nPersonas;
        this.aula = aula;
        this.usuario = usuario;
    }

    /**
     * Obtiene el identificador único de la reserva.
     * 
     * @return id de la reserva (DynamoDB Hash Key)
     */
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único de la reserva.
     * 
     * @param id nuevo identificador único
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha y hora de inicio de la reserva.
     * 
     * @return fechaInicio como objeto Date
     */
    @DynamoDBAttribute(attributeName = "fechaInicio")
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha y hora de inicio de la reserva.
     * 
     * @param fechaInicio nueva fecha de inicio
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha y hora de finalización de la reserva.
     * 
     * @return fechaFin como objeto Date
     */
    @DynamoDBAttribute(attributeName = "fechaFin")
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha y hora de finalización de la reserva.
     * 
     * @param fechaFin nueva fecha de fin
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene el número de personas para la reserva.
     * 
     * @return número de personas como Integer
     */
    @DynamoDBAttribute(attributeName = "nPersonas")
    public Integer getnPersonas() {
        return nPersonas;
    }

    /**
     * Establece el número de personas para la reserva.
     * 
     * @param nPersonas nuevo número de personas
     */
    public void setnPersonas(Integer nPersonas) {
        this.nPersonas = nPersonas;
    }

    /**
     * Obtiene el objeto Aula reservado.
     * 
     * @return objeto Aula asociado a la reserva
     */
    @DynamoDBAttribute(attributeName = "aula")
    public Aula getAula() {
        return aula;
    }

    /**
     * Establece el aula reservada.
     * 
     * @param aula nuevo objeto Aula
     */
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    /**
     * Obtiene el objeto Usuario que realiza la reserva.
     * 
     * @return objeto Usuario asociado
     */
    @DynamoDBAttribute(attributeName = "usuario")
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que realiza la reserva.
     * 
     * @param usuario nuevo objeto Usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Genera representación en String de la reserva para debugging/logging.
     * 
     * @return String con todos los campos de la reserva
     */
    @Override
    public String toString() {
        return "Reserva [id=" + id + ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin + ", nPersonas=" + nPersonas +
                ", aula=" + aula + ", usuario=" + usuario + "]";
    }
}
