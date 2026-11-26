package Controladoras;

import Controladoras.EntornoAdmin.ControladoraVentanaAdmin;
import datos.ProductoDAO;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modeloProductos.Producto;

public class controladoraTienda {

    private ProductoDAO productoDAO = new ProductoDAO();
    private ControladoraVentanaAdmin admin;

    private List<Producto> listaProductos;
    private String categoriaActual = null;

    @FXML
    private FlowPane contenedorProductos;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAdmin;
    @FXML
    private HBox contenedorBanner;
    @FXML
    private ImageView imagenBanner;

    public void setAdmin(ControladoraVentanaAdmin admin) {
        this.admin = admin;
    }

    @FXML
    void initialize() {

        cargarProductos();
        
    txtBuscar.setOnAction(this::buscarProducto);
        
    }

    // evita que se repitan los productos y crea un recuadro por producto
    void cargarProductos() {
        try {
            listaProductos = productoDAO.listar();
            mostrarProductos(listaProductos);
        } catch (Exception e) {
            javafx.scene.control.Alert alerta
                    = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alerta.setTitle("Hada Beauty");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudieron cargar los productos: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    private void mostrarProductos(List<Producto> productos) {
        contenedorProductos.getChildren().clear();
        for (Producto producto : productos) {
            contenedorProductos.getChildren().add(crearRecuadroProducto(producto));
        }
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

    private void filtrarPorCategoria(String categoria) {

        //Si se oprime 2 veces la misma categoría se muestran nuevamente todos los productos
        if (categoria.equalsIgnoreCase(categoriaActual)) {
            categoriaActual = null;
            mostrarProductos(listaProductos);
            return;
        }

        categoriaActual = categoria;

        List<Producto> filtrados = new java.util.ArrayList<>();
        for (Producto producto : listaProductos) {
            if (producto.getCategoria() != null
                    && producto.getCategoria().equalsIgnoreCase(categoria)) {
                filtrados.add(producto);
            }
        }
        mostrarProductos(filtrados);
    }

    @FXML

    void buscarProducto(javafx.event.ActionEvent event) {
        filtrarPorTexto(txtBuscar.getText());
    }

    private void filtrarPorTexto(String texto) {
        if (listaProductos == null || txtBuscar == null) {
            return;
        }

        if (texto == null || texto.isBlank()) {
            categoriaActual = null;
            mostrarProductos(listaProductos);
            return;
        }

        String t = texto.trim().toLowerCase();
        List<Producto> resultado = new java.util.ArrayList<>();

        for (Producto p : listaProductos) {
            String todo = (p.getNombre() + " " + p.getMarca() + " " + p.getDescripcion() + " "
                    + p.getCategoria() + " " + String.join(" ", p.getEtiquetas() != null ? p.getEtiquetas() : java.util.List.of())).toLowerCase();

            if (todo.contains(t)) {
                resultado.add(p);
            }
        }

        mostrarProductos(resultado);

        if (resultado.isEmpty()) {
            Navegador.alerta("Hada Beauty", "No encontramos productos con \"" + texto.trim() + "\".",
                    javafx.scene.control.Alert.AlertType.INFORMATION);
        }
    }

    VBox crearRecuadroProducto(Producto producto) {

        VBox recuadro = new VBox(6);
        recuadro.setPrefWidth(195);
        recuadro.setMinWidth(195);
        recuadro.setMaxWidth(195);
        recuadro.setAlignment(Pos.TOP_CENTER);
        recuadro.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #f2d9df;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 12;");

        // imagen ( misma ruta utilizada en GestionProductos)
        ImageView imagen = new ImageView();
        imagen.setFitWidth(110);
        imagen.setFitHeight(110);
        imagen.setPreserveRatio(true);

        File archivoImagen = new File("imagenes/productos", producto.getImagen());
        if (archivoImagen.exists()) {
            imagen.setImage(new Image(archivoImagen.toURI().toString()));
        }

        Label nombre = new Label(producto.getNombre());
        nombre.setWrapText(true);
        nombre.setStyle("-fx-font-family: georgia; -fx-font-weight: bold;"
                + "-fx-text-fill: #8f294d; -fx-font-size: 12;");

        Label descripcion = new Label(producto.getDescripcion());
        descripcion.setWrapText(true);
        descripcion.setMaxHeight(40);
        descripcion.setStyle("-fx-text-fill: #9b717b; -fx-font-size: 10;");

        Label precio = new Label("$ " + formatearPrecio(producto.getPrecio()));
        precio.setStyle("-fx-text-fill: #ff4f9a; -fx-font-weight: bold; -fx-font-size: 14;");

        Button btnVer = new Button("Ver producto");
        btnVer.setMaxWidth(Double.MAX_VALUE);
        btnVer.setStyle("-fx-background-color: #ff4f9a; -fx-text-fill: white;"
                + "-fx-background-radius: 20; -fx-cursor: hand;");
        btnVer.setOnAction(e -> verDetalles(producto));
        recuadro.getChildren().addAll(imagen, nombre, descripcion, precio, btnVer);
        return recuadro;
    }

    private String formatearPrecio(double precio) {
        NumberFormat formato = NumberFormat.getInstance(Locale.forLanguageTag("es-CO"));
        formato.setMaximumFractionDigits(0);
        return formato.format(precio);
    }

    //Busca el diseño de la ficha, le pasa el producto seleccionado para que la llene y lo abre en otra ventana con la información del producto. 
    //El try-catch evita que se cierre toda la ventana tienda cuando en caso de que la ficha no se pueda abrir.
    private void verDetalles(Producto producto) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/vista/vistaAdmin/FichaProducto.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("NO ENCONTRÉ EL FXML, revisa la ruta");
                return;
            }

            javafx.scene.Parent root = loader.load();

            Controladoras.EntornoAdmin.ControladoraFichaProducto ficha = loader.getController();
            ficha.setProducto(producto);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Ficha de producto - " + producto.getNombre());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alerta.setTitle("Hada Beauty");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir la ficha: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    void abrirPerfilAdmin(javafx.event.ActionEvent event) {
        try {

            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/vista/vistaAdmin/PerfilAdministrador.fxml"));

            // Si no encuentra el archivo, te avisa y no se cierra la tienda
            if (loader.getLocation() == null) {
                javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alerta.setTitle("Hada Beauty");
                alerta.setHeaderText("Perfil Administrador");
                alerta.setContentText("Estás en la cuenta de administrador.\n\nAún no tienes creada la vista PerfilAdministrador.fxml");
                alerta.showAndWait();
                return;
            }

            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Perfil Administrador - Hada Beauty");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
