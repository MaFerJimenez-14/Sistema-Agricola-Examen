package com.finca.modelo;

public abstract class Responsable {
    private String identificacion;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String tipoResponsable;

    public Responsable() {}

    public Responsable(String identificacion, String nombreCompleto,
                       String correo, String telefono, String tipoResponsable) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.tipoResponsable = tipoResponsable;
    }

    public abstract String obtenerRol();

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoResponsable() { return tipoResponsable; }
    public void setTipoResponsable(String tipoResponsable) { this.tipoResponsable = tipoResponsable; }

    @Override
    public String toString() {
        return identificacion + " - " + nombreCompleto + " [" + tipoResponsable + "]";
    }
}