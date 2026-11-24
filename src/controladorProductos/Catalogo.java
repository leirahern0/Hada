package controladorProductos;

import java.util.ArrayList;
import java.util.List;
import modeloProductos.Producto;

public class Catalogo {

    private List<Producto> productos;

    public Catalogo(List<Producto> productos) {
        this.productos = productos;
    }

    public final List<Producto> buscarPorEtiqueta(String busqueda) {

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
