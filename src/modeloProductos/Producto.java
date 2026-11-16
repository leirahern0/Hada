package modeloProductos;


import java.util.ArrayList;
import java.util.List;

public class Producto {

    private String nombre;
    private String codigo;
    private double precio;
    private String marca;
    private int stock;
    private String imagen;
    private String descripcion;
    private String categoria;   
    private boolean disponible;
    private List<String> etiquetas;
    private String color;

    
    public Producto() {
        this.disponible = true;
        this.etiquetas = new ArrayList<>();
    }

    public Producto(String nombre, String codigo, double precio, String marca, int stock, String imagen, String descripcion, List<String> etiquetas) {

        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.marca = marca;
        this.stock = stock;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.disponible = true;
        this.etiquetas = new ArrayList<>(etiquetas);
    }
    
    public void agregarEtiqueta (String etiqueta){
        etiquetas.add(etiqueta);
    }
    
    public void eliminarEtiqueta (String etiqueta){
        etiquetas.remove(etiqueta);
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getStock() {
        return stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getCategoria() { 
        return categoria; 
    }
    public void setCategoria(String categoria) { 
        this.categoria = categoria; 
    }

    public boolean isDisponible() { 
        return disponible; 
    }
    public void setDisponible(boolean disponible) { 
        this.disponible = disponible; 
    }
    
    public void actualizarStock(int cantidad) {
        stock += cantidad;
        if (stock < 0) stock = 0;
        disponible = stock > 0;
    }

    public String toLinea() {
    
        return nombre + "|" + codigo + "|" + precio + "|" + marca + "|" +
           stock + "|" + imagen + "|" + descripcion + "|" +
           (categoria != null ? categoria : "") + "|" + disponible + "|" +
           (etiquetas != null ? String.join(",", etiquetas) : "");
    
    }


   public static Producto desdelinea(String linea) {
    String[] p = linea.split("\\|", -1);
    if (p.length < 10) return null;

    List<String> etiquetas = new ArrayList<>();
    if (!p[9].isEmpty()) {
        String[] tags = p[9].split(",");
        for (String t : tags) {
            etiquetas.add(t);
        }
    }

    Producto prod = new Producto(p[0], p[1], Double.parseDouble(p[2]),
            p[3], Integer.parseInt(p[4]), p[5], p[6], etiquetas);
    prod.setCategoria(p[7]);
    prod.setDisponible(Boolean.parseBoolean(p[8]));
    return prod;
}
    
    
}
