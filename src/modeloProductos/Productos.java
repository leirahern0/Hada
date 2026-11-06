package modeloProductos;

public class Productos {

    private String nombre;
    private String codigo;
    private double precio;
    private String marca;
    private int stock;
    private String imagen;
    private String descripcion;
    private String categoria;   
    private boolean disponible;

    public Productos() {
        this.disponible = true;
    }

    public Productos(String nombre, String codigo, double precio, String marca, int stock, String imagen, String descripcion) {

        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.marca = marca;
        this.stock = stock;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.disponible = true;
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

    public void setStock(int stock) {
        this.stock = stock;
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
        this.stock += cantidad;
        if (this.stock < 0) this.stock = 0;
        this.disponible = this.stock > 0;
    }

    public String lineaArchivo() {
        
        return nombre + "|" + codigo + "|" + precio + "|" + marca + "|" +
               stock + "|" + imagen + "|" + descripcion + "|" +
               (categoria != null ? categoria : "") + "|" + disponible;
    }

    public static Productos desdelineaArchivo(String linea) {
        String[] p = linea.split("\\|");
        if (p.length < 9) return null;
        Productos prod = new Productos(p[0], p[1], Double.parseDouble(p[2]),
                p[3], Integer.parseInt(p[4]), p[5], p[6]);
        prod.setCategoria(p[7]);
        prod.setDisponible(Boolean.parseBoolean(p[8]));
        return prod;
    }
}
