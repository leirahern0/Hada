package Controladoras;

import modeloPersonas.Usuario;
import controladorUsuarios.controladorUsuarios;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControladoraLogin {

    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtClave;

    private controladorUsuarios gestionUsuarios = new controladorUsuarios();

    @FXML
    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String clave = txtClave.getText();

        if (correo.isEmpty() || clave.isEmpty()) {

            mostrarMensaje("Debes ingresar tu correo y contraseña.");
            return;
        }

        Usuario usuario = gestionUsuarios.buscarUsuario(correo);

        if (usuario == null) {

            mostrarMensaje("No tienes una cuenta en Hada Beauty. Por favor, regístrate.");
            return;
        }

        if (!usuario.getContraseña().equals(clave)) {

            mostrarMensaje("La contraseña es incorrecta.");
            return;
        }

        mostrarMensaje("¡Bienvenida a Hada Beauty!");

    }

    private void abrirPantalla(String archivo, ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/vista/" + archivo)
            );

            Parent root = loader.load();

            Node componente = (Node) event.getSource();

            Stage escenario = (Stage) componente.getScene().getWindow();

            escenario.setScene(new Scene(root));
            escenario.show();

        } catch (IOException e) {

            e.printStackTrace();

            mostrarMensaje("No se pudo abrir la ventana: " + archivo);
        }
    }

    @FXML
    private void abrirRegistro(ActionEvent event) {
        abrirPantalla("Registro.fxml", event);
    }

    private void mostrarMensaje(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Hada Beauty");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

}
