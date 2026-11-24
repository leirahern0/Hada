package Controladoras;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import modeloPersonas.Usuario;

public class ControladoraMiPerfil implements ConUsuario {

    @FXML
    private Label lblIniciales, lblNombreLateral, lblCorreoLateral,
            lblNombre, lblCorreo, lblTelefono, lblDireccion;

    private Usuario usuario;

    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblIniciales.setText(Navegador.iniciales(usuario.getNombre()));
        lblNombreLateral.setText(usuario.getNombre());
        lblCorreoLateral.setText(usuario.getCorreo());
        lblNombre.setText(usuario.getNombre());
        lblCorreo.setText(usuario.getCorreo());
        lblTelefono.setText(textoOGuion(usuario.getTelefono()));
        lblDireccion.setText(textoOGuion(usuario.getDireccion()));
    }

    private String textoOGuion(String t) {
        return (t == null || t.isBlank()) ? "—" : t;
    }

    @FXML
    private void volverInicio() {
        Navegador.ir(Navegador.INICIO, lblNombre, usuario);
    }

    @FXML
    private void editarPerfil() {
        Navegador.ir(Navegador.EDITAR_PERFIL, lblNombre, usuario);
    }

    @FXML
    private void irMisPedidos() {
        Navegador.ir(Navegador.MIS_PEDIDOS, lblNombre, usuario);
    }

    @FXML
    private void irFavoritos() {
        Navegador.ir(Navegador.FAVORITOS, lblNombre, usuario);
    }

    @FXML
    private void cerrarSesion() {
        Navegador.cerrarSesion(lblNombre);
    }
}
