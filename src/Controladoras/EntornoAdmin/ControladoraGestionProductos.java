package Controladoras.EntornoAdmin;

import Controladoras.EntornoAdmin.ControladoraFichaProducto;
import Controladoras.EntornoAdmin.ControladoraEditarProducto;
import Controladoras.Navegador;
import datos.PedidoServicio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import datos.ProductoDAO;
import modeloProductos.Producto;
import java.io.File;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ControladoraGestionProductos {

    private ProductoDAO productoDAO = new ProductoDAO();
    private final PedidoServicio pedidoServicio = new PedidoServicio();
    private List<Producto> listaProductos;

    @FXML
    private VBox contenedorProductos;
    @FXML
    private Label lblContador;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnFiltroTodos, btnFiltroRostro, btnFiltroLabios, btnFiltroOjos, btnFiltroBrochas;

    private ControladoraVentanaAdmin admin;

    @FXML
    private void initialize() {
        cargarProductos();

        seleccionarFiltro(btnFiltroTodos);
    }

    // Permite cambiar de color los botones de filtro dependiendo de cual sea seleccionado
    private void seleccionarFiltro(Button botonSeleccionado) {

        Button[] botones = {
            btnFiltroTodos,
            btnFiltroRostro,
            btnFiltroLabios,
            btnFiltroOjos,
            btnFiltroBrochas
        };

        for (Button boton : botones) {

            if (boton == botonSeleccionado) {

                boton.setStyle(
                        "-fx-background-color: #ffc6ad;"
                        + "-fx-text-fill: #8f294d;"
                        + "-fx-background-radius: 8;"
                        + "-fx-cursor: hand;"
                );

            } else {

                boton.setStyle(
                        "-fx-background-color: #fff4e9;"
                        + "-fx-text-fill: #8f294d;"
                        + "-fx-background-radius: 8;"
                        + "-fx-cursor: hand;"
                );
            }
        }
    }

    // Limpia las filas anteriores para evitar duplicados, recorre los productos que recupera del archivo txt, coloca la informacion del producto por columna y actualiza el contador
    private void cargarProductos() {

        try {
            listaProductos = productoDAO.listar();
            mostrarProductos(listaProductos);

        } catch (Exception e) {
            mostrarError("No se pudieron cargar los productos: " + e.getMessage());
        }
    }

    // Recorre la lista de los productos y construye una fila visual para cada objeto tipo producto
    private void mostrarProductos(List<Producto> productos) {

        contenedorProductos.getChildren().clear();

        for (Producto producto : productos) {

            GridPane fila = new GridPane();

            fila.setHgap(8);
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setStyle(
                    "-fx-background-color: white;"
                    + "-fx-border-color: transparent transparent #f2d9df transparent;"
                    + "-fx-padding: 8 5;"
            );

            fila.getColumnConstraints().addAll(
                    columnaResponsive(8),
                    columnaResponsive(30),
                    columnaResponsive(12),
                    columnaResponsive(12),
                    columnaResponsive(38)
            );

            ImageView imagen = new ImageView();
            imagen.setFitWidth(32);
            imagen.setFitHeight(32);
            imagen.setPreserveRatio(true);

            File archivoImagen = new File(
                    "imagenes/productos",
                    producto.getImagen()
            );

            if (archivoImagen.exists()) {
                imagen.setImage(new Image(archivoImagen.toURI().toString()));
            }

            fila.add(imagen, 0, 0);

            VBox informacion = new VBox(4);
            informacion.setMaxWidth(Double.MAX_VALUE);

            Label nombre = new Label(producto.getNombre());
            nombre.setMinWidth(0);
            nombre.setWrapText(true);
            nombre.setStyle(
                    "-fx-font-weight: bold;"
                    + "-fx-text-fill: #8f294d;"
                    + "-fx-font-size: 12;"
            );

            Label descripcion = new Label(producto.getDescripcion());
            descripcion.setMinWidth(0);
            descripcion.setWrapText(true);
            descripcion.setStyle(
                    "-fx-text-fill: #9b717b;"
                    + "-fx-font-size: 10;"
            );

            informacion.getChildren().addAll(nombre, descripcion);
            fila.add(informacion, 1, 0);

            Label categoria = new Label(producto.getCategoria());
            categoria.setMinWidth(0);
            categoria.setWrapText(true);
            categoria.setStyle("-fx-text-fill: #713249;");
            fila.add(categoria, 2, 0);

            Label precio = new Label("$ " + producto.getPrecio());
            precio.setStyle(
                    "-fx-text-fill: #8f294d;"
                    + "-fx-font-weight: bold;"
            );
            fila.add(precio, 3, 0);

            HBox acciones = new HBox(5);
            acciones.setAlignment(Pos.CENTER_LEFT);
            acciones.setMinWidth(0);
            acciones.setMaxWidth(Double.MAX_VALUE);

            Button btnVer = new Button("Ver detalles");
            btnVer.setStyle(
                    "-fx-background-color: white;"
                    + "-fx-text-fill: #FF4F9A;"
                    + "-fx-border-color: #FF4F9A;"
                    + "-fx-border-radius: 15;"
                    + "-fx-background-radius: 15;"
                    + "-fx-font-size: 10;"
                    + "-fx-padding: 4 8;"
                    + "-fx-cursor: hand;"
            );
            btnVer.setOnAction(e -> verDetalles(producto));

            Button btnEditar = new Button("✎");
            btnEditar.setStyle(
                    "-fx-background-color: #FFC6AD;"
                    + "-fx-text-fill: #8F294D;"
                    + "-fx-background-radius: 50;"
                    + "-fx-font-size: 14;"
                    + "-fx-font-family: 'Segoe UI Symbol';"
                    + "-fx-padding: 0;"
                    + "-fx-cursor: hand;"
                    + "-fx-pref-width: 32;"
                    + "-fx-pref-height: 32;"
                    + "-fx-min-width: 32;"
                    + "-fx-min-height: 32;"
                    + "-fx-max-width: 32;"
                    + "-fx-max-height: 32;"
            );
            btnEditar.setOnAction(e -> editarProducto(producto));
            Button btnEliminar = new Button("🗑");
            btnEliminar.setStyle(
                    "-fx-background-color: transparent;"
                    + "-fx-text-fill: #8F294D;"
                    + "-fx-font-size: 13;"
                    + "-fx-cursor: hand;"
                    + "-fx-pref-width: 28;"
                    + "-fx-pref-height: 28;"
                    + "-fx-min-width: 28;"
                    + "-fx-min-height: 28;"
            );
            btnEliminar.setOnAction(e -> eliminarProducto(producto));

            acciones.getChildren().addAll(btnVer, btnEditar, btnEliminar);
            fila.add(acciones, 4, 0);

            contenedorProductos.getChildren().add(fila);
        }

        lblContador.setText(
                "Mostrando " + productos.size() + " productos"
        );
    }

    private javafx.scene.layout.ColumnConstraints columnaResponsive(double porcentaje) {
        javafx.scene.layout.ColumnConstraints columna
                = new javafx.scene.layout.ColumnConstraints();
        columna.setPercentWidth(porcentaje);
        columna.setMinWidth(0);
        return columna;
    }

    public void setAdmin(ControladoraVentanaAdmin admin) {
        this.admin = admin;
    }

    @FXML
    private void nuevoProducto() {
        if (admin != null) {
            admin.mostrarNuevoProducto();
        }
    }

    @FXML
    private void vaciarPedidos() {
        if (!Navegador.confirmar("Vaciar pedidos",
                "Se eliminarán todos los pedidos registrados y sus productos "
                + "volverán al inventario. ¿Deseas continuar?")) {
            return;
        }

        try {
            int eliminados = pedidoServicio.eliminarTodosLosPedidosYDevolverStock();
            String mensaje = eliminados == 0
                    ? "No hay pedidos para eliminar."
                    : eliminados + (eliminados == 1
                            ? " pedido eliminado. El stock fue actualizado."
                            : " pedidos eliminados. El stock fue actualizado.");
            Navegador.alerta("Vaciar pedidos", mensaje,
                    javafx.scene.control.Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarError("No se pudieron eliminar los pedidos: " + e.getMessage());
        }
    }

    @FXML
    private void filtroTodos() {

        seleccionarFiltro(btnFiltroTodos);

        mostrarProductos(listaProductos);
    }

    @FXML
    private void filtroRostro() {

        seleccionarFiltro(btnFiltroRostro);

        filtrarPorCategoria("Rostro");
    }

    @FXML
    private void filtroLabios() {

        seleccionarFiltro(btnFiltroLabios);

        filtrarPorCategoria("Labios");
    }

    @FXML
    private void filtroOjos() {

        seleccionarFiltro(btnFiltroOjos);

        filtrarPorCategoria("Ojos");
    }

    @FXML
    private void filtroBrochas() {

        seleccionarFiltro(btnFiltroBrochas);

        filtrarPorCategoria("Brochas y accesorios");
    }

    private void filtrarPorCategoria(String categoria) {

        List<Producto> productosFiltrados = new java.util.ArrayList<>();

        for (Producto producto : listaProductos) {

            if (producto.getCategoria() != null
                    && producto.getCategoria().equalsIgnoreCase(categoria)) {

                productosFiltrados.add(producto);
            }
        }

        mostrarProductos(productosFiltrados);
    }

    public void agregarProducto(String nombre, String descripcion, String categoria, String precio, String urlImagen) {

    }

    // Permite buscar los productos por nombre, código
    @FXML
    public void buscarProducto(KeyEvent event) {

        String texto = txtBuscar.getText().trim().toLowerCase();

        List<Producto> encontrados = new java.util.ArrayList<>();

        for (Producto producto : listaProductos) {

            boolean coincideNombre = producto.getNombre() != null
                    && producto.getNombre().toLowerCase().contains(texto);

            boolean coincideCodigo = producto.getCodigo() != null
                    && producto.getCodigo().toLowerCase().contains(texto);

            boolean coincideDescripcion = producto.getDescripcion() != null
                    && producto.getDescripcion().toLowerCase().contains(texto);

            boolean coincideEtiqueta = producto.getEtiquetas() != null
                    && producto.getEtiquetas().stream()
                            .anyMatch(etiqueta -> etiqueta.toLowerCase().contains(texto));

            if (coincideNombre || coincideCodigo
                    || coincideDescripcion || coincideEtiqueta) {

                encontrados.add(producto);
            }
        }

        mostrarProductos(encontrados);

        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            if (encontrados.isEmpty() && !texto.isEmpty()) {
                Navegador.alerta("Buscar producto",
                        "Producto no encontrado \"" + txtBuscar.getText().trim() + "\"",
                        javafx.scene.control.Alert.AlertType.INFORMATION);
            }
        }
    }

    private void verDetalles(Producto producto) {
        try {

            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/vista/vistaAdmin/FichaProducto.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("NO ENCONTRÉ EL FXML, revisa la ruta");
                return;
            }

            javafx.scene.Parent root = loader.load();

            ControladoraFichaProducto ficha = loader.getController();
            ficha.setProducto(producto);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Ficha de producto - " + producto.getNombre());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("No se pudo abrir la ficha: " + e.getMessage());
        }
    }

    private void editarProducto(Producto producto) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/vista/vistaAdmin/EditarProducto.fxml"));
            javafx.scene.Parent root = loader.load();

            ControladoraEditarProducto ctrl = loader.getController();
            ctrl.setProducto(producto, () -> cargarProductos());

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Editar producto - " + producto.getNombre());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarProducto(Producto producto) {
        javafx.scene.control.Alert confirmar = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Hada Beauty");
        confirmar.setHeaderText(null);
        confirmar.setContentText("¿Eliminar " + producto.getNombre() + "?");

        if (confirmar.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            try {

                productoDAO.eliminar(producto.getCodigo());
                cargarProductos();
            } catch (Exception e) {
                mostrarError("No se pudo eliminar: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {

        javafx.scene.control.Alert alerta
                = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR
                );

        alerta.setTitle("Hada Beauty");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
