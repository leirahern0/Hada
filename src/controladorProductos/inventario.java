/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorProductos;

import java.util.ArrayList;
import java.util.List;
import modeloProductos.Productos;

public class inventario {
    private List<Productos> productos;

    public inventario() {
        this.productos = new ArrayList<>();
    }

    public List<Productos> getProductos() {
        return productos;
    }

    public void agregarProducto(Productos producto) {
        
        if (producto != null) {
            productos.add(producto);
        }
    }

    public boolean eliminarProducto(Productos producto) {
        return productos.remove(producto);
    }

    public boolean eliminarPorCodigo(String codigo) {
        return productos.removeIf(p -> p.getCodigo().equalsIgnoreCase(codigo));
    }
    public Productos buscarPorCodigo(String codigo) {
        for (Productos p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }
    public Productos buscarPorNombre(String nombre) {
        for (Productos p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
    public void agregarUnidades(Productos producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            producto.actualizarStock(cantidad);
        }
    }

    public void eliminarUnidades(Productos producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            producto.actualizarStock(-cantidad);
        }
    }
    
    public int totalProductos() {
        return productos.size();
    }
    
    public List<Productos> obtenerDisponibles() {
        List<Productos> disponibles = new ArrayList<>();
        for (Productos p : productos) {
            if (p.isDisponible()) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }
}
