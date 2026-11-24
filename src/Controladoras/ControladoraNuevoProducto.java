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
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import modeloProductos.Producto;

public class ControladoraNuevoProducto {

    private ControladoraVentana admin;

    public void setAdmin(ControladoraVentana admin) {
        this.admin = admin;
    }

    @FXML
    private ImageView vistaImagen;
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

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtEtiqueta;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private CheckBox chkDisponible;
    @FXML
    private Button btnSeleccionarFoto;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnCrearProducto;
    @FXML
    private HBox contenedorEtiquetas;
    @FXML
    private Button btnAgregarEt;

    @FXML
    private void initialize() {

        cmbCategoria.getItems().addAll(
                "Rostro",
                "Labios",
                "Ojos",
                "Brochas y accesorios"
        );

        txtEtiqueta.setOnAction(e -> agregarEtiqueta());

    }

    @FXML
    private void volver() {
        if (admin != null) {
            admin.mostrarProductos();
        }
    }

    @FXML
    private HBox crearBotonEtiqueta(String texto) {

        Label lbl = new Label(texto);

        lbl.setStyle(
                "-fx-text-fill: #5b2c6f;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 12px;"
        );

        Button btnX = new Button("✕");

        btnX.setStyle(
                "-fx-background-color: transparent;"
                + "-fx-text-fill: #5b2c6f;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
                + "-fx-padding: 0 2 0 2;"
        );

        HBox chip = new HBox(6, lbl, btnX);

        chip.setAlignment(Pos.CENTER);

        chip.setStyle(
                "-fx-background-color: #F3E8FF;"
                + "-fx-background-radius: 20;"
                + "-fx-border-color: #D8B4FE;"
                + "-fx-border-radius: 20;"
                + "-fx-padding: 5 10 5 10;"
        );

        btnX.setOnAction(e -> {
            etiquetasProd.remove(texto);
            contenedorEtiquetas.getChildren().remove(chip);
        });

        return chip;
    }

    @FXML
    private void agregarEtiqueta() {

        String texto = txtEtiqueta.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            mostrarError("Escribe una etiqueta antes de agregarla.");
            return;
        }

        String[] nuevasEtiquetas = texto.split(",");
        List<String> etiquetasNuevas = new ArrayList<>();

        for (String etiqueta : nuevasEtiquetas) {

            String etiquetaLimpia = etiqueta.trim();

            if (etiquetaLimpia.isEmpty()) {
                mostrarError("No puedes agregar etiquetas vacías.");
                return;
            }

            if (!etiquetaLimpia.matches("[a-záéíóúñ ]+")) {
                mostrarError("Las etiquetas solo pueden contener letras.");
                return;
            }

            if (etiquetasProd.contains(etiquetaLimpia)
                    || etiquetasNuevas.contains(etiquetaLimpia)) {
                mostrarError("La etiqueta '" + etiquetaLimpia + "' ya está agregada.");
                return;
            }

            etiquetasNuevas.add(etiquetaLimpia);
        }

        for (String etiqueta : etiquetasNuevas) {
            etiquetasProd.add(etiqueta);
            contenedorEtiquetas.getChildren().add(crearBotonEtiqueta(etiqueta));
        }

        txtEtiqueta.clear();
        txtEtiqueta.requestFocus();
    }

    private boolean validarFormatoCodigo(String codigo) {
        return codigo.matches("^[A-Z]{3}-[0-9]{3}$");
    }

    @FXML
    private void elegirFotoVistaImagen() {

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Seleccionar fotografía del producto");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imágenes",
                        "*.png", "*.jpg", "*.jpeg"
                )
        );

        File carpetaInicial = new File(System.getProperty("user.home"));

        if (carpetaInicial.exists()) {
            chooser.setInitialDirectory(carpetaInicial);
        }

        File archivo = chooser.showOpenDialog(
                btnSeleccionarFoto.getScene().getWindow()
        );

        if (archivo != null) {

            fotoElegida = archivo;

            Image imagen = new Image(archivo.toURI().toString());

            vistaImagen.setImage(imagen);

            btnSeleccionarFoto.setText(
                    "Fotografía seleccionada: " + archivo.getName()
            );
        }
    }

    private String NombreImagenParaGuardar() throws IOException {

        if (fotoElegida != null) {
            String nombreFoto = "prod_" + System.currentTimeMillis() + ".jpg";
            //dejamos el metodo currentTimeMillis o creamos un método que no dé números aleatorios
            File carpeta = new File("imagenes/productos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            Files.copy(fotoElegida.toPath(), new File(carpeta, nombreFoto).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            return nombreFoto;
        }
        return "sin-foto.png";
    }

    @FXML
    private void crearProducto() {

        try {

            nombreProd = txtNombre.getText().trim();
            codigoProd = txtCodigo.getText().trim();
            marcaProd = txtMarca.getText().trim();
            descripcionProd = txtDescripcion.getText().trim();

            if (nombreProd.isEmpty() || codigoProd.isEmpty()
                    || marcaProd.isEmpty() || descripcionProd.isEmpty()
                    || txtPrecio.getText().trim().isEmpty()
                    || txtStock.getText().trim().isEmpty()
                    || cmbCategoria.getValue() == null) {

                mostrarError("Completa todos los campos obligatorios.");
                return;
            }

            if (!validarFormatoCodigo(codigoProd)) {
                mostrarError("Este código no cumple con el formato. "
                        + "Debe cumplir con el formato de código: ABC-123.");
                return;
            }

            if (productoDAO.buscarPorCodigo(codigoProd) != null) {
                mostrarError("Ya existe un producto registrado con ese código.");
                return;
            }

            precioProd = Double.parseDouble(txtPrecio.getText().trim());
            stockProd = Integer.parseInt(txtStock.getText().trim());

            if (precioProd <= 0) {
                mostrarError("El precio debe ser mayor que cero.");
                return;
            }

            if (stockProd < 0) {
                mostrarError("El stock no puede ser negativo.");
                return;
            }

            if (stockProd == 0 && chkDisponible.isSelected()) {
                mostrarError("No puedes marcar un producto como disponible si su stock es cero.");
                return;
            }

            String categoriaProd = cmbCategoria.getValue();
            boolean disponibleProd = chkDisponible.isSelected();

            imagenProd = NombreImagenParaGuardar();

            Producto nuevo = new Producto(
                    nombreProd,
                    codigoProd,
                    precioProd,
                    marcaProd,
                    stockProd,
                    imagenProd,
                    descripcionProd,
                    etiquetasProd
            );

            nuevo.setCategoria(cmbCategoria.getValue());
            nuevo.setDisponible(chkDisponible.isSelected());

            productoDAO.guardar(nuevo);

            mostrarMensaje("Producto creado correctamente");

            if (admin != null) {
                admin.mostrarProductos();
            }

        } catch (NumberFormatException e) {

            mostrarError("Precio y stock deben ser números válidos.");

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

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hada Beauty");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cambiarPantalla(String ruta) {

    }

}
