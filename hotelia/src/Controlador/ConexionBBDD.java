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
    //metodo que recoge las fechas del inicio de reserva
    public List<LocalDate> buscarFechaInicio() {
        try {
            List<LocalDate> lista = new ArrayList<LocalDate>();
            
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT fecha FROM reservas ");
            
            return lista;
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
