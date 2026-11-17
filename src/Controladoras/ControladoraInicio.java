package Controladoras;

import controladorUsuarios.controladorUsuarios;
import modeloPersonas.Usuario;

import java.io.IOException;
import java.util.regex.Pattern;
import datos.UsuarioDAO;
import modeloPersonas.Usuario;
import modeloPersonas.Persona;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Controladoras.ControladoraUsuario;
import javafx.application.Platform;
import modeloPersonas.Administrador;

public class ControladoraInicio {

    @FXML
    private TextField txtCorreoLogin;

    @FXML
    private PasswordField pwdClaveLogin;

    @FXML
    private TextField txtNombreRegistro;

    @FXML
    private TextField txtCorreoRegistro;

    @FXML
    private PasswordField pwdClaveRegistro;

    @FXML
    private PasswordField pwdConfirmarClaveR;

    @FXML
    private AnchorPane apLogin;

    @FXML
    private AnchorPane apRegistro;
    
    private final UsuarioDAO UsuarioDAO = new UsuarioDAO();
    
    private final controladorUsuarios gestionUsuarios = new controladorUsuarios();

    @FXML
    public void initialize() {

        apLogin.setVisible(true);
        apLogin.setManaged(true);

        apRegistro.setVisible(false);
        apRegistro.setManaged(false);
        Platform.runLater(() -> txtCorreoLogin.requestFocus());
    }

    @FXML
    private void abrirRegistro() {

        apLogin.setVisible(false);
        apLogin.setManaged(false);

        apRegistro.setVisible(true);
        apRegistro.setManaged(true);
    }

    @FXML
    private void abrirInicio() {

        apRegistro.setVisible(false);
        apRegistro.setManaged(false);

        apLogin.setVisible(true);
        apLogin.setManaged(true);
    }

    @FXML
    private void registrarUsuario() {

        String nombre = txtNombreRegistro.getText().trim();
        String correo = txtCorreoRegistro.getText().trim();
        String clave = pwdClaveRegistro.getText();
        String confirmarClave = pwdConfirmarClaveR.getText();
        

        if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()) {

            mostrarMensaje(
                    "Campos incompletos",
                    "Debes completar todos los campos.",
                    Alert.AlertType.WARNING
            );

            return;
        }

        if (!Pattern.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                correo)) {

            mostrarMensaje(
                    "Correo invalido",
                    "Ingresa una direccion de correo valida.",
                    Alert.AlertType.WARNING
            );

            return;
        }

        if (!clave.equals(confirmarClave)) {

            mostrarMensaje(
                    "Contraseñas diferentes",
                    "Las contraseñas no coinciden.",
                    Alert.AlertType.WARNING
            );

            return;
        }

        Usuario nuevoUsuario = new Usuario(
                nombre,
                clave,
                "",
                correo,
                ""
        );

        boolean registrado = gestionUsuarios.registro(nuevoUsuario);

        if (!registrado) {

            mostrarMensaje(
                    "Usuario existente",
                    "Ya existe una cuenta con este correo.",
                    Alert.AlertType.WARNING
            );

            return;
        }

        mostrarMensaje(
                "Registro exitoso",
                "Tu cuenta ha sido creada correctamente.",
                Alert.AlertType.INFORMATION
        );

        txtNombreRegistro.clear();
        txtCorreoRegistro.clear();
        pwdClaveRegistro.clear();
        pwdConfirmarClaveR.clear();

        abrirInicio();

        txtCorreoLogin.setText(correo);
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {

    String correo = txtCorreoLogin.getText().trim();
    String clave = pwdClaveLogin.getText().trim();
    Administrador admin = new Administrador();
    String correoAdmin = admin.getCorreo();
    String contraseñaAdmin = admin.getContraseña();

    if (correo.isEmpty() || clave.isEmpty()) {
        mostrarMensaje("Campos incompletos", "Debes ingresar tu correo y contraseña.", Alert.AlertType.WARNING);
        return;
    }

    if (!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", correo)) {
        mostrarMensaje("Correo invalido", "Ingresa un correo electronico valido.", Alert.AlertType.WARNING);
        return;
    }

    if (correo.equalsIgnoreCase(correoAdmin)) {
        if (clave.equals(contraseñaAdmin)) {
            mostrarMensaje("Bienvenida", "Inicio de sesion como administradora.", Alert.AlertType.INFORMATION);
            abrirPantalla("Admin.fxml", event);
        } else {
            mostrarMensaje("Contraseña incorrecta", "La contraseña del administrador es incorrecta.", Alert.AlertType.ERROR);
        }
        return;
    }


    try {
        Usuario usuario = UsuarioDAO.buscarPorCorreo(correo);

        if (usuario == null) {
            mostrarMensaje("Usuario no registrado", "No existe una cuenta con este correo.", Alert.AlertType.WARNING);
            return;
        }

        if (!usuario.getContraseña().equals(clave)) {
            mostrarMensaje("Contraseña incorrecta", "La contraseña ingresada no es correcta.", Alert.AlertType.ERROR);
            return;
        }

        mostrarMensaje("Bienvenida a Hada Beauty", "Has iniciado sesion correctamente.", Alert.AlertType.INFORMATION);
        abrirPantallaUsuario(usuario, event);

    } catch (Exception e) {
        // Si el archivo no existe o está dañado, caes aquí
        mostrarMensaje("Error", "No se pudo leer los usuarios. Intenta de nuevo.", Alert.AlertType.ERROR);
        e.printStackTrace();
    }
    }

    private void abrirPantallaUsuario(Usuario usuario, ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/vista/Usuarios.fxml")
            );

            Parent root = loader.load();

            ControladoraUsuario controlador = loader.getController();

            controlador.setUsuario(usuario);

            Node componente = (Node) event.getSource();

            Stage escenario = (Stage) componente.getScene().getWindow();

            escenario.setScene(new Scene(root));
            escenario.show();

        } catch (IOException | NullPointerException e) {

            e.printStackTrace();

            mostrarMensaje(
                    "Error",
                    "No se pudo abrir la ventana de usuarios.",
                    Alert.AlertType.ERROR
            );
        }
    }

    private void abrirPantalla(String archivo, ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/vista/" + archivo)
            );

            Parent root = loader.load();

            Node componente = (Node) event.getSource();

            Stage escenario
                    = (Stage) componente.getScene().getWindow();

            escenario.setScene(new Scene(root));
            escenario.show();

        } catch (IOException | NullPointerException e) {

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
