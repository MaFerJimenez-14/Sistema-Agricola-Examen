package com.finca.modelo;

public class LaborAgricola {
    private String codigo;
    private Parcela parcela;
    private Cultivo cultivo;
    private Responsable responsable;
    private String fecha;
    private String tipoLabor;
    private String descripcion;
    private double costoEstimado;

    public LaborAgricola() {}

    public LaborAgricola(String codigo, Parcela parcela, Cultivo cultivo,
                         Responsable responsable, String fecha, String tipoLabor,
                         String descripcion, double costoEstimado) {
        this.codigo = codigo;
        this.parcela = parcela;
        this.cultivo = cultivo;
        this.responsable = responsable;
        this.fecha = fecha;
        this.tipoLabor = tipoLabor;
        this.descripcion = descripcion;
        this.costoEstimado = costoEstimado;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Parcela getParcela() { return parcela; }
    public void setParcela(Parcela parcela) { this.parcela = parcela; }

    public Cultivo getCultivo() { return cultivo; }
    public void setCultivo(Cultivo cultivo) { this.cultivo = cultivo; }

    public Responsable getResponsable() { return responsable; }
    public void setResponsable(Responsable responsable) { this.responsable = responsable; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTipoLabor() { return tipoLabor; }
    public void setTipoLabor(String tipoLabor) { this.tipoLabor = tipoLabor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(double costoEstimado) { this.costoEstimado = costoEstimado; }

    @Override
    public String toString() {
        return codigo + " | " + (parcela != null ? parcela.getNombre() : "") +
               " | " + (cultivo != null ? cultivo.getNombre() : "") +
               " | " + tipoLabor;
    }
}