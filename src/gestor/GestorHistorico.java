package gestor;

import dao.HistoricoDAO;
import modelo.HistoricoResolucion;

import java.util.ArrayList;

public class GestorHistorico {
    HistoricoDAO historicoDAO = new HistoricoDAO();

    /**
     * Método para registrar un HistoricoResolucion en la base de datos
     *
     * @param hr HistóricoResolución a registrar.
     * @return boolean para comprobar que la operación ha funcionado
     */
    public boolean registrarHistorico(HistoricoResolucion hr) {
        return historicoDAO.registrarHistorico(hr);
    }

    /**
     * Método para conseguir un HistóricoResolución mediante la ID de una incidencia.
     *
     * @param idincidencia ID (int) de la incidencia cuyo histórico quieres conseguir.
     * @return
     */
    public ArrayList<HistoricoResolucion> getHistoricoByIncidencia(int idincidencia) {
        return historicoDAO.getHistoricoByIncidencia(idincidencia);
    }
}