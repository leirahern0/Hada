/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorProductos;

import java.util.ArrayList;
import java.util.List;
import datos.PedidoDAO;
import modeloProductos.Pedido;

/**
 *
 * @author Usuario
 */
public class Pedidos {
    private List<Pedido> pedidos; 
    private PedidoDAO pedidoDAO;
    
    public Pedidos (){
        pedidos = new ArrayList<>(); 
        try {   
            pedidos = pedidoDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 public void ordenarSegunSeleccion(String seleccion) {
        if (seleccion == null) return;

        if (seleccion.equals("Fecha: más reciente")) {
            pedidos = pedidos.stream()
                    .sorted((a, b) -> b.getFecha().compareTo(a.getFecha()))
                    .toList();
        }
        else if (seleccion.equals("Fecha: más antigua")) {
            pedidos = pedidos.stream()
                    .sorted((a, b) -> a.getFecha().compareTo(b.getFecha()))
                    .toList();
        }
        else if (seleccion.equals("Precio: mayor a menor")) {
            pedidos = pedidos.stream()
                    .sorted((a, b) -> Double.compare(b.getTotal(), a.getTotal()))
                    .toList();
        }
        else if (seleccion.equals("Precio: menor a mayor")) {
            pedidos = pedidos.stream()
                    .sorted((a, b) -> Double.compare(a.getTotal(), b.getTotal()))
                    .toList();
        }
    }
}
    
    

