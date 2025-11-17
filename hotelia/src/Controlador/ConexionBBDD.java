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
    
    public void insertaCliente(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO cliente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? , ?)";
        
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, cliente.getDni_nie());
        ps.setString(2, cliente.getNombre());
        ps.setString(3, cliente.getApellido1());
        ps.setString(4, cliente.getApellido2());
        ps.setDate(5, (Date) cliente.getFecha_nac());
        ps.setString(6, cliente.getMail());
        ps.setString(7, cliente.getTelefono());
        ps.setString(8, cliente.getNacionalidad());
        ps.setString(9, cliente.getPais());
        ps.setString(10, cliente.getCalle_numero());        
        ps.setString(11, cliente.getPoblacion());
        ps.setString(12, cliente.getPiso());
        
    //    ps.executeUpdate();**********************************************************
    }
    //metodo que recoge una lista de las habitaciones
    public int[] buscarHabitaciones() {
        int[] lista = null;
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT idHabitacion FROM habitacion");
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int tam = rsmd.getColumnCount();
            for (int i = 1; i <= tam; i++) {
                lista[i] = rs.getInt(i);
            }
            
            
            return lista;
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }
    
    
    //metodo que recoge las fechas del inicio de reserva por habitacion
    public List<LocalDate> buscarFechaInicio(int idHabitacion) {
        try {
            List<LocalDate> lista = new ArrayList<LocalDate>();
            
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT fecha_inicio FROM reserva where idHabitacion = ?");
            
            return lista;
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
        //metodo que recoge las fechas del inicio de reserva
    public LocalDate buscarFechaFin(int idHabitacion) {
        try {
            
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT fecha_inicio FROM reserva where idHabitacion = ?");
                    ps.setInt(0,idHabitacion);
                    ResultSet reg = ps.executeQuery();
            
            return null;
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
