package Controladoras.EntornoUsuario;

import Controladoras.EntornoUsuario.ConUsuario;
import Controladoras.Navegador;
import datos.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modeloPersonas.Usuario;

public class ControladoraEditarPerfil implements ConUsuario {

    @FXML
    private Label lblIniciales, lblNombreResumen, lblCorreoResumen;
    @FXML
    private TextField txtNombre, txtCorreo, txtTelefono, txtDireccion;
    @FXML
    private PasswordField pwdActual, pwdNueva, pwdConfirmar;

    private Usuario usuario;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblIniciales.setText(Navegador.iniciales(usuario.getNombre()));
        lblNombreResumen.setText(usuario.getNombre());
        lblCorreoResumen.setText(usuario.getCorreo());
        txtNombre.setText(usuario.getNombre());
        txtCorreo.setText(usuario.getCorreo());
        txtTelefono.setText(usuario.getTelefono() == null ? "" : usuario.getTelefono());
        txtDireccion.setText(usuario.getDireccion() == null ? "" : usuario.getDireccion());
    }

    @FXML
    private void volver() {
        Navegador.ir(Navegador.PERFIL, txtNombre, usuario);
    }

    @FXML
    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (nombre.isEmpty()) {
            aviso("Escribe tu nombre completo.");
            return;
        }
        if (nombre.contains("|") || direccion.contains("|")) {
            aviso("No uses el símbolo | en el nombre ni en la dirección.");
            return;
        }
        if (!telefono.isEmpty() && !telefono.matches("\\d{10}")) {
            aviso("El teléfono debe tener 10 dígitos.");
            return;
        }

        boolean cambiaClave = !pwdActual.getText().isEmpty()
                || !pwdNueva.getText().isEmpty()
                || !pwdConfirmar.getText().isEmpty();
        if (cambiaClave) {
            if (!pwdActual.getText().equals(usuario.getContraseña())) {
                aviso("La contraseña actual no es correcta.");
                return;
            }
            if (pwdNueva.getText().length() < 6) {
                aviso("La contraseña nueva debe tener mínimo 6 caracteres.");
                return;
            }
            if (!pwdNueva.getText().equals(pwdConfirmar.getText())) {
                aviso("Las contraseñas nuevas no coinciden.");
                return;
            }
        }

        try {
            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            if (cambiaClave) {
                usuario.setContraseña(pwdNueva.getText());
            }
            usuarioDAO.editar(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            Navegador.alerta("Error", "No se pudieron guardar los cambios: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            return;
        }
        Navegador.alerta("Hada Beauty", "Tu perfil fue actualizado.", Alert.AlertType.INFORMATION);
        Navegador.ir(Navegador.PERFIL, txtNombre, usuario);
    }

    private void aviso(String mensaje) {
        Navegador.alerta("Revisa tus datos", mensaje, Alert.AlertType.WARNING);
    }
}
