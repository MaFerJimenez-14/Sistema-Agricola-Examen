package com.finca.modelo;

public abstract class Cultivo {
    private String codigo;
    private String nombre;
    private String variedad;
    private String fechaSiembra;
    private String tipoCultivo;

    public Cultivo() {}

    public Cultivo(String codigo, String nombre, String variedad,
                   String fechaSiembra, String tipoCultivo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.variedad = variedad;
        this.fechaSiembra = fechaSiembra;
        this.tipoCultivo = tipoCultivo;
    }

    public abstract String obtenerDescripcion();

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getVariedad() { return variedad; }
    public void setVariedad(String variedad) { this.variedad = variedad; }

    public String getFechaSiembra() { return fechaSiembra; }
    public void setFechaSiembra(String fechaSiembra) { this.fechaSiembra = fechaSiembra; }

    public String getTipoCultivo() { return tipoCultivo; }
    public void setTipoCultivo(String tipoCultivo) { this.tipoCultivo = tipoCultivo; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " [" + tipoCultivo + "]";
    }
}