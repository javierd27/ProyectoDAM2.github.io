/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import static Controlador.ConexionBBDD.conexion;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DAM2Alu4
 */
public class EmpleadoDAO {

    private Connection conexion;
    public EmpleadoDAO() {
        this.conexion = ConexionBBDD.getConnection();
    }
        // CONSULTAS PARA LA APP 
    public String BuscarContraseñaEmpleado(String us) {
        String resul = "";
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT contrasenya FROM empleado WHERE usuario = ?"
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

    /**
     * Consulta para recoger la toda la informacion de los empleados excepto la
     * contraseña Para llevarlo a un Jtable
     *
     * @param dtm
     */
    public void selectTodosEmpleados(DefaultTableModel dtm) {
        try {
            PreparedStatement ps;
            ps = conexion.prepareStatement("Select dni_nie, nombre, apellido1, apellido2, fecha_nac,usuario,rol,correo,telefono,nacionalidad,pais,calle_numero,poblacion,piso from empleado;");
            ResultSet rs = ps.executeQuery();
            // recorremos todos los empleados
            while (rs.next()) {
                Object[] fila = new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getString(10),
                    rs.getString(11),
                    rs.getString(12),
                    rs.getString(13),
                    rs.getString(14)
                };
                dtm.addRow(fila);
            }// end while
        } // selectSQL
        catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }// end selectTodosEmpleados
    
}
