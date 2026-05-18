package com.finca.modelo;

public class CultivoAnual extends Cultivo {
    private int duracionCicloDias;

    public CultivoAnual() {}

    public CultivoAnual(String codigo, String nombre, String variedad,
                        String fechaSiembra, int duracionCicloDias) {
        super(codigo, nombre, variedad, fechaSiembra, "Anual");
        this.duracionCicloDias = duracionCicloDias;
    }

    public int getDuracionCicloDias() { return duracionCicloDias; }
    public void setDuracionCicloDias(int d) { this.duracionCicloDias = d; }

    @Override
    public String obtenerDescripcion() {
        return "Cultivo anual con ciclo aproximado de " + duracionCicloDias + " días.";
    }
}