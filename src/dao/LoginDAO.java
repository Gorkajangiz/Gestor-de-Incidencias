package dao;

import java.sql.*;

public class LoginDAO {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Conectar con la base de datos
     *
     * @return Connection
     */
    private Connection conectar() {
        Connection con = null;
        try {
            String usuario = "root";
            String password = "Enara1997";
            con = DriverManager.getConnection("jdbc:mysql://localhost/gestion_incidencias", usuario, password);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }

    /**
     * Método para verificar los datos del usuario que inicia sesión en la base de datos.
     *
     * @param email    Email del usuario
     * @param password Contraseña del usuario
     * @return Número asignado a cada panel (0 -> Usuario, 1 -> Técnico, 2 -> Administrador, 3 -> Error)
     */
    public int verificarLogin(String email, String password) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("select perfil from usuarios where email = ? and password = ?");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            int respuesta = 3;
            if (rs.next()) {
                if (rs.getString("perfil").equals("ADMINISTRADOR")) respuesta = 2;
                if (rs.getString("perfil").equals("TECNICO")) respuesta = 1;
                if (rs.getString("perfil").equals("USUARIO")) respuesta = 0;
            }
            rs.close();
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para registrar un inicio de sesión en la base de datos, registra al usuario y la hora.
     *
     * @param email Email para identificar al usuario a registrar.
     */
    public void registrarLogin(String email) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("insert into logins (id_usuario, fecha_login) values (?,?)");
            ps.setInt(1, usuarioDAO.getUsuarioEmail(email).getIdUsuario());
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Método para registrar un cierre de sesión en la base de datos, registra al usuario y la hora.
     *
     * @param email Email para identificar al usuario a registrar.
     */
    public void registrarLogout(String email) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("update logins set fecha_logout = ? where id_usuario = ?");
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, usuarioDAO.getUsuarioEmail(email).getIdUsuario());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}