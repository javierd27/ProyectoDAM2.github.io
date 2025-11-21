/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author DAM2Alu3
 * @author DAM2Alu4
 */
import Modelo.Cliente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class ConexionBBDD {

// cnxBBDD=new ConexionBBDD("localhost", "3306", "world","root", "root");
    /**
     * Method to connect to the database by passing parameters. Método para
     * establecer la conexión a la base de datos mediante el paso de parámetros.
     *
     * @param host <code>String</code> host name or ip. Nombre del host o ip.
     * @param port <code>String</code> listening database port. Puerto en el que
     * escucha la base de datos.
     * @param database <code>String</code> database name for the conexion.
     * Nombre de la base de datos para la conexión.
     * @param user <code>String</code> user name. Nombre de usuario.
     * @param password  <code>String</code> user password. Password del usuario.
     */
    String host;
    String port;
    String database;
    String user;
    String password;
    Connection conexion = null;

    public ConexionBBDD() {
        this.host = "localhost";
        this.port = "3306";
        this.database = "hotelia";
        this.user = "root";
        this.password = "root";
        conectar();
    }

    public ConexionBBDD(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        conectar();
    }

    private void conectar() {
        String url = "";
        try {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de MySQL: " + ex);
            }
            url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            // Database connect
            // Conectamos con la base de datos
            conexion = DriverManager.getConnection(
                    url,
                    user, password);
            boolean valid = conexion.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error al conectar con la base de datos de MySQL(" + url + "): " + sqle);
        }
    }

    public Connection getConnection() {
        return conexion;
    }



    // CONSULTAS PARA LA APP 
    public String BuscarContraseñaEmpleado(String us) {
        String resul = "";
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT contrasenya FROM empleados WHERE usuario = ?"
            );
            ps.setString(1, us); // CORREGIDO

            ResultSet reg = ps.executeQuery();
            if (reg.next()) {
                resul = reg.getString(1);
            }
            return resul;

        } catch (SQLException ex) {
            Logger.getLogger(ConexionBBDD.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public int insertaCliente(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO cliente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, cliente.getDni_nie());
        ps.setString(2, cliente.getNombre());
        ps.setString(3, cliente.getApellido1());
        ps.setString(4, cliente.getApellido2());
       // ps.setDate(5, (java.sql.Date) cliente.getFecha_nac());
        ps.setDate(5,new java.sql.Date(cliente.getFecha_nac().getTime()));
        ps.setString(6, cliente.getMail());
        ps.setString(7, cliente.getTelefono());
        ps.setString(8, cliente.getNacionalidad());
        ps.setString(9, cliente.getPais());
        ps.setString(10, cliente.getCalle_numero());        
        ps.setString(11, cliente.getPoblacion());
        ps.setString(12, cliente.getPiso());
        
        return ps.executeUpdate();
    }
    
    public ResultSet buscaCliente(String dni) throws SQLException{
        String sql = "SELECT * FROM cliente WHERE dni_nie = ?";
        
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);
        
        ResultSet rs = ps.executeQuery();
      
        return rs;
    }
    //metodo que recoge una lista de las habitaciones
    public List<String> buscarHabitaciones() {
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
    
    
    //metodo que recoge las fechas del inicio de reserva por habitacion
    public LocalDate buscarFechaInicio(int idHabitacion) {
        try {
            LocalDate fecha_inicio = null;
            
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT fecha_inicio FROM reserva where idHabitacion = ?");
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fecha_inicio = rs.getDate(1).toLocalDate();
            }
          
            return fecha_inicio;
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
        //metodo que recoge las fechas del inicio de reserva
    public LocalDate buscarFechaFin(int idHabitacion) {
        try {
             LocalDate fecha_fin = null;
            
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT fecha_fin FROM reserva where idHabitacion = ?");
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fecha_fin = rs.getDate(1).toLocalDate();
            }
          
            return fecha_fin;
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
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
     
    
    //metodo que busca el estado de la reserva por id habitacion
     public String buscarEstadoDeReservaPorHabitacion(int idHabitacion) {
        try {
            String estado = null;
            
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT estado FROM reserva where idHabitacion = ?");
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
    

    public void cerrar() {
        try {
            conexion.close();
        } catch (java.sql.SQLException sqle) {
            System.out.println("No se ha podido cerrar la conexión");
        }
    }

}
