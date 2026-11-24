package Controladoras;

import datos.PedidoDAO;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        Label estado = new Label(p.getEstadoPago());
        estado.setStyle("-fx-background-color: #ffe9b8; -fx-text-fill: #8a5a00; -fx-font-size: 11;"
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
        tarjeta.getChildren().addAll(pago, entrega, total);

        return tarjeta;
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
