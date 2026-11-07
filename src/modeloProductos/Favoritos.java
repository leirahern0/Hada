package modeloProductos;

import java.util.ArrayList;
import java.util.List;

public class Favoritos {

    private List<Producto> favoritos;

    public Favoritos() {
        favoritos = new ArrayList<>();
    }
    
    public List<Producto> getFavoritos() {
        
        
        return favoritos;
    }
    
    public boolean esFavorito(Producto producto) {
        
        for (Producto p : favoritos) {
            if (p.getCodigo()== producto.getCodigo()) {
                return true;
            }
        }
        
        return false;
        
    }


    public boolean corazonFavoritos (Producto producto){
        
        for (int i = 0; i < favoritos.size(); i++) {
            
            if (favoritos.get(i).getCodigo() == producto.getCodigo()) {
                
                favoritos.remove(i);
                return false; 
            }
        }
        
        favoritos.add(producto);
        return true; 
    }
    
}
