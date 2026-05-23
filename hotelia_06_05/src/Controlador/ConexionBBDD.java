/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author DAM2Alu3
 * @author DAM2Alu4
 * @author DAM2Alu16
 */

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelo.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

// Imports para cargar datos de prueba
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConexionBBDD {

    String host;
    String port;
    String database;
    String user;
    String password;
    static Connection conexion = null;

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
                return;
            }
            url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            conexion = DriverManager.getConnection(url, user, password);
            
            if (conexion != null && conexion.isValid(5)) {
                System.out.println("Conexión a BD exitosa: " + url);
            } else {
                System.out.println(" Validación de conexión falló");
                conexion = null;
            }
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error al conectar con MySQL (" + url + "): " + sqle.getMessage());
            conexion = null;
        }
    }

    public static Connection getConnection() {
        return conexion;
    }
    
    /**
     * Comprueba si la conexión a la BD es válida
     * @return true si la conexión está activa
     */
    public static boolean isConexionValida() {
        return conexion != null;
    }

    /**
     * Método para buscar la contraseña de un empleado por su usuario
     * @param us nombre de usuario
     * @return hash de la contraseña o cadena vacía
     */
    public String BuscarContraseñaEmpleado(String us) {
        if (conexion == null) {
            System.out.println("Error: Conexión a BD no disponible");
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
            Logger.getLogger(ConexionBBDD.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    /**
     * Método para consultar el rol del empleado
     * @param usuario nombre de usuario
     * @return "administrador" o "empleado" o cadena vacía
     */
    public String comprobarRol(String usuario) {
        if (conexion == null) {
            System.out.println("Error: Conexión a BD no disponible");
            return "";
        }
        
        String rol = "";
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT rol FROM empleado WHERE usuario = ?"
            );
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rol = rs.getString(1);
            }
            rs.close();
            ps.close();
            return rol;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBBDD.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public void cerrar() {
        try {
            if (conexion != null) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (java.sql.SQLException sqle) {
            System.out.println("No se ha podido cerrar la conexión: " + sqle.getMessage());
        }
    }
    
    // ============================================================
    // MÉTODO PARA CARGAR DATOS DE PRUEBA DESDE XML/JSON
    // ============================================================
    
    /**
     * Abre un JFileChooser para seleccionar un archivo XML o JSON
     * y carga los datos en la BD para pruebas.
     * 
     * Soporta archivos XML con la estructura:
     * <datos>
     *   <clientes>
     *     <cliente>
     *       <dni_nie>12345678A</dni_nie>
     *       <nombre>...</nombre>
     *       ...
     *     </cliente>
     *   </clientes>
     *   <empleados>...</empleados>
     *   <habitaciones>...</habitaciones>
     *   <servicios>...</servicios>
     * </datos>
     * 
     * @return true si la carga fue exitosa
     */
    public boolean cargarDatosDePrueba() {
        if (conexion == null) {
            JOptionPane.showMessageDialog(null, 
                "No hay conexión a la base de datos", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Crear JFileChooser para seleccionar archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona archivo de datos de prueba (XML o JSON)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Archivos XML y JSON (*.xml, *.json)", "xml", "json");
        fileChooser.setFileFilter(filter);
        
        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        
        File archivo = fileChooser.getSelectedFile();
        String nombreArchivo = archivo.getName().toLowerCase();
        
        try {
            if (nombreArchivo.endsWith(".xml")) {
                return cargarDatosDesdeXML(archivo);
            } else if (nombreArchivo.endsWith(".json")) {
                return cargarDatosDesdeJSON(archivo);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Formato no soportado. Usa XML o JSON.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                " Error al cargar datos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Carga datos desde un archivo XML
     */
    private boolean cargarDatosDesdeXML(File archivo) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(archivo);
        doc.getDocumentElement().normalize();
        
        int totalInsertados = 0;
        
        // ---- CARGAR CLIENTES ----
        NodeList clientes = doc.getElementsByTagName("cliente");
        for (int i = 0; i < clientes.getLength(); i++) {
            Node nodo = clientes.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nodo;
                try {
                    PreparedStatement ps = conexion.prepareStatement(
                        "INSERT IGNORE INTO cliente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    );
                    ps.setString(1, getTagValue(e, "dni_nie"));
                    ps.setString(2, getTagValue(e, "nombre"));
                    ps.setString(3, getTagValue(e, "apellido1"));
                    ps.setString(4, getTagValue(e, "apellido2"));
                    ps.setDate(5, java.sql.Date.valueOf(getTagValue(e, "fecha_nac")));
                    ps.setString(6, getTagValue(e, "correo"));
                    ps.setString(7, getTagValue(e, "telefono"));
                    ps.setString(8, getTagValue(e, "nacionalidad"));
                    ps.setString(9, getTagValue(e, "pais"));
                    ps.setString(10, getTagValue(e, "calle_numero"));
                    ps.setString(11, getTagValue(e, "poblacion"));
                    ps.setString(12, getTagValue(e, "piso"));
                    totalInsertados += ps.executeUpdate();
                    ps.close();
                } catch (Exception ex) {
                    System.out.println("Error al insertar cliente: " + ex.getMessage());
                }
            }
        }
        
        // ---- CARGAR EMPLEADOS ----
        NodeList empleados = doc.getElementsByTagName("empleado");
        for (int i = 0; i < empleados.getLength(); i++) {
            Node nodo = empleados.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nodo;
                try {
                    PreparedStatement ps = conexion.prepareStatement(
                        "INSERT IGNORE INTO empleado (dni_nie, nombre, apellido1, apellido2, fecha_nac, usuario, contrasenya, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    );
                    ps.setString(1, getTagValue(e, "dni_nie"));
                    ps.setString(2, getTagValue(e, "nombre"));
                    ps.setString(3, getTagValue(e, "apellido1"));
                    ps.setString(4, getTagValue(e, "apellido2"));
                    ps.setDate(5, java.sql.Date.valueOf(getTagValue(e, "fecha_nac")));
                    ps.setString(6, getTagValue(e, "usuario"));
                    // Hashear la contraseña con BCrypt al cargar
                    String passOriginal = getTagValue(e, "contrasenya");
                    String passHash = Seguridad.hashPassword(passOriginal);
                    ps.setString(7, passHash);
                    ps.setString(8, getTagValue(e, "rol"));
                    ps.setString(9, getTagValue(e, "correo"));
                    ps.setString(10, getTagValue(e, "telefono"));
                    ps.setString(11, getTagValue(e, "nacionalidad"));
                    ps.setString(12, getTagValue(e, "pais"));
                    ps.setString(13, getTagValue(e, "calle_numero"));
                    ps.setString(14, getTagValue(e, "poblacion"));
                    ps.setString(15, getTagValue(e, "piso"));
                    totalInsertados += ps.executeUpdate();
                    ps.close();
                } catch (Exception ex) {
                    System.out.println("Error al insertar empleado: " + ex.getMessage());
                }
            }
        }
        
        // ---- CARGAR HABITACIONES ----
        NodeList habitaciones = doc.getElementsByTagName("habitacion");
        for (int i = 0; i < habitaciones.getLength(); i++) {
            Node nodo = habitaciones.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nodo;
                try {
                    PreparedStatement ps = conexion.prepareStatement(
                        "INSERT IGNORE INTO habitacion (numero, tipo, capacidad, precio_base, precio_publico, estado) VALUES (?, ?, ?, ?, ?, ?)"
                    );
                    ps.setInt(1, Integer.parseInt(getTagValue(e, "numero")));
                    ps.setString(2, getTagValue(e, "tipo"));
                    ps.setInt(3, Integer.parseInt(getTagValue(e, "capacidad")));
                    ps.setDouble(4, Double.parseDouble(getTagValue(e, "precio_base")));
                    ps.setDouble(5, Double.parseDouble(getTagValue(e, "precio_publico")));
                    ps.setString(6, getTagValue(e, "estado"));
                    totalInsertados += ps.executeUpdate();
                    ps.close();
                } catch (Exception ex) {
                    System.out.println("Error al insertar habitación: " + ex.getMessage());
                }
            }
        }
        
        // ---- CARGAR SERVICIOS ----
        NodeList servicios = doc.getElementsByTagName("servicio");
        for (int i = 0; i < servicios.getLength(); i++) {
            Node nodo = servicios.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nodo;
                try {
                    PreparedStatement ps = conexion.prepareStatement(
                        "INSERT IGNORE INTO servicio (nombre, precio, descripcion) VALUES (?, ?, ?)"
                    );
                    ps.setString(1, getTagValue(e, "nombre"));
                    ps.setDouble(2, Double.parseDouble(getTagValue(e, "precio")));
                    ps.setString(3, getTagValue(e, "descripcion"));
                    totalInsertados += ps.executeUpdate();
                    ps.close();
                } catch (Exception ex) {
                    System.out.println("Error al insertar servicio: " + ex.getMessage());
                }
            }
        }
        
        JOptionPane.showMessageDialog(null, 
            "✅ Datos cargados correctamente.\nRegistros insertados: " + totalInsertados, 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    /**
     * Carga datos desde un archivo JSON (parser simple)
     */
    private boolean cargarDatosDesdeJSON(File archivo) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
        }
        String json = sb.toString();
        int totalInsertados = 0;
        
        // Parser JSON simple buscando bloques por tipo
        totalInsertados += parsearBloqueJSON(json, "clientes", "cliente");
        totalInsertados += parsearBloqueJSON(json, "empleados", "empleado");
        totalInsertados += parsearBloqueJSON(json, "habitaciones", "habitacion");
        totalInsertados += parsearBloqueJSON(json, "servicios", "servicio");
        
        JOptionPane.showMessageDialog(null, 
            "✅ Datos JSON cargados correctamente.\nRegistros insertados: " + totalInsertados, 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    /**
     * Parser simple de bloques JSON. Busca un array por su nombre y procesa los objetos.
     */
    private int parsearBloqueJSON(String json, String nombreArray, String tipo) {
        int total = 0;
        int inicioArray = json.indexOf("\"" + nombreArray + "\"");
        if (inicioArray == -1) return 0;
        
        int inicioCorchete = json.indexOf("[", inicioArray);
        int finCorchete = encontrarCierreCorchete(json, inicioCorchete);
        if (inicioCorchete == -1 || finCorchete == -1) return 0;
        
        String contenidoArray = json.substring(inicioCorchete + 1, finCorchete);
        
        // Buscar cada objeto { }
        int pos = 0;
        while (pos < contenidoArray.length()) {
            int inicioObj = contenidoArray.indexOf("{", pos);
            if (inicioObj == -1) break;
            int finObj = encontrarCierreLlave(contenidoArray, inicioObj);
            if (finObj == -1) break;
            
            String objeto = contenidoArray.substring(inicioObj + 1, finObj);
            try {
                if (tipo.equals("cliente")) {
                    total += insertarClienteDesdeJSON(objeto);
                } else if (tipo.equals("empleado")) {
                    total += insertarEmpleadoDesdeJSON(objeto);
                } else if (tipo.equals("habitacion")) {
                    total += insertarHabitacionDesdeJSON(objeto);
                } else if (tipo.equals("servicio")) {
                    total += insertarServicioDesdeJSON(objeto);
                }
            } catch (Exception e) {
                System.out.println("Error procesando objeto " + tipo + ": " + e.getMessage());
            }
            pos = finObj + 1;
        }
        return total;
    }
    
    private int encontrarCierreLlave(String texto, int inicio) {
        int contador = 0;
        for (int i = inicio; i < texto.length(); i++) {
            if (texto.charAt(i) == '{') contador++;
            else if (texto.charAt(i) == '}') {
                contador--;
                if (contador == 0) return i;
            }
        }
        return -1;
    }
    
    private int encontrarCierreCorchete(String texto, int inicio) {
        int contador = 0;
        for (int i = inicio; i < texto.length(); i++) {
            if (texto.charAt(i) == '[') contador++;
            else if (texto.charAt(i) == ']') {
                contador--;
                if (contador == 0) return i;
            }
        }
        return -1;
    }
    
    private String getValorJSON(String objetoJSON, String clave) {
        int idx = objetoJSON.indexOf("\"" + clave + "\"");
        if (idx == -1) return "";
        int dosPuntos = objetoJSON.indexOf(":", idx);
        if (dosPuntos == -1) return "";
        
        int inicio = dosPuntos + 1;
        while (inicio < objetoJSON.length() && (objetoJSON.charAt(inicio) == ' ' || objetoJSON.charAt(inicio) == '\t')) {
            inicio++;
        }
        
        if (inicio >= objetoJSON.length()) return "";
        
        if (objetoJSON.charAt(inicio) == '"') {
            // String value
            int fin = objetoJSON.indexOf("\"", inicio + 1);
            return objetoJSON.substring(inicio + 1, fin);
        } else {
            // Numérico o booleano
            int fin = inicio;
            while (fin < objetoJSON.length() && objetoJSON.charAt(fin) != ',' && objetoJSON.charAt(fin) != '}' && objetoJSON.charAt(fin) != '\n') {
                fin++;
            }
            return objetoJSON.substring(inicio, fin).trim();
        }
    }
    
    private int insertarClienteDesdeJSON(String obj) throws SQLException {
        PreparedStatement ps = conexion.prepareStatement(
            "INSERT IGNORE INTO cliente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        ps.setString(1, getValorJSON(obj, "dni_nie"));
        ps.setString(2, getValorJSON(obj, "nombre"));
        ps.setString(3, getValorJSON(obj, "apellido1"));
        ps.setString(4, getValorJSON(obj, "apellido2"));
        ps.setDate(5, java.sql.Date.valueOf(getValorJSON(obj, "fecha_nac")));
        ps.setString(6, getValorJSON(obj, "correo"));
        ps.setString(7, getValorJSON(obj, "telefono"));
        ps.setString(8, getValorJSON(obj, "nacionalidad"));
        ps.setString(9, getValorJSON(obj, "pais"));
        ps.setString(10, getValorJSON(obj, "calle_numero"));
        ps.setString(11, getValorJSON(obj, "poblacion"));
        ps.setString(12, getValorJSON(obj, "piso"));
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }
    
    private int insertarEmpleadoDesdeJSON(String obj) throws SQLException {
        PreparedStatement ps = conexion.prepareStatement(
            "INSERT IGNORE INTO empleado (dni_nie, nombre, apellido1, apellido2, fecha_nac, usuario, contrasenya, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        ps.setString(1, getValorJSON(obj, "dni_nie"));
        ps.setString(2, getValorJSON(obj, "nombre"));
        ps.setString(3, getValorJSON(obj, "apellido1"));
        ps.setString(4, getValorJSON(obj, "apellido2"));
        ps.setDate(5, java.sql.Date.valueOf(getValorJSON(obj, "fecha_nac")));
        ps.setString(6, getValorJSON(obj, "usuario"));
        // Hashear contraseña
        String passOriginal = getValorJSON(obj, "contrasenya");
        ps.setString(7, Seguridad.hashPassword(passOriginal));
        ps.setString(8, getValorJSON(obj, "rol"));
        ps.setString(9, getValorJSON(obj, "correo"));
        ps.setString(10, getValorJSON(obj, "telefono"));
        ps.setString(11, getValorJSON(obj, "nacionalidad"));
        ps.setString(12, getValorJSON(obj, "pais"));
        ps.setString(13, getValorJSON(obj, "calle_numero"));
        ps.setString(14, getValorJSON(obj, "poblacion"));
        ps.setString(15, getValorJSON(obj, "piso"));
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }
    
    private int insertarHabitacionDesdeJSON(String obj) throws SQLException {
        PreparedStatement ps = conexion.prepareStatement(
            "INSERT IGNORE INTO habitacion (numero, tipo, capacidad, precio_base, precio_publico, estado) VALUES (?, ?, ?, ?, ?, ?)"
        );
        ps.setInt(1, Integer.parseInt(getValorJSON(obj, "numero")));
        ps.setString(2, getValorJSON(obj, "tipo"));
        ps.setInt(3, Integer.parseInt(getValorJSON(obj, "capacidad")));
        ps.setDouble(4, Double.parseDouble(getValorJSON(obj, "precio_base")));
        ps.setDouble(5, Double.parseDouble(getValorJSON(obj, "precio_publico")));
        ps.setString(6, getValorJSON(obj, "estado"));
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }
    
    private int insertarServicioDesdeJSON(String obj) throws SQLException {
        PreparedStatement ps = conexion.prepareStatement(
            "INSERT IGNORE INTO servicio (nombre, precio, descripcion) VALUES (?, ?, ?)"
        );
        ps.setString(1, getValorJSON(obj, "nombre"));
        ps.setDouble(2, Double.parseDouble(getValorJSON(obj, "precio")));
        ps.setString(3, getValorJSON(obj, "descripcion"));
        int r = ps.executeUpdate();
        ps.close();
        return r;
    }
    
    /**
     * Método auxiliar para obtener el valor de un tag XML
     */
    private String getTagValue(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0 && list.item(0).getFirstChild() != null) {
            return list.item(0).getFirstChild().getNodeValue();
        }
        return "";
    }
}
