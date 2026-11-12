/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

import modeloPersonas.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControladoraUsuario {

    @FXML
    private Label lblUsuario;

    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {

        this.usuarioActual = usuario;

        if (usuarioActual != null) {
            lblUsuario.setText("Hola, " + usuarioActual.getNombre());
        }
    }

    @FXML
    private void abrirCatalogo() {
        // Pendiente de conectar
    }

    @FXML
    private void abrirFavoritos() {
        // Pendiente de conectar
    }

    @FXML
    private void abrirCarrito() {
        // Pendiente de conectar
    }

    @FXML
    private void abrirPerfil() {
        // Pendiente de conectar
    }

    @FXML
    private void cerrarSesion() {
        // Pendiente de conectar
    }
}
