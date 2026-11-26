package datos;

import modeloProductos.Pedido;
import java.util.*;

public class PedidoDAO {

    private String ruta = "datos/pedidos.txt";

    public PedidoDAO() {
    }

    public List<Pedido> listar() throws Exception {
        List<Pedido> lista = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) {
            if (!l.isBlank()) {
                lista.add(Pedido.desdeLinea(l));
            }
        }
        return lista;
    }

    public void guardar(Pedido p) throws Exception {
        ArchivoUtil.agregar(ruta, p.toLinea());
    }

    public String siguienteNumero() throws Exception {
        int mayor = 0;
        for (Pedido pedido : listar()) {
            String numero = pedido.getNumeroPedido();
            if (numero == null || !numero.startsWith("HB-")) {
                continue;
            }
            try {
                mayor = Math.max(mayor,
                        Integer.parseInt(numero.substring(3)));
            } catch (NumberFormatException ignorado) {
                // Ignora identificadores antiguos que no terminan en números.
            }
        }
        return String.format("HB-%04d", mayor + 1);
    }

    public void eliminar(String numero) throws Exception {
        List<Pedido> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (numero.equals(lista.get(i).getNumeroPedido())) {
                lista.remove(i);
                break;
            }
        }
        List<String> lineas = new ArrayList<>();
        for (Pedido p : lista) {
            lineas.add(p.toLinea());
        }
        ArchivoUtil.reescribir(ruta, lineas);
    }

    public void eliminarTodos() throws Exception {
        List<String> pedidosAnteriores = ArchivoUtil.leer(ruta);
        try {
            ArchivoUtil.reescribir(ruta, Collections.emptyList());
        } catch (Exception error) {
            try {
                ArchivoUtil.reescribir(ruta, pedidosAnteriores);
            } catch (Exception rollback) {
                error.addSuppressed(rollback);
            }
            throw error;
        }
    }
    
    public void actualizar(Pedido actualizado) throws Exception {
    List<Pedido> lista = listar();
    boolean encontrado = false;

    for (int i = 0; i < lista.size(); i++) {
        if (lista.get(i).getNumeroPedido()
                .equals(actualizado.getNumeroPedido())) {
            lista.set(i, actualizado);
            encontrado = true;
            break;
        }
    }

    if (!encontrado) {
        throw new Exception(
                "No se encontró el pedido "
                + actualizado.getNumeroPedido()
        );
    }

    List<String> lineas = new ArrayList<>();
    for (Pedido pedido : lista) {
        lineas.add(pedido.toLinea());
    }

    ArchivoUtil.reescribir(ruta, lineas);
}
}
