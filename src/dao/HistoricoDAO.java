package dao;

import gestor.GestorUsuarios;
import modelo.*;

import java.sql.*;
import java.util.ArrayList;

public class HistoricoDAO {
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
     * Método para registrar un cambio de cualquier tipo en el histórico de la base de datos
     *
     * @param hr HistoricoResolucion que almacena todos los datos.
     * @return boolean para confirmar que el cambio se ha efectuado
     */
    public boolean registrarHistorico(HistoricoResolucion hr) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("insert into historico_resoluciones (id_incidencia, fecha_cambio, estado_anterior, estado_nuevo, prioridad_anterior, prioridad_nueva, comentario, id_tecnico) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, hr.getIncidencia().getIdIncidencia());
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setString(3, hr.getEstadoPrevio().name());
            ps.setString(4, hr.getEstadoNuevo() != null ? hr.getEstadoNuevo().name() : null);
            ps.setString(5, hr.getPrioridadPrevia().name());
            ps.setString(6, hr.getPrioridadNueva() != null ? hr.getPrioridadNueva().name() : null);
            ps.setString(7, hr.getComentario());
            ps.setInt(8, hr.getTecnico().getIdUsuario());
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
     * Método para conseguir el histórico de una incidencia de la base de datos mediante su ID
     *
     * @param idincidencia ID (int) de la incidencia a buscar
     * @return ArrayList<HistoricoResolucion> con todos los cambios hechos, dentro de objetos HistoricoResolucion.
     */
    public ArrayList<HistoricoResolucion> getHistoricoByIncidencia(int idincidencia) {
        try {
            ArrayList<HistoricoResolucion> lista = new ArrayList<>();
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement("select * from historico_resoluciones where id_incidencia = ?");
            ps.setInt(1, idincidencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HistoricoResolucion hr = new HistoricoResolucion();
                hr.setIdHistorico(rs.getInt("id_historico"));
                hr.setIdIncidencia(rs.getInt("id_incidencia"));
                hr.setFechaCambio(rs.getTimestamp("fecha_cambio"));
                hr.setEstadoPrevio(Estado.valueOf(rs.getString("estado_anterior")));
                hr.setEstadoNuevo(Estado.valueOf(rs.getString("estado_nuevo")));
                hr.setPrioridadPrevia(Prioridad.valueOf(rs.getString("prioridad_anterior")));
                hr.setPrioridadNueva(Prioridad.valueOf(rs.getString("prioridad_nueva")));
                GestorUsuarios gu = new GestorUsuarios();
                hr.setTecnico(gu.getUsuarioID(rs.getInt("id_tecnico")));
                hr.setComentario(rs.getString("comentario"));
                lista.add(hr);
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