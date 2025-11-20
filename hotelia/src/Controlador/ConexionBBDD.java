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
    
    public Cliente buscaCliente(String dni) throws SQLException{
        String sql = "SELECT * FROM cliente WHERE dni_nie = ?";
        
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);
        
        ResultSet rs = ps.executeQuery();
        if (!rs.next()){
            return null;
        }else{
            return new Cliente(rs.getString("dni_nie"), rs.getString("nombre"), rs.getString("apellido1"), rs.getString("apellido2"), rs.getDate("fecha_nac"), 
                                rs.getString("correo"), rs.getString("telefono"), rs.getString("nacionalidad"), rs.getString("pais"), rs.getString("calle_numero"), 
                                rs.getString("poblacion"), rs.getString("piso"));
        }
    }
    
    public int editarCliente(Cliente cliente, String dni) throws SQLException{
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

    public void cerrar() {
        try {
            conexion.close();
        } catch (java.sql.SQLException sqle) {
            System.out.println("No se ha podido cerrar la conexión");
        }
    }

}
