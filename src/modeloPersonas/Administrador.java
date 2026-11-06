package modeloPersonas;

import controladorProductos.inventario;
import java.io.*;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import modeloProductos.Productos;

public class Administrador extends Persona {

    private String codigoAdministrador;
    private inventario inventario;               
    private Stack<String> historialCambios;        
    private Queue<Productos> colaPendientes;

    public Administrador() {
        this.inventario = new inventario();
        this.historialCambios = new Stack<>();
        this.colaPendientes = new LinkedList<>();
    }

    public Administrador(String nombre, String contrasena, String telefono, String usuario, int edad, String sexo, String codigoAdministrador) {
        super(nombre, contrasena, telefono, usuario, edad, sexo);
        
        this.codigoAdministrador = codigoAdministrador;
        this.inventario = new inventario();
        this.historialCambios = new Stack<>();
        this.colaPendientes = new LinkedList<>();
    }

    public String getCodigoAdministrador() {
        return codigoAdministrador;
    }

    public void setCodigoAdministrador(String codigoAdministrador) {
        this.codigoAdministrador = codigoAdministrador;
    }

    public inventario getInventario() {
        return inventario;
    }

    public Stack<String> getHistorialCambios() {
        return historialCambios;
    }

    public Queue<Productos> getColaPendientes() {
        return colaPendientes;
    }
    
    public List<Productos> gestionarProductos() {
        return inventario.getProductos();
    }

    public int totalProductosEnTienda() {
        return inventario.totalProductos();
    }
    
    public boolean agregarNuevoProducto(String nombre, String codigo, double precio,
                                        String marca, int stock, String imagen,
                                        String descripcion, String categoria) {

        if (inventario.buscarPorCodigo(codigo) != null) {
            return false;
        }

        Productos nuevo = new Productos(nombre, codigo, precio, marca, stock, imagen, descripcion);
        nuevo.setCategoria(categoria);

        inventario.agregarProducto(nuevo);
        colaPendientes.offer(nuevo);                   
        registrarCambio("AGREGADO: " + codigo + " - " + nombre);

        return true;
    }

    public boolean editarProducto(String codigo, String nuevoNombre, String nuevaDescripcion,
                                  String nuevaMarca, String nuevaCategoria, double nuevoPrecio) {

        Productos p = inventario.buscarPorCodigo(codigo);
        if (p == null) return false;

        String antes = p.lineaArchivo();             

        p.setNombre(nuevoNombre);
        p.setDescripcion(nuevaDescripcion);
        p.setMarca(nuevaMarca);
        p.setCategoria(nuevaCategoria);
        p.setPrecio(nuevoPrecio);

        registrarCambio("EDITADO: " + codigo + " | Antes: " + antes);
        return true;
    }

    public boolean modificarFoto(String codigo, String nuevaRutaImagen) {
        Productos p = inventario.buscarPorCodigo(codigo);
        if (p == null) return false;

        String imagenAnterior = p.getImagen();
        p.setImagen(nuevaRutaImagen);

        registrarCambio("FOTO MODIFICADA: " + codigo + " | Anterior: " + imagenAnterior);
        return true;
    }

    public boolean actualizarStock(String codigo, int cantidad) {
        Productos p = inventario.buscarPorCodigo(codigo);
        if (p == null) return false;

        p.actualizarStock(cantidad);
        registrarCambio("STOCK: " + codigo + " ajustado en " + cantidad);
        return true;
    }

    public boolean eliminarProducto(String codigo) {
        Productos p = inventario.buscarPorCodigo(codigo);
        if (p == null) return false;

        inventario.eliminarProducto(p);
        registrarCambio("ELIMINADO: " + codigo + " - " + p.getNombre());
        return true;
    }

    public Productos atenderSiguientePendiente() {
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

    // ===================== ARCHIVOS DE TEXTO =====================

    public boolean guardarCatalogoEnArchivo(String rutaArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Productos p : inventario.getProductos()) {
                pw.println(p.lineaArchivo());
            }
            registrarCambio("CATÁLOGO GUARDADO en " + rutaArchivo);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    

    public boolean cargarCatalogoDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int cargados = 0;
            while ((linea = br.readLine()) != null) {
                Productos p = Productos.desdelineaArchivo(linea);
                if (p != null) {
                    inventario.agregarProducto(p);
                    cargados++;
                }
            }
            registrarCambio("CATÁLOGO CARGADO: " + cargados + " productos desde " + rutaArchivo);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
