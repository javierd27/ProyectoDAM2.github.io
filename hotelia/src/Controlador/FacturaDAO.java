/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import static Controlador.ConexionBBDD.conexion;
import java.sql.*;
import Modelo.Factura;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author DAM2Alu4
 */
public class FacturaDAO {
    private Connection conexion;
    public FacturaDAO() {
        this.conexion = ConexionBBDD.getConnection();
    }
        
        public Factura buscaFactura(String dni) throws SQLException {
        String sql = "SELECT * FROM factura WHERE idCliente = ? AND estado = Pendiente";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
               
            return new Factura(rs.getInt("descuento"), rs.getInt("iva"), rs.getString("estado"), 
                                rs.getString("idCliente"), rs.getString("metodo_pago"), rs.getString("observacion"), 
                                rs.getDate("fecha_emision"), rs.getDouble("sub_total"), rs.getDouble("total"));
        }
    }
}
