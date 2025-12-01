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
import Modelo.Cliente;
import Modelo.Factura;
import Modelo.Habitacion;
import Modelo.Reserva;
import Modelo.Servicio;
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

    public int insertaCliente(Cliente cliente) throws SQLException {
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

    public Cliente buscaCliente(String dni) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE dni_nie = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
            return new Cliente(rs.getString("dni_nie"), rs.getString("nombre"), rs.getString("apellido1"), rs.getString("apellido2"), rs.getDate("fecha_nac"),
                    rs.getString("correo"), rs.getString("telefono"), rs.getString("nacionalidad"), rs.getString("pais"), rs.getString("calle_numero"),
                    rs.getString("poblacion"), rs.getString("piso"));
        }
    }

    public int editarCliente(Cliente cliente, String dni) throws SQLException {
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
    
    
     public int insertaServicio(Servicio nuevo) throws SQLException {
        String sql = "INSERT INTO servicio (nombre, precio, descripcion) VALUES (?, ?, ?)";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nuevo.getNombre());
        ps.setDouble(2, nuevo.getPrecio());
        ps.setString(3, nuevo.getDescripción());

        return ps.executeUpdate();
    }
     
     
    public Servicio buscaServicio(String nombre) throws SQLException {
        String sql = "SELECT * FROM servicio WHERE nombre = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nombre);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
            return new Servicio(rs.getString("nombre"),  rs.getString("descripcion"), rs.getDouble("precio"));
        }
    }
     
    
    public int editarServicio(Servicio servicio, String nombre) throws SQLException {
        String sql = "UPDATE servicio SET precio = ?, descripcion = ? WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setDouble(1, servicio.getPrecio());
        ps.setString(2, servicio.getDescripción());
        ps.setString(3, nombre);
       
        return ps.executeUpdate();
    }
    
    
    public int eliminaServicio(String nombre) throws SQLException {
        String sql = "DELETE FROM servicio WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, nombre);
        
        return ps.executeUpdate();
    }
    
                       
    public int buscarServicioId(String nombre) throws SQLException {
        String sql = "SELECT idServicio FROM servicio WHERE nombre = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nombre);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return 0;
        } else {
            return rs.getInt("idServicio");
        }
    }

    
    public ResultSet todosServicios() throws SQLException {
        String sql = "SELECT nombre FROM servicio";
        PreparedStatement ps = conexion.prepareStatement(sql);
        return ps.executeQuery();
    }
      
      
    public ResultSet todasHabitacionesLibres() throws SQLException {
        String sql = "SELECT DISTINCT tipo FROM habitacion WHERE estado = 'Libre'";
        PreparedStatement ps = conexion.prepareStatement(sql);
        return ps.executeQuery();
    }
    
      
    
    public int insertaReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reserva (idServicio, idHabitacion, idCliente, idFactura, fecha_inicio, fecha_fin, cantidad_personas, fecha_hora_reserva, estado) +"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, reserva.getIdServicio());
        ps.setInt(2, reserva.getIdHabitacion());
        ps.setString(3, reserva.getIdCliente());
        ps.setInt(4, reserva.getIdFactura());
        ps.setDate(5, new java.sql.Date(reserva.getFecha_inicio().getTime()));
        ps.setDate(6, new java.sql.Date(reserva.getFecha_fin().getTime()));
        ps.setInt(7, reserva.getCantidad_personas());
        ps.setDate(8, new java.sql.Date(reserva.getFecha_hora_reserva().getTime()));
        ps.setString(9, reserva.getEstado());

        

        return ps.executeUpdate();
    }
      
    
    public Reserva buscaReserva(String dni) throws SQLException {
        String sql = "SELECT * FROM reserva WHERE idCliente = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
            return new Reserva(rs.getInt("idServicio"), rs.getInt("idHabitacion"), rs.getInt("idFactura"), rs.getInt("cantidad_personas"),
                                rs.getString("idCliente"), rs.getString("estado"), rs.getDate("fecha_inicio"), rs.getDate("fecha_fin"), 
                                rs.getDate("fecha_hora_reserva"));
        }
    }
    /*
    
    public int editarReserva(Reserva reserva, String dni) throws SQLException {
        String sql = "UPDATE reserva SET precio = ?, descripcion = ? WHERE nombre = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setDouble(1, servicio.getPrecio());
        ps.setString(2, servicio.getDescripción());
        ps.setString(3, nombre);
       
        return ps.executeUpdate();
    }
    */
    
    public Factura buscaFactura(String dni) throws SQLException {
        String sql = "SELECT * FROM factura WHERE idCliente = ? AND estado = Pendiente";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        } else {
               
            return new Factura(rs.getInt("descuento"), rs.getInt("iva"), rs.getString("estado"), 
                                rs.getString("idCliente"), rs.getString("metodo_pago"), rs.getString("observacion"), 
                                rs.getDate("fecha_emision"), rs.getDouble("sub_total"), rs.getDouble("total"));
        }
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
    }

    /**
     * Metodo que elimina de la base de datos los empleados seleccionados (se
     * eliminara de 1 en 1)
     *
     * @param dtm
     * @param id
     * @return todo: mirar para que si se pueda borrar varios a la vez (Con un
     * arrayList)
     */
    public int deleteUsuario(DefaultTableModel dtm, String id) {
        try {
            PreparedStatement ps = conexion.prepareStatement("delete from empleado where dni_nie = ?");
            ps.setString(1, id);
            return ps.executeUpdate();

        } catch (SQLException ex) {

            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return 0;
        }
    }

    /**
     * Metodo para consultar que rol tiene el empleado
     *
     * @param usuario
     * @return
     */
    public String comprobarRol(String usuario) {
        String nombre ="";
        try {
            PreparedStatement ps = conexion.prepareStatement("Select rol from empleado where usuario = ?");
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nombre=rs.getString(1);
            }
            return nombre;
            
        } catch (SQLException ex) {
            System.getLogger(ConexionBBDD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return nombre;
        }
    }

    public void cerrar() {
        try {
            conexion.close();
        } catch (java.sql.SQLException sqle) {
            System.out.println("No se ha podido cerrar la conexión");
        }
    }

}


