package com.finca.dao;

import Conexion.ConexionBD;
import com.finca.modelo.Parcela;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcelaDAO {

    public boolean insertar(Parcela parcela) throws SQLException {
        String sql = "INSERT INTO parcelas(codigo, nombre, ubicacion, area, tipo_suelo, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, parcela.getCodigo());
            ps.setString(2, parcela.getNombre());
            ps.setString(3, parcela.getUbicacion());
            ps.setDouble(4, parcela.getArea());
            ps.setString(5, parcela.getTipoSuelo());
            ps.setString(6, parcela.getEstado());
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean actualizar(Parcela parcela) throws SQLException {
        String sql = "UPDATE parcelas SET nombre=?, ubicacion=?, area=?, tipo_suelo=?, estado=? WHERE codigo=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, parcela.getNombre());
            ps.setString(2, parcela.getUbicacion());
            ps.setDouble(3, parcela.getArea());
            ps.setString(4, parcela.getTipoSuelo());
            ps.setString(5, parcela.getEstado());
            ps.setString(6, parcela.getCodigo());
            ps.executeUpdate();
            return true;
        } finally {
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
    }

    public boolean eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM parcelas WHERE codigo=?";
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
        String sql = "SELECT codigo FROM parcelas WHERE codigo = ?";
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

    public List<Parcela> listar() throws SQLException {
        List<Parcela> lista = new ArrayList<>();
        String sql = "SELECT * FROM parcelas";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Parcela p = new Parcela(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("ubicacion"),
                    rs.getDouble("area"),
                    rs.getString("tipo_suelo"),
                    rs.getString("estado")
                );
                lista.add(p);
            }
        } finally {
            ConexionBD.close(rs);
            ConexionBD.close(ps);
            ConexionBD.close(con);
        }
        return lista;
    }
}