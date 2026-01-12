/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import static Controlador.ConexionBBDD.conexion;
import Modelo.Reserva;
import java.sql.*;

/**
 *
 * @author DAM2Alu4
 */
public class ReservaDAO {
    private Connection conexion;
    public ReservaDAO() {
        this.conexion = ConexionBBDD.getConnection();
    }
    
     public int insertaReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reserva (idServicio, idHabitacion, idCliente, idFactura, fecha_inicio, fecha_fin, cantidad_personas, fecha_hora_reserva, estado) +"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, reserva.getIdServicio());
        ps.setInt(2, reserva.getIdHabitacion());
        ps.setString(3, reserva.getIdCliente());
        ps.setInt(4, reserva.getIdFactura());
        ps.setDate(5, new java.sql.Date(reserva.getFecha_inicio().getTime()));
        ps.setDate(6, new java.sql.Date(reserva.getFecha_fin().getTime()));
        ps.setInt(7, reserva.getCantidad_personas());
        ps.setDate(8, new java.sql.Date(reserva.getFecha_hora_reserva().getTime()));
        ps.setString(9, reserva.getEstado());

        

        return ps.executeUpdate();
    }
      
    
    public Reserva buscaReserva(String dni) throws SQLException {
        String sql = "SELECT * FROM reserva WHERE idCliente = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
            return new Reserva(rs.getInt("idServicio"), rs.getInt("idHabitacion"), rs.getInt("idFactura"), rs.getInt("cantidad_personas"),
                                rs.getString("idCliente"), rs.getString("estado"), rs.getDate("fecha_inicio"), rs.getDate("fecha_fin"), 
                                rs.getDate("fecha_hora_reserva"));
        }
    }
    /*
    
    public int editarReserva(Reserva reserva, String dni) throws SQLException {
        String sql = "UPDATE reserva SET precio = ?, descripcion = ? WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setDouble(1, servicio.getPrecio());
        ps.setString(2, servicio.getDescripción());
        ps.setString(3, nombre);
       
        return ps.executeUpdate();
    }
    */
}
