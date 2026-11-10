/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import modeloProductos.Pedido;
import java.util.*;

public class PedidoDAO {
    private String ruta = "datos/pedidos.txt";
    public List<Pedido> listar() throws Exception {
        List<Pedido> lista = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) if (!l.isBlank()) lista.add(Pedido.desdeLinea(l));
        return lista;
    }
    public void guardar(Pedido p) throws Exception { ArchivoUtil.agregar(ruta, p.toLinea()); }
    public void eliminar(String numero) throws Exception {
        List<Pedido> lista = listar();
        lista.removeIf(p -> p.getNumeroPedido().equals(numero));
        List<String> lineas = new ArrayList<>();
        for (Pedido p : lista) lineas.add(p.toLinea());
        ArchivoUtil.reescribir(ruta, lineas);
    }
}