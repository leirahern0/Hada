package Controladoras;

import controladorUsuarios.controladorUsuarios;
import modeloPersonas.Usuario;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import datos.UsuarioDAO;

public class ControladoraRegistro {

    @FXML
    private TextField txtNombreCompleto;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private PasswordField txtConfirmarContrasena;

    private controladorUsuarios gestionUsuarios;

    private Runnable accionVolver;

    public void setGestionUsuarios(controladorUsuarios gestion) {
        this.gestionUsuarios = gestion;
    }

    public void setAccionVolver(Runnable accion) {
        this.accionVolver = accion;
    }

    @FXML
    private void registrarUsuario() {

        String nombre = txtNombreCompleto.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText();
        String confirmar = txtConfirmarContrasena.getText();

        if (nombre.isEmpty() || correo.isEmpty()
                || contrasena.isEmpty() || confirmar.isEmpty()) {

            mostrarMensaje(
                    "Campos incompletos",
                    "Debes completar todos los campos.",
                    Alert.AlertType.ERROR
            );
            return;
        }

        if (!contrasena.equals(confirmar)) {

            mostrarMensaje(
                    "Error",
                    "Las contraseñas no coinciden.",
                    Alert.AlertType.ERROR
            );
            return;
        }

        Usuario nuevo = new Usuario();

        nuevo.setNombre(nombre);
        nuevo.setUsuario(correo);
        nuevo.setContraseña(contrasena);

        boolean registrado = gestionUsuarios.registro(nuevo);
        

        if (registrado) {

            mostrarMensaje(
                    "Registro exitoso",
                    "Tu cuenta ha sido creada correctamente.",
                    Alert.AlertType.INFORMATION
            );

            if (accionVolver != null) {
                accionVolver.run();
            }

        } else {

            mostrarMensaje(
                    "Correo registrado",
                    "Ya existe una cuenta con ese correo.",
                    Alert.AlertType.ERROR
            );
        }
    }

    @FXML
    private void volverLogin() {

        if (accionVolver != null) {
            accionVolver.run();
        }
    }

    private void mostrarMensaje(
            String titulo,
            String mensaje,
            Alert.AlertType tipo) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
