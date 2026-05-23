/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Habitacion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DAM2Alu4
 */
public class HabitacionDAO {

    private Connection conexion;

    public HabitacionDAO() {
        // Creo primero la conexion
        new ConexionBBDD();
        this.conexion = ConexionBBDD.getConnection();
    }

    /**
     * ✅ CORREGIDO: Antes buscaba en la tabla 'servicio' (BUG grave).
     * Ahora busca correctamente en la tabla 'habitacion'.
     * 
     * NOTA: este método busca por "nombre" pero la tabla habitacion no tiene
     * columna 'nombre'. Se ha cambiado a buscar por 'tipo' que es lo lógico.
     */
    public int buscarHabitacion(String tipo) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        // ✅ CORREGIDO: FROM habitacion (antes era FROM servicio)
        String sql = "SELECT idHabitacion FROM habitacion WHERE tipo = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, tipo);

        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("idHabitacion");
        }
        rs.close();
        ps.close();
        return id;
    }

    //metodo que recoge una lista del id de las habitaciones
    public List<String> buscarIdHabitaciones() {
        List<String> lista = new ArrayList<>();
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return lista;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT idHabitacion FROM habitacion");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(String.valueOf(rs.getInt(1)));
            }
            rs.close();
            ps.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(HabitacionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    //metodo que recoge las fechas de inicio y fin de reserva y el estado de reserva por habitacion
    public List<Object[]> buscarFechayEstadoPorHabitacion(int idHab) {
        List<Object[]> lista = new ArrayList<>();
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return lista;
        }
        
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
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return null;
        }
        
        try {
            String estado = null;
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT estado FROM habitacion WHERE idHabitacion = ?");
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                estado = rs.getString(1);
            }
            rs.close();
            ps.close();
            return estado;
        } catch (SQLException ex) {
            Logger.getLogger(HabitacionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //metodo que actualiza estado de la habitacion
    public void ActualizarEstadoHabitacion(int idHabitacion, String estado) {
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "UPDATE habitacion SET estado = ? WHERE idHabitacion = ?");
            ps.setString(1, estado);
            ps.setInt(2, idHabitacion);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(HabitacionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //metodo que busca los datos de las habitaciones por id
    public Habitacion buscarHabitacionPorId(int idHabitacion) {
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return null;
        }
        
        Habitacion habitacion = null;
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT idHabitacion, numero, tipo, capacidad, precio_base, precio_publico, estado FROM habitacion WHERE idHabitacion = ?");
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                habitacion = new Habitacion(
                    rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), 
                    rs.getDouble(5), rs.getDouble(6), rs.getString(7)
                );
            }
            rs.close();
            ps.close();
            return habitacion;
        } catch (SQLException ex) {
            Logger.getLogger(HabitacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            return habitacion;
        }
    }

    /**
     * Método que actualiza los datos de las habitaciones por id.
     * ✅ Ya estaba correcto (sin FROM en UPDATE).
     */
    public void actualizarHabitacionPorId(int idHabitacion, int numero, String tipo,
                                          int capacidad, double precio_base,
                                          double precio_publico, String estado) {
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "UPDATE habitacion SET numero = ?, tipo = ?, capacidad = ?, "
                + "precio_base = ?, precio_publico = ?, estado = ? WHERE idHabitacion = ?"
            );
            ps.setInt(1, numero);
            ps.setString(2, tipo);
            ps.setInt(3, capacidad);
            ps.setDouble(4, precio_base);
            ps.setDouble(5, precio_publico);
            ps.setString(6, estado);
            ps.setInt(7, idHabitacion);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(HabitacionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getTotalHabitaciones(LocalDate fechaLimite) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT COUNT(DISTINCT idHabitacion) AS totalHabitaciones FROM reserva WHERE fecha_hora_reserva >= ?";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setDate(1, java.sql.Date.valueOf(fechaLimite));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalHabitaciones");
            }
        }
        return 0;
    }

    public String getCantidadHabitacionesPorTipoString(LocalDate fechaLimite) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT h.tipo, COUNT(*) AS cantidad FROM reserva r JOIN habitacion h ON r.idHabitacion = h.idHabitacion WHERE r.fecha_hora_reserva >= ? GROUP BY h.tipo";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setDate(1, java.sql.Date.valueOf(fechaLimite));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("tipo")).append(": ").append(rs.getInt("cantidad")).append("\n");
            }
        }
        return sb.toString();
    }

    //Julia
    public ResultSet todasHabitacionesLibres() throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        PreparedStatement ps = conexion.prepareStatement(
            "SELECT DISTINCT tipo FROM habitacion WHERE estado = 'Libre'"
        );
        return ps.executeQuery();
    }

    public int buscarHabitacionId(String tipo) throws SQLException {
        if (conexion == null) throw new SQLException("Conexión no disponible");
        
        String sql = "SELECT idHabitacion FROM habitacion WHERE tipo = ? AND estado = 'Libre' LIMIT 1";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, tipo);
        ResultSet rs = ps.executeQuery();

        int idHabitacion = 0;
        if (rs.next()) {
            idHabitacion = rs.getInt("idHabitacion");
        }
        rs.close();
        ps.close();
        return idHabitacion;
    }

    //metodo que inserta una nueva habitacion
    public void insertarHabitacion(int numero, String tipo, int capacidad,
                                    double precio_base, double precio_publico, String estado) {
        if (conexion == null) {
            System.out.println("❌ Conexión no disponible");
            return;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "INSERT INTO habitacion (numero, tipo, capacidad, precio_base, precio_publico, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setInt(1, numero);
            ps.setString(2, tipo);
            ps.setInt(3, capacidad);
            ps.setDouble(4, precio_base);
            ps.setDouble(5, precio_publico);
            ps.setString(6, estado);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(HabitacionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
