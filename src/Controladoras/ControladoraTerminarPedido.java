package Controladoras;

import datos.CarritoDAO;
import datos.PedidoDAO;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import modeloPersonas.Usuario;
import modeloProductos.Carrito;
import modeloProductos.Pedido;
import datos.DocumentoDAO;

public class ControladoraTerminarPedido implements ConUsuario {

    @FXML
    private VBox raiz;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private ComboBox<String> cmbTipoDocumento;
    @FXML
    private TextField txtDocumento;
    @FXML
    private TextField txtCelular;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtCiudad;
    @FXML
    private ComboBox<String> cmbMetodoPago;
    @FXML
    private Label lblResumen;
    @FXML
    private Label lblTotal;

    private Usuario usuario;
    private Carrito carrito = new Carrito();
    private final CarritoDAO carritoDAO = new CarritoDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    @FXML
    private void initialize() {
        cmbTipoDocumento.setItems(FXCollections.observableArrayList(
                "Cédula de ciudadanía", "Cédula de extranjería", "Tarjeta de identidad", "Pasaporte", "NIT"));
        cmbMetodoPago.setItems(FXCollections.observableArrayList(
                "Tarjeta de crédito", "Tarjeta débito", "PSE", "Nequi", "Transferencia bancaria"));
    }

    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        raiz.getChildren().add(0, Navegador.encabezado(usuario));

        txtCorreo.setText(usuario.getCorreo());
        if (usuario.getTelefono() != null) {
            txtCelular.setText(usuario.getTelefono());
        }
        if (usuario.getDireccion() != null) {
            txtDireccion.setText(usuario.getDireccion());
        }
        try {
            carrito = carritoDAO.cargar(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int n = carrito.getProductos().size();
        lblResumen.setText(n + (n == 1 ? " producto" : " productos"));
        lblTotal.setText(Navegador.precio(carrito.calcularTotalCarrito()));
    }

    @FXML
    private void volverCarrito() {
        Navegador.ir(Navegador.CARRITO, raiz, usuario);
    }

    @FXML
    private void confirmarPedido() {
        String error = validar();
        if (error != null) {
            Navegador.alerta("Revisa tus datos", error, Alert.AlertType.WARNING);
            return;
        }
        try {
            String numero = String.format("HB-%04d", pedidoDAO.listar().size() + 1);
            String metodo = cmbMetodoPago.getValue();

            Pedido pedido = new Pedido(numero, usuario, new ArrayList<>(carrito.getProductos()), 0, metodo);
            pedido.calcularTotal();
            pedido.setDireccionEntrega(txtDireccion.getText().trim() + ", " + txtCiudad.getText().trim());

            documentoDAO.guardar(usuario.getCorreo(), cmbTipoDocumento.getValue(), txtDocumento.getText().trim());
            pedidoDAO.guardar(pedido);

            carrito.vaciar();
            carritoDAO.guardar(usuario, carrito);

            Navegador.alerta("Pedido confirmado",
                    "Tu pedido #" + numero + " fue registrado correctamente.", Alert.AlertType.INFORMATION);
            Navegador.ir(Navegador.INICIO, raiz, usuario);

        } catch (Exception e) {
            e.printStackTrace();
            Navegador.alerta("Error", "No se pudo registrar el pedido: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String validar() {
        if (carrito.getProductos().isEmpty()) {
            return "Tu carrito está vacío.";
        }
        if (!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", txtCorreo.getText().trim())) {
            return "Ingresa un correo electrónico válido.";
        }
        if (txtNombres.getText().isBlank() || txtApellidos.getText().isBlank()) {
            return "Escribe tus nombres y apellidos.";
        }
        if (cmbTipoDocumento.getValue() == null) {
            return "Selecciona el tipo de documento.";
        }
        if (!txtDocumento.getText().trim().matches("\\d{5,15}")) {
            return "El documento debe tener solo números (entre 5 y 15 dígitos).";
        }
        try {
            if (documentoDAO.usadoPorOtro(txtDocumento.getText().trim(), usuario.getCorreo())) {
                return "Este documento ya está registrado con otra cuenta.";
            }
        } catch (Exception e) {
            return "No se pudo verificar el documento: " + e.getMessage();
        }

        if (!txtCelular.getText().trim().matches("\\d{10}")) {
            return "El celular debe tener 10 dígitos.";
        }
        if (txtDireccion.getText().isBlank() || txtCiudad.getText().isBlank()) {
            return "Escribe la dirección y la ciudad de entrega.";
        }
        if (cmbMetodoPago.getValue() == null) {
            return "Selecciona un método de pago.";
        }
        return null;
    }

}
