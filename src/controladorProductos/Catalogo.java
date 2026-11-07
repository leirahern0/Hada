/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorProductos;

import java.util.ArrayList;
import java.util.List;
import modeloProductos.Producto;

public class Catalogo {
    private List<Producto> productos;

    public Catalogo(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Producto> buscarPorEtiqueta(String busqueda) {
    
        List<Producto> resultado = new ArrayList<>();
    
        for (Producto p : productos) {
        
            for (String etiqueta : p.getEtiquetas()) {
            
                if (etiqueta.equalsIgnoreCase(busqueda)) {
                
                    resultado.add(p);
                
                    break; 
            
                }
        
            }
    
        }
    
        return resultado;
    
    }

    public List<Producto> getCatalogo() {
        return productos;
    }
}
