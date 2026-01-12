/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import static Controlador.ConexionBBDD.conexion;
import Modelo.Habitacion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author DAM2Alu4
 */
public class HabitacionDAO {
    private Connection conexion;
    public HabitacionDAO() {
        this.conexion = ConexionBBDD.getConnection();
    }
    public int buscarHabitacion(String nombre) throws SQLException {
        String sql = "SELECT idHabitacion FROM servicio WHERE nombre = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nombre);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return 0;
        } else {
            return rs.getInt("idHabitacion");
        }
    }
        
    //metodo que recoge una lista del id de las habitaciones
    public List<String> buscarIdHabitaciones() {
        List<String> lista = new ArrayList<>();
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT idHabitacion FROM habitacion");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(String.valueOf(rs.getInt(1)));
            }

            return lista;
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

        //metodo que recoge las fechas de inicio y fin de reserva y el estado de reserva por habitacion
    public List<Object[]> buscarFechayEstadoPorHabitacion(int idHab) {
        List<Object[]> lista = new ArrayList<>();
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "SELECT fecha_inicio, fecha_fin, estado FROM reserva WHERE idHabitacion = ?"
            );
            ps.setInt(1, idHab);
            ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    LocalDate inicio = rs.getDate("fecha_inicio").toLocalDate();
                    LocalDate fin = rs.getDate("fecha_fin").toLocalDate();
                    String estado = rs.getString("estado");

                    lista.add(new Object[]{inicio, fin, estado});
                }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    //metodo que busca el estado de la habitacion
    public String buscarEstadoHabitacion(int idHabitacion) {
        try {
            String estado = null;

            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT estado FROM habitacion where idHabitacion = ?");
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                estado = rs.getString(1);
            }

            return estado;
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }

    //metodo que actualiza estado de la habitacion
    public void ActualizarEstadoHabitacion(int idHabitacion, String estado) {
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "UPDATE habitacion SET estado = ? where idHabitacion = ?");
            ps.setString(1, estado);
            ps.setInt(2, idHabitacion);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    //metodo que busca los datos de las habitaciones por id
    public Habitacion buscarHabitacionPorId(int idHabitacion) {

        Habitacion habitacion = null;

        PreparedStatement ps;
        try {
            ps = conexion.prepareStatement(
                    "SELECT idHabitacion, numero, tipo, capacidad, precio_base, precio_publico, estado FROM habitacion WHERE idHabitacion = ?");
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //idHabitacion,   numero,          tipo,     capacidad,    precio_base,   precio_publico      estado
                habitacion = new Habitacion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7));

            }
            return habitacion;
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return habitacion;
        }

    }

    //metodo que actualiza los datos de las habitaciones por id
    public void actualizarHabitacionPorId(int idHabitacion, int numero, String tipo, int capacidad, double precio_base, String estado) {

        PreparedStatement ps;
        try {
            ps = conexion.prepareStatement(
                    "Update habitacion set numero = ?, tipo = ?, capacidad = ?, precio_base = ?, estado = ? FROM habitacion WHERE idHabitacion = ?");
            ps.setInt(1, numero);
            ps.setString(2, tipo);
            ps.setInt(3, capacidad);
            ps.setDouble(4, precio_base);
            ps.setString(5, estado);
            ps.setInt(6, idHabitacion);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
