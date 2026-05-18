package com.finca.modelo;

public class Productor extends Responsable {
    private String nombreFincaAsociacion;

    public Productor() {}

    public Productor(String identificacion, String nombreCompleto,
                     String correo, String telefono, String nombreFincaAsociacion) {
        super(identificacion, nombreCompleto, correo, telefono, "Productor");
        this.nombreFincaAsociacion = nombreFincaAsociacion;
    }

    public String getNombreFincaAsociacion() { return nombreFincaAsociacion; }
    public void setNombreFincaAsociacion(String n) { this.nombreFincaAsociacion = n; }

    @Override
    public String obtenerRol() {
        return "Productor de la finca/asociación: " + nombreFincaAsociacion;
    }
}