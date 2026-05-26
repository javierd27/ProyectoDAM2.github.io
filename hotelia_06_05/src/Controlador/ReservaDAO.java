/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Reserva;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DAM2Alu4
 */
public class ReservaDAO {

    private Connection conexion;

    public ReservaDAO() {
        // Creo primero la conexion
        new ConexionBBDD();
        this.conexion = ConexionBBDD.getConnection();
    }

    public void selectTodasReservas(DefaultTableModel dtm) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "SELECT idReserva, idServicio, idHabitacion, idCliente, idFactura, fecha_inicio, fecha_fin, cantidad_personas, fecha_hora_reserva, estado FROM reserva ORDER BY fecha_inicio"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[]{
                    rs.getInt(1),
                    rs.getObject(2),
                    rs.getObject(3),
                    rs.getString(4),
                    rs.getInt(5),
                    rs.getDate(6),
                    rs.getDate(7),
                    rs.getInt(8),
                    rs.getDate(9),
                    rs.getString(10)
                };
                dtm.addRow(fila);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReservaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int insertaReserva(Reserva reserva) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "INSERT INTO reserva (idServicio, idHabitacion, idCliente, idFactura, fecha_inicio, fecha_fin, cantidad_personas, fecha_hora_reserva, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conexion.prepareStatement(sql);
        if (reserva.getIdServicio() == null) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, reserva.getIdServicio());
        }

        if (reserva.getIdHabitacion() == null) {
            ps.setNull(2, java.sql.Types.INTEGER);
        } else {
            ps.setInt(2, reserva.getIdHabitacion());
        }
        ps.setString(3, reserva.getIdCliente());
        ps.setInt(4, reserva.getIdFactura());
        ps.setDate(5, new java.sql.Date(reserva.getFecha_inicio().getTime()));
        ps.setDate(6, new java.sql.Date(reserva.getFecha_fin().getTime()));
        ps.setInt(7, reserva.getCantidad_personas());
        ps.setDate(8, new java.sql.Date(reserva.getFecha_hora_reserva().getTime()));
        ps.setString(9, reserva.getEstado());

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }

    public Reserva buscaReserva(String dni) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT * FROM reserva WHERE idCliente = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        Reserva reserva = null;
        if (rs.next()) {
            
            Integer idServ = rs.getObject("idServicio") != null ? rs.getInt("idServicio") : null;
            Integer idHab = rs.getObject("idHabitacion") != null ? rs.getInt("idHabitacion") : null;
            
            reserva = new Reserva(
                idServ, idHab, rs.getInt("idFactura"), rs.getInt("cantidad_personas"),
                rs.getString("idCliente"), rs.getString("estado"), rs.getDate("fecha_inicio"), 
                rs.getDate("fecha_fin"), rs.getDate("fecha_hora_reserva")
            );
        }
        rs.close();
        ps.close();
        return reserva;
    }

 
    public int editarReserva(Reserva reserva, int idReserva) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "UPDATE reserva SET idServicio=?, idHabitacion=?, idFactura=?, fecha_inicio=?, "
                + "fecha_fin=?, cantidad_personas=?, fecha_hora_reserva=?, estado=? "
                + "WHERE idReserva=?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        if (reserva.getIdServicio() == null) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, reserva.getIdServicio());
        }
        
        if (reserva.getIdHabitacion() == null) {
            ps.setNull(2, java.sql.Types.INTEGER);
        } else {
            ps.setInt(2, reserva.getIdHabitacion());
        }
        
        ps.setInt(3, reserva.getIdFactura());
        ps.setDate(4, new java.sql.Date(reserva.getFecha_inicio().getTime()));
        ps.setDate(5, new java.sql.Date(reserva.getFecha_fin().getTime()));
        ps.setInt(6, reserva.getCantidad_personas());
        ps.setDate(7, new java.sql.Date(reserva.getFecha_hora_reserva().getTime()));
        ps.setString(8, reserva.getEstado());
        ps.setInt(9, idReserva);

        int resultado = ps.executeUpdate();
        ps.close();
        return resultado;
    }

    public Reserva buscaReservaId(int idReserva) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT * FROM reserva WHERE idReserva = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idReserva);

        ResultSet rs = ps.executeQuery();
        Reserva reserva = null;
        if (rs.next()) {
            Integer idServ = rs.getObject("idServicio") != null ? rs.getInt("idServicio") : null;
            Integer idHab = rs.getObject("idHabitacion") != null ? rs.getInt("idHabitacion") : null;
            
            reserva = new Reserva(
                idServ, idHab, rs.getInt("idFactura"), rs.getInt("cantidad_personas"),
                rs.getString("idCliente"), rs.getString("estado"), rs.getDate("fecha_inicio"), 
                rs.getDate("fecha_fin"), rs.getDate("fecha_hora_reserva")
            );
            reserva.setIdReserva(rs.getInt("idReserva"));
        }
        rs.close();
        ps.close();
        return reserva;
    }

    public int eliminarReserva(DefaultTableModel dtm, int id) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return 0;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "DELETE FROM reserva WHERE idReserva = ?"
            );
            ps.setInt(1, id);
            int resultado = ps.executeUpdate();
            ps.close();
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(ReservaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
