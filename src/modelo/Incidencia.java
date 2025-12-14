package modelo;

import java.sql.Date;
import java.sql.Timestamp;

public class Incidencia implements Comparable {
    private int idIncidencia;
    private String titulo;
    private String descripcion;
    private boolean activo;
    private Timestamp fechaAlta;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private Estado estado;
    private Prioridad prioridad;
    private Usuario autor;
    private Usuario tecnico;
    private Incidencia incidenciaAnterior;

    public Incidencia() {
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Timestamp getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Timestamp fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public Incidencia getIncidenciaAnterior() {
        return incidenciaAnterior;
    }

    public void setIncidenciaAnterior(Incidencia incidenciaAnterior) {
        this.incidenciaAnterior = incidenciaAnterior;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.prioridad.ordinal(), ((Incidencia) o).prioridad.ordinal());
    }

    @Override
    public String toString() {
        return "ID: " + idIncidencia + " / Prioridad: " + prioridad + " / Título: " + titulo;
    }
}
