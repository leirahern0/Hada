package modeloProductos;

import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private List<Producto> productos;
    private double precioEnvio;

    public Carrito() {
        productos = new ArrayList<>();
        precioEnvio = 0;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double getPrecioEnvio() {
        return precioEnvio;
    }

    public void setPrecioEnvio(double precioEnvio) {
        this.precioEnvio = precioEnvio;
    }

    public void agregarUnidad(Producto producto) {
        productos.add(producto);
    }

    public double calcularTotalCarrito() {

        double subTotal = 0;
        double total = 0;

        if (!productos.isEmpty()) {

            for (Producto producto : productos) {
                subTotal += producto.getPrecio();
            }

            total = subTotal + precioEnvio;
        } else {

            total = 0;

        }

        return total;
    }

    public int cantidadProducto(Producto producto) {

        int cantidad = 0;

        for (Producto p : productos) {

            if (p.getCodigo().equals(producto.getCodigo())) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public void eliminarUnidad(Producto producto) {

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo().equals(producto.getCodigo())) {
                productos.remove(i);
                break;
            }
        }

    }

    public void vaciar() {
        productos.clear();
    }

    public double calcularSubtotal() {
        double subTotal = 0;
        for (Producto producto : productos) {
            subTotal += producto.getPrecio();
        }
        return subTotal;

    }


}
