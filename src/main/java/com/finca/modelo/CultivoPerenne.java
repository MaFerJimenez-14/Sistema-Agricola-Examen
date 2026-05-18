package com.finca.modelo;

public class CultivoPerenne extends Cultivo {
    private int aniosProduccion;

    public CultivoPerenne() {}

    public CultivoPerenne(String codigo, String nombre, String variedad,
                          String fechaSiembra, int aniosProduccion) {
        super(codigo, nombre, variedad, fechaSiembra, "Perenne");
        this.aniosProduccion = aniosProduccion;
    }

    public int getAniosProduccion() { return aniosProduccion; }
    public void setAniosProduccion(int a) { this.aniosProduccion = a; }

    @Override
    public String obtenerDescripcion() {
        return "Cultivo perenne con producción estimada de " + aniosProduccion + " años.";
    }
}