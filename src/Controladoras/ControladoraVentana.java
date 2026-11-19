/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import modeloProductos.Producto;

public class ControladoraVentana {

    @FXML
    private Label lblAdmin;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private StackPane contenedorVistas;

    private Object cargarVista(String nombreFxml) {

        try {

            var recurso = getClass().getResource("/vista/" + nombreFxml);

            if (recurso == null) {
                throw new IOException(
                        "No se encontró el archivo: /vista/" + nombreFxml
                );
            }

            FXMLLoader loader = new FXMLLoader(recurso);

            Node vista = loader.load();

            contenedorVistas.getChildren().clear();
            contenedorVistas.getChildren().add(vista);

            return loader.getController();

        } catch (Exception e) {

            e.printStackTrace();

            javafx.scene.control.Alert alerta
                    = new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.ERROR
                    );

            alerta.setTitle("Error al abrir la ventana");
            alerta.setHeaderText("No se pudo cargar " + nombreFxml);
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();

            return null;
        }
    }

    @FXML
    void initialize() {
        mostrarProductos();
    }

    public void mostrarProductos() {
        Object controlador = cargarVista("vistaAdmin/GestionProductos.fxml");
        if (controlador instanceof ControladoraGestionProductos gestion) {
            gestion.setAdmin(this);
        }
    }

    public void mostrarNuevoProducto() {
        Object controlador = cargarVista("vistaAdmin/AgregarProducto.fxml");
        if (controlador instanceof ControladoraNuevoProducto nuevo) {
            nuevo.setAdmin(this);
        }
    }
    
    public void mostrarTienda() {
    Object controlador = cargarVista("vistaAdmin/tiendaAdmin.fxml");
    if (controlador instanceof controladoraTiendaAdmin tienda) {
        tienda.setAdmin(this);
    }
}

    @FXML
    private void irProductos() {
        mostrarProductos();
    }

    @FXML
    private void nuevoProducto() {
        mostrarNuevoProducto();
    }

    @FXML
    private void irInicio() {

    }

    @FXML
    private void irPedidos() {

    }

    @FXML
    private void irUsuarios() {

    }

    @FXML
    private void irMiPerfil() {

    }

    @FXML
    private void verTienda() {
    mostrarTienda();
    }

    @FXML
    private void cerrarSesion() {

    }

    @FXML
    private void buscarProducto() {

    }
}
