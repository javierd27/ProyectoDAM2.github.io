/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author DAM2Alu3
 */
public class Factura {
    int descuento, idFactura, iva;
    String estado, idCliente, metodo_pago, observacion;
    Date fecha_emision;
    Double sub_total, total;

    public Factura(int descuento, int idFactura, int iva, String estado, String idCliente, String metodo_pago, String observacion, Date fecha_emision, Double sub_total, Double total) {
        this.descuento = descuento;
        this.idFactura = idFactura;
        this.iva = iva;
        this.estado = estado;
        this.idCliente = idCliente;
        this.metodo_pago = metodo_pago;
        this.observacion = observacion;
        this.fecha_emision = fecha_emision;
        this.sub_total = sub_total;
        this.total = total;
    }

    
    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public Double getSub_total() {
        return sub_total;
    }

    public void setSub_total(Double sub_total) {
        this.sub_total = sub_total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Factura{" + "descuento=" + descuento + ", idFactura=" + idFactura + ", iva=" + iva + ", estado=" + estado + ", idCliente=" + idCliente + ", metodo_pago=" + metodo_pago + ", observacion=" + observacion + ", fecha_emision=" + fecha_emision + ", sub_total=" + sub_total + ", total=" + total + '}';
    }

    
    
    
     public  String[] getColumnas(){
        String[] columnas = {"IdFactura", "IdCliente", "Fecha emision", "Subtotal", "IVA", "Descuento", "Metodo de pago", "Estado", "Total", "Observacion"};
        return columnas;
    }
    
    
    public String[] devuelveFila(){
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, hh:mm");

        String[] fila = {
                String.valueOf(idFactura),
                idCliente,
                sdf.format(fecha_emision),
                String.valueOf(sub_total),
                String.valueOf(iva),
                String.valueOf(descuento),
                metodo_pago,
                estado,
                String.valueOf(total),
                observacion
                };
        
        return fila;
    }
}
