package g6.dynamodb.Service;

/**
 * Servicio de negocio para gestión de reservas de aulas.
 * 
 * Maneja la lógica de creación de reservas, validación de fechas, detección de solapamientos
 * y asignación automática de estado (ACEPTADA/RECHAZADA). Genera IDs únicos y verifica
 * disponibilidad del aula solicitada.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import g6.dynamodb.DAO.ReservaDAO;
import g6.dynamodb.Dictionary;
import g6.dynamodb.Model.Reserva;
import g6.dynamodb.Util.AWSClient;

public class ReservaService {
    private final AWSClient cliente;
    private final DynamoDBMapper mapper;

    /**
     * Crea una nueva instancia del servicio de reservas.
     * 
     * @param cliente cliente AWS configurado para DynamoDB
     */
    public ReservaService(AWSClient cliente) {
        this.cliente = cliente;
        this.mapper = new DynamoDBMapper(cliente.getDynamoDB());
    }

    /**
     * Crea una nueva reserva con validaciones completas.
     * 
     * Genera ID único UUID, valida orden de fechas, verifica solapamientos con otras reservas
     * del mismo aula y asigna estado automáticamente. Guarda la reserva en DynamoDB.
     * 
     * @param reserva reserva con datos completos (aula, fechas, usuario, etc.)
     * @return reserva persistida con ID único y estado asignado
     */
    public Reserva crearReserva(Reserva reserva) {
        ReservaDAO dao = new ReservaDAO(this.cliente.getDynamoDB());
        boolean es_unica = false;
        
        // Genera ID único UUID hasta encontrar uno disponible
        while (!es_unica) {
            String id = UUID.randomUUID().toString();
            if (dao.findById(id) == null) {
                es_unica = true;
                reserva.setId(id); // Asigna el ID generado
            }
        }

        // Validaciones y asignación de estado
        if (!fechasValidas(LocalDateTime.parse(reserva.getFechaInicio()), 
                          LocalDateTime.parse(reserva.getFechaFin()))) {
            reserva.setEstado(Dictionary.Estado.RECHAZADA.toString());
        } else if (existeSolapamiento(reserva)) {
            reserva.setEstado(Dictionary.Estado.RECHAZADA.toString());
        } else {
            reserva.setEstado(Dictionary.Estado.ACEPTADA.toString());
        }
        
        // Persiste la reserva
        mapper.save(reserva);
        return reserva;
    }

    /**
     * Valida que la fecha de inicio sea anterior a la fecha de fin.
     * 
     * @param inicio fecha/hora de inicio
     * @param fin fecha/hora de fin
     * @return true si inicio < fin
     */
    private boolean fechasValidas(LocalDateTime inicio, LocalDateTime fin) {
        return inicio.isBefore(fin);
    }

    /**
     * Detecta si la nueva reserva solapa con reservas existentes del mismo aula.
     * 
     * Ignora reservas RECHAZADAS y aulas diferentes. Verifica solapamiento temporal:
     * nueva.inicio < existente.fin && nueva.fin > existente.inicio
     * 
     * @param nueva reserva a validar
     * @return true si existe solapamiento
     */
    private boolean existeSolapamiento(Reserva nueva) {
        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        List<Reserva> reservas = mapper.scan(Reserva.class, scan);

        for (Reserva r : reservas) {
            // Ignorar aulas diferentes
            if (!r.getAula().getId().equals(nueva.getAula().getId())) {
                continue;
            }
            
            // Ignorar reservas rechazadas
            if (r.getEstado().equals(Dictionary.Estado.RECHAZADA.toString())) {
                continue;
            }

            // Verificar solapamiento temporal
            boolean solapa = LocalDateTime.parse(nueva.getFechaInicio()).isBefore(LocalDateTime.parse(r.getFechaFin()))
                    && LocalDateTime.parse(nueva.getFechaFin()).isAfter(LocalDateTime.parse(r.getFechaInicio()));

            if (solapa) {
                return true;
            }
        }

        return false;
    }
}
