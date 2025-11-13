package Modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



/**
 *
 * @author DAM2Alu3
 */
public class Cliente {
    String nombre, apellido1, apellido2, dni_nie, mail, telefono, nacionalidad, calle_numero, piso, poblacion, pais;
    Date fecha_nac;
    
    public Cliente(String nombre, String apellido1, String apellido2, String dni_nie, String mail, String telefono, String nacionalidad, String calle_numero, String piso, String poblacion, String pais, Date fecha_nac) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.dni_nie = dni_nie;
        this.mail = mail;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
        this.calle_numero = calle_numero;
        this.piso = piso;
        this.poblacion = poblacion;
        this.pais = pais;
        this.fecha_nac = fecha_nac;
    }
    
    
    public Cliente(String nombre, String apellido1, String apellido2, String dni_nie, String mail, String telefono, String nacionalidad, String calle_numero, String poblacion, String pais, Date fecha_nac) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.dni_nie = dni_nie;
        this.mail = mail;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
        this.calle_numero = calle_numero;
        this.poblacion = poblacion;
        this.pais = pais;
        this.fecha_nac = fecha_nac;
    }
    

    public Cliente(String nombre, String apellido1, String dni_nie, String mail, String telefono, String nacionalidad, String calle_numero, String poblacion, String pais, Date fecha_nac) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.dni_nie = dni_nie;
        this.mail = mail;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
        this.calle_numero = calle_numero;
        this.poblacion = poblacion;
        this.pais = pais;
        this.fecha_nac = fecha_nac;
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

        public String getDni_nie() {
            return dni_nie;
        }

        public void setDni_nie(String dni_nie) {
            this.dni_nie = dni_nie;
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

        public String getCalle_numero() {
            return calle_numero;
        }

        public void setCalle_numero(String calle_numero) {
            this.calle_numero = calle_numero;
        }

        public String getPiso() {
            return piso;
        }

        public void setPiso(String piso) {
            this.piso = piso;
        }

        public String getPoblacion() {
            return poblacion;
        }

        public void setPoblacion(String poblacion) {
            this.poblacion = poblacion;
        }

        public String getPais() {
            return pais;
        }

        public void setPais(String pais) {
            this.pais = pais;
        }

        public Date getFecha_nac() {
            return fecha_nac;
        }

        public void setFecha_nac(Date fecha_nac) {
            this.fecha_nac = fecha_nac;
        }
    
  
    
    }

    
    
    public s String[] getColumnas(){
        String[] columnas = {"DNI/NIE", "Nombre", "Apellido1", "Apellido2", "Fecha de nacimiento", "Telefono", "Correo", "Nacionalidad", "Calle y número", "Piso", "Poblacion", "Pais"};
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
    
    
    

