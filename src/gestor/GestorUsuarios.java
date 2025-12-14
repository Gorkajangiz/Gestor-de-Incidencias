package gestor;

import dao.LoginDAO;
import dao.UsuarioDAO;
import modelo.Usuario;

import java.util.ArrayList;

public class GestorUsuarios {
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    LoginDAO loginDAO = new LoginDAO();

    /**
     * Método para verificar los datos del usuario que inicia sesión en la base de datos.
     *
     * @param email    Email del usuario
     * @param password Contraseña del usuario
     * @return Número asignado a cada panel (0 -> Usuario, 1 -> Técnico, 2 -> Administrador, 3 -> Error)
     */
    public int verificarLogin(String email, String password) {
        return loginDAO.verificarLogin(email, password);
    }

    /**
     * Método para recoger al usuario por su email de la base de datos.
     *
     * @param email Email del usuario a buscar
     * @return Usuario
     */
    public Usuario getUsuarioEmail(String email) {
        return usuarioDAO.getUsuarioEmail(email);
    }

    /**
     * Método para recoger el usuario por su ID de la base de datos.
     *
     * @param id ID (int) del usuario a buscar.
     * @return Usuario
     */
    public Usuario getUsuarioID(int id) {
        return usuarioDAO.getUsuarioID(id);
    }

    /**
     * Método para registrar un cierre de sesión en la base de datos, registra al usuario y la hora.
     *
     * @param email Email para identificar al usuario a registrar.
     */
    public void registrarLogout(String email) {
        loginDAO.registrarLogout(email);
    }

    /**
     * Método para recoger a todos los usuarios de Perfil 'Técnico' de la base de datos
     *
     * @return ArrayList<Usuario>
     */
    public ArrayList<Usuario> getTodosTecnicos() {
        return usuarioDAO.getAllTecnicos();
    }
}