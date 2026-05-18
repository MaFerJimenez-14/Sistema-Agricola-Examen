package com.finca.dao;

import Conexion.ConexionBD;
import com.finca.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CultivoDAO {

    public boolean insertar(Cultivo cultivo) throws SQLException {
        String sql = "INSERT INTO cultivos(codigo, nombre, variedad, fecha_siembra, "
                   + "tipo_cultivo, duracion_ciclo_dias, anios_produccion) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cultivo.getCodigo());
            ps.setString(2, cultivo.getNombre());
            ps.setString(3, cultivo.getVariedad());
            ps.setString(4, cultivo.getFechaSiembra());
            ps.setString(5, cultivo.getTipoCultivo());
            if (cultivo instanceof CultivoAnual) {
                ps.setInt(6, ((CultivoAnual) cultivo).getDuracionCicloDias());
                ps.setNull(7, Types.INTEGER);
            } else if (cultivo instanceof CultivoPerenne) {
                ps.setNull(6, Types.INTEGER);
                ps.setInt(7, ((CultivoPerenne) cultivo).getAniosProduccion());
            } else {
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.INTEGER);
            }
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean actualizar(Cultivo cultivo) throws SQLException {
        String sql = "UPDATE cultivos SET nombre=?, variedad=?, fecha_siembra=?, tipo_cultivo=?, "
                   + "duracion_ciclo_dias=?, anios_produccion=? WHERE codigo=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cultivo.getNombre());
            ps.setString(2, cultivo.getVariedad());
            ps.setString(3, cultivo.getFechaSiembra());
            ps.setString(4, cultivo.getTipoCultivo());
            if (cultivo instanceof CultivoAnual) {
                ps.setInt(5, ((CultivoAnual) cultivo).getDuracionCicloDias());
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setNull(5, Types.INTEGER);
                ps.setInt(6, ((CultivoPerenne) cultivo).getAniosProduccion());
            }
            ps.setString(7, cultivo.getCodigo());
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM cultivos WHERE codigo=?";
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
        String sql = "SELECT codigo FROM cultivos WHERE codigo = ?";
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

    public List<Cultivo> listar() throws SQLException {
        List<Cultivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM cultivos";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String tipo = rs.getString("tipo_cultivo");
                Cultivo c;
                if ("Anual".equalsIgnoreCase(tipo)) {
                    c = new CultivoAnual(
                        rs.getString("codigo"), rs.getString("nombre"),
                        rs.getString("variedad"), rs.getString("fecha_siembra"),
                        rs.getInt("duracion_ciclo_dias")
                    );
                } else {
                    c = new CultivoPerenne(
                        rs.getString("codigo"), rs.getString("nombre"),
                        rs.getString("variedad"), rs.getString("fecha_siembra"),
                        rs.getInt("anios_produccion")
                    );
                }
                lista.add(c);
            }
        } finally {
            ConexionBD.close(rs);
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
        return lista;
    }
}