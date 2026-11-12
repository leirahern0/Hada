
package controladorProductos;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import modeloProductos.Producto;

public class Inventario {

    private List<Producto> productos;
    private Stack<String> historialCambios;
    private Queue<Producto> colaPendientes;

    public Inventario() {

        this.productos = new ArrayList<>();
        this.historialCambios = new Stack<>();
        this.colaPendientes = new LinkedList<>();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public Stack<String> getHistorialCambios() {
        return historialCambios;
    }

    public Queue<Producto> getColaPendientes() {
        return colaPendientes;
    }

    public void agregarProducto(Producto producto) {

        if (producto != null) {
            productos.add(producto);
        }
    }

    public boolean eliminarProducto(Producto producto) {
        return productos.remove(producto);
    }

    public boolean eliminarPorCodigo(String codigo) {
        return productos.removeIf(p -> p.getCodigo().equalsIgnoreCase(codigo));
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public boolean editarProducto(String codigo, String nuevoNombre, String nuevaDescripcion,
            String nuevaMarca, String nuevaCategoria, double nuevoPrecio) {
        Producto p = buscarPorCodigo(codigo);
        if (p == null) {
            return false;
        }

        p.setNombre(nuevoNombre);
        p.setDescripcion(nuevaDescripcion);
        p.setMarca(nuevaMarca);
        p.setCategoria(nuevaCategoria);
        p.setPrecio(nuevoPrecio);

        registrarCambio("EDITADO: " + codigo);
        return true;
    }

    public boolean modificarFoto(String codigo, String nuevaRutaImagen) {
        Producto p = buscarPorCodigo(codigo);
        if (p == null) {
            return false;
        }

        String imagenAnterior = p.getImagen();
        p.setImagen(nuevaRutaImagen);
        registrarCambio("FOTO MODIFICADA: " + codigo + " | Anterior: " + imagenAnterior);
        return true;
    }

    public void agregarUnidades(Producto producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            producto.actualizarStock(cantidad);
        }
    }

    public void eliminarUnidades(Producto producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            producto.actualizarStock(-cantidad);
        }
    }

    public boolean actualizarStock(String codigo, int cantidad) {
        Producto p = buscarPorCodigo(codigo);
        if (p == null) {
            return false;
        }
        p.actualizarStock(cantidad);
        registrarCambio("STOCK ajustado en " + codigo + ": " + cantidad);
        return true;
    }

    public int totalProductos() {
        return productos.size();
    }

    public List<Producto> obtenerDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto p : productos) {
            if (p.isDisponible()) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }

    public Producto atenderSiguientePendiente() {
        return colaPendientes.poll();
    }

    public int cantidadPendientes() {
        return colaPendientes.size();
    }

    private void registrarCambio(String mensaje) {
        historialCambios.push(mensaje);
    }

    public String verUltimoCambio() {
        return historialCambios.isEmpty() ? null : historialCambios.peek();
    }

    public String deshacerRegistro() {
        return historialCambios.isEmpty() ? null : historialCambios.pop();
    }
    
    //------------------Archivos de texto---------------//

    public boolean guardarEnArchivo(String rutaArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Producto p : productos) {
                pw.println(p.lineaArchivo());
            }
            registrarCambio("CATÁLOGO GUARDADO en " + rutaArchivo);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean cargarDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int cargados = 0;
            while ((linea = br.readLine()) != null) {
                Producto p = Producto.desdelineaArchivo(linea);
                if (p != null) {
                    productos.add(p);
                    cargados++;
                }
            }
            registrarCambio("CATÁLOGO CARGADO: " + cargados + " productos");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
