/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author DAM2Alu16
 * 
 * Clase habitacion con sus componentes
 */
public class Habitacion {
    int idHabitacion;
    int numero;
    String tipo;
    int capacidad;
    double precio_base;
    double precio_publico;
    String estado;

    public Habitacion(int idHabitacion, int numero, String tipo, int capacidad, double precio_base, double precio_publico, String estado) {
        this.idHabitacion = idHabitacion;
        this.numero = numero;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.precio_base = precio_base;
        this.precio_publico = precio_publico;
        this.estado = estado;
    }
    
    public Habitacion(){}

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getPrecio_base() {
        return precio_base;
    }

    public void setPrecio_base(double precio_base) {
        this.precio_base = precio_base;
    }

    public double getPrecio_publico() {
        return precio_publico;
    }

    public void setPrecio_publico(double precio_publico) {
        this.precio_publico = precio_publico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * ✅ AÑADIDO: toString para consistencia con otros modelos
     */
    @Override
    public String toString() {
        return "Habitacion{" + "idHabitacion=" + idHabitacion + ", numero=" + numero + 
               ", tipo=" + tipo + ", capacidad=" + capacidad + ", precio_base=" + precio_base + 
               ", precio_publico=" + precio_publico + ", estado=" + estado + '}';
    }
    
    /**
     * ✅ AÑADIDO: getColumnas para JTable (consistencia con otros modelos)
     */
    public static String[] getColumnas() {
        String[] columnas = {"idHabitacion", "Número", "Tipo", "Capacidad", "Precio base", "Precio público", "Estado"};
        return columnas;
    }
    
    /**
     * ✅ AÑADIDO: devuelveFila para JTable (consistencia con otros modelos)
     */
    public String[] devuelveFila() {
        String[] fila = {
            String.valueOf(idHabitacion),
            String.valueOf(numero),
            tipo,
            String.valueOf(capacidad),
            String.valueOf(precio_base),
            String.valueOf(precio_publico),
            estado
        };
        return fila;
    }
}
