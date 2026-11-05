package modeloProductos;

/**
 *
 * @author pc
 */
import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private List<Productos> productos;
    private double precioEnvio;

    public Carrito() {
        productos = new ArrayList<>();
        precioEnvio = 20000;
    }

    public List<Productos> getProductos() {
        return productos;
    }

    public void agregarProducto(Productos producto) {
        productos.add(producto);
    }

    public void eliminarProducto(Productos producto) {
        productos.remove(producto);
    }

    public double calcularTotal() {

        double total = 0;

        for (Productos producto : productos) {
            total += producto.getPrecio();
        }

        return total + precioEnvio;
    }

    public int contarProducto(Productos producto) {

        int cantidad = 0;

        for (Productos p : productos) {

            if (p == producto) {
                cantidad++;
            }
        }

        return cantidad;
    }
}
