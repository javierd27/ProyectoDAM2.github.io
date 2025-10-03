/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package E14_Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DAM2Alu3
 */
public class LogicaNegocio {
    static List<Cliente>listaClientes= new ArrayList();
    
    public static void cargaPrueba(){
        //String[] columnas = {"Nombre", "Apellido", "Provincia", "Edad", "Correo", "Alta"};
        for (int i = 0; i < 30; i++){
            Cliente cliente = new Cliente("Nombre" + i,
                                        "Apellido" + i,
                                        "Huesca",
                                        "mail" + i,
                                        20+i,
                                        new Date());
            
            listaClientes.add(cliente);
        }
        
    }
    
    public static void addCliente(Cliente cliente){
        listaClientes.add(cliente);
    }
    
    public static void removeCliente(Cliente cliente){
        listaClientes.remove(cliente);
    }
    
    public static void removeCliente(int id){
        for (Cliente listaCli : listaClientes) {
            if(listaCli.getId() == id){
                listaClientes.remove(listaCli);
            }
        }   
        
    }
    
    public static Cliente getCliente(int id){
        for(Cliente cliente: listaClientes){
            if(cliente.getId() == id)
                return cliente;
        }
        return null;
    }
    
    public static void editCliente(Cliente editar){
        for (int i = 0; i < listaClientes.size(); i++) {
            if(listaClientes.get(i).getId()== editar.getId()){
                listaClientes.set(i, editar);
            }
        }
    }
    
    public static List<Cliente> getClientes(){
        return listaClientes;
    }
}
