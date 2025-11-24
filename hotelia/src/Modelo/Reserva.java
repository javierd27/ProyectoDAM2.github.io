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
public class Reserva {
    int idServicio, idHabitacion, idFactura, cantidad_personas;
    String idCliente, estado;
    Date fecha_inicio, fecha_fin, fecha_hora_reserva;

    public Reserva(int idServicio, int idHabitacion, int idFactura, int cantidad_personas, String idCliente, String estado, Date fecha_inicio, Date fecha_fin, Date fecha_hora_reserva) {
        this.idServicio = idServicio;
        this.idHabitacion = idHabitacion;
        this.idFactura = idFactura;
        this.cantidad_personas = cantidad_personas;
        this.idCliente = idCliente;
        this.estado = estado;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_hora_reserva = fecha_hora_reserva;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getCantidad_personas() {
        return cantidad_personas;
    }

    public void setCantidad_personas(int cantidad_personas) {
        this.cantidad_personas = cantidad_personas;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public Date getFecha_hora_reserva() {
        return fecha_hora_reserva;
    }

    public void setFecha_hora_reserva(Date fecha_hora_reserva) {
        this.fecha_hora_reserva = fecha_hora_reserva;
    }

    @Override
    public String toString() {
        return "Reserva{" + "idServicio=" + idServicio + ", idHabitacion=" + idHabitacion + ", idFactura=" + idFactura + ", cantidad_personas=" + cantidad_personas + ", idCliente=" + idCliente + ", estado=" + estado + ", fecha_inicio=" + fecha_inicio + ", fecha_fin=" + fecha_fin + ", fecha_hora_reserva=" + fecha_hora_reserva + '}';
    }

       
    public  String[] getColumnas(){
        String[] columnas = {"idReserva", "idServicio", "idHabitacion", "idCliente", "idFactura", "Fecha inicio", "Fecha fin", "Cantidad personas", "Fecha y hora de la reserva", "Estado"};
        return columnas;
    }
    
    
    public String[] devuelveFila(){
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, hh:mm");
        
        String[] fila = {
                String.valueOf(idServicio),
                String.valueOf(idHabitacion),
                idCliente,
                String.valueOf(idFactura),
                sdf.format(fecha_inicio),
                sdf.format(fecha_fin),
                String.valueOf(cantidad_personas),
                sdf.format(fecha_hora_reserva),
                estado
                };
        
        return fila;
    }
}
    
    
    

