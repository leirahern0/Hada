/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

import datos.ProductoDAO;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import modeloProductos.Producto;

public class ControladoraNuevoProducto {
    @FXML private ImageView vistaImagen;
    private File fotoElegida;
    private String nombreProd;
    private String codigoProd;
    private String marcaProd;
    private double precioProd;
    private int stockProd;
    private String descripcionProd;
    private String imagenProd;
    private List<String> etiquetasProd = new ArrayList<>();
    ProductoDAO productoDAO = new ProductoDAO();
    @FXML private TextField txtNombreProd;
    @FXML private TextField txtCodigoProd;
    @FXML private TextField txtMarcaProd;
    @FXML private TextField txtPrecioProd;
    @FXML private TextField txtStockProd;
    @FXML private TextField txtDescripcionProd;
    @FXML private TextField txtEtiquetasProd;
    @FXML private Button agregarEtiqueta;
    @FXML private FlowPane pnlEtiquetas;
    
    
    @FXML

    void initialize() {
    txtEtiquetasProd.setOnAction(e -> agregarEtiqueta());

}

    
    
    
    @FXML
    private void volver() {
        
    }
    
    @FXML
    private HBox crearBotonEtiqueta(String texto) {
    Label lbl = new Label(texto);
    lbl.setStyle("-fx-text-fill: #5b2c6f; -fx-font-weight: bold; -fx-font-size: 12px;");

    Button btnX = new Button("✕");
    btnX.setStyle("-fx-background-color: transparent; -fx-text-fill: #5b2c6f; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 2 0 2;");
    btnX.setOnAction(e -> {
        etiquetasProd.remove(texto);
        pnlEtiquetas.getChildren().remove(btnX.getParent());
    });

    HBox chip = new HBox(6, lbl, btnX);
    chip.setAlignment(Pos.CENTER);
    chip.setStyle(
        "-fx-background-color: #F3E8FF;" +
        "-fx-background-radius: 20;" +
        "-fx-border-color: #D8B4FE;" +
        "-fx-border-radius: 20;" +
        "-fx-padding: 5 10 5 10;"
    );
    return chip;

    }
    
    @FXML

    private void agregarEtiqueta() {
    
            String texto = txtEtiquetasProd.getText().trim().toLowerCase();
    
            if (texto.isEmpty()) return;

    
    if (etiquetasProd.contains(texto)) {
        mostrarError("Esa etiqueta ya está agregada");
        txtEtiquetasProd.clear();
        return;
    }

    
    if (!texto.matches("[a-záéíóúñ ]+")) {
        mostrarError("Solo letras, sin números ni símbolos");
        return;
    }

    etiquetasProd.add(texto);
    pnlEtiquetas.getChildren().add(crearBotonEtiqueta(texto));
    txtEtiquetasProd.clear();
    txtEtiquetasProd.requestFocus(); 
}


    @FXML
    void elegirFotoVistaImagen() {
        
    FileChooser chooser = new FileChooser();
    chooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
    );
    File archivo = chooser.showOpenDialog(vistaImagen.getScene().getWindow());
    
     if (archivo != null) {
        fotoElegida = archivo;
        vistaImagen.setImage(new Image(archivo.toURI().toString()));
     }

    }
    
    private String NombreImagenParaGuardar() throws IOException {
        
    if (fotoElegida != null) {
        String nombreFoto = "prod_" + System.currentTimeMillis() + ".jpg";
        File carpeta = new File("imagenes/productos");
        if (!carpeta.exists()) carpeta.mkdirs();
        Files.copy(fotoElegida.toPath(), new File(carpeta, nombreFoto).toPath(),
                   StandardCopyOption.REPLACE_EXISTING);
        return nombreFoto;
    }
    return "sin-foto.png";
}

    
    @FXML
    private void crearProducto() {
        
        try{
        nombreProd = txtNombreProd.getText().trim();
        codigoProd = txtCodigoProd.getText().trim();
        precioProd = Double.parseDouble(txtPrecioProd.getText().trim());
        marcaProd = txtMarcaProd.getText().trim();
        stockProd = Integer.parseInt(txtStockProd.getText().trim());
        imagenProd = NombreImagenParaGuardar();
        descripcionProd = txtDescripcionProd.getText().trim();
      
        Producto nuevo = new Producto (nombreProd, codigoProd, precioProd, 
                marcaProd, stockProd, imagenProd, descripcionProd, etiquetasProd);
        productoDAO.guardar(nuevo);
        //mostrar mensaje de éxito
        //falta validar si el producto es duplicado
        //falta darle formato al código
        }
        
        catch (NumberFormatException e) {
            mostrarError("Precio y Stock deben ser números. Ejemplo: Precio 15000, Stock 10");

        } catch (IOException e) {
            mostrarError("No se pudo guardar la imagen: " + e.getMessage());

        } catch (Exception e) {
            mostrarError("Error al crear el producto: " + e.getMessage());

        }
        
          
    }

    private void mostrarError(String mensaje) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Nuevo producto");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();

    }
    
    private void cambiarPantalla (String ruta){
        
    }
    
    
    
    
}
