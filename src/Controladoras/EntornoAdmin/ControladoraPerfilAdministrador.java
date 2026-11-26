package Controladoras.EntornoAdmin;

import datos.DatosAdministrador;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class ControladoraPerfilAdministrador {

    @FXML
    private TextField txtNombre, txtCorreo, txtTelefono, txtDocumento, txtCiudad;
    @FXML
    private Button btnEditarPerfil, btnCancelar, btnGuardar;
    @FXML
    private HBox hboxBotonesEditar;
    @FXML
    private Label lblRolTitulo, lblCuenta, lblIdentificador, lblRol;

    private String nombreOriginal, correoOriginal, telefonoOriginal, documentoOriginal, ciudadOriginal;

    public void setDatos(String nombre, String correo, String telefono, String documento, String ciudad, String rol, String identificador) {
        txtNombre.setText(nombre);
        txtCorreo.setText(correo);
        txtTelefono.setText(telefono);
        txtDocumento.setText(documento);
        txtCiudad.setText(ciudad);
        lblRol.setText(rol);
        lblRolTitulo.setText(rol + " · Hada Beauty");
        lblIdentificador.setText(identificador);
        lblCuenta.setText("Cuenta administrativa: " + identificador);
    }

    @FXML
    void editarPerfil() {
        nombreOriginal = txtNombre.getText();
        correoOriginal = txtCorreo.getText();
        telefonoOriginal = txtTelefono.getText();
        documentoOriginal = txtDocumento.getText();
        ciudadOriginal = txtCiudad.getText();

        setEditable(true);
        btnEditarPerfil.setVisible(false);
        hboxBotonesEditar.setVisible(true);
        hboxBotonesEditar.setManaged(true);
    }

    @FXML
    void cancelarEdicion() {
        txtNombre.setText(nombreOriginal);
        txtCorreo.setText(correoOriginal);
        txtTelefono.setText(telefonoOriginal);
        txtDocumento.setText(documentoOriginal);
        txtCiudad.setText(ciudadOriginal);

        setEditable(false);
        btnEditarPerfil.setVisible(true);
        hboxBotonesEditar.setVisible(false);
        hboxBotonesEditar.setManaged(false);
    }

    @FXML
    void guardarPerfil() {
        DatosAdministrador.nombre = txtNombre.getText();
        DatosAdministrador.correo = txtCorreo.getText();
        DatosAdministrador.telefono = txtTelefono.getText();
        DatosAdministrador.documento = txtDocumento.getText();
        DatosAdministrador.ciudad = txtCiudad.getText();

        DatosAdministrador.guardar();

        setEditable(false);
        btnEditarPerfil.setVisible(true);
        hboxBotonesEditar.setVisible(false);
        hboxBotonesEditar.setManaged(false);
        new Alert(Alert.AlertType.INFORMATION, "Perfil guardado correctamente").showAndWait();
    }

    private void setEditable(boolean editable) {
        String edit = "-fx-background-color: #FFF4E9; -fx-border-color: #F2DDD0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: #8f294d; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 6 8;";
        String lectura = "-fx-background-color: transparent; -fx-text-fill: #8f294d; -fx-font-weight: bold; -fx-font-size: 13px;";
        for (TextField t : new TextField[]{txtNombre, txtCorreo, txtTelefono, txtDocumento, txtCiudad}) {
            t.setEditable(editable);
            t.setStyle(editable ? edit : lectura);
        }
    }
}
