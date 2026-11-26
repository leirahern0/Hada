/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras.EntornoAdmin;

import Controladoras.Navegador;
import datos.PedidoDAO;
import datos.PedidoServicio;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import modeloProductos.Pedido;

public class ControladoraGestionPedidos {

    @FXML private Label lblCount;
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbOrdenar;
    @FXML private Button btnTodos;
    @FXML private VBox vboxPedidos;
    @FXML private VBox vboxVacio;

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final PedidoServicio pedidoServicio = new PedidoServicio();
    private final List<Pedido> pedidos = new ArrayList<>();

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy", Locale.forLanguageTag("es-CO"));
    private final DateTimeFormatter formatoHora =
            DateTimeFormatter.ofPattern(
                    "hh:mm a", Locale.forLanguageTag("es-CO"));

    @FXML
    private void initialize() {
        cmbOrdenar.getItems().setAll(
                "Fecha: más reciente",
                "Fecha: más antigua",
                "Precio: mayor a menor",
                "Precio: menor a mayor"
        );
        cmbOrdenar.getSelectionModel().selectFirst();

        txtBuscar.textProperty().addListener(
                (observable, anterior, nuevo) -> cargarRecuadroPedido()
        );
        cmbOrdenar.setOnAction(event -> cargarRecuadroPedido());
        btnTodos.setOnAction(event -> txtBuscar.clear());

        cargarPedidos();
    }

    private void cargarPedidos() {
        try {
            pedidos.clear();
            pedidos.addAll(pedidoDAO.listar());
            cargarRecuadroPedido();
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los pedidos", e);
        }
    }

    @FXML
    private void mostrarTodos() {
        txtBuscar.clear();
    }

    @FXML
    private void ordenar() {
        cargarRecuadroPedido();
    }

    @FXML
    private void vaciarPedidos() {
        if (pedidos.isEmpty()) {
            Navegador.alerta("Vaciar pedidos", "No hay pedidos para eliminar.",
                    Alert.AlertType.INFORMATION);
            return;
        }

        if (!Navegador.confirmar("Vaciar todos los pedidos",
                "Se eliminarán los " + pedidos.size()
                + " pedidos registrados y sus productos volverán al inventario. "
                + "Esta acción no se puede deshacer. ¿Deseas continuar?")) {
            return;
        }

        try {
            int eliminados = pedidoServicio.eliminarTodosLosPedidosYDevolverStock();
            pedidos.clear();
            cargarRecuadroPedido();
            Navegador.alerta("Pedidos eliminados",
                    eliminados + (eliminados == 1
                            ? " pedido eliminado. El stock fue actualizado."
                            : " pedidos eliminados. El stock fue actualizado."),
                    Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarError("No se pudieron vaciar los pedidos", e);
        }
    }

    @FXML
    private void cargarRecuadroPedido() {
        String consulta = txtBuscar.getText() == null
                ? ""
                : txtBuscar.getText().trim().toLowerCase(Locale.ROOT);

        List<Pedido> visibles = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (coincideBusqueda(pedido, consulta)) {
                visibles.add(pedido);
            }
        }

        ordenarLista(visibles);
        vboxPedidos.getChildren().clear();

        if (visibles.isEmpty()) {
            vboxPedidos.getChildren().add(vboxVacio);
        } else {
            for (Pedido pedido : visibles) {
                vboxPedidos.getChildren().add(crearFilaPedido(pedido));
            }
        }

        lblCount.setText(visibles.size()
                + (visibles.size() == 1 ? " pedido" : " pedidos"));
    }

    private boolean coincideBusqueda(Pedido pedido, String consulta) {
        if (consulta.isEmpty()) {
            return true;
        }

        String numero = texto(pedido.getNumeroPedido());
        String nombre = pedido.getCliente() == null
                ? ""
                : texto(pedido.getCliente().getNombre());
        String correo = pedido.getCliente() == null
                ? ""
                : texto(pedido.getCliente().getCorreo());

        return numero.toLowerCase(Locale.ROOT).contains(consulta)
                || nombre.toLowerCase(Locale.ROOT).contains(consulta)
                || correo.toLowerCase(Locale.ROOT).contains(consulta);
    }

    private void ordenarLista(List<Pedido> lista) {
        String seleccion = cmbOrdenar.getValue();

        if ("Fecha: más antigua".equals(seleccion)) {
            lista.sort(Comparator.comparing(
                    Pedido::getFecha,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ));
        } else if ("Precio: mayor a menor".equals(seleccion)) {
            lista.sort((a, b) -> Double.compare(b.getTotal(), a.getTotal()));
        } else if ("Precio: menor a mayor".equals(seleccion)) {
            lista.sort(Comparator.comparingDouble(Pedido::getTotal));
        } else {
            lista.sort((a, b) -> compararFechasDesc(a.getFecha(), b.getFecha()));
        }
    }

    private int compararFechasDesc(LocalDateTime a, LocalDateTime b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;
        if (b == null) return -1;
        return b.compareTo(a);
    }

    private HBox crearFilaPedido(Pedido pedido) {
        HBox fila = new HBox();
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setSpacing(0);
        fila.setStyle("-fx-background-color: white;"
                + "-fx-border-color: #F3E6DA;"
                + "-fx-border-width: 0 0 1 0;"
                + "-fx-padding: 14 24 14 24;");

        CheckBox check = new CheckBox();
        check.setMinWidth(30);
        check.setPrefWidth(30);
        check.setMaxWidth(30);
        check.setPadding(Insets.EMPTY);

        VBox colPedido = new VBox(2);
        colPedido.setMinWidth(90);
        colPedido.setPrefWidth(90);

        // El número ya contiene "HB-", así que solo se antepone "#".
        Label lblId = new Label("#" + texto(pedido.getNumeroPedido()));
        lblId.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;"
                + "-fx-text-fill: #7A1E3A;");

        Label lblCantidad =
                new Label(pedido.cantidadProductos() + " productos");
        lblCantidad.setStyle("-fx-font-size: 11px; -fx-text-fill: #8A7A7A;");
        colPedido.getChildren().addAll(lblId, lblCantidad);

        String nombre = pedido.getCliente() == null
                ? "Cliente no disponible"
                : texto(pedido.getCliente().getNombre());
        String correo = pedido.getCliente() == null
                ? "—"
                : texto(pedido.getCliente().getCorreo());

        HBox colCliente = new HBox(10);
        colCliente.setMinWidth(170);
        colCliente.setPrefWidth(170);
        colCliente.setAlignment(Pos.CENTER_LEFT);

        Circle avatar = new Circle(20, Color.web("#7A1E3A"));
        Label iniciales = new Label(getIniciales(nombre));
        iniciales.setStyle("-fx-text-fill: white; -fx-font-size: 11px;"
                + "-fx-font-weight: bold;");
        StackPane stackAvatar = new StackPane(avatar, iniciales);

        VBox datosCliente = new VBox(2);
        datosCliente.setMaxWidth(120);
        Label lblNombre = new Label(nombre);
        lblNombre.setWrapText(true);
        lblNombre.setStyle("-fx-font-size: 13px; -fx-text-fill: #2B2B2B;");
        Label lblCorreo = new Label(correo);
        lblCorreo.setWrapText(true);
        lblCorreo.setStyle("-fx-font-size: 11px; -fx-text-fill: #8A7A7A;");
        datosCliente.getChildren().addAll(lblNombre, lblCorreo);
        colCliente.getChildren().addAll(stackAvatar, datosCliente);

        VBox colFecha = new VBox(2);
        colFecha.setMinWidth(90);
        colFecha.setPrefWidth(90);

        if (pedido.getFecha() == null) {
            colFecha.getChildren().add(new Label("—"));
        } else {
            Label fecha = new Label(pedido.getFecha().format(formatoFecha));
            fecha.setStyle("-fx-font-size: 13px; -fx-text-fill: #2B2B2B;");
            Label hora = new Label(pedido.getFecha().format(formatoHora));
            hora.setStyle("-fx-font-size: 11px; -fx-text-fill: #8A7A7A;");
            colFecha.getChildren().addAll(fecha, hora);
        }

        Label lblTotal = new Label(Navegador.precio(pedido.getTotal()));
        lblTotal.setMinWidth(90);
        lblTotal.setPrefWidth(90);
        lblTotal.setStyle("-fx-font-size: 13px; -fx-text-fill: #2B2B2B;");

        VBox colPago = new VBox(4);
        colPago.setMinWidth(90);
        colPago.setPrefWidth(90);
        colPago.getChildren().add(etiqueta(texto(pedido.getMetodoPago())));

        VBox colEnvio = new VBox(4);
        colEnvio.setMinWidth(97);
        colEnvio.setPrefWidth(97);
        colEnvio.getChildren().add(
                etiqueta(texto(pedido.getDireccionEntrega()))
        );

        HBox colAcciones = new HBox(6);
        colAcciones.setMinWidth(112);
        colAcciones.setPrefWidth(112);
        colAcciones.setAlignment(Pos.CENTER_LEFT);

        Button btnVer = crearBotonAccion("Ver");
        btnVer.setPrefWidth(42);
        btnVer.setMinWidth(42);
        btnVer.setOnAction(event -> verPedido(pedido));

        Button btnEliminar = crearBotonAccion("Eliminar");
        btnEliminar.setPrefWidth(62);
        btnEliminar.setMinWidth(62);
        btnEliminar.setOnAction(event -> eliminarPedido(pedido));

        colAcciones.getChildren().addAll(btnVer, btnEliminar);

        fila.getChildren().addAll(
                check, colPedido, colCliente, colFecha,
                lblTotal, colPago, colEnvio, colAcciones
        );
        return fila;
    }

    private Label etiqueta(String valor) {
        Label label = new Label(valor);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 11px; -fx-text-fill: #8A7A7A;");
        return label;
    }

    private Button crearBotonAccion(String texto) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: white;"
                + "-fx-border-color: #F3E6DA;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-font-size: 10px;"
                + "-fx-padding: 4 4 4 4;"
                + "-fx-cursor: hand;");
        return boton;
    }

    private String getIniciales(String nombre) {
        if (nombre == null || nombre.isBlank()
                || "Cliente no disponible".equals(nombre)) {
            return "—";
        }

        String[] partes = nombre.trim().split("\\s+");
        String resultado = partes[0].substring(0, 1);

        if (partes.length > 1) {
            resultado += partes[partes.length - 1].substring(0, 1);
        }

        return resultado.toUpperCase(Locale.ROOT);
    }

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "—" : valor;
    }

    private void verPedido(Pedido pedido) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/vista/vistaAdmin/FichaPedido.fxml"
                    )
            );
            Parent contenido = loader.load();

            ControladoraFichaPedido ficha = loader.getController();
            ficha.setPedido(pedido);

            Rectangle2D pantalla = Screen.getPrimary().getVisualBounds();
            double ancho = Math.min(760, pantalla.getWidth() * 0.92);
            double alto = Math.min(600, pantalla.getHeight() * 0.90);

            ScrollPane desplazamiento = new ScrollPane(contenido);
            desplazamiento.setFitToWidth(true);
            desplazamiento.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            desplazamiento.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            desplazamiento.setPannable(true);
            desplazamiento.setStyle("-fx-background: #FFF6EC;"
                    + "-fx-background-color: #FFF6EC;");

            Stage ventana = new Stage();
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.initOwner(vboxPedidos.getScene().getWindow());
            ventana.setTitle("Pedido #" + texto(pedido.getNumeroPedido()));
            ventana.setResizable(true);
            ventana.setScene(new Scene(desplazamiento, ancho, alto));
            ventana.showAndWait();

            cargarPedidos();
        } catch (Exception e) {
            mostrarError("No se pudo abrir la ficha del pedido", e);
        }
    }

    private void eliminarPedido(Pedido pedido) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar pedido");
        confirmacion.setHeaderText("¿Eliminar el pedido? ");
        confirmacion.setContentText(
                "Se eliminará el pedido " + texto(pedido.getNumeroPedido())
        );

        ButtonType eliminar = new ButtonType(
                "Eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType(
                "Conservar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(eliminar, cancelar);

        if (confirmacion.showAndWait().orElse(cancelar) != eliminar) {
            return;
        }

        try {
            pedidoServicio.eliminarPedidoYDevolverStock(
                    pedido.getNumeroPedido());
            cargarPedidos();
        } catch (Exception e) {
            mostrarError("No se pudo eliminar el pedido", e);
        }
    }

    private void mostrarError(String encabezado, Exception e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}
