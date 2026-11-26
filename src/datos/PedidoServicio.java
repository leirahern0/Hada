package datos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modeloPersonas.Usuario;
import modeloProductos.Carrito;
import modeloProductos.Pedido;
import modeloProductos.Producto;

/** Coordina el pedido y los cambios de inventario persistente. */
public class PedidoServicio {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    public void confirmarPedido(
            Pedido pedido,
            Usuario usuario,
            Carrito carrito,
            CarritoDAO carritoDAO) throws Exception {

        if (pedido == null || pedido.getProductos() == null
                || pedido.getProductos().isEmpty()) {
            throw new IllegalArgumentException("El pedido no contiene productos.");
        }

        Map<String, Integer> cantidades = contarPorCodigo(pedido.getProductos());
        Map<String, Integer> descuentos = invertir(cantidades);
        List<Producto> productosOriginales =
                new ArrayList<>(carrito.getProductos());

        // Esta operación valida todo el stock antes de guardar cambios.
        productoDAO.aplicarAjustesStock(descuentos);

        boolean pedidoGuardado = false;
        try {
            pedidoDAO.guardar(pedido);
            pedidoGuardado = true;

            carrito.vaciar();
            carritoDAO.guardar(usuario, carrito);
        } catch (Exception error) {
            carrito.getProductos().clear();
            carrito.getProductos().addAll(productosOriginales);

            boolean pedidoRetirado = true;
            if (pedidoGuardado) {
                try {
                    pedidoDAO.eliminar(pedido.getNumeroPedido());
                } catch (Exception rollbackPedido) {
                    pedidoRetirado = false;
                    error.addSuppressed(rollbackPedido);
                }
            }

            if (pedidoRetirado) {
                try {
                    productoDAO.aplicarAjustesStock(cantidades);
                } catch (Exception rollbackStock) {
                    error.addSuppressed(rollbackStock);
                }
            }

            try {
                carritoDAO.guardar(usuario, carrito);
            } catch (Exception rollbackCarrito) {
                error.addSuppressed(rollbackCarrito);
            }

            throw error;
        }
    }

    public void eliminarPedidoYDevolverStock(String numeroPedido) throws Exception {
        Pedido pedidoEncontrado = null;
        for (Pedido pedido : pedidoDAO.listar()) {
            if (pedido.getNumeroPedido() != null
                    && pedido.getNumeroPedido().equals(numeroPedido)) {
                pedidoEncontrado = pedido;
                break;
            }
        }

        if (pedidoEncontrado == null) {
            throw new IllegalStateException(
                    "No se encontró el pedido " + numeroPedido + "."
            );
        }

        Map<String, Integer> devoluciones =
                contarPorCodigo(pedidoEncontrado.getProductos());
        Map<String, Integer> descuentos = invertir(devoluciones);

        // Primero devuelve las unidades. Si borrar el pedido falla,
        // se vuelve a descontar el stock para conservar consistencia.
        productoDAO.aplicarAjustesStock(devoluciones);
        try {
            pedidoDAO.eliminar(numeroPedido);
        } catch (Exception error) {
            try {
                productoDAO.aplicarAjustesStock(descuentos);
            } catch (Exception rollbackStock) {
                error.addSuppressed(rollbackStock);
            }
            throw error;
        }
    }

    /** Elimina todos los pedidos y devuelve al inventario las unidades asociadas. */
    public int eliminarTodosLosPedidosYDevolverStock() throws Exception {
        List<Pedido> pedidos = pedidoDAO.listar();
        if (pedidos.isEmpty()) {
            return 0;
        }

        Map<String, Integer> devoluciones = new LinkedHashMap<>();
        for (Pedido pedido : pedidos) {
            Map<String, Integer> cantidades = contarPorCodigo(pedido.getProductos());
            for (Map.Entry<String, Integer> entrada : cantidades.entrySet()) {
                devoluciones.merge(entrada.getKey(), entrada.getValue(), Math::addExact);
            }
        }
        Map<String, Integer> descuentos = invertir(devoluciones);

        productoDAO.aplicarAjustesStock(devoluciones);
        try {
            pedidoDAO.eliminarTodos();
        } catch (Exception error) {
            try {
                productoDAO.aplicarAjustesStock(descuentos);
            } catch (Exception rollbackStock) {
                error.addSuppressed(rollbackStock);
            }
            throw error;
        }

        return pedidos.size();
    }

    private Map<String, Integer> contarPorCodigo(List<Producto> productos) {
        Map<String, Integer> cantidades = new LinkedHashMap<>();
        if (productos == null) {
            return cantidades;
        }

        for (Producto producto : productos) {
            if (producto == null || producto.getCodigo() == null
                    || producto.getCodigo().isBlank()) {
                throw new IllegalStateException(
                        "Hay un producto del pedido sin código."
                );
            }
            cantidades.merge(producto.getCodigo(), 1, Integer::sum);
        }
        return cantidades;
    }

    private Map<String, Integer> invertir(Map<String, Integer> cantidades) {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entrada : cantidades.entrySet()) {
            resultado.put(entrada.getKey(), -entrada.getValue());
        }
        return resultado;
    }
}
