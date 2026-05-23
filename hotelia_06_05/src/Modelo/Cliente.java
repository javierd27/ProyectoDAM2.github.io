package Modelo;

import java.text.SimpleDateFormat;
import java.util.Date;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DAM2Alu3
 */
public class Cliente {
    String dni_nie, nombre, apellido1, apellido2;
    Date fecha_nac;
    String mail, telefono, nacionalidad, pais, calle_numero, poblacion, piso;

    public Cliente(String dni_nie, String nombre, String apellido1, String apellido2, Date fecha_nac, String mail, String telefono, String nacionalidad, String pais, String calle_numero, String poblacion, String piso) {
        this.dni_nie = dni_nie;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fecha_nac = fecha_nac;
        this.mail = mail;
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

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
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

    @Override
    public String toString() {
        return "Cliente{" + "dni_nie=" + dni_nie + ", nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2=" + apellido2 + ", fecha_nac=" + fecha_nac + ", mail=" + mail + ", telefono=" + telefono + ", nacionalidad=" + nacionalidad + ", pais=" + pais + ", calle_numero=" + calle_numero + ", poblacion=" + poblacion + ", piso=" + piso + '}';
    }
  
    
    public  String[] getColumnas(){
        String[] columnas = {"DNI/NIE", "Nombre", "Apellido1", "Apellido2", "Fecha de nacimiento", "Correo", "Teléfono", "Nacionalidad", "Pais", "Calle y número", "Poblacion", "Piso"};
        return columnas;
    }
    
    
    public String[] devuelveFila(){
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, hh:mm");
        
        String[] fila = {
                dni_nie,
                nombre,
                apellido1,
                apellido2,
                sdf.format(fecha_nac),
                mail,
                telefono,
                nacionalidad,
                pais,
                calle_numero,
                poblacion,
                piso
                };
        
        return fila;
    }
}
    
    
    

