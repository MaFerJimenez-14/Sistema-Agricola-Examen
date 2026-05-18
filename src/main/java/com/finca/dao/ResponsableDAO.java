package com.finca.dao;

import Conexion.ConexionBD;
import com.finca.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsableDAO {

    public boolean insertar(Responsable responsable) throws SQLException {
        String sql = "INSERT INTO responsables(identificacion, nombre_completo, correo, "
                   + "telefono, tipo_responsable, nombre_finca_asociacion, especialidad_tecnica) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, responsable.getIdentificacion());
            ps.setString(2, responsable.getNombreCompleto());
            ps.setString(3, responsable.getCorreo());
            ps.setString(4, responsable.getTelefono());
            ps.setString(5, responsable.getTipoResponsable());
            if (responsable instanceof Productor) {
                ps.setString(6, ((Productor) responsable).getNombreFincaAsociacion());
                ps.setNull(7, Types.VARCHAR);
            } else if (responsable instanceof TecnicoAgricola) {
                ps.setNull(6, Types.VARCHAR);
                ps.setString(7, ((TecnicoAgricola) responsable).getEspecialidadTecnica());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean actualizar(Responsable responsable) throws SQLException {
        String sql = "UPDATE responsables SET nombre_completo=?, correo=?, telefono=?, "
                   + "tipo_responsable=?, nombre_finca_asociacion=?, especialidad_tecnica=? "
                   + "WHERE identificacion=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, responsable.getNombreCompleto());
            ps.setString(2, responsable.getCorreo());
            ps.setString(3, responsable.getTelefono());
            ps.setString(4, responsable.getTipoResponsable());
            if (responsable instanceof Productor) {
                ps.setString(5, ((Productor) responsable).getNombreFincaAsociacion());
                ps.setNull(6, Types.VARCHAR);
            } else if (responsable instanceof TecnicoAgricola) {
                ps.setNull(5, Types.VARCHAR);
                ps.setString(6, ((TecnicoAgricola) responsable).getEspecialidadTecnica());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
            }
            ps.setString(7, responsable.getIdentificacion());
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean eliminar(String identificacion) throws SQLException {
        String sql = "DELETE FROM responsables WHERE identificacion=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, identificacion);
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean existeIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT identificacion FROM responsables WHERE identificacion = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, identificacion);
            rs = ps.executeQuery();
            return rs.next();
        } finally {
            ConexionBD.close(rs);
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public List<Responsable> listar() throws SQLException {
        List<Responsable> lista = new ArrayList<>();
        String sql = "SELECT * FROM responsables";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String tipo = rs.getString("tipo_responsable");
                Responsable r;
                if ("Productor".equalsIgnoreCase(tipo)) {
                    r = new Productor(
                        rs.getString("identificacion"), rs.getString("nombre_completo"),
                        rs.getString("correo"), rs.getString("telefono"),
                        rs.getString("nombre_finca_asociacion")
                    );
                } else {
                    r = new TecnicoAgricola(
                        rs.getString("identificacion"), rs.getString("nombre_completo"),
                        rs.getString("correo"), rs.getString("telefono"),
                        rs.getString("especialidad_tecnica")
                    );
                }
                lista.add(r);
            }
        } finally {
            ConexionBD.close(rs);
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
        return lista;
    }
}