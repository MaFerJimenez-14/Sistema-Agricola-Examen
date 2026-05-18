package com.finca.controlador;

import com.finca.dao.*;
import com.finca.modelo.*;
import java.sql.SQLException;
import java.util.List;

public class GestorAgrario {

    private ParcelaDAO parcelaDAO;
    private CultivoDAO cultivoDAO;
    private ResponsableDAO responsableDAO;
    private LaborAgricolaDAO laborDAO;

    public GestorAgrario() {
        this.parcelaDAO = new ParcelaDAO();
        this.cultivoDAO = new CultivoDAO();
        this.responsableDAO = new ResponsableDAO();
        this.laborDAO = new LaborAgricolaDAO();
    }

    //PARCELAS
    public boolean registrarParcela(Parcela p) throws SQLException {
        if (p.getCodigo() == null || p.getCodigo().isBlank())
            throw new IllegalArgumentException("El código de la parcela no puede estar vacío.");
        if (p.getArea() <= 0)
            throw new IllegalArgumentException("El área debe ser mayor que cero.");
        if (parcelaDAO.existeCodigo(p.getCodigo()))
            throw new IllegalArgumentException("Ya existe una parcela con ese código.");
        return parcelaDAO.insertar(p);
    }

    public boolean actualizarParcela(Parcela p) throws SQLException {
        if (p.getCodigo() == null || p.getCodigo().isBlank())
            throw new IllegalArgumentException("El código de la parcela no puede estar vacío.");
        if (p.getArea() <= 0)
            throw new IllegalArgumentException("El área debe ser mayor que cero.");
        return parcelaDAO.actualizar(p);
    }

    public boolean eliminarParcela(String codigo) throws SQLException {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("El código no puede estar vacío.");
        return parcelaDAO.eliminar(codigo);
    }

    public List<Parcela> listarParcelas() throws SQLException {
        return parcelaDAO.listar();
    }

    //CULTIVOS
    public boolean registrarCultivo(Cultivo c) throws SQLException {
        if (c.getCodigo() == null || c.getCodigo().isBlank())
            throw new IllegalArgumentException("El código del cultivo no puede estar vacío.");
        if (cultivoDAO.existeCodigo(c.getCodigo()))
            throw new IllegalArgumentException("Ya existe un cultivo con ese código.");
        return cultivoDAO.insertar(c);
    }

    public boolean actualizarCultivo(Cultivo c) throws SQLException {
        if (c.getCodigo() == null || c.getCodigo().isBlank())
            throw new IllegalArgumentException("El código del cultivo no puede estar vacío.");
        return cultivoDAO.actualizar(c);
    }

    public boolean eliminarCultivo(String codigo) throws SQLException {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("El código no puede estar vacío.");
        return cultivoDAO.eliminar(codigo);
    }

    public List<Cultivo> listarCultivos() throws SQLException {
        return cultivoDAO.listar();
    }

    //RESPONSABLES
    public boolean registrarResponsable(Responsable r) throws SQLException {
        if (r.getIdentificacion() == null || r.getIdentificacion().isBlank())
            throw new IllegalArgumentException("La identificación no puede estar vacía.");
        if (responsableDAO.existeIdentificacion(r.getIdentificacion()))
            throw new IllegalArgumentException("Ya existe un responsable con esa identificación.");
        return responsableDAO.insertar(r);
    }

    public boolean actualizarResponsable(Responsable r) throws SQLException {
        if (r.getIdentificacion() == null || r.getIdentificacion().isBlank())
            throw new IllegalArgumentException("La identificación no puede estar vacía.");
        return responsableDAO.actualizar(r);
    }

    public boolean eliminarResponsable(String identificacion) throws SQLException {
        if (identificacion == null || identificacion.isBlank())
            throw new IllegalArgumentException("La identificación no puede estar vacía.");
        return responsableDAO.eliminar(identificacion);
    }

    public List<Responsable> listarResponsables() throws SQLException {
        return responsableDAO.listar();
    }

    //LABORES
    public boolean registrarLabor(LaborAgricola l) throws SQLException {
        if (l.getCodigo() == null || l.getCodigo().isBlank())
            throw new IllegalArgumentException("El código de la labor no puede estar vacío.");
        if (l.getParcela() == null)
            throw new IllegalArgumentException("Debe seleccionar una parcela.");
        if (l.getCultivo() == null)
            throw new IllegalArgumentException("Debe seleccionar un cultivo.");
        if (l.getResponsable() == null)
            throw new IllegalArgumentException("Debe seleccionar un responsable.");
        if (l.getCostoEstimado() < 0)
            throw new IllegalArgumentException("El costo estimado no puede ser negativo.");
        if (laborDAO.existeCodigo(l.getCodigo()))
            throw new IllegalArgumentException("Ya existe una labor con ese código.");
        return laborDAO.insertar(l);
    }

    public boolean actualizarLabor(LaborAgricola l) throws SQLException {
        if (l.getCodigo() == null || l.getCodigo().isBlank())
            throw new IllegalArgumentException("El código de la labor no puede estar vacío.");
        if (l.getParcela() == null)
            throw new IllegalArgumentException("Debe seleccionar una parcela.");
        if (l.getCultivo() == null)
            throw new IllegalArgumentException("Debe seleccionar un cultivo.");
        if (l.getResponsable() == null)
            throw new IllegalArgumentException("Debe seleccionar un responsable.");
        if (l.getCostoEstimado() < 0)
            throw new IllegalArgumentException("El costo estimado no puede ser negativo.");
        return laborDAO.actualizar(l);
    }

    public boolean eliminarLabor(String codigo) throws SQLException {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("El código no puede estar vacío.");
        return laborDAO.eliminar(codigo);
    }

    public List<LaborAgricola> listarLabores() throws SQLException {
        return laborDAO.listar();
    }
}