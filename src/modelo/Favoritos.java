package modelo;

import java.util.ArrayList;
import java.util.List;

public class Favoritos {

    private List<Productos> favoritos;

    public Favoritos() {
        favoritos = new ArrayList<>();
    }

    public void agregarProducto(Productos producto) {
        favoritos.add(producto);
    }

    public void eliminarProducto(Productos producto) {
        favoritos.remove(producto);
    }
}
