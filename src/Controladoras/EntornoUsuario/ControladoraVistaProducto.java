package Controladoras.EntornoUsuario;

import Controladoras.EntornoUsuario.ConUsuario;
import Controladoras.Navegador;
import datos.CarritoDAO;
import java.io.File;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import modeloPersonas.Usuario;
import modeloProductos.Carrito;
import modeloProductos.Producto;

public class ControladoraVistaProducto implements ConUsuario {

    @FXML
    private VBox raiz;
    @FXML
    private Label lblRutaProducto, lblCategoria, lblNombre, lblPrecio, lblCantidad,
            lblMarca, lblCategoriaCard, lblEstado, lblDescripcion;
    @FXML
    private ImageView imgProducto, imgMini1, imgMini2, imgMini3;
    @FXML
    private Button btnMenos, btnMas, btnCarrito;

    private Usuario usuario;
    private Producto producto;
    private String volverA = Navegador.INICIO;
    private int cantidad = 1;
    private Carrito carrito = new Carrito();
    private final CarritoDAO carritoDAO = new CarritoDAO();

    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        raiz.getChildren().add(0, Navegador.encabezado(usuario));
        try {
            carrito = carritoDAO.cargar(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProducto(Producto producto, String volverA) {
        this.producto = producto;
        if (volverA != null) {
            this.volverA = volverA;
        }

        lblNombre.setText(producto.getNombre());
        lblPrecio.setText(Navegador.precio(producto.getPrecio()));
        lblMarca.setText(producto.getMarca());
        lblCategoria.setText(producto.getCategoria());
        lblCategoriaCard.setText(producto.getCategoria());
        lblDescripcion.setText(producto.getDescripcion());
        lblCantidad.setText("1");

        boolean hay = producto.isDisponible() && producto.getStock() > 0;
        lblEstado.setText(hay ? "En stock" : "Agotado");
        btnCarrito.setDisable(!hay);
        btnMenos.setDisable(!hay);
        btnMas.setDisable(!hay);

        File f = new File("imagenes/productos", producto.getImagen() == null ? "" : producto.getImagen());
        if (f.exists()) {
            Image img = new Image(f.toURI().toString());
            imgProducto.setImage(img);
            imgMini1.setImage(img);
        }
        // el producto solo tiene una foto: ocultamos las otras dos miniaturas
        imgMini2.setVisible(false);
        imgMini2.setManaged(false);
        imgMini3.setVisible(false);
        imgMini3.setManaged(false);

        lblRutaProducto.setText("‹ Volver");
        lblRutaProducto.setStyle(lblRutaProducto.getStyle() + "-fx-cursor: hand;");
        lblRutaProducto.setOnMouseClicked(e -> Navegador.ir(this.volverA, raiz, usuario));
    }

    @FXML
    void restarCantidad() {
        if (cantidad > 1) {
            cantidad--;
            lblCantidad.setText(String.valueOf(cantidad));
        }
    }

    @FXML
    void sumarCantidad() {
        int disponibles = producto.getStock() - carrito.cantidadProducto(producto);
        if (cantidad >= disponibles) {
            Navegador.alerta("Stock insuficiente",
                    "Solo hay " + producto.getStock() + " unidades disponibles de este producto.",
                    Alert.AlertType.INFORMATION);
            return;
        }
        cantidad++;
        lblCantidad.setText(String.valueOf(cantidad));
    }

    @FXML
    void agregarCarrito() {
        if (guardarEnCarrito()) {
            btnCarrito.setText("✓ Agregado");
            PauseTransition pausa = new PauseTransition(Duration.seconds(1.2));
            pausa.setOnFinished(ev -> btnCarrito.setText("Agregar al carrito 🛒"));
            pausa.play();
        }
    }

    @FXML
    void comprarAhora() {
        if (guardarEnCarrito()) {
            Navegador.ir(Navegador.CARRITO, raiz, usuario);
        }
    }

    private boolean guardarEnCarrito() {
        if (carrito.cantidadProducto(producto) + cantidad > producto.getStock()) {
            Navegador.alerta("Stock insuficiente",
                    "Solo hay " + producto.getStock() + " unidades disponibles de este producto.",
                    Alert.AlertType.INFORMATION);
            return false;
        }
        for (int i = 0; i < cantidad; i++) {
            carrito.agregarUnidad(producto);
        }
        try {
            carritoDAO.guardar(usuario, carrito);
            Navegador.actualizarContadorCarrito(
                    usuario, carrito.getProductos().size());
        } catch (Exception e) {
            e.printStackTrace();
            Navegador.alerta("Error", "No se pudo guardar el carrito.", Alert.AlertType.ERROR);
            return false;
        }
        cantidad = 1;
        lblCantidad.setText("1");
        return true;
    }
}
