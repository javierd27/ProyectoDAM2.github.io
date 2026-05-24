/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import static Controlador.ConexionBBDD.conexion;
import Modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author DAM2Alu4
 */
public class ClienteDAO {
        private Connection conexion;
    public ClienteDAO() {
        this.conexion = ConexionBBDD.getConnection();
    }
    public int insertaCliente(Cliente cliente) throws SQLException {
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

        return ps.executeUpdate();
    }

    public Cliente buscaCliente(String dni) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE dni_nie = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
            return new Cliente(rs.getString("dni_nie"), rs.getString("nombre"), rs.getString("apellido1"), rs.getString("apellido2"), rs.getDate("fecha_nac"),
                    rs.getString("correo"), rs.getString("telefono"), rs.getString("nacionalidad"), rs.getString("pais"), rs.getString("calle_numero"),
                    rs.getString("poblacion"), rs.getString("piso"));
        }
    }

    public int editarCliente(Cliente cliente, String dni) throws SQLException {
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

        return ps.executeUpdate();
    }
    
}
