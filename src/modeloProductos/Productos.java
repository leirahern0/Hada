package modeloProductos;

/**
 *
 * @author Usuario
 */
public class Productos {

    private String nombre;
    private String codigo;
    private double precio;
    private String marca;
    private int stock;
    private String imagen;
    private String descripcion;

    public Productos() {
    }

    public Productos(String nombre, String codigo, double precio, String marca, int stock, String imagen, String descripcion) {

        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.marca = marca;
        this.stock = stock;
        this.imagen = imagen;
        this.descripcion = descripcion;
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
}
