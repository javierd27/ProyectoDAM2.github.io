/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.text.SimpleDateFormat;

/**
 *
 * @author DAM2Alu3
 */
public class Servicio {
    int idServicio;
    String nombre, descripcion;
    Double precio;

    public Servicio(String nombre, String descripción, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripción;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripción() {
        return descripcion;
    }

    public void setDescripción(String descripción) {
        this.descripcion = descripción;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Servicio{" + "nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + '}';
    }
    
    
     public static String[] getColumnas(){
        String[] columnas = {"idServicio", "Nombre", "Precio", "Descripción"};
        return columnas;
    }
    
    
    public String[] devuelveFila(){
                
        String[] fila = {
                String.valueOf(idServicio),
                nombre,
                String.valueOf(precio),
                descripcion
                };
        
        return fila;
    }
}
