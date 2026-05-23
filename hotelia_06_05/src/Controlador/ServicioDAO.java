/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.*;
import Modelo.Servicio;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author DAM2Alu4
 */
public class ServicioDAO {

    private Connection conexion;

    public ServicioDAO() {
        // Creo primero la conexion
        new ConexionBBDD();
        this.conexion = ConexionBBDD.getConnection();
    }

    public int insertaServicio(Servicio nuevo) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "INSERT INTO servicio (nombre, precio, descripcion) VALUES (?, ?, ?)";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nuevo.getNombre());
        ps.setDouble(2, nuevo.getPrecio());
        ps.setString(3, nuevo.getDescripción());

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }

    public Servicio buscaServicio(String nombre) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT * FROM servicio WHERE nombre = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nombre);

        ResultSet rs = ps.executeQuery();
        Servicio servicio = null;
        if (rs.next()) {
            servicio = new Servicio(
                rs.getString("nombre"), 
                rs.getString("descripcion"), 
                rs.getDouble("precio")
            );
        }
        rs.close();
        ps.close();
        return servicio;
    }

    public int editarServicio(Servicio servicio, String nombre) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "UPDATE servicio SET precio = ?, descripcion = ? WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setDouble(1, servicio.getPrecio());
        ps.setString(2, servicio.getDescripción());
        ps.setString(3, nombre);

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }

    public int eliminaServicio(String nombre) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "DELETE FROM servicio WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, nombre);

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }

    public int buscarServicioId(String nombre) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT idServicio FROM servicio WHERE nombre = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nombre);

        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("idServicio");
        }
        rs.close();
        ps.close();
        return id;
    }

    public ResultSet todosServicios() throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT nombre FROM servicio";
        PreparedStatement ps = conexion.prepareStatement(sql);
        return ps.executeQuery();
    }

    public int getTotalServicios(LocalDate fechaLimite) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT COUNT(DISTINCT idServicio) AS totalServicios FROM reserva WHERE fecha_hora_reserva >= ? AND idServicio IS NOT NULL";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setDate(1, java.sql.Date.valueOf(fechaLimite));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalServicios");
            }
        }
        return 0;
    }

    public String getCantidadServiciosPorNombreString(LocalDate fechaLimite) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT s.nombre, COUNT(*) AS cantidad FROM reserva r JOIN servicio s ON r.idServicio = s.idServicio WHERE r.fecha_hora_reserva >= ? AND r.idServicio IS NOT NULL GROUP BY s.nombre";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setDate(1, java.sql.Date.valueOf(fechaLimite));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("nombre")).append(": ").append(rs.getInt("cantidad")).append("\n");
            }
        }
        return sb.toString();
    }
}
