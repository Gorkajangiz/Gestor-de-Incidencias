package gestor;

import dao.IncidenciaDAO;
import modelo.*;

import java.sql.Timestamp;
import java.util.ArrayList;

public class GestorIncidencias {
    IncidenciaDAO incidenciaDAO = new IncidenciaDAO();

    /**
     * Método para dar de alta la incidencia en la base de datos. Genera la incidencia en el gestor.
     *
     * @param titulo    Título de la incidencia.
     * @param desc      Descripción de la incidencia.
     * @param user      Usuario que ha creado la incidencia
     * @param prioridad Prioridad de la incidencia
     * @return boolean para determinar si la operación se ha completado con éxito
     */
    public boolean alta(String titulo, String desc, Usuario user, Prioridad prioridad) {
        Incidencia incidencia = new Incidencia();
        incidencia.setTitulo(titulo);
        incidencia.setDescripcion(desc);
        incidencia.setPrioridad(prioridad);
        incidencia.setAutor(user);
        incidencia.setFechaAlta(new Timestamp(System.currentTimeMillis()));
        incidencia.setEstado(Estado.PENDIENTE);
        incidencia.setActivo(true);
        return incidenciaDAO.alta(incidencia);
    }

    /**
     * Método para modificar una incidencia en la base de datos.
     *
     * @param incidencia  Incidencia a modificar
     * @param nuevoTitulo Título nuevo de la incidencia
     * @param nuevoDesc   Descripción nueva de la incidencia.
     * @param prioridad   Prioridad nueva de la incidencia
     * @return boolean para comprobar si la operación ha funcionado.
     */
    public boolean modificar(Incidencia incidencia, String nuevoTitulo, String nuevoDesc, Prioridad prioridad) {
        return incidenciaDAO.modificar(incidencia, nuevoTitulo, nuevoDesc, prioridad);
    }

    /**
     * Método para eliminar una incidencia en la base de datos.
     *
     * @param incidencia La incidencia a dar de alta, con todos sus datos asignados
     * @return boolean para comprobar si la operación ha funcionado.
     */
    public boolean eliminar(Incidencia incidencia) {
        return incidenciaDAO.eliminar(incidencia);
    }

    /**
     * Método para reabrir una incidencia cerrada en la base de datos. Se envía la incidencia anterior para crear una
     * nueva a partir de ella.
     *
     * @param incidencia Incidencia anterior para asignar los datos a la nueva.
     * @return boolean para comprobar si la operación ha funcionado.
     */
    public boolean reabrir(Incidencia incidencia) {
        if (incidencia.getEstado() != Estado.CERRADA) {
            return false;
        }
        return incidenciaDAO.reabrir(incidencia);
    }

    /**
     * Método para solucionar la incidencia como técnico o administrador (Pendiente -> En proceso).
     *
     * @param incidencia Incidencia a solucionar
     * @param user       Usuario que modifica el estado de la incidencia (Técnico o Administrador)
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean solucionarIncidencias(Incidencia incidencia, Usuario user) {
        if (incidencia.getEstado() != Estado.PENDIENTE) return false;
        if (user.getPerfil() != Perfil.TECNICO && user.getPerfil() != Perfil.ADMINISTRADOR) return false;
        return incidenciaDAO.solucionarIncidencia(incidencia, user);
    }

    /**
     * Método para poner en espera la incidencia como técnico o administrador (En proceso -> Espera).
     *
     * @param incidencia Incidencia a poner en espera
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean ponerEnEspera(Incidencia incidencia) {
        return incidenciaDAO.ponerEnEsperaIncidencia(incidencia);
    }

    /**
     * Método para cerrar la incidencia como técnico o administrador (En proceso / Espera -> Cerrada).
     *
     * @param incidencia Incidencia a cerrar
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean cerrarIncidencia(Incidencia incidencia) {
        return incidenciaDAO.cerrarIncidencia(incidencia);
    }

    /**
     * Método para asignar un técnico a una incidencia de la base de datos, exclusivo de administrador.
     *
     * @param incidencia Incidencia a la que asignar un técnico.
     * @param tecnico    Técnico que se va a asignar a la incidencia
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean asignarTecnico(Incidencia incidencia, Usuario tecnico) {
        return incidenciaDAO.asignarTecnico(incidencia, tecnico);
    }

    /**
     * Método para borrar permanentemente una incidencia como administrador. Elimina por completo la incidencia de la
     * base de datos, es imposible reabrirla.
     *
     * @param incidencia Incidencia a borrar permanentemente
     * @return boolean para comprobar si la operación ha funcionado
     */
    public boolean borrarPermanente(Incidencia incidencia) {
        return incidenciaDAO.borradoPermanente(incidencia);
    }

    /**
     * Método para recoger una incidencia de la base de datos mediante su ID
     *
     * @param id ID (int) de la incidencia a recoger.
     * @return Incidencia recogida de la base de datos con el ID correspondiente.
     */
    public Incidencia getIncidencia(int id) {
        return incidenciaDAO.getIncidencia(id);
    }

    /**
     * Método para recuperar un ArrayList de todas las incidencias del sistema, que luego se filtran.
     *
     * @return Un ArrayList<Incidencia> con todas las incidencias del sistema.
     */
    public ArrayList<Incidencia> getAllIncidencias() {
        return incidenciaDAO.getAllIncidencias();
    }
}