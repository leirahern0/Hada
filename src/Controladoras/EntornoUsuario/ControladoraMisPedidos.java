package Controladoras.EntornoUsuario;

import Controladoras.EntornoUsuario.ConUsuario;
import Controladoras.Navegador;
import datos.PedidoDAO;
import datos.PedidoServicio;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import modeloPersonas.Usuario;
import modeloProductos.Pedido;
import modeloProductos.Producto;

public class ControladoraMisPedidos implements ConUsuario {

    @FXML
    private VBox contenedorPedidos;
    @FXML
    private VBox panelVacio;

    private Usuario usuario;
    private final PedidoServicio pedidoServicio = new PedidoServicio();

    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarPedidos();
    }

    private void cargarPedidos() {
        contenedorPedidos.getChildren().clear();
        List<Pedido> mios = new ArrayList<>();
        try {
            for (Pedido p : new PedidoDAO().listar()) {
                if (p.getCliente() != null
                        && p.getCliente().getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                    mios.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Navegador.alerta("Error", "No se pudieron cargar tus pedidos: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
        Collections.reverse(mios);   // los más recientes primero

        for (Pedido p : mios) {
            contenedorPedidos.getChildren().add(crearTarjeta(p));
        }
        boolean vacio = mios.isEmpty();
        contenedorPedidos.setManaged(!vacio);
        panelVacio.setVisible(vacio);
        panelVacio.setManaged(vacio);
    }

    private VBox crearTarjeta(Pedido p) {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 14;"
                + "-fx-border-color: #f2d9df; -fx-border-radius: 14; -fx-padding: 14 18 14 18;");

        //número, fecha y estado
        Label numero = new Label("Pedido #" + p.getNumeroPedido());
        numero.setStyle("-fx-font-family: Georgia; -fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #8f294d;");
        Label fecha = new Label(p.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        fecha.setStyle("-fx-font-size: 11; -fx-text-fill: #9b717b;");
        Label estado = new Label("Pedido registrado");
        estado.setStyle("-fx-background-color: #E6F7ED; -fx-text-fill: #1A7A4A; -fx-font-size: 11;"
                + "-fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 3 12 3 12;");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox cabecera = new HBox(12, numero, fecha, espacio, estado);
        cabecera.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        tarjeta.getChildren().add(cabecera);

        // Productos agrupados por código
        Map<String, Integer> cantidades = new LinkedHashMap<>();
        Map<String, Producto> productos = new LinkedHashMap<>();
        for (Producto pr : p.getProductos()) {
            cantidades.merge(pr.getCodigo(), 1, Integer::sum);
            productos.putIfAbsent(pr.getCodigo(), pr);
        }
        for (Map.Entry<String, Producto> e : productos.entrySet()) {
            Producto pr = e.getValue();
            int cant = cantidades.get(e.getKey());
            Label nombre = new Label(cant + " x " + pr.getNombre());
            nombre.setStyle("-fx-font-size: 12; -fx-text-fill: #5a3a44;");
            Label sub = new Label(Navegador.precio(pr.getPrecio() * cant));
            sub.setStyle("-fx-font-size: 12; -fx-text-fill: #8f294d;");
            Region r = new Region();
            HBox.setHgrow(r, Priority.ALWAYS);
            HBox fila = new HBox(10, Navegador.imagenProducto(pr, 40), nombre, r, sub);
            fila.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            tarjeta.getChildren().add(fila);
        }

        //pago, dirección y total
        Label pago = new Label("Método de pago: " + p.getMetodoPago());
        pago.setStyle("-fx-font-size: 11; -fx-text-fill: #9b717b;");
        String dir = p.getDireccionEntrega() == null || p.getDireccionEntrega().isBlank()
                ? "—" : p.getDireccionEntrega();
        Label entrega = new Label("Entrega: " + dir);
        entrega.setWrapText(true);
        entrega.setStyle("-fx-font-size: 11; -fx-text-fill: #9b717b;");
        Label total = new Label("Total: " + Navegador.precio(p.getTotal()));
        total.setStyle("-fx-font-family: Georgia; -fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #ff4f9a;");
        HBox acciones = new HBox(10);
        acciones.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Region espacioAcciones = new Region();
        HBox.setHgrow(espacioAcciones, Priority.ALWAYS);
        Button btnEliminar = new Button("Eliminar pedido");
        btnEliminar.setStyle("-fx-background-color: white; -fx-border-color: #d94a68;"
                + "-fx-border-radius: 16; -fx-background-radius: 16; -fx-text-fill: #b52f50;"
                + "-fx-font-size: 11; -fx-font-weight: bold; -fx-padding: 7 14 7 14;"
                + "-fx-cursor: hand;");
        btnEliminar.setOnAction(event -> eliminarPedido(p));
        acciones.getChildren().addAll(total, espacioAcciones, btnEliminar);
        tarjeta.getChildren().addAll(pago, entrega, acciones);

        return tarjeta;
    }

    private void eliminarPedido(Pedido pedido) {
        if (usuario == null || pedido == null || pedido.getCliente() == null
                || pedido.getCliente().getCorreo() == null
                || !pedido.getCliente().getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
            Navegador.alerta("No se puede eliminar",
                    "Ese pedido no pertenece a la cuenta activa.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar pedido");
        confirmacion.setHeaderText("¿Eliminar este pedido?");
        confirmacion.setContentText("Se eliminará el pedido #" + pedido.getNumeroPedido()
                + " y sus productos volverán al inventario.");

        ButtonType eliminar = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType conservar = new ButtonType("Conservar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(eliminar, conservar);

        if (confirmacion.showAndWait().orElse(conservar) != eliminar) {
            return;
        }

        try {
            pedidoServicio.eliminarPedidoYDevolverStock(pedido.getNumeroPedido());
            cargarPedidos();
        } catch (Exception e) {
            Navegador.alerta("No se pudo eliminar el pedido", e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void volver() {
        Navegador.ir(Navegador.PERFIL, contenedorPedidos, usuario);
    }

    @FXML
    private void explorarProductos() {
        Navegador.ir(Navegador.INICIO, contenedorPedidos, usuario);
    }
}
