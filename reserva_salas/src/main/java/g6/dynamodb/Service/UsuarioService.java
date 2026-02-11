package g6.dynamodb.Service;

import java.util.UUID;

import g6.dynamodb.DAO.UsuarioDAO;
import g6.dynamodb.Model.Usuario;
import g6.dynamodb.Util.AWSClient;

public class UsuarioService {

    private final AWSClient cliente;

    public UsuarioService(AWSClient cliente) {
        this.cliente = cliente;
    }

    // public boolean loginUsuario()

    public Usuario altaUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        boolean es_unica = false;
        while (!es_unica) {
            String id = UUID.randomUUID().toString();
            if (dao.findById(id) == null) {
                es_unica = true;
                u.setUsername(id);
            }
        }
        dao.save(u);
        return u;
    }

    public Usuario actualizarUsuario(Usuario u){
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(u.getUsername()) == null)
            return null;
        dao.save(u);
        return u;
    }
}