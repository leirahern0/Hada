/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorProductos;

import java.util.ArrayList;
import java.util.List;
import modeloProductos.Producto;

/**
 *
 * @author Usuario
 */
public class Inventario {
    
    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void agregarProducto(Producto producto) {
        
        if (producto != null) {
            productos.add(producto);
        }
    }

    public boolean eliminarProducto(Producto producto) {
        return productos.remove(producto);
    }

    public boolean eliminarPorCodigo(String codigo) {
        return productos.removeIf(p -> p.getCodigo().equalsIgnoreCase(codigo));
    }
    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }
    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
    public void agregarUnidades(Producto producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            producto.actualizarStock(cantidad);
        }
    }

    public void eliminarUnidades(Producto producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            producto.actualizarStock(-cantidad);
        }
    }
    
    public int totalProductos() {
        return productos.size();
    }
    
    public List<Producto> obtenerDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto p : productos) {
            if (p.isDisponible()) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }
}
