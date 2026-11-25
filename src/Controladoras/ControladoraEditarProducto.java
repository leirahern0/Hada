package Controladoras;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modeloProductos.Producto;
import datos.ProductoDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ControladoraEditarProducto {

    @FXML
    private Label lblVolver;
    @FXML
    private TextField txtNombre, txtCodigo, txtMarca, txtPrecio, txtStock, txtUrlImagen, txtEtiquetas;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private CheckBox chkDisponible;
    @FXML
    private Button btnSeleccionarFoto, btnAgregarEtiqueta, btnCancelar, btnGuardar;
    @FXML
    private ScrollPane scrollEtiquetas;
    @FXML
    private HBox hboxEtiquetas;

    private Producto producto;
    private Runnable alGuardar;
    private List<String> etiquetasActuales = new ArrayList<>();
    private String rutaFotoSeleccionada;
    private ProductoDAO productoDAO = new ProductoDAO();

    @FXML
    void initialize() {
        cmbCategoria.getItems().addAll("Rostro", "Labios", "Ojos", "Piel", "Uñas", "Cabello", "Brochas y accesorios");
    }

    //Este método recibe el producto que seleccionamos para editar y lo asigna a los campos para que sea visible
    public void setProducto(Producto p, Runnable alGuardar) {
        this.producto = p;
        this.alGuardar = alGuardar;

        txtNombre.setText(valor(p.getNombre()));
        txtCodigo.setText(valor(p.getCodigo()));
        txtMarca.setText(valor(p.getMarca()));
        txtDescripcion.setText(valor(p.getDescripcion()));
        txtUrlImagen.setText(valor(p.getImagen()));

        txtPrecio.setText(String.valueOf(p.getPrecio()));

        try {
            txtStock.setText(String.valueOf(p.getStock()));
        } catch (Exception e) {
            txtStock.setText("");
        }
        try {
            chkDisponible.setSelected(p.isDisponible());
        } catch (Exception e) {
            chkDisponible.setSelected(true);
        }

        if (p.getCategoria() != null) {
            if (!cmbCategoria.getItems().contains(p.getCategoria())) {
                cmbCategoria.getItems().add(p.getCategoria());
            }
            cmbCategoria.setValue(p.getCategoria());
        }

        if (p.getEtiquetas() != null) {
            etiquetasActuales = new ArrayList<>(p.getEtiquetas());
            txtEtiquetas.setText(String.join(", ", etiquetasActuales));
            mostrarEtiquetas();
        }
    }

    private String valor(String s) {
        return s == null ? "" : s;
    }

    @FXML
    void volver() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cambiarFotoVistaImagen() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File f = fc.showOpenDialog(btnSeleccionarFoto.getScene().getWindow());
        if (f != null) {
            rutaFotoSeleccionada = f.getName();
            txtUrlImagen.setText(f.getName());
        }
    }

    @FXML
    void etiquetas() {
        if (!txtEtiquetas.getText().isBlank()) {
            String[] partes = txtEtiquetas.getText().split("\\s*,\\s*");
            for (String e : partes) {
                if (!e.isBlank() && !etiquetasActuales.contains(e.trim())) {
                    etiquetasActuales.add(e.trim());
                }
            }
            mostrarEtiquetas();
            txtEtiquetas.clear();
            txtEtiquetas.setPromptText("Etiqueta agregada");
        }
    }

    private void mostrarEtiquetas() {
        hboxEtiquetas.getChildren().clear();
        for (String e : etiquetasActuales) {
            Label chip = new Label(e + "  x");
            chip.setStyle("-fx-background-color: #F0658C; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 4 10; -fx-font-size: 11; -fx-cursor: hand;");
            chip.setOnMouseClicked(ev -> {
                etiquetasActuales.remove(e);
                mostrarEtiquetas();
            });
            hboxEtiquetas.getChildren().add(chip);
        }
    }

    @FXML
    void guardarCambios() {
        try {
            producto.setNombre(txtNombre.getText());
            producto.setMarca(txtMarca.getText());
            producto.setCategoria(cmbCategoria.getValue());
            producto.setDescripcion(txtDescripcion.getText());

            String precioLimpio = txtPrecio.getText().replace("$", "").replace(" ", "").trim();
            producto.setPrecio(Double.parseDouble(precioLimpio));

            if (!txtStock.getText().isBlank()) {
                producto.setStock(Integer.parseInt(txtStock.getText().trim()));
            }
            producto.setDisponible(chkDisponible.isSelected());

            if (!txtEtiquetas.getText().isBlank()) {
                etiquetas();
            }
            producto.setEtiquetas(new ArrayList<>(etiquetasActuales));

            if (rutaFotoSeleccionada != null) {
                producto.setImagen(rutaFotoSeleccionada);
            } else if (!txtUrlImagen.getText().isBlank()) {
                producto.setImagen(txtUrlImagen.getText().trim());
            }

            productoDAO.editar(producto);

            if (alGuardar != null) {
                alGuardar.run();
            }
            volver();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Precio y Stock deben ser números").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).showAndWait();
        }
    }
}
