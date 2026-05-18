package com.finca.modelo;

public class Parcela {
    private String codigo;
    private String nombre;
    private String ubicacion;
    private double area;
    private String tipoSuelo;
    private String estado;

    public Parcela() {}

    public Parcela(String codigo, String nombre, String ubicacion,
                   double area, String tipoSuelo, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.area = area;
        this.tipoSuelo = tipoSuelo;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public String getTipoSuelo() { return tipoSuelo; }
    public void setTipoSuelo(String tipoSuelo) { this.tipoSuelo = tipoSuelo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + estado + ")";
    }
}