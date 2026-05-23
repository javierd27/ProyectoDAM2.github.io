/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.*;
import Modelo.Factura;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DAM2Alu4
 */
public class FacturaDAO {

    private Connection conexion;

    public FacturaDAO() {
        // Creo primero la conexion
        new ConexionBBDD();
        this.conexion = ConexionBBDD.getConnection();
    }

    public Factura buscaFactura(String dni) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT * FROM factura WHERE idCliente = ? AND estado = 'Pendiente'";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        Factura f = null;
        if (rs.next()) {
            f = new Factura(
                rs.getInt("descuento"), rs.getInt("iva"), rs.getString("estado"),
                rs.getString("idCliente"), rs.getString("metodo_pago"), rs.getString("observacion"),
                rs.getDate("fecha_emision"), rs.getDouble("sub_total"), rs.getDouble("total")
            );
            f.setIdFactura(rs.getInt("idFactura"));
        }
        rs.close();
        ps.close();
        return f;
    }

    public Factura buscaFacturas(int idFactura) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT * FROM factura WHERE idFactura = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idFactura);

        ResultSet rs = ps.executeQuery();
        Factura f = null;
        if (rs.next()) {
            f = new Factura(
                rs.getInt("descuento"), rs.getInt("iva"), rs.getString("estado"),
                rs.getString("idCliente"), rs.getString("metodo_pago"), rs.getString("observacion"),
                rs.getDate("fecha_emision"), rs.getDouble("sub_total"), rs.getDouble("total")
            );
            f.setIdFactura(rs.getInt("idFactura"));
        }
        rs.close();
        ps.close();
        return f;
    }

    
    public int crearFacturaPendiente(String dni) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "INSERT INTO factura(idCliente, fecha_emision, sub_total, iva, descuento, metodo_pago, estado, total, observacion)" +
                " VALUES (?, NOW(), 0, 21, 0, 'Efectivo', 'Pendiente', 0, '')";

        // ✅ CORREGIDO: Statement.RETURN_GENERATED_KEYS para obtener el ID generado
        PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, dni);
        ps.executeUpdate();

        int idGenerado = 0;
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            idGenerado = rs.getInt(1);
        }
        rs.close();
        ps.close();
        return idGenerado;
    }

    public void recalcularFactura(int idFactura) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT SUM(IFNULL(h.precio_publico,0) + IFNULL(s.precio,0)) AS subtotal "
                + "FROM reserva r "
                + "LEFT JOIN habitacion h ON r.idHabitacion = h.idHabitacion "
                + "LEFT JOIN servicio s ON r.idServicio = s.idServicio "
                + "WHERE r.idFactura = ? AND r.estado = 'Aceptado'";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idFactura);

        ResultSet rs = ps.executeQuery();
        double subtotal = 0;
        if (rs.next()) {
            subtotal = rs.getDouble("subtotal");
        }
        rs.close();
        ps.close();

        // calcular total con IVA 21%
        double total = subtotal * 1.21;

        String update = "UPDATE factura SET sub_total = ?, total = ? WHERE idFactura = ?";
        PreparedStatement psUpdate = conexion.prepareStatement(update);
        psUpdate.setDouble(1, subtotal);
        psUpdate.setDouble(2, total);
        psUpdate.setInt(3, idFactura);
        psUpdate.executeUpdate();
        psUpdate.close();
    }

    public void selectTodasFacturas(DefaultTableModel dtm) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        PreparedStatement ps = conexion.prepareStatement(
            "SELECT idFactura, idCliente, fecha_emision, sub_total, iva, descuento, metodo_pago, estado, total, observacion FROM factura ORDER BY fecha_emision"
        );

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Object[] fila = new Object[]{
                rs.getInt(1),
                rs.getString(2),
                rs.getDate(3),
                rs.getDouble(4),
                rs.getInt(5),
                rs.getInt(6),
                rs.getString(7),
                rs.getString(8),
                rs.getDouble(9),
                rs.getString(10)
            };
            dtm.addRow(fila);
        }
        rs.close();
        ps.close();
    }

    public int eliminarFactura(DefaultTableModel dtm, int id) {
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return 0;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "DELETE FROM factura WHERE idFactura = ?"
            );
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            ps.close();
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(FacturaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int editarFactura(Factura factura) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "UPDATE factura SET metodo_pago = ?, estado = ?, descuento = ?, observacion = ? WHERE idFactura = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, factura.getMetodo_pago());
        ps.setString(2, factura.getEstado());
        ps.setInt(3, factura.getDescuento());
        ps.setString(4, factura.getObservacion());
        ps.setInt(5, factura.getIdFactura());

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }
}
