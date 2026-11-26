package Controladoras.EntornoAdmin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import modeloProductos.Producto;
import java.io.File;

public class ControladoraFichaProducto {

    @FXML
    private Label lblTituloProducto;
    @FXML
    private Label lblNombre, lblCodigo, lblMarca, lblCategoria, lblDescripcion;
    @FXML
    private Label lblPrecio, lblPrecioOferta, lblStock, lblDisponible;
    @FXML
    private Label lblImagenUrl, lblColor, lblEtiquetas;
    @FXML
    private ImageView imgProducto;
    @FXML
    private Button btnCerrar;

    public void setProducto(Producto p) {
        lblTituloProducto.setText(texto(p.getNombre()));
        lblNombre.setText(texto(p.getNombre()));
        lblCodigo.setText(texto(p.getCodigo()));
        lblCategoria.setText(texto(p.getCategoria()));
        lblDescripcion.setText(texto(p.getDescripcion()));
        lblPrecio.setText("$ " + p.getPrecio());
        lblImagenUrl.setText(texto(p.getImagen()));
        lblMarca.setText(texto(p.getMarca()));
        lblPrecioOferta.setText("Sin registrar");
        lblStock.setText(String.valueOf(p.getStock()));
        lblDisponible.setText(p.isDisponible() ? "Sí" : "No");
        lblColor.setText("Sin registrar");

        if (p.getEtiquetas() != null && !p.getEtiquetas().isEmpty()) {
            lblEtiquetas.setText(String.join(", ", p.getEtiquetas()));
            lblEtiquetas.setStyle("-fx-background-color: #FFF7F7; -fx-background-radius: 8; -fx-padding: 10; -fx-text-fill: black;");
        } else {
            lblEtiquetas.setText("Sin registrar");
        }

        if (p.getImagen() != null && !p.getImagen().isBlank()) {
            File f = new File("imagenes/productos", p.getImagen());
            if (f.exists()) {
                imgProducto.setImage(new Image(f.toURI().toString()));
            }
        }
    }

    private String texto(String v) {
        return (v == null || v.isBlank()) ? "Sin registrar" : v;
    }

    @FXML
    private void cerrar() {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }
}
