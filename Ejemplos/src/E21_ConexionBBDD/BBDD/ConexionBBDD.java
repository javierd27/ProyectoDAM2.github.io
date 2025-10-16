/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package E21_ConexionBBDD.BBDD;

/**
 *
 * @author DAM2Alu3
 */

    
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class ConexionBBDD {
    
// cnxBBDD=new ConexionBBDD("localhost", "3306", "world","root", "root");

 
    /**
     * Method to connect to the database by passing parameters.
     * Método para establecer la conexión a la base de datos mediante el paso de parámetros.
     * 
     * @param host <code>String</code> host name or ip. Nombre del host o ip.
     * @param port <code>String</code> listening database port. Puerto en el que escucha la base de datos.
     * @param database <code>String</code> database name for the conexion. Nombre de la base de datos para la conexión.
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
        this.database = "world";
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
    public Connection getConnection(){
        return conexion;
    }
    public void selectSQL(PreparedStatement ps, DefaultTableModel dtm){
        try {         
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int tam=rsmd.getColumnCount();
             for (int i = 1; i <= tam; i++) {
                dtm.addColumn(rsmd.getColumnLabel(i));               
            }
         
            while(rs.next()){
                String [] fila = new String[tam];
                for (int i = 0; i < tam; i++) {
                    fila[i]=rs.getString(i+1);                 
                }
                dtm.addRow(fila);   
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBBDD.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    public void selectSQL(PreparedStatement ps, DefaultComboBoxModel dcbm){
        try {

            ResultSet rs = ps.executeQuery();        
            while(rs.next()){
                dcbm.addElement(rs.getString(1));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBBDD.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    public void selectSQL(PreparedStatement ps, DefaultListModel dlm){
        try {

            ResultSet rs = ps.executeQuery();        
            while(rs.next()){
                dlm.addElement(rs.getString(1));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBBDD.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    public int ejecutaSQL(PreparedStatement ps) {
        try {
            int resultado = ps.executeUpdate();
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBBDD.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    public void cerrar(){
        try {
            conexion.close();
        } catch (java.sql.SQLException sqle) {
            System.out.println("No se ha podido cerrar la conexión");
        }
    }

}

