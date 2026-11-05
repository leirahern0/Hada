/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorProductos;

import java.util.ArrayList;
import java.util.List;
import modeloProductos.Productos;

public class Catalogo {
    private List<Productos> productos;

    public Catalogo(List<Productos> productos) {
        this.productos = productos;
    }

    public Productos buscarProducto(String nombre) {
        for (Productos p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null; // si no lo encuentra
    }

    public List<Productos> verCatalogo() {
        return productos;
    }
}
