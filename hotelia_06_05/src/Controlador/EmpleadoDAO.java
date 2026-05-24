/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        // Creo primero la conexion
        new ConexionBBDD();
        this.conexion = ConexionBBDD.getConnection();
    }
    // CONSULTAS PARA LA APP 

    public String BuscarContraseñaEmpleado(String us) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return "";
        }
        
        String resul = "";
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT contrasenya FROM empleado WHERE usuario = ?"
            );
            ps.setString(1, us);

            ResultSet reg = ps.executeQuery();
            if (reg.next()) {
                resul = reg.getString(1);
            }
            reg.close();
            ps.close();
            return resul;

        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "SELECT dni_nie, nombre, apellido1, apellido2, fecha_nac, usuario, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso FROM empleado"
            );
            ResultSet rs = ps.executeQuery();
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
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Empleado> selectTodosEmpleadosEditar() {
        List<Empleado> listaEmpleado = new ArrayList<>();
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return listaEmpleado;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "SELECT dni_nie, nombre, apellido1, apellido2, fecha_nac, usuario, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso FROM empleado"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11),
                        rs.getString(12),
                        rs.getString(13),
                        rs.getString(14));
                listaEmpleado.add(empleado);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Ha entrado en el sqlException");
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaEmpleado;
    }

    /**
     * Almaceno un empleado
     *
     * @param id
     * @return
     */
    public Empleado selectEmpleadoUnico(String id) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return null;
        }
        
        Empleado empleado = null;
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "SELECT dni_nie, nombre, apellido1, apellido2, fecha_nac, usuario, contrasenya, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso FROM empleado WHERE dni_nie = ?"
            );
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                empleado = new Empleado(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11),
                        rs.getString(12),
                        rs.getString(13),
                        rs.getString(14),
                        rs.getString(15));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Ha entrado en el sqlException");
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return empleado;
    }

    /**
     * Actualizar empleado segun lo que quiera el cliente
     *
     * @param e
     * @return
     */
    public boolean updateEmpleado(Empleado e) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return false;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "UPDATE empleado SET nombre = ?, apellido1 = ?, apellido2 = ?, fecha_nac = ?, usuario = ?, rol = ?, correo = ?, telefono = ?, nacionalidad = ?, pais = ?, calle_numero = ?, poblacion = ?, piso = ? WHERE dni_nie = ?"
            );
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido1());
            ps.setString(3, e.getApellido2());
            ps.setDate(4, new java.sql.Date(e.getFecha_nac().getTime()));
            ps.setString(5, e.getUsuario());
            ps.setString(6, e.getRol());
            ps.setString(7, e.getCorreo());
            ps.setString(8, e.getTelefono());
            ps.setString(9, e.getNacionalidad());
            ps.setString(10, e.getPais());
            ps.setString(11, e.getCalle_numero());
            ps.setString(12, e.getPoblacion());
            ps.setString(13, e.getPiso());
            ps.setString(14, e.getDni_nie());
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException ex) {
            System.out.println("Ha petado al actualizar: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Consulta para comprobar al insertar un empleado no haya otro empleado con
     * el mismo id
     *
     * @param dni Si devuelve true es que el dni ya existe por lo cual no se
     * puede insertar el Empleado
     */
    public boolean consultaIdEmpleado(String dni) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return false;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "SELECT * FROM empleado WHERE dni_nie = ?"
            );
            ps.setString(1, dni);
            ResultSet con = ps.executeQuery();
            boolean existe = con.next();
            con.close();
            ps.close();
            return existe;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertEmpleado(Empleado empleado) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return false;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "INSERT INTO empleado (dni_nie, nombre, apellido1, apellido2, fecha_nac, usuario, contrasenya, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, empleado.getDni_nie());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getApellido1());
            ps.setString(4, empleado.getApellido2());
            ps.setDate(5, new java.sql.Date(empleado.getFecha_nac().getTime()));
            ps.setString(6, empleado.getUsuario());
            ps.setString(7, empleado.getContrasenya());
            ps.setString(8, empleado.getRol());
            ps.setString(9, empleado.getCorreo());
            ps.setString(10, empleado.getTelefono());
            ps.setString(11, empleado.getNacionalidad());
            ps.setString(12, empleado.getPais());
            ps.setString(13, empleado.getCalle_numero());
            ps.setString(14, empleado.getPoblacion());
            ps.setString(15, empleado.getPiso());

            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int deleteEmpleado(DefaultTableModel dtm, String id) {
        if (conexion == null) {
            System.out.println("Conexión no disponible");
            return 0;
        }
        
        try {
            PreparedStatement ps = conexion.prepareStatement(
                "DELETE FROM empleado WHERE dni_nie = ?"
            );
            ps.setString(1, id);
            int resultado = ps.executeUpdate();
            ps.close();
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
