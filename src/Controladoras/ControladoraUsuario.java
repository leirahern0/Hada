package Controladoras;

import datos.ProductoDAO;
import modeloProductos.Producto;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modeloPersonas.Usuario;
import javafx.fxml.FXML;

public class ControladoraUsuario {

    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {

        this.usuarioActual = usuario;

    }

    private ProductoDAO productoDAO = new ProductoDAO();

    private List<Producto> listaProductos;

    @FXML
    private FlowPane contenedorProductos;

    @FXML
    private void initialize() {

        System.out.println(
                "FlowPane conectado: " + (contenedorProductos != null)
        );

        cargarProductos();
    }

    private void cargarProductos() {

        try {

            listaProductos = productoDAO.listar();

            mostrarProductos(listaProductos);

        } catch (Exception e) {

            Alert alerta = new Alert(Alert.AlertType.ERROR);

            alerta.setTitle("Hada Beauty");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "No se pudieron cargar los productos: " + e.getMessage()
            );

            alerta.showAndWait();
        }
    }

    //ESTE MÉTODO LIMPIA EL CONTENEDOR Y RECORRE LA LISTA PARA CONSTRUIR LA TARJETA DE CADA PRODUCTO.
    private void mostrarProductos(List<Producto> productos) {

        contenedorProductos.getChildren().clear();

        for (Producto producto : productos) {

            contenedorProductos.getChildren().add(
                    crearTarjetaProducto(producto)
            );
        }
    }

// ESTE MÉTODO CONSTRUYE LA TARJETA DE CADA PRODUCTO USANDO LOS DATOS DEL MISMO.
    private VBox crearTarjetaProducto(Producto producto) {

        VBox tarjeta = new VBox(7);

        tarjeta.setPrefWidth(150);
        tarjeta.setMinWidth(150);
        tarjeta.setMaxWidth(150);

        tarjeta.setAlignment(Pos.TOP_CENTER);

        tarjeta.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #f2d9df;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
                + "-fx-padding: 10;"
        );

        // IMAGEN DEL PRODUCTO
        ImageView imagen = new ImageView();

        imagen.setFitWidth(115);
        imagen.setFitHeight(115);
        imagen.setPreserveRatio(true);

        File archivoImagen = new File(
                "imagenes/productos",
                producto.getImagen()
        );

        if (archivoImagen.exists()) {

            imagen.setImage(
                    new Image(archivoImagen.toURI().toString())
            );
        }

        // NOMBRE
        Label nombre = new Label(producto.getNombre());

        nombre.setWrapText(true);
        nombre.setMaxWidth(130);

        nombre.setStyle(
                "-fx-font-family: Georgia;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #8f294d;"
                + "-fx-font-size: 12;"
        );

        // DESCRIPCIÓN
        Label descripcion = new Label(
                producto.getDescripcion()
        );

        descripcion.setWrapText(true);
        descripcion.setMaxWidth(130);
        descripcion.setMaxHeight(55);

        descripcion.setStyle(
                "-fx-text-fill: #9b717b;"
                + "-fx-font-size: 10;"
        );

        // PRECIO
        NumberFormat formato = NumberFormat.getInstance(
                Locale.forLanguageTag("es-CO")
        );

        formato.setMaximumFractionDigits(0);

        Label precio = new Label(
                "$ " + formato.format(producto.getPrecio())
        );

        precio.setStyle(
                "-fx-text-fill: #ff4f9a;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 14;"
        );

        // BOTÓN VER PRODUCTO
        Button btnVerProducto = new Button("Ver producto");

        btnVerProducto.setMaxWidth(Double.MAX_VALUE);

        btnVerProducto.setStyle(
                "-fx-background-color: #ff4f9a;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 20;"
                + "-fx-font-size: 12;"
                + "-fx-cursor: hand;"
        );

        // AGREGAR LOS ELEMENTOS A LA TARJETA
        tarjeta.getChildren().addAll(
                imagen,
                nombre,
                descripcion,
                precio,
                btnVerProducto
        );

        return tarjeta;
    }

    @FXML
    private void filtroLabios() {
        filtrarPorCategoria("Labios");
    }

    @FXML
    private void filtroRostro() {
        filtrarPorCategoria("Rostro");
    }

    @FXML
    private void filtroOjos() {
        filtrarPorCategoria("Ojos");
    }

    @FXML
    private void filtroBrochas() {
        filtrarPorCategoria("Brochas y accesorios");
    }

    //RECORRE LA LISTA DE PRODUCTOS Y COMPARA LA CATEGORÍA CON LA CATEGORÍA SELECCIONADA.
    private void filtrarPorCategoria(String categoria) {

        List<Producto> productosFiltrados = new ArrayList<>();

        for (Producto producto : listaProductos) {

            if (producto.getCategoria() != null
                    && producto.getCategoria().equalsIgnoreCase(categoria)) {

                productosFiltrados.add(producto);
            }
        }

        mostrarProductos(productosFiltrados);
    }

    @FXML
    private void verTodos() {

        cargarProductos();
    }

    @FXML
    private void abrirCatalogo() {
        // Pendiente de conectar
    }

    @FXML
    private void abrirFavoritos() {
        // Pendiente de conectar
    }

    @FXML
    private void abrirCarrito() {
        // Pendiente de conectar
    }

    @FXML
    private void abrirPerfil() {
        // Pendiente de conectar
    }

    @FXML
    private void cerrarSesion() {
        // Pendiente de conectar
    }
}
