package modeloProductos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modeloPersonas.Usuario;
import datos.ProductoDAO;
import datos.UsuarioDAO;

public class Pedido {
   
    private String numeroPedido;       
    private Usuario cliente;
    private List<Producto> productos;
    private LocalDateTime fecha;
    private double total;
    private String metodoPago;      
    private String estadoPago;           
    private String estadoPreparacion;   
    private String direccionEntrega;
    
    public Pedido() {
        this.productos = new ArrayList<>();
        this.fecha = LocalDateTime.now();
        this.estadoPago = "En espera de pago";
        this.estadoPreparacion = "En espera de empaque";
    }

    public Pedido(String numeroPedido, Usuario cliente, List<Producto> productos,
                  double total, String metodoPago) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.productos = productos != null ? productos : new ArrayList<>();
        this.fecha = LocalDateTime.now();
        this.total = total;
        this.metodoPago = metodoPago;
        this.estadoPago = "En espera de pago";
        this.estadoPreparacion = "En espera de empaque";
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getEstadoPreparacion() {
        return estadoPreparacion;
    }

    public void setEstadoPreparacion(String estadoPreparacion) {
        this.estadoPreparacion = estadoPreparacion;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }


    public void agregarProducto(Producto producto) {
        if (producto != null) {
            productos.add(producto);
        }
    }

    public int cantidadProductos() {
        return productos.size();
    }

    public void calcularTotal() {
        double suma = 0;
         for (Producto p : productos) {
            suma += p.getPrecio();
        }
        this.total = suma;
    }
    
     public String toLinea() {
        
        List<String> codigos = new ArrayList<>();
        for (Producto p : productos) codigos.add(p.getCodigo());
        return numeroPedido + "|" + (cliente != null ? cliente.getCorreo() : "") + "|"
            + String.join(",", codigos) + "|" + fecha.toString() + "|" + total + "|"
            + metodoPago + "|" + estadoPago + "|" + estadoPreparacion + "|"
            + (direccionEntrega != null ? direccionEntrega : "");
    }

    public static Pedido desdeLinea(String linea) throws Exception {
        String[] p = linea.split("\\|", -1);
        Usuario cliente = (Usuario) new UsuarioDAO().buscarPorCorreo(p[1]);
        List<Producto> prods = new ArrayList<>();
        ProductoDAO pdao = new ProductoDAO();
        if (!p[2].isEmpty()) for (String cod : p[2].split(",")) {
            Producto prod = pdao.buscarPorCodigo(cod);
            if (prod != null) prods.add(prod);
        }
        Pedido ped = new Pedido(p[0], cliente, prods, Double.parseDouble(p[4]), p[5]);
        ped.setFecha(java.time.LocalDateTime.parse(p[3]));
        ped.setEstadoPago(p[6]);
        ped.setEstadoPreparacion(p[7]);
        ped.setDireccionEntrega(p[8]);
        return ped;
    }
}
