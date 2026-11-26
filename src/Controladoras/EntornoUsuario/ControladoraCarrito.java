package Controladoras.EntornoUsuario;

import Controladoras.Navegador;
import datos.CarritoDAO;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modeloPersonas.Usuario;
import modeloProductos.Carrito;
import modeloProductos.Producto;

public class ControladoraCarrito implements ConUsuario {

    private static final double W_IMG = 70, W_DESC = 210, W_ENVIO = 80, W_PRECIO = 85, W_CANT = 100, W_SUB = 90;

    @FXML
    private VBox raiz;
    @FXML
    private VBox contenedorItems;
    @FXML
    private Label lblVacio;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnVaciar;
    @FXML
    private Button btnContinuar;

    private Usuario usuario;
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
        mostrar();
    }

    private void mostrar() {
        contenedorItems.getChildren().clear();

        Map<String, Producto> unicos = new LinkedHashMap<>();
        for (Producto p : carrito.getProductos()) {
            unicos.putIfAbsent(p.getCodigo(), p);
        }

        boolean vacio = unicos.isEmpty();
        lblVacio.setVisible(vacio);
        lblVacio.setManaged(vacio);
        btnVaciar.setVisible(!vacio);
        btnVaciar.setManaged(!vacio);
        btnContinuar.setDisable(vacio);

        if (!vacio) {
            contenedorItems.getChildren().add(fila(
                    celda("Producto", W_IMG, true), celda("Descripción", W_DESC, true),
                    celda("Envío", W_ENVIO, true), celda("Precio", W_PRECIO, true),
                    celda("Cantidad", W_CANT, true), celda("Subtotal", W_SUB, true)));
            for (Producto p : unicos.values()) {
                contenedorItems.getChildren().add(crearFila(p));
            }
        }
        lblTotal.setText(Navegador.precio(carrito.calcularTotalCarrito()));
    }

    private HBox crearFila(Producto p) {
        int cantidad = carrito.cantidadProducto(p);

        VBox descripcion = new VBox(2);
        descripcion.setPrefWidth(W_DESC);
        Label nombre = new Label(p.getNombre());
        nombre.setWrapText(true);
        nombre.setStyle("-fx-font-family: Georgia; -fx-text-fill: #8f294d; -fx-font-size: 12;");
        descripcion.getChildren().add(nombre);
        if (p.getColor() != null && !p.getColor().isBlank()) {
            descripcion.getChildren().add(detalle("Tono: ", p.getColor()));
        }
        descripcion.getChildren().add(detalle("Referencia: ", p.getCodigo()));

        String envio = carrito.getPrecioEnvio() > 0 ? Navegador.precio(carrito.getPrecioEnvio()) : "Por definir";

        Button menos = new Button("-");
        Button mas = new Button("+");
        for (Button b : new Button[]{menos, mas}) {
            b.setStyle("-fx-background-color: transparent; -fx-text-fill: #8f294d; -fx-font-weight: bold; -fx-cursor: hand;");
        }
        Label cant = new Label(String.valueOf(cantidad));
        cant.setStyle("-fx-text-fill: #8f294d; -fx-font-weight: bold;");
        HBox selector = new HBox(4, menos, cant, mas);
        selector.setAlignment(Pos.CENTER);
        selector.setMaxWidth(W_CANT - 20);
        selector.setStyle("-fx-border-color: #8f294d; -fx-border-radius: 20; -fx-background-radius: 20;");

        menos.setOnAction(e -> {
            carrito.eliminarUnidad(p);
            guardarYRefrescar();
        });
        mas.setOnAction(e -> {
            if (carrito.cantidadProducto(p) >= p.getStock()) {
                Navegador.alerta("Stock insuficiente",
                        "Solo hay " + p.getStock() + " unidades disponibles de este producto.",
                        Alert.AlertType.INFORMATION);
                return;
            }
            carrito.agregarUnidad(p);
            guardarYRefrescar();
        });

        HBox celdaCant = new HBox(selector);
        celdaCant.setAlignment(Pos.CENTER);
        celdaCant.setPrefWidth(W_CANT);

        StackPane celdaImg = new StackPane(Navegador.imagenProducto(p, 64));
        celdaImg.setPrefWidth(W_IMG);
        celdaImg.setMinWidth(W_IMG);

        HBox f = fila(celdaImg, descripcion,
                celda(envio, W_ENVIO, false),
                celda(Navegador.precio(p.getPrecio()), W_PRECIO, false),
                celdaCant,
                celda(Navegador.precio(p.getPrecio() * cantidad), W_SUB, false));
        f.setStyle("-fx-background-color: #fff4e9; -fx-background-radius: 14; -fx-padding: 8 4 8 4;");
        return f;
    }

    private Label detalle(String titulo, String valor) {
        Label l = new Label(titulo + valor);
        l.setStyle("-fx-font-size: 10; -fx-text-fill: #5a3a44;");
        return l;
    }

    private Label celda(String texto, double ancho, boolean encabezado) {
        Label l = new Label(texto);
        l.setPrefWidth(ancho);
        l.setMinWidth(ancho);
        l.setAlignment(Pos.CENTER);
        if (encabezado) {
            l.getStyleClass().add("celda-encabezado");
        } else {
            l.setStyle("-fx-text-fill: #8f294d; -fx-font-weight: bold; -fx-font-size: 12;");
        }
        return l;
    }

    private HBox fila(Node... nodos) {
        HBox f = new HBox(6, nodos);
        f.setAlignment(Pos.CENTER_LEFT);
        return f;
    }

    private void guardarYRefrescar() {
        try {
            carritoDAO.guardar(usuario, carrito);
            Navegador.actualizarContadorCarrito(
                    usuario, carrito.getProductos().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Navegador.ir(Navegador.CARRITO, raiz, usuario);
    }

    @FXML
    private void vaciarCarrito() {
        if (Navegador.confirmar("Vaciar carrito", "¿Deseas quitar todos los productos del carrito?")) {
            carrito.vaciar();
            guardarYRefrescar();
        }

    }

    @FXML
    private void continuarPago() {
        if (carrito.getProductos().isEmpty()) {
            Navegador.alerta("Carrito vacío", "Agrega productos antes de continuar.", Alert.AlertType.WARNING);
            return;
        }
        Navegador.ir(Navegador.TERMINAR, raiz, usuario);
    }

    @FXML
    private void seguirComprando() {
        Navegador.ir(Navegador.INICIO, raiz, usuario);
    }
}
