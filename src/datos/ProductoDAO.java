/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import modeloProductos.Producto;
import java.util.*;

public class ProductoDAO {
    private String ruta = "datos/productos.txt";

    public List<Producto> listar() throws Exception {
        List<Producto> lista = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) if (!l.isBlank()) {
            Producto p = Producto.desdelineaArchivo(l);
            if (p != null) lista.add(p);
        }
        return lista;
    }
    public void guardar(Producto p) throws Exception { ArchivoUtil.agregar(ruta, p.lineaArchivo()); }
    public void eliminar(String codigo) throws Exception {
        List<Producto> lista = listar();
        lista.removeIf(p -> p.getCodigo().equals(codigo));
        guardarTodos(lista);
    }
    public void editar(Producto nuevo) throws Exception {
        List<Producto> lista = listar();
        for (int i = 0; i < lista.size(); i++) if (lista.get(i).getCodigo().equals(nuevo.getCodigo())) lista.set(i, nuevo);
        guardarTodos(lista);
    }
    private void guardarTodos(List<Producto> lista) throws Exception {
        List<String> lineas = new ArrayList<>();
        for (Producto p : lista) lineas.add(p.lineaArchivo());
        ArchivoUtil.reescribir(ruta, lineas);
    }
    public Producto buscarPorCodigo(String codigo) throws Exception {
        for (Producto p : listar()) if (p.getCodigo().equals(codigo)) return p;
        return null;
    }
}