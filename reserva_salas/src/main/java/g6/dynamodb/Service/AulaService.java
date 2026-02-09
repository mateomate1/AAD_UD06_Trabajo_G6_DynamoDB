package g6.dynamodb.Service;

import java.util.UUID;

import g6.dynamodb.DAO.AulaDAO;
import g6.dynamodb.Model.Aula;
import g6.dynamodb.Util.AWSClient;

public class AulaService {
    private final AWSClient cliente;

    public AulaService(AWSClient cliente) {
        this.cliente = cliente;
    }

    public Aula crearAula(Aula a) {
        AulaDAO dao = new AulaDAO(this.cliente.getDynamoDB());

        boolean es_unica = false;

        // Genera ID único UUID hasta encontrar uno disponible
        while (!es_unica) {
            String id = UUID.randomUUID().toString();
            if (dao.findById(id) == null) {
                es_unica = true;
                a.setId(id); // Asigna el ID generado
            }
        }

        dao.save(a);
        return a;
    }

    public Aula actualizarAula(Aula a) {
        AulaDAO dao = new AulaDAO(this.cliente.getDynamoDB());
        if (dao.findById(a.getId()) == null)
            return null;
        dao.save(a);
        return a;
    }
}
