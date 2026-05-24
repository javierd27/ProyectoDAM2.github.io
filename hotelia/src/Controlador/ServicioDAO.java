/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.*;
import static Controlador.ConexionBBDD.conexion;
import Modelo.Servicio;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author DAM2Alu4
 */
public class ServicioDAO {
    private Connection conexion;
    public ServicioDAO() {
        this.conexion = ConexionBBDD.getConnection();
    }
    public int insertaServicio(Servicio nuevo) throws SQLException {
        String sql = "INSERT INTO servicio (nombre, precio, descripcion) VALUES (?, ?, ?)";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nuevo.getNombre());
        ps.setDouble(2, nuevo.getPrecio());
        ps.setString(3, nuevo.getDescripción());

        return ps.executeUpdate();
    }

     
    public Servicio buscaServicio(String nombre) throws SQLException {
        String sql = "SELECT * FROM servicio WHERE nombre = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nombre);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
            return new Servicio(rs.getString("nombre"),  rs.getString("descripcion"), rs.getDouble("precio"));
        }
    }

    
    public int editarServicio(Servicio servicio, String nombre) throws SQLException {
        String sql = "UPDATE servicio SET precio = ?, descripcion = ? WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setDouble(1, servicio.getPrecio());
        ps.setString(2, servicio.getDescripción());
        ps.setString(3, nombre);

        return ps.executeUpdate();
    }

    
    public int eliminaServicio(String nombre) throws SQLException {
        String sql = "DELETE FROM servicio WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, nombre);

        return ps.executeUpdate();
    }
    
                       
    public int buscarServicioId(String nombre) throws SQLException {
        String sql = "SELECT idServicio FROM servicio WHERE nombre = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nombre);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return 0;
        } else {
            return rs.getInt("idServicio");
        }
    }

    
    public ResultSet todosServicios() throws SQLException {
        String sql = "SELECT nombre FROM servicio";
        PreparedStatement ps = conexion.prepareStatement(sql);
        return ps.executeQuery();
    }
}
