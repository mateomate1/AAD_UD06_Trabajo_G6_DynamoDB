package service;

/**
 * Servicio de negocio para gestion de reservas.
 * 
 * Valida creacion reservas: genera UUID unico, verifica fechas ordenadas,
 * detecta solapamientos por aula y asigna estado automatico 
 * (ACEPTADA/RECHAZADA). Coordina con ReservaDAO.
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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import domain.model.Reserva;
import persistence.dao.ReservaDAO;
import persistence.dynamodb.AWSClient;
import util.Dictionary;

public class ReservaService {
    private final AWSClient cliente;

    /**
     * Constructor con cliente AWS configurado.
     * 
     * @param cliente instancia AWSClient (local/cloud)
     */
    public ReservaService(AWSClient cliente) {
        this.cliente = cliente;
    }

    /**
     * Crea reserva validada con todas las reglas de negocio.
     * 
     * 1. Genera ID UUID unico
     * 2. Valida fechaInicio < fechaFin
     * 3. Detecta solapamientos por aula
     * 4. Asigna estado automatico
     * 5. Persiste en DynamoDB
     * 
     * @param reserva datos reserva completa
     * @return reserva persistida con ID/estado
     */
    public Reserva crearReserva(Reserva reserva) {
        ReservaDAO dao = new ReservaDAO(this.cliente.getDynamoDB());
        boolean esUnica = false;

        // Genera UUID hasta ID disponible
        while (!esUnica) {
            String id = UUID.randomUUID().toString();
            if (dao.findById(id) == null) {
                esUnica = true;
                reserva.setId(id);
            }
        }

        // Validaciones logicas
        if (!fechasValidas(
                LocalDateTime.parse(reserva.getFechaInicio()),
                LocalDateTime.parse(reserva.getFechaFin()))) {
            reserva.setEstado(Dictionary.Estado.RECHAZADA.toString());
        } else if (existeSolapamiento(reserva)) {
            reserva.setEstado(Dictionary.Estado.RECHAZADA.toString());
        } else {
            reserva.setEstado(Dictionary.Estado.ACEPTADA.toString());
        }

        dao.save(reserva);
        return reserva;
    }

    /**
     * Valida orden temporal de reserva.
     * 
     * @param inicio fecha/hora inicio
     * @param fin    fecha/hora fin
     * @return true si inicio.isBefore(fin)
     */
    private boolean fechasValidas(LocalDateTime inicio, LocalDateTime fin) {
        return inicio.isBefore(fin);
    }

    /**
     * Detecta solapamientos con reservas existentes.
     * 
     * Reglas:
     * - Misma aula solamente
     * - Ignora RECHAZADA
     * - Formula: nueva.inicio < existente.fin && nueva.fin > existente.inicio
     * 
     * @param nueva reserva candidato
     * @return true si conflicto detectado
     */
    private boolean existeSolapamiento(Reserva nueva) {
        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        ReservaDAO dao = new ReservaDAO(this.cliente.getDynamoDB());
        List<Reserva> reservas = dao.scan(scan);

        for (Reserva r : reservas) {
            // Filtrar aulas diferentes
            if (!r.getAula().getId().equals(nueva.getAula().getId())) {
                continue;
            }

            // Ignorar rechazadas
            if (r.getEstado().equals(Dictionary.Estado.RECHAZADA.toString())) {
                continue;
            }

            // Detectar solapamiento temporal
            boolean solapa = LocalDateTime.parse(nueva.getFechaInicio())
                    .isBefore(LocalDateTime.parse(r.getFechaFin()))
                    && LocalDateTime.parse(nueva.getFechaFin())
                            .isAfter(LocalDateTime.parse(r.getFechaInicio()));

            if (solapa) {
                return true;
            }
        }

        return false;
    }

    public boolean eliminarReserva(Reserva r) {
        ReservaDAO dao = new ReservaDAO(this.cliente.getDynamoDB());
        if (dao.findById(r.getId()) == null)
            return true;
        else {
            dao.delete(r);
            return dao.findById(r.getId()) == null;
        }
    }

    public Reserva buscar(Reserva a) {
        ReservaDAO dao = new ReservaDAO(this.cliente.getDynamoDB());
        return dao.findById(a.getId());
    }

    public Reserva buscar(String a) {
        ReservaDAO dao = new ReservaDAO(this.cliente.getDynamoDB());
        return dao.findById(a);
    }
}
