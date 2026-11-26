package Controladoras.EntornoAdmin;

import Controladoras.Navegador;
import datos.PedidoServicio;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modeloProductos.Pedido;
import modeloProductos.Producto;

public class ControladoraFichaPedido {

    @FXML private VBox rootPopup;
    @FXML private VBox vboxProductos;
    @FXML private VBox vboxVacioProductos;

    @FXML private Label lblNumeroPedido;
    @FXML private Label lblSubtitulo;
    @FXML private Label lblImporteTopTitulo;
    @FXML private Label lblImporteTop;
    @FXML private Label lblCountProductos;
    @FXML private Label lblTotalPedido;
    @FXML private Label lblAvatarCliente;
    @FXML private Label lblNombreCliente;
    @FXML private Label lblEmailCliente;
    @FXML private Label lblTelefono;
    @FXML private Label lblDireccion;

    private Pedido pedido;
    private final PedidoServicio pedidoServicio = new PedidoServicio();

    /** Recibe el pedido elegido en la lista y llena la ventana emergente. */
    public void setPedido(Pedido pedido) {
        if (pedido == null) {
            return;
        }

        this.pedido = pedido;

        lblNumeroPedido.setText("Pedido #" + texto(pedido.getNumeroPedido()));
        lblSubtitulo.setText(pedido.getFecha() == null
                ? "Fecha no disponible"
                : "Realizado el " + pedido.getFecha().format(
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy HH:mm",
                                Locale.forLanguageTag("es-CO")
                        )
                ));

        lblImporteTopTitulo.setText("Total del pedido");
        String total = "COP " + Navegador.precio(pedido.getTotal());
        lblImporteTop.setText(total);
        lblTotalPedido.setText(total);

        if (pedido.getCliente() == null) {
            lblAvatarCliente.setText("—");
            lblNombreCliente.setText("Cliente no disponible");
            lblEmailCliente.setText("—");
            lblTelefono.setText("Teléfono: No disponible");
        } else {
            String nombre = texto(pedido.getCliente().getNombre());
            lblAvatarCliente.setText(iniciales(nombre));
            lblNombreCliente.setText(nombre);
            lblEmailCliente.setText(texto(pedido.getCliente().getCorreo()));
            lblTelefono.setText(
                    "Teléfono: " + texto(pedido.getCliente().getTelefono())
            );
        }

        lblDireccion.setText(texto(pedido.getDireccionEntrega()));
        mostrarProductos();
    }

    private void mostrarProductos() {
        vboxProductos.getChildren().clear();

        if (pedido.getProductos() == null || pedido.getProductos().isEmpty()) {
            vboxProductos.getChildren().add(vboxVacioProductos);
            lblCountProductos.setText("0 productos");
            return;
        }

        Map<String, Producto> productosUnicos = new LinkedHashMap<>();
        Map<String, Integer> cantidades = new LinkedHashMap<>();

        for (Producto producto : pedido.getProductos()) {
            if (producto == null) {
                continue;
            }
            String clave = producto.getCodigo() == null
                    ? texto(producto.getNombre())
                    : producto.getCodigo();
            productosUnicos.putIfAbsent(clave, producto);
            cantidades.merge(clave, 1, Integer::sum);
        }

        for (Map.Entry<String, Producto> entrada : productosUnicos.entrySet()) {
            Producto producto = entrada.getValue();
            int cantidad = cantidades.get(entrada.getKey());

            HBox fila = new HBox();
            fila.setSpacing(8);
            fila.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            fila.setStyle("-fx-padding: 10 14;"
                    + "-fx-border-color: transparent transparent #F3E6DA transparent;");

            Label nombre = new Label(texto(producto.getNombre()));
            nombre.setPrefWidth(165);
            nombre.setMinWidth(0);
            nombre.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(nombre, Priority.ALWAYS);
            nombre.setWrapText(true);

            Label precio = new Label(Navegador.precio(producto.getPrecio()));
            precio.setPrefWidth(75);
            precio.setAlignment(javafx.geometry.Pos.CENTER);

            Label cantidadLabel = new Label(String.valueOf(cantidad));
            cantidadLabel.setPrefWidth(40);
            cantidadLabel.setAlignment(javafx.geometry.Pos.CENTER);

            Label importe = new Label(
                    Navegador.precio(producto.getPrecio() * cantidad)
            );
            importe.setPrefWidth(85);
            importe.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

            fila.getChildren().addAll(nombre, precio, cantidadLabel, importe);
            vboxProductos.getChildren().add(fila);
        }

        int cantidadTotal = pedido.cantidadProductos();
        lblCountProductos.setText(cantidadTotal
                + (cantidadTotal == 1 ? " producto" : " productos"));
    }

    @FXML
    private void cerrar() {
        if (rootPopup.getScene() != null) {
            rootPopup.getScene().getWindow().hide();
        }
    }

    @FXML
    private void eliminarPedido() {
        if (pedido == null || pedido.getNumeroPedido() == null) {
            Navegador.alerta("No se puede eliminar", "No se encontró el pedido.",
                    Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar pedido");
        confirmacion.setHeaderText("¿Eliminar este pedido?");
        confirmacion.setContentText("Se eliminará el pedido #"
                + pedido.getNumeroPedido()
                + " y sus productos volverán al inventario.");

        ButtonType eliminar = new ButtonType(
                "Eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType conservar = new ButtonType(
                "Conservar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(eliminar, conservar);

        if (confirmacion.showAndWait().orElse(conservar) != eliminar) {
            return;
        }

        try {
            pedidoServicio.eliminarPedidoYDevolverStock(
                    pedido.getNumeroPedido());
            cerrar();
        } catch (Exception e) {
            Navegador.alerta("No se pudo eliminar el pedido", e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private String iniciales(String nombre) {
        if (nombre == null || nombre.isBlank() || "—".equals(nombre)) {
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
}
