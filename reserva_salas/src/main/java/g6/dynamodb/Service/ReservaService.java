package g6.dynamodb.Service;

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

    public ReservaService(AWSClient cliente) {
        this.cliente = cliente;
        this.mapper = new DynamoDBMapper(cliente.getDynamoDB());
    }

    public Reserva crearReserva(Reserva reserva) {
        ReservaDAO dao = new ReservaDAO(this.cliente.getDynamoDB());
        boolean es_unica = false;
        while (!es_unica) {
            String id = UUID.randomUUID().toString();
            if (dao.findById(id) == null) {
                es_unica = true;
            }
        }

        if (!fechasValidas(LocalDateTime.parse(reserva.getFechaInicio()), LocalDateTime.parse(reserva.getFechaFin()))) {
            reserva.setEstado(Dictionary.Estado.RECHAZADA.toString());
            mapper.save(reserva);
        }else if(existeSolapamiento(reserva)){
            reserva.setEstado(Dictionary.Estado.RECHAZADA.toString());
        } else{
            reserva.setEstado(Dictionary.Estado.ACEPTADA.toString());
        }

        return reserva;
    }

    private boolean fechasValidas(LocalDateTime inicio, LocalDateTime fin) {
        return inicio.isBefore(fin);
    }

    private boolean existeSolapamiento(Reserva nueva) {

        DynamoDBScanExpression scan = new DynamoDBScanExpression();
        List<Reserva> reservas = mapper.scan(Reserva.class, scan);

        for (Reserva r : reservas) {

            if (!r.getAula().getId().equals(nueva.getAula().getId())) {
                continue;
            }

            if (r.getEstado().equals(Dictionary.Estado.RECHAZADA.toString())) {
                continue;
            }

            boolean solapa = LocalDateTime.parse(nueva.getFechaInicio()).isBefore(LocalDateTime.parse(r.getFechaFin()))
            && LocalDateTime.parse(nueva.getFechaFin()).isAfter(LocalDateTime.parse(r.getFechaInicio()));

            if (solapa) {
                return true;
            }
        }

        return false;
    }

}
