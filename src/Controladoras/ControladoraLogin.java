package Controladoras;

import modeloPersonas.Usuario;
import controladorUsuarios.controladorUsuarios;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ControladoraLogin {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtClave;

    private controladorUsuarios gestionUsuarios
            = new controladorUsuarios();

    private final String ADMIN_CORREO = "admin@hadabeauty.com";
    private final String ADMIN_CLAVE = "Admin123";
    @FXML
    private StackPane contenedorAcceso;

    private Node vistaLogin;

    @FXML
    private void iniciarSesion(ActionEvent event) {

        mostrarMensaje(
                "Prueba",
                "Sesión iniciada, para continuar crea la página de inicio.",
                Alert.AlertType.INFORMATION
        );

        String correo = txtCorreo.getText().trim();
        String clave = txtClave.getText();

        if (correo.isEmpty() || clave.isEmpty()) {
            mostrarMensaje(
                    "Error",
                    "Debes ingresar tu correo y contraseña.",
                    Alert.AlertType.ERROR
            );
            return;
        }

        if (correo.equals(ADMIN_CORREO)
                && clave.equals(ADMIN_CLAVE)) {

            mostrarMensaje(
                    "Bienvenida",
                    "Inicio de sesión como administradora.",
                    Alert.AlertType.INFORMATION
            );

            abrirPantalla("Admin.fxml", event);
            return;
        }

        Usuario usuario = gestionUsuarios.buscarUsuario(correo);

        if (usuario == null) {

            mostrarMensaje(
                    "Usuario no registrado",
                    "No encontramos una cuenta con ese correo. "
                    + "Serás dirigida al registro.",
                    Alert.AlertType.INFORMATION
            );

            abrirRegistro(event);
            return;
        }

        if (!usuario.getContraseña().equals(clave)) {

            mostrarMensaje(
                    "Error",
                    "La contraseña ingresada es incorrecta.",
                    Alert.AlertType.ERROR
            );

            return;
        }

        mostrarMensaje(
                "Bienvenid@ a Hada Beauty",
                "Inicio de sesión exitoso.",
                Alert.AlertType.INFORMATION
        );

        abrirPantalla("Usuarios.fxml", event);;
    }

    @FXML
    private void abrirRegistro(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/vista/Registro.fxml")
            );

            Parent vistaRegistro = loader.load();

            ControladoraRegistro registroController = loader.getController();

            registroController.setGestionUsuarios(gestionUsuarios);

            registroController.setAccionVolver(() -> {
                contenedorAcceso.getChildren().setAll(vistaLogin);
            });

            contenedorAcceso.getChildren().setAll(vistaRegistro);

        } catch (IOException e) {

            e.printStackTrace();

            mostrarMensaje(
                    "Error",
                    "No se pudo cargar el registro.",
                    Alert.AlertType.ERROR
            );
        }
    }

    @FXML
    public void initialize() {
        vistaLogin = contenedorAcceso.getChildren().get(0);
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

            mostrarMensaje(
                    "Error",
                    "No se pudo abrir la ventana: " + archivo,
                    Alert.AlertType.ERROR
            );
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
