
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

import datos.ProductoDAO;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modeloProductos.Producto;

public class controladoraTiendaAdmin {
    
    
 private ProductoDAO productoDAO = new ProductoDAO();
    private ControladoraVentana admin;
    
private List<Producto> listaProductos;
private String categoriaActual = null; 

    @FXML
    private FlowPane contenedorProductos;

    public void setAdmin(ControladoraVentana admin) {
        this.admin = admin;
    }

    @FXML
    void initialize() {
        cargarProductos();
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
    
@FXML private void filtroLabios(){ 
    filtrarPorCategoria("Labios"); 
}
@FXML private void filtroRostro(){ 
    filtrarPorCategoria("Rostro"); 
}
@FXML private void filtroOjos(){ 
    filtrarPorCategoria("Ojos"); 
}
@FXML private void filtroBrochas(){ 
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
        

        recuadro.getChildren().addAll(imagen, nombre, descripcion, precio, btnVer);
        return recuadro;
    }

    private String formatearPrecio(double precio) {
        NumberFormat formato = NumberFormat.getInstance(Locale.forLanguageTag("es-CO"));
        formato.setMaximumFractionDigits(0);
        return formato.format(precio);
    }
    
}
