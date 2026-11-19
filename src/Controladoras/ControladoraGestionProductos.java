/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

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
    private List<Producto> listaProductos;

    @FXML
    private VBox contenedorProductos;
    @FXML
    private Label lblContador;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnFiltroTodos, btnFiltroRostro, btnFiltroLabios, btnFiltroOjos, btnFiltroBrochas;

    private ControladoraAdmin admin;

    @FXML
    private void initialize() {
        cargarProductos();

        seleccionarFiltro(btnFiltroTodos);
    }

//PERMITE CAMBIAR DE COLOR LOS BOTONES DE FILTRO DEPENDIENDO DE CUAL ESTÁ SELECCIONADO
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

    // ESTE MÉTODO LIMPIA LAS FILAS ANTERIORES PARA EVITAR DUPLICADOS, RECORRE LOS PRODUCTOS QUE RECUUPERA DEL ARCHIVO TXT, COLOCA LA INFORMACIÓN DEL PRODUCTO POR COLUMNA Y ACTUALIZA EL CONTADOR. <3
    private void cargarProductos() {

        try {
            listaProductos = productoDAO.listar();
            mostrarProductos(listaProductos);

        } catch (Exception e) {
            mostrarError("No se pudieron cargar los productos: " + e.getMessage());
        }
    }

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

            // Columnas de la fila
            fila.getColumnConstraints().addAll(
                    new javafx.scene.layout.ColumnConstraints(65),
                    new javafx.scene.layout.ColumnConstraints(300),
                    new javafx.scene.layout.ColumnConstraints(125),
                    new javafx.scene.layout.ColumnConstraints(100),
                    new javafx.scene.layout.ColumnConstraints(150)
            );

            // Imagen
            ImageView imagen = new ImageView();
            imagen.setFitWidth(48);
            imagen.setFitHeight(48);
            imagen.setPreserveRatio(true);

            File archivoImagen = new File(
                    "imagenes/productos",
                    producto.getImagen()
            );

            if (archivoImagen.exists()) {
                imagen.setImage(new Image(archivoImagen.toURI().toString()));
            }

            fila.add(imagen, 0, 0);

            // Nombre y descripción
            VBox informacion = new VBox(4);

            Label nombre = new Label(producto.getNombre());
            nombre.setStyle(
                    "-fx-font-weight: bold;"
                    + "-fx-text-fill: #8f294d;"
                    + "-fx-font-size: 12;"
            );

            Label descripcion = new Label(producto.getDescripcion());
            descripcion.setWrapText(true);
            descripcion.setStyle(
                    "-fx-text-fill: #9b717b;"
                    + "-fx-font-size: 10;"
            );

            informacion.getChildren().addAll(nombre, descripcion);
            fila.add(informacion, 1, 0);

            // Categoría
            Label categoria = new Label(producto.getCategoria());
            categoria.setStyle("-fx-text-fill: #713249;");
            fila.add(categoria, 2, 0);

            // Precio
            Label precio = new Label("$ " + producto.getPrecio());
            precio.setStyle(
                    "-fx-text-fill: #8f294d;"
                    + "-fx-font-weight: bold;"
            );
            fila.add(precio, 3, 0);

            // Acciones (las conectaremos después eliminar y editar: pendientes)
            Button btnEliminar = new Button("🗑 Eliminar");

            btnEliminar.setStyle(
                    "-fx-background-color: #ffc6ad;"
                    + "-fx-text-fill: #8f294d;"
                    + "-fx-background-radius: 8;"
                    + "-fx-cursor: hand;"
            );

            Label acciones = new Label("—");
            fila.add(acciones, 4, 0);

            contenedorProductos.getChildren().add(fila);
        }

        lblContador.setText(
                "Mostrando " + productos.size() + " productos"
        );
    }

    public void setAdmin(ControladoraAdmin admin) {
        this.admin = admin;
    }

    @FXML
    private void nuevoProducto() {
        if (admin != null) {
            admin.mostrarNuevoProducto();
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
//PERMITE BUSCAR LOS PRODUCTOS POR NOMBRE, CÓDIGO 

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
    }

    //FALTA AGREGAR LOS BOTONES DE ACCIONES: EDITAR O ELIMINAR PRODUCTO DE LA TABLA 
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
