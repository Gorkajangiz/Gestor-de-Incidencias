package dao;

import modelo.*;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {
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
     * Método para recoger al usuario por su email de la base de datos.
     *
     * @param email Email del usuario a buscar
     * @return Usuario
     */
    public Usuario getUsuarioEmail(String email) {
        try {
            Usuario usuario = new Usuario();
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("select * from usuarios where email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setContraseña(rs.getString("password"));
                usuario.setEmail(rs.getString("email"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setPerfil(Perfil.valueOf(rs.getString("perfil")));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
            }
            rs.close();
            ps.close();
            con.close();
            return usuario;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para recoger el usuario por su ID de la base de datos.
     *
     * @param id ID (int) del usuario a buscar.
     * @return Usuario
     */
    public Usuario getUsuarioID(int id) {
        try {
            Usuario usuario = new Usuario();
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("select * from usuarios where id_usuario = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setContraseña(rs.getString("password"));
                usuario.setEmail(rs.getString("email"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setPerfil(Perfil.valueOf(rs.getString("perfil")));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
            }
            rs.close();
            ps.close();
            con.close();
            return usuario;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para recoger a todos los usuarios de Perfil 'Técnico' de la base de datos
     *
     * @return ArrayList<Usuario>
     */
    public ArrayList<Usuario> getAllTecnicos() {
        try {
            ArrayList<Usuario> lista = new ArrayList<>();
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("select * from usuarios where perfil = 'TECNICO' and activo = TRUE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario tecnico = new Usuario();
                tecnico.setIdUsuario(rs.getInt("id_usuario"));
                tecnico.setEmail(rs.getString("email"));
                tecnico.setNombre(rs.getString("nombre"));
                tecnico.setApellidos(rs.getString("apellidos"));
                tecnico.setPerfil(Perfil.valueOf(rs.getString("perfil")));
                lista.add(tecnico);
            }
            rs.close();
            ps.close();
            con.close();
            return lista;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}