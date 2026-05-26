/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author DAM2Alu4
 */
public class Empleado {

    String dni_nie;
    String nombre, apellido1, apellido2;
    Date fecha_nac;
    String usuario, contrasenya, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso;

    // constructor completo
    public Empleado(String dni_nie, String nombre, String apellido1, String apellido2, Date fecha_nac, String usuario, String contrasenya, String rol, String correo, String telefono, String nacionalidad, String pais, String calle_numero, String poblacion, String piso) {
        this.dni_nie = dni_nie;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fecha_nac = fecha_nac;
        this.usuario = usuario;
        this.contrasenya = contrasenya;
        this.rol = rol;
        this.correo = correo;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
        this.pais = pais;
        this.calle_numero = calle_numero;
        this.poblacion = poblacion;
        this.piso = piso;
    }
        // constructor Sin contraseña
    public Empleado(String dni_nie, String nombre, String apellido1, String apellido2, Date fecha_nac, String usuario, String rol, String correo, String telefono, String nacionalidad, String pais, String calle_numero, String poblacion, String piso) {
        this.dni_nie = dni_nie;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fecha_nac = fecha_nac;
        this.usuario = usuario;
        this.rol = rol;
        this.correo = correo;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
        this.pais = pais;
        this.calle_numero = calle_numero;
        this.poblacion = poblacion;
        this.piso = piso;
    }

    // constructor sin apelldio2 y sin piso
    public Empleado(String dni_nie, String nombre, String apellido1, Date fecha_nac, String usuario, String contrasenya, String rol, String correo, String telefono, String nacionalidad, String pais, String calle_numero, String poblacion) {
        this.dni_nie = dni_nie;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.fecha_nac = fecha_nac;
        this.usuario = usuario;
        this.contrasenya = contrasenya;
        this.rol = rol;
        this.correo = correo;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
        this.pais = pais;
        this.calle_numero = calle_numero;
        this.poblacion = poblacion;
    }

    // constructor sin apelldio2 y con piso
    public Empleado(String dni_nie, String nombre, String apellido1, Date fecha_nac, String usuario, String contrasenya, String rol, String correo, String telefono, String nacionalidad, String pais, String calle_numero, String poblacion, String piso) {
        this.dni_nie = dni_nie;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.fecha_nac = fecha_nac;
        this.usuario = usuario;
        this.contrasenya = contrasenya;
        this.rol = rol;
        this.correo = correo;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
        this.pais = pais;
        this.calle_numero = calle_numero;
        this.poblacion = poblacion;
        this.piso = piso;
    }


    public String getDni_nie() {
        return dni_nie;
    }

    public void setDni_nie(String dni_nie) {
        this.dni_nie = dni_nie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCalle_numero() {
        return calle_numero;
    }

    public void setCalle_numero(String calle_numero) {
        this.calle_numero = calle_numero;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public static String[] devuelveColumna(){
        String[] columna = {"dni_nie", "nombre", "apellido1","apellido2", "fecha_nac", "usuario", "rol","correo","telefono","nacionalidad","pais","calle_numero","poblacion","piso"};
        return columna;
    }
    
    public String[] devuelveFila() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String[] fila = {String.valueOf(dni_nie), nombre, apellido1, apellido2, sdf.format(fecha_nac), usuario, rol, correo, telefono, nacionalidad, pais, calle_numero, poblacion, piso};

        return fila;

    }// end devuelveFila

}// end class
