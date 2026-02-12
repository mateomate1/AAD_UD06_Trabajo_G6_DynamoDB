package g6.dynamodb.Service;

<<<<<<< HEAD
=======
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
import java.util.UUID;

>>>>>>> d98dbc3f4011ce79360c93ffe41e17203ba29367
import g6.dynamodb.DAO.UsuarioDAO;
import g6.dynamodb.Model.Usuario;
import g6.dynamodb.Util.AWSClient;

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

<<<<<<< HEAD
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

    public Usuario buscarUsuario(String username){
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        return dao.findById(username);
    }

    public boolean deleteUsuario(String username){
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        Usuario u = dao.findById(username);
        dao.delete(u);
        return dao.findById(username)==null;
    }

    public boolean deleteUsuario(Usuario u){
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        String username = u.getUsername();
        dao.delete(u);
        return dao.findById(username)==null;
    }
}
=======
    /**
     * Crea usuario con username UUID unico.
     * 
     * Genera IDs hasta evitar colisiones, asigna a username.
     * 
     * @param u usuario con password (username ignorado)
     * @return usuario persistido con ID unico
     */
    public Usuario altaUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        boolean esUnica = false;
        
        // Genera UUID hasta username disponible
        while (!esUnica) {
            String id = UUID.randomUUID().toString();
            if (dao.findById(id) == null) {
                esUnica = true;
                u.setUsername(id);
            }
        }
        
        dao.save(u);
        return u;
    }

    /**
     * Actualiza usuario existente.
     * 
     * Valida existencia previa por username.
     * 
     * @param u usuario completo con username valido
     * @return usuario actualizado o null si no existia
     */
    public Usuario actualizarUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO(this.cliente.getDynamoDB());
        if (dao.findById(u.getUsername()) == null) {
            return null;
        }
        dao.save(u);
        return u;
    }
}
>>>>>>> d98dbc3f4011ce79360c93ffe41e17203ba29367
