package Controladoras.EntornoAdmin;

import datos.DatosAdministrador;
import Controladoras.EntornoAdmin.ControladoraPerfilAdministrador;
import Controladoras.EntornoAdmin.ControladoraNuevoProducto;
import Controladoras.EntornoAdmin.ControladoraGestionProductos;
import Controladoras.controladoraTienda;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ControladoraVentanaAdmin {

    @FXML
    private Label lblAdmin;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnProductos;
    @FXML
    private Button btnPedidos;
    @FXML
    private Button btnMiPerfil;
    @FXML
    private Button btnTienda;
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
            marcarSeccionActiva(btnProductos);
        }
    }

    public void mostrarNuevoProducto() {
        Object controlador = cargarVista("vistaAdmin/AgregarProducto.fxml");
        if (controlador instanceof ControladoraNuevoProducto nuevo) {
            nuevo.setAdmin(this);
            marcarSeccionActiva(btnProductos);
        }
    }

    public void mostrarTienda() {
        Object controlador = cargarVista("tienda.fxml");
        if (controlador instanceof controladoraTienda tienda) {
            tienda.setAdmin(this);
            marcarSeccionActiva(btnTienda);
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
    private void irPedidos() {
        Object controlador = cargarVista("vistaAdmin/GestionPedidos.fxml");
        if (controlador instanceof ControladoraGestionPedidos) {
            marcarSeccionActiva(btnPedidos);
        }
    }

    @FXML
    private void irUsuarios() {

    }

    @FXML
    void irMiPerfil() {
        try {
            DatosAdministrador.cargar(); // Lee el txt

            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/vista/vistaAdmin/PerfilAdministrador.fxml"));
            javafx.scene.Parent vista = loader.load();

            ControladoraPerfilAdministrador ctrl = loader.getController();
            ctrl.setDatos(
                    DatosAdministrador.nombre,
                    DatosAdministrador.correo,
                    DatosAdministrador.telefono,
                    DatosAdministrador.documento,
                    DatosAdministrador.ciudad,
                    DatosAdministrador.rol,
                    DatosAdministrador.identificador
            );

            contenedorVistas.getChildren().clear();
            contenedorVistas.getChildren().add(vista);
            marcarSeccionActiva(btnMiPerfil);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void verTienda() {
        mostrarTienda();
    }

    private void marcarSeccionActiva(Button activa) {
        Button[] botones = {btnProductos, btnPedidos, btnMiPerfil, btnTienda};

        for (Button boton : botones) {
            if (boton == null) {
                continue;
            }

            boolean seleccionada = boton == activa;
            String fondo = seleccionada ? "#ffffff" : "transparent";
            String grosor = seleccionada ? "bold" : "normal";

            boton.setStyle(
                    "-fx-background-color: " + fondo + ";"
                    + "-fx-background-radius: 10;"
                    + "-fx-font-family: Arial;"
                    + "-fx-font-size: 12px;"
                    + "-fx-font-weight: " + grosor + ";"
                    + "-fx-text-fill: #8f294d;"
                    + "-fx-padding: 10 12;"
                    + "-fx-cursor: hand;"
            );
        }
    }

    @FXML
    private void cerrarSesion() {

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar sesión");
        confirmacion.setHeaderText("¿Cerrar sesión?");
        confirmacion.setContentText("¿Estás segura que deseas salir de tu cuenta de Hada Beauty?");

        ButtonType si = new ButtonType("Sí, cerrar sesión", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(si, no);

        if (confirmacion.showAndWait().orElse(no) != si) {
            return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Inicio.fxml"));
            Stage escenario = (Stage) contenedorVistas.getScene().getWindow();

            double alto = Screen.getPrimary().getVisualBounds().getHeight() * 0.90;
            escenario.setScene(new Scene(root, 500, alto));
            escenario.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarProducto() {

    }
}
