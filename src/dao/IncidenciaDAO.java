package dao;

import modelo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class IncidenciaDAO {
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
     * Método para dar de alta una incidencia en la base de datos.
     *
     * @param incidencia La incidencia a dar de alta, con todos sus datos asignados
     * @return boolean para comprobar si la operación ha funcionado.
     */
    public boolean alta(Incidencia incidencia) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("insert into incidencias (titulo, descripcion, fecha_alta, fecha_inicio, fecha_fin, estado, prioridad, id_autor, id_tecnico_asignado, id_incidencia_anterior, activa) values(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, incidencia.getTitulo());
            ps.setString(2, incidencia.getDescripcion());
            ps.setTimestamp(3, incidencia.getFechaAlta());
            ps.setTimestamp(4, incidencia.getFechaInicio());
            ps.setTimestamp(5, incidencia.getFechaFin());
            ps.setString(6, incidencia.getEstado().name());
            ps.setString(7, incidencia.getPrioridad().name());
            ps.setInt(8, incidencia.getAutor().getIdUsuario());
            ps.setNull(9, Types.INTEGER);
            if (incidencia.getIncidenciaAnterior() != null) {
                ps.setInt(10, incidencia.getIncidenciaAnterior().getIdIncidencia());
            } else {
                ps.setNull(10, Types.INTEGER);
            }
            ps.setBoolean(11, incidencia.isActivo());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para eliminar una incidencia en la base de datos.
     *
     * @param incidencia La incidencia a dar de alta, con todos sus datos asignados
     * @return boolean para comprobar si la operación ha funcionado.
     */
    public boolean eliminar(Incidencia incidencia) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("update incidencias set estado = 'CERRADA' where id_incidencia = ?");
            ps.setInt(1, incidencia.getIdIncidencia());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para modificar una incidencia en la base de datos.
     *
     * @param incidencia       Incidencia a modificar
     * @param tituloNuevo      Título nuevo de la incidencia
     * @param descripcionNueva Descripción nueva de la incidencia.
     * @param prioridadNueva   Prioridad nueva de la incidencia
     * @return boolean para comprobar si la operación ha funcionado.
     */
    public boolean modificar(Incidencia incidencia, String tituloNuevo, String descripcionNueva, Prioridad prioridadNueva) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("update incidencias set titulo = ?, descripcion = ?, prioridad = ? where id_incidencia = ?");
            ps.setString(1, tituloNuevo);
            ps.setString(2, descripcionNueva);
            ps.setString(3, prioridadNueva.name());
            ps.setInt(4, incidencia.getIdIncidencia());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para reabrir una incidencia cerrada en la base de datos. Se envía la incidencia anterior para crear una
     * nueva a partir de ella.
     *
     * @param incidenciaAnterior Incidencia anterior para asignar los datos a la nueva.
     * @return boolean para comprobar si la operación ha funcionado.
     */
    public boolean reabrir(Incidencia incidenciaAnterior) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("insert into incidencias (titulo, descripcion, fecha_alta, fecha_inicio, fecha_fin, estado, prioridad, id_autor, id_tecnico_asignado, id_incidencia_anterior, activa) values(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, incidenciaAnterior.getTitulo());
            ps.setString(2, incidenciaAnterior.getDescripcion());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setNull(4, Types.TIMESTAMP);
            ps.setNull(5, Types.TIMESTAMP);
            ps.setString(6, "PENDIENTE");
            ps.setString(7, incidenciaAnterior.getPrioridad().name());
            ps.setInt(8, incidenciaAnterior.getAutor().getIdUsuario());
            ps.setNull(9, Types.NULL);
            ps.setInt(10, incidenciaAnterior.getIdIncidencia());
            ps.setBoolean(11, true);
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para solucionar la incidencia como técnico o administrador (Pendiente -> En proceso).
     *
     * @param incidencia Incidencia a solucionar
     * @param user       Usuario que modifica el estado de la incidencia (Técnico o Administrador)
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean solucionarIncidencia(Incidencia incidencia, Usuario user) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("update incidencias set estado = 'EN_PROCESO', id_tecnico_asignado = ?, fecha_inicio = ? where id_incidencia = ?");
            ps.setInt(1, user.getIdUsuario());
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, incidencia.getIdIncidencia());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para poner en espera la incidencia como técnico o administrador (En proceso -> Espera).
     *
     * @param incidencia Incidencia a poner en espera
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean ponerEnEsperaIncidencia(Incidencia incidencia) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("update incidencias set estado = 'ESPERA' where id_incidencia = ?");
            ps.setInt(1, incidencia.getIdIncidencia());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para cerrar la incidencia como técnico o administrador (En proceso / Espera -> Cerrada).
     *
     * @param incidencia Incidencia a cerrar
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean cerrarIncidencia(Incidencia incidencia) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("update incidencias set estado = 'CERRADA', fecha_fin = ? where id_incidencia = ?");
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, incidencia.getIdIncidencia());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para borrar permanentemente una incidencia como administrador. Elimina por completo la incidencia de la
     * base de datos, es imposible reabrirla.
     *
     * @param incidencia Incidencia a borrar permanentemente
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean borradoPermanente(Incidencia incidencia) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("delete from historico_resoluciones where id_incidencia = ?");
            ps.setInt(1, incidencia.getIdIncidencia());
            ps.executeUpdate();
            ps = con.prepareStatement("update incidencias set id_incidencia_anterior = null where id_incidencia_anterior = ?");
            ps.setInt(1, incidencia.getIdIncidencia());
            ps.executeUpdate();
            ps = con.prepareStatement("delete from incidencias where id_incidencia = ?");
            ps.setInt(1, incidencia.getIdIncidencia());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para asignar un técnico a una incidencia de la base de datos, exclusivo de administrador.
     *
     * @param incidencia Incidencia a la que asignar un técnico.
     * @param tecnico    Técnico que se va a asignar a la incidencia
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean asignarTecnico(Incidencia incidencia, Usuario tecnico) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("update incidencias set id_tecnico_asignado = ?, estado = 'EN_PROCESO' where id_incidencia = ?");
            ps.setInt(1, tecnico.getIdUsuario());
            ps.setInt(2, incidencia.getIdIncidencia());
            boolean respuesta = ps.executeUpdate() > 0;
            ps.close();
            con.close();
            return respuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para recoger una incidencia de la base de datos mediante su ID
     *
     * @param id ID (int) de la incidencia a recoger.
     * @return Incidencia recogida de la base de datos con el ID correspondiente.
     */
    public Incidencia getIncidencia(int id) {
        try {
            Connection con = conectar();
            Incidencia incidencia = new Incidencia();
            PreparedStatement ps = con.prepareStatement("select * from incidencias where id_incidencia = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                incidencia = new Incidencia();
                incidencia.setIdIncidencia(rs.getInt(1));
                incidencia.setTitulo(rs.getString(2));
                incidencia.setDescripcion(rs.getString(3));
                incidencia.setFechaAlta(rs.getTimestamp(4));
                incidencia.setFechaInicio(rs.getTimestamp(5));
                incidencia.setFechaFin(rs.getTimestamp(6));
                incidencia.setEstado(Estado.valueOf(rs.getString(7)));
                incidencia.setPrioridad(Prioridad.valueOf(rs.getString(8)));
                int idAutor = rs.getInt("id_autor");
                if (!rs.wasNull()) {
                    incidencia.setAutor(usuarioDAO.getUsuarioID(idAutor));
                } else {
                    incidencia.setAutor(null);
                }
                int idTecnico = rs.getInt("id_tecnico_asignado");
                if (!rs.wasNull()) {
                    incidencia.setTecnico(usuarioDAO.getUsuarioID(idTecnico));
                } else {
                    incidencia.setTecnico(null);
                }
                int idIncidenciaAnterior = rs.getInt("id_incidencia_anterior");
                if (!rs.wasNull() && idIncidenciaAnterior > 0) {
                    Incidencia anterior = new Incidencia();
                    anterior.setIdIncidencia(idIncidenciaAnterior);
                    incidencia.setIncidenciaAnterior(anterior);
                } else {
                    incidencia.setIncidenciaAnterior(null);
                }
                incidencia.setActivo(rs.getBoolean("activa"));
            }
            rs.close();
            ps.close();
            con.close();
            return incidencia;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Método para recuperar un ArrayList de todas las incidencias del sistema, que luego se filtran.
     *
     * @return Un ArrayList<Incidencia> con todas las incidencias del sistema.
     */
    public ArrayList<Incidencia> getAllIncidencias() {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("select * from incidencias");
            ArrayList<Incidencia> incidencias = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Incidencia incidencia = new Incidencia();
                incidencia.setIdIncidencia(rs.getInt(1));
                incidencia.setTitulo(rs.getString(2));
                incidencia.setDescripcion(rs.getString(3));
                incidencia.setFechaAlta(rs.getTimestamp(4));
                incidencia.setFechaInicio(rs.getTimestamp(5));
                incidencia.setFechaFin(rs.getTimestamp(6));
                incidencia.setEstado(Estado.valueOf(rs.getString(7)));
                incidencia.setPrioridad(Prioridad.valueOf(rs.getString(8)));
                Usuario usuario = usuarioDAO.getUsuarioID(rs.getInt("id_autor"));
                incidencia.setAutor(usuario);
                if (rs.getString("id_tecnico_asignado") != null) {
                    incidencia.setTecnico(usuarioDAO.getUsuarioID(rs.getInt("id_tecnico_asignado")));
                } else {
                    incidencia.setTecnico(null);
                }
                if (rs.getString("id_incidencia_anterior") != null) {
                    int idAnterior = rs.getInt("id_incidencia_anterior");
                    Incidencia anterior = new Incidencia();
                    anterior.setIdIncidencia(idAnterior);
                    incidencia.setIncidenciaAnterior(anterior);
                } else {
                    incidencia.setIncidenciaAnterior(null);
                }
                incidencia.setActivo(rs.getBoolean("activa"));
                incidencias.add(incidencia);
            }
            rs.close();
            ps.close();
            con.close();
            Collections.sort(incidencias);
            return incidencias;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}