package com.finca.modelo;

public class TecnicoAgricola extends Responsable {
    private String especialidadTecnica;

    public TecnicoAgricola() {}

    public TecnicoAgricola(String identificacion, String nombreCompleto,
                           String correo, String telefono, String especialidadTecnica) {
        super(identificacion, nombreCompleto, correo, telefono, "Técnico Agrícola");
        this.especialidadTecnica = especialidadTecnica;
    }

    public String getEspecialidadTecnica() { return especialidadTecnica; }
    public void setEspecialidadTecnica(String e) { this.especialidadTecnica = e; }

    @Override
    public String obtenerRol() {
        return "Técnico especializado en: " + especialidadTecnica;
    }
}