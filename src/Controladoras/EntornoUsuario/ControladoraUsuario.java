package Controladoras.EntornoUsuario;

import Controladoras.EntornoUsuario.ConUsuario;
import Controladoras.Navegador;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import modeloPersonas.Usuario;
import javafx.fxml.FXML;
import datos.CarritoDAO;
import datos.FavoritosDAO;
import modeloProductos.Carrito;
import modeloProductos.Favoritos;
import javafx.animation.PauseTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.control.TextField;

public class ControladoraUsuario implements ConUsuario {

    @FXML
    private Button btnCarrito;
    @FXML
    private HBox contenedorBanner;
    @FXML
    private ImageView imagenBanner;
    @FXML
    private AnchorPane contenedorVistas;
    @FXML
    private Label lblTituloCategorias, lblCategoriaLabios, lblCategoriaRostro,
            lblCategoriaOjos, lblCategoriaBrochas;
    @FXML
    private Line lineaCategoriasIzquierda, lineaCategoriasDerecha;
    @FXML
    private Button btnCategoriaLabios, btnCategoriaRostro,
            btnCategoriaOjos, btnCategoriaBrochas, btnVerTodo;

    private Usuario usuarioActual;

    @Override
    public void setUsuario(Usuario usuario) {

        this.usuarioActual = usuario;

        try {
            favoritos = favoritosDAO.cargar(usuario);
            carrito = carritoDAO.cargar(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Navegador.agregarContadorCarrito(
                btnCarrito, usuario, carrito.getProductos().size());

        // Redibuja las tarjetas para que los corazones salgan rellenos donde corresponda
        if (listaProductos != null) {
            if (Navegador.textoBusqueda != null) {
                String t = Navegador.textoBusqueda;
                Navegador.textoBusqueda = null;
                txtBuscar.setText(t);
                filtrarPorTexto(t);
            } else {
                mostrarProductos(listaProductos);
            }
        }
    }

    @FXML
    private void buscarProducto() {
        filtrarPorTexto(txtBuscar.getText());
    }

    private void filtrarPorTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            mostrarProductos(listaProductos);
            return;
        }
        String t = texto.trim().toLowerCase();
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : listaProductos) {
            String todo = (p.getNombre() + " " + p.getMarca() + " " + p.getDescripcion() + " "
                    + p.getCategoria() + " " + String.join(" ", p.getEtiquetas())).toLowerCase();
            if (todo.contains(t)) {
                resultado.add(p);
            }
        }
        mostrarProductos(resultado);
        if (resultado.isEmpty()) {
            Navegador.alerta("Hada Beauty", "No encontramos productos con \"" + texto + "\".",
                    Alert.AlertType.INFORMATION);
        }
    }

    private final FavoritosDAO favoritosDAO = new FavoritosDAO();
    private final CarritoDAO carritoDAO = new CarritoDAO();
    private Favoritos favoritos = new Favoritos();
    private Carrito carrito = new Carrito();
    private ProductoDAO productoDAO = new ProductoDAO();
    private List<Producto> listaProductos;

    private Button crearCorazon(Producto producto) {

        Button corazon = new Button();
        actualizarCorazon(corazon, favoritos.esFavorito(producto));
        StackPane.setAlignment(corazon, Pos.TOP_RIGHT);

        corazon.setOnAction(e -> {
            if (usuarioActual == null) {
                return;
            }
            boolean esFavorito = favoritos.corazonFavoritos(producto);
            actualizarCorazon(corazon, esFavorito);
            try {
                favoritosDAO.guardar(usuarioActual, favoritos);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return corazon;
    }

    private void actualizarCorazon(Button boton, boolean relleno) {
        boton.setText(relleno ? "♥" : "♡");
        boton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 2 0 2;"
                + "-fx-font-size: 22; -fx-text-fill: " + (relleno ? "#8f294d" : "#ff4f9a") + ";");
    }

    private Button crearBotonCarrito(Producto producto) {

        Button boton = new Button("Agregar al carrito");
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setStyle("-fx-background-color: #8f294d; -fx-text-fill: white;"
                + "-fx-background-radius: 20; -fx-font-size: 11; -fx-cursor: hand;");

        boton.setOnAction(e -> {
            if (usuarioActual == null) {
                return;
            }
            if (producto.getStock() <= 0 || !producto.isDisponible()) {
                Navegador.alerta("Producto agotado", "Este producto no está disponible.", Alert.AlertType.INFORMATION);
                return;
            }
            if (carrito.cantidadProducto(producto) >= producto.getStock()) {
                Navegador.alerta("Stock insuficiente",
                        "Solo hay " + producto.getStock() + " unidades disponibles.", Alert.AlertType.INFORMATION);
                return;
            }

            carrito.agregarUnidad(producto);
            try {
                carritoDAO.guardar(usuarioActual, carrito);
                Navegador.actualizarContadorCarrito(
                        usuarioActual, carrito.getProductos().size());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            boton.setText("✓ Agregado");
            PauseTransition pausa = new PauseTransition(Duration.seconds(1.2));
            pausa.setOnFinished(ev -> boton.setText("Agregar al carrito"));
            pausa.play();
        });

        return boton;
    }

    @FXML
    private FlowPane contenedorProductos;

    @FXML
    private TextField txtBuscar;

    @FXML
    private void initialize() {

        contenedorBanner.prefHeightProperty()
                .bind(contenedorBanner.widthProperty().multiply(0.26));
        imagenBanner.fitWidthProperty()
                .bind(contenedorBanner.widthProperty().multiply(0.81));
        imagenBanner.fitHeightProperty()
                .bind(contenedorBanner.widthProperty().multiply(0.26));

        contenedorVistas.widthProperty().addListener(
                (observable, anterior, ancho) -> ajustarCategorias(ancho.doubleValue())
        );
        ajustarCategorias(900);

        System.out.println(
                "FlowPane conectado: " + (contenedorProductos != null)
        );

        cargarProductos();
    }

    private void ajustarCategorias(double ancho) {
        if (ancho <= 0) {
            return;
        }

        double[] centros = {
            ancho * 0.14, ancho * 0.38, ancho * 0.63, ancho * 0.86
        };
        Button[] botones = {
            btnCategoriaLabios, btnCategoriaRostro,
            btnCategoriaOjos, btnCategoriaBrochas
        };
        Label[] nombres = {
            lblCategoriaLabios, lblCategoriaRostro,
            lblCategoriaOjos, lblCategoriaBrochas
        };

        for (int i = 0; i < centros.length; i++) {
            botones[i].setLayoutX(centros[i] - 34);
            nombres[i].setPrefWidth(i == 3 ? 180 : 100);
            nombres[i].setAlignment(Pos.CENTER);
            nombres[i].setLayoutX(centros[i] - (i == 3 ? 90 : 50));
        }

        lblTituloCategorias.setPrefWidth(270);
        lblTituloCategorias.setAlignment(Pos.CENTER);
        lblTituloCategorias.setLayoutX((ancho - 270) / 2);

        double mediaLinea = ancho * 0.09;
        lineaCategoriasIzquierda.setStartX(-mediaLinea);
        lineaCategoriasIzquierda.setEndX(mediaLinea);
        lineaCategoriasIzquierda.setLayoutX(ancho * 0.25);
        lineaCategoriasDerecha.setStartX(-mediaLinea);
        lineaCategoriasDerecha.setEndX(mediaLinea);
        lineaCategoriasDerecha.setLayoutX(ancho * 0.75);

        btnVerTodo.setLayoutX(ancho - 157);
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

    // Limpia el contenedor y recorre la lista para construir la tarjeta o el cuadrículo de cada producto
    private void mostrarProductos(List<Producto> productos) {

        contenedorProductos.getChildren().clear();

        for (Producto producto : productos) {

            contenedorProductos.getChildren().add(
                    crearTarjetaProducto(producto)
            );
        }
    }

    // Construye la tarjeta de cada producto usando sus datos propios
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

        // Imagen del producto
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

        // Nombre
        Label nombre = new Label(producto.getNombre());

        nombre.setWrapText(true);
        nombre.setMaxWidth(130);

        nombre.setStyle(
                "-fx-font-family: Georgia;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #8f294d;"
                + "-fx-font-size: 12;"
        );

        // Descripción
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

        // Precio
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

        // Botón de ¨Ver Producto¨
        Button btnVerProducto = new Button("Ver producto");

        btnVerProducto.setMaxWidth(Double.MAX_VALUE);

        btnVerProducto.setStyle(
                "-fx-background-color: #ff4f9a;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 20;"
                + "-fx-font-size: 12;"
                + "-fx-cursor: hand;"
        );

        btnVerProducto.setOnAction(e
                -> Navegador.irAProducto(producto, contenedorProductos, usuarioActual, Navegador.INICIO));

        // AGREGAR LOS ELEMENTOS A LA TARJETA
        // La imagen y el corazón van juntos: el corazón queda arriba a la derecha
        StackPane zonaImagen = new StackPane(imagen, crearCorazon(producto));

        tarjeta.getChildren().addAll(
                zonaImagen,
                nombre,
                descripcion,
                precio,
                crearBotonCarrito(producto),
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

    // Recorre la lista de productos y compara la categoría con la categoría seleccionada
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
        Navegador.ir(Navegador.FAVORITOS, contenedorProductos, usuarioActual);
    }

    @FXML
    private void abrirCarrito() {
        Navegador.ir(Navegador.CARRITO, contenedorProductos, usuarioActual);

    }

    @FXML
    private void abrirPerfil() {
        Navegador.ir(Navegador.PERFIL, contenedorProductos, usuarioActual);
    }

    @FXML
    private void cerrarSesion() {

        Navegador.cerrarSesion(contenedorProductos);
    }
}
