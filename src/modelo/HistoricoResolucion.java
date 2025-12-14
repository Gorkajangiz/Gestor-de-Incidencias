package modelo;

import java.sql.Timestamp;

public class HistoricoResolucion {
    private int idHistorico;
    private int idIncidencia;
    private String comentario;
    private Timestamp fechaCambio;
    private Incidencia incidencia;
    private Estado estadoPrevio;
    private Estado estadoNuevo;
    private Prioridad prioridadPrevia;
    private Prioridad prioridadNueva;
    private Usuario tecnico;

    public HistoricoResolucion() {
    }

    public int getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(int idHistorico) {
        this.idHistorico = idHistorico;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Timestamp fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public Estado getEstadoPrevio() {
        return estadoPrevio;
    }

    public void setEstadoPrevio(Estado estadoPrevio) {
        this.estadoPrevio = estadoPrevio;
    }

    public Estado getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(Estado estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public Prioridad getPrioridadPrevia() {
        return prioridadPrevia;
    }

    public void setPrioridadPrevia(Prioridad prioridadPrevia) {
        this.prioridadPrevia = prioridadPrevia;
    }

    public Prioridad getPrioridadNueva() {
        return prioridadNueva;
    }

    public void setPrioridadNueva(Prioridad prioridadNueva) {
        this.prioridadNueva = prioridadNueva;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }
}
