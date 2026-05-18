package com.finca.dao;

import Conexion.ConexionBD;
import com.finca.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaborAgricolaDAO {

    public boolean insertar(LaborAgricola labor) throws SQLException {
        String sql = "INSERT INTO labores_agricolas(codigo, codigo_parcela, codigo_cultivo, "
                   + "identificacion_responsable, fecha_realizacion, tipo_labor, descripcion, costo_estimado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, labor.getCodigo());
            ps.setString(2, labor.getParcela().getCodigo());
            ps.setString(3, labor.getCultivo().getCodigo());
            ps.setString(4, labor.getResponsable().getIdentificacion());
            ps.setString(5, labor.getFecha());
            ps.setString(6, labor.getTipoLabor());
            ps.setString(7, labor.getDescripcion());
            ps.setDouble(8, labor.getCostoEstimado());
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean actualizar(LaborAgricola labor) throws SQLException {
        String sql = "UPDATE labores_agricolas SET codigo_parcela=?, codigo_cultivo=?, "
                   + "identificacion_responsable=?, fecha_realizacion=?, tipo_labor=?, "
                   + "descripcion=?, costo_estimado=? WHERE codigo=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, labor.getParcela().getCodigo());
            ps.setString(2, labor.getCultivo().getCodigo());
            ps.setString(3, labor.getResponsable().getIdentificacion());
            ps.setString(4, labor.getFecha());
            ps.setString(5, labor.getTipoLabor());
            ps.setString(6, labor.getDescripcion());
            ps.setDouble(7, labor.getCostoEstimado());
            ps.setString(8, labor.getCodigo());
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM labores_agricolas WHERE codigo=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean existeCodigo(String codigo) throws SQLException {
        String sql = "SELECT codigo FROM labores_agricolas WHERE codigo = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            return rs.next();
        } finally {
            ConexionBD.close(rs);
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public List<LaborAgricola> listar() throws SQLException {
        List<LaborAgricola> lista = new ArrayList<>();
        String sql = "SELECT l.*, "
                   + "p.nombre AS nom_parcela, p.ubicacion, p.area, p.tipo_suelo, p.estado, "
                   + "c.nombre AS nom_cultivo, c.variedad, c.fecha_siembra, c.tipo_cultivo, "
                   + "c.duracion_ciclo_dias, c.anios_produccion, "
                   + "r.nombre_completo, r.correo, r.telefono, r.tipo_responsable, "
                   + "r.nombre_finca_asociacion, r.especialidad_tecnica "
                   + "FROM labores_agricolas l "
                   + "INNER JOIN parcelas p ON l.codigo_parcela = p.codigo "
                   + "INNER JOIN cultivos c ON l.codigo_cultivo = c.codigo "
                   + "INNER JOIN responsables r ON l.identificacion_responsable = r.identificacion";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Parcela parcela = new Parcela(
                    rs.getString("codigo_parcela"), rs.getString("nom_parcela"),
                    rs.getString("ubicacion"), rs.getDouble("area"),
                    rs.getString("tipo_suelo"), rs.getString("estado")
                );
                String tipoCultivo = rs.getString("tipo_cultivo");
                Cultivo cultivo;
                if ("Anual".equalsIgnoreCase(tipoCultivo)) {
                    cultivo = new CultivoAnual(
                        rs.getString("codigo_cultivo"), rs.getString("nom_cultivo"),
                        rs.getString("variedad"), rs.getString("fecha_siembra"),
                        rs.getInt("duracion_ciclo_dias")
                    );
                } else {
                    cultivo = new CultivoPerenne(
                        rs.getString("codigo_cultivo"), rs.getString("nom_cultivo"),
                        rs.getString("variedad"), rs.getString("fecha_siembra"),
                        rs.getInt("anios_produccion")
                    );
                }
                String tipoResp = rs.getString("tipo_responsable");
                Responsable responsable;
                if ("Productor".equalsIgnoreCase(tipoResp)) {
                    responsable = new Productor(
                        rs.getString("identificacion_responsable"), rs.getString("nombre_completo"),
                        rs.getString("correo"), rs.getString("telefono"),
                        rs.getString("nombre_finca_asociacion")
                    );
                } else {
                    responsable = new TecnicoAgricola(
                        rs.getString("identificacion_responsable"), rs.getString("nombre_completo"),
                        rs.getString("correo"), rs.getString("telefono"),
                        rs.getString("especialidad_tecnica")
                    );
                }
                lista.add(new LaborAgricola(
                    rs.getString("codigo"), parcela, cultivo, responsable,
                    rs.getString("fecha_realizacion"), rs.getString("tipo_labor"),
                    rs.getString("descripcion"), rs.getDouble("costo_estimado")
                ));
            }
        } finally {
            ConexionBD.close(rs);
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
        return lista;
    }
}