package datos;

import modeloProductos.Producto;
import modeloProductos.Pedido;
import java.util.*;
import controladorProductos.Catalogo;

public class ProductoDAO {
    private String ruta = "datos/productos.txt";

    public ProductoDAO() {
    }
    

    public List<Producto> listar() throws Exception {
        List<Producto> lista = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) if (!l.isBlank()) {
            Producto p = Producto.desdelinea(l);
            if (p != null) lista.add(p);
        }
        return lista;
    }
    public void guardar(Producto p) throws Exception { 
        ArchivoUtil.agregar(ruta, p.toLinea());
    }
    
    public void eliminar(String codigo) throws Exception {
        for (Pedido pedido : new PedidoDAO().listar()) {
            if (pedido.getProductos() == null) {
                continue;
            }
            for (Producto productoPedido : pedido.getProductos()) {
                if (productoPedido != null
                        && codigo.equals(productoPedido.getCodigo())) {
                    throw new IllegalStateException(
                            "No se puede eliminar este producto porque aparece "
                            + "en el pedido " + pedido.getNumeroPedido()
                            + ". Elimina primero ese pedido."
                    );
                }
            }
        }

        List<Producto> lista = listar();
        lista.removeIf(p -> p.getCodigo().equals(codigo));
        guardarTodos(lista);
    }
    public void editar(Producto nuevo) throws Exception {
        List<Producto> lista = listar();
        for (int i = 0; i < lista.size(); i++) if (lista.get(i).getCodigo().equals(nuevo.getCodigo())) lista.set(i, nuevo);
        guardarTodos(lista);
    }

    /**
     * Aplica cambios de stock por código de producto y guarda el catálogo
     * completo en una sola escritura. Los cambios pueden ser positivos o
     * negativos; un resultado negativo se rechaza.
     */
    public void aplicarAjustesStock(Map<String, Integer> ajustes) throws Exception {
        if (ajustes == null || ajustes.isEmpty()) {
            return;
        }

        List<Producto> lista = listar();
        Map<String, Producto> porCodigo = new HashMap<>();
        for (Producto producto : lista) {
            porCodigo.put(producto.getCodigo(), producto);
        }

        Map<Producto, Integer> nuevosStocks = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> ajuste : ajustes.entrySet()) {
            String codigo = ajuste.getKey();
            Integer delta = ajuste.getValue();
            if (codigo == null || delta == null || delta == 0) {
                continue;
            }

            Producto producto = porCodigo.get(codigo);
            if (producto == null) {
                throw new IllegalStateException(
                        "No se encontró el producto con código " + codigo
                );
            }

            long nuevoStockLong = (long) producto.getStock() + delta;
            if (nuevoStockLong < 0) {
                throw new IllegalStateException(
                        "Stock insuficiente para " + producto.getNombre()
                        + ". Disponible: " + producto.getStock()
                        + ", requerido: " + (-delta) + "."
                );
            }
            if (nuevoStockLong > Integer.MAX_VALUE) {
                throw new IllegalStateException(
                        "El stock de " + producto.getNombre()
                        + " supera el límite permitido."
                );
            }

            nuevosStocks.put(producto, (int) nuevoStockLong);
        }

        for (Map.Entry<Producto, Integer> cambio : nuevosStocks.entrySet()) {
            Producto producto = cambio.getKey();
            int nuevoStock = cambio.getValue();
            producto.setStock(nuevoStock);
            producto.setDisponible(nuevoStock > 0);
        }

        guardarTodos(lista);
    }
    private void guardarTodos(List<Producto> lista) throws Exception {
        List<String> lineas = new ArrayList<>();
        for (Producto p : lista) lineas.add(p.toLinea());
        ArchivoUtil.reescribir(ruta, lineas);
    }
    public Producto buscarPorCodigo(String codigo) throws Exception {
        for (Producto p : listar()) if (p.getCodigo().equals(codigo)) return p;
        return null;
    }
    
    
}
