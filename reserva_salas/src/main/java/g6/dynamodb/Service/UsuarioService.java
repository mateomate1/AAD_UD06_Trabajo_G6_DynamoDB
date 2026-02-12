package g6.dynamodb.Service;

import g6.dynamodb.DAO.UsuarioDAO;
import g6.dynamodb.Model.Usuario;
import g6.dynamodb.Util.AWSClient;

/**
 * Servicio de negocio para gestion de usuarios.
 * 
 * Maneja creacion con username UUID unico automatico y
 * actualizacion validada por existencia. Coordina UsuarioDAO.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
public class UsuarioService {

    private final AWSClient cliente;

    /**
     * Constructor con cliente AWS inyectado.
     * 
     * @param cliente AWSClient configurado
     */
    public UsuarioService(AWSClient cliente) {
        this.cliente = cliente;
    }

    public boolean altaUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(u.getUsername()) == null) {
            dao.save(u);
            return true;
        }
        return false;
    }

    public boolean altaUsuario(String usuario, String contrasena) {
        Usuario u = new Usuario(usuario, contrasena);
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(usuario) == null) {
            dao.save(u);
            return true;
        }
        return false;
    }

    public boolean iniciarSesion(String usuario, String contrasena) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        Usuario u = dao.findById(usuario);
        return u != null && u.getPassword().equals(contrasena);
    }

    public Usuario actualizarUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(u.getUsername()) == null)
            return null;
        dao.save(u);
        return u;
    }

    public Usuario buscarUsuario(String username) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        return dao.findById(username);
    }

    public boolean deleteUsuario(String username) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        Usuario u = dao.findById(username);
        dao.delete(u);
        return dao.findById(username) == null;
    }

    public boolean deleteUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        String username = u.getUsername();
        dao.delete(u);
        return dao.findById(username) == null;
    }
}
