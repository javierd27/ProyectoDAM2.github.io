/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package E20_JTableAvanzado;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author DAM2Alu3
 */
public class Cliente {
    String nombre, apellidos, provincia, mail;
    int edad;
    Date fecha;
    int id;
    static int contadorID = 0;

    
    public Cliente() {
        id = contadorID;
        contadorID++;
    }

    public Cliente(String nombre, String apellidos, String provincia, String mail, int edad, Date fecha) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.provincia = provincia;
        this.mail = mail;
        this.edad = edad;
        this.fecha = fecha;
        id=contadorID++;
    }

    
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getContadorID() {
        return contadorID;
    }

    public static void setContadorID(int contadorID) {
        Cliente.contadorID = contadorID;
    }
    
    

    
    
    @Override
    public String toString() {
        return "Cliente{" + "nombre=" + nombre + ", apellidos=" + apellidos + ", provincia=" + provincia + ", mail=" + mail + ", edad=" + edad + ", fecha=" + fecha + '}';
    }

    
    
    public static String[] getColumnas(){
        String[] columnas = {"ID", "Nombre", "Apellido", "Provincia", "Edad", "Correo", "Alta"};
        return columnas;
    }
    
    
    public String[] devuelveFila(){
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, hh:mm");
        
        String[] fila = {
                String.valueOf(id),
                nombre,
                apellidos,
                provincia,
                String.valueOf(edad),
                mail,
                sdf.format(fecha)                
                };
        
        return fila;
    }
    
    
    
}
