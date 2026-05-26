/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DAM2Alu4
 */
public class ClienteDAO {

    private Connection conexion;

    public ClienteDAO() {
        // Creo primero la conexion
        new ConexionBBDD();
        this.conexion = ConexionBBDD.getConnection();
    }

    public void selectTodosClientes(DefaultTableModel dtm) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");

        PreparedStatement ps = conexion.prepareStatement(
            "SELECT dni_nie, nombre, apellido1, apellido2, fecha_nac, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso FROM cliente ORDER BY nombre"
        );

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] fila = new Object[]{
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDate(5),
                rs.getString(6),
                rs.getString(7),
                rs.getString(8),
                rs.getString(9),
                rs.getString(10),
                rs.getString(11),
                rs.getString(12)
            };

            dtm.addRow(fila);
        }

        rs.close();
        ps.close();
    }
    
    public int insertaCliente(Cliente cliente) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "INSERT INTO cliente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, cliente.getDni_nie());
        ps.setString(2, cliente.getNombre());
        ps.setString(3, cliente.getApellido1());
        ps.setString(4, cliente.getApellido2());
        ps.setDate(5, new java.sql.Date(cliente.getFecha_nac().getTime()));
        ps.setString(6, cliente.getMail());
        ps.setString(7, cliente.getTelefono());
        ps.setString(8, cliente.getNacionalidad());
        ps.setString(9, cliente.getPais());
        ps.setString(10, cliente.getCalle_numero());
        ps.setString(11, cliente.getPoblacion());
        ps.setString(12, cliente.getPiso());

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }

    public Cliente buscaCliente(String dni) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT * FROM cliente WHERE dni_nie = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        Cliente cliente = null;
        if (rs.next()) {
            cliente = new Cliente(
                rs.getString("dni_nie"), rs.getString("nombre"), 
                rs.getString("apellido1"), rs.getString("apellido2"), 
                rs.getDate("fecha_nac"), rs.getString("correo"), 
                rs.getString("telefono"), rs.getString("nacionalidad"), 
                rs.getString("pais"), rs.getString("calle_numero"),
                rs.getString("poblacion"), rs.getString("piso")
            );
        }
        rs.close();
        ps.close();
        return cliente;
    }

    public int editarCliente(Cliente cliente, String dni) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "UPDATE cliente SET nombre = ?, apellido1 = ?, apellido2 = ?, correo = ?, telefono = ?, nacionalidad = ?, pais = ?, calle_numero = ?, poblacion = ?, piso = ? "
                + "WHERE dni_nie = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, cliente.getNombre());
        ps.setString(2, cliente.getApellido1());
        ps.setString(3, cliente.getApellido2());
        ps.setString(4, cliente.getMail());
        ps.setString(5, cliente.getTelefono());
        ps.setString(6, cliente.getNacionalidad());
        ps.setString(7, cliente.getPais());
        ps.setString(8, cliente.getCalle_numero());
        ps.setString(9, cliente.getPoblacion());
        ps.setString(10, cliente.getPiso());
        ps.setString(11, dni);

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }
    
    public int getTotalClientes(LocalDate fechaLimite) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT COUNT(DISTINCT idCliente) AS totalClientes FROM reserva WHERE fecha_hora_reserva >= ?";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setDate(1, java.sql.Date.valueOf(fechaLimite));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalClientes");
            }
        }
        return 0;
    }
}
