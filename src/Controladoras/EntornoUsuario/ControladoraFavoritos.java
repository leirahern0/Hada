package Controladoras.EntornoUsuario;

import Controladoras.EntornoUsuario.ConUsuario;
import Controladoras.Navegador;
import datos.CarritoDAO;
import datos.FavoritosDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import modeloPersonas.Usuario;
import modeloProductos.Carrito;
import modeloProductos.Favoritos;
import modeloProductos.Producto;

public class ControladoraFavoritos implements ConUsuario {

    @FXML
    private VBox raiz;
    @FXML
    private FlowPane contenedorFavoritos;
    @FXML
    private VBox panelVacio;

    private Usuario usuario;
    private Favoritos favoritos = new Favoritos();
    private Carrito carrito = new Carrito();
    private final FavoritosDAO favoritosDAO = new FavoritosDAO();
    private final CarritoDAO carritoDAO = new CarritoDAO();

    @Override
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        raiz.getChildren().add(0, Navegador.encabezado(usuario));
        try {
            favoritos = favoritosDAO.cargar(usuario);
            carrito = carritoDAO.cargar(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mostrar();
    }

    private void mostrar() {
        contenedorFavoritos.getChildren().clear();
        for (Producto p : favoritos.getFavoritos()) {
            contenedorFavoritos.getChildren().add(crearTarjeta(p));
        }
        boolean vacio = favoritos.getFavoritos().isEmpty();
        panelVacio.setVisible(vacio);
        panelVacio.setManaged(vacio);
    }

    private VBox crearTarjeta(Producto p) {
        VBox tarjeta = new VBox(6);
        tarjeta.setPrefWidth(190);
        tarjeta.setMinWidth(190);
        tarjeta.setMaxWidth(190);
        tarjeta.setAlignment(Pos.TOP_LEFT);
        tarjeta.setStyle("-fx-background-color: white; -fx-border-color: #ffc6ad; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 12;");

        Button corazon = new Button("♥");
        corazon.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 2 0 2; -fx-font-size: 22; -fx-text-fill: #8f294d;");
        corazon.setOnAction(e -> {
            favoritos.corazonFavoritos(p); 
            guardarFavoritos();
            mostrar();
        });
        StackPane.setAlignment(corazon, Pos.TOP_RIGHT);
        StackPane zonaImagen = new StackPane(Navegador.imagenProducto(p, 130), corazon);

        Label nombre = new Label(p.getNombre());
        nombre.setWrapText(true);
        nombre.setStyle("-fx-font-family: Georgia; -fx-text-fill: #1f1f1f; -fx-font-size: 13;");
        Label marca = new Label(p.getMarca());
        marca.setStyle("-fx-text-fill: #8f6a74; -fx-font-size: 10;");
        Label precio = new Label(Navegador.precio(p.getPrecio()));
        precio.setStyle("-fx-text-fill: #8f294d; -fx-font-weight: bold; -fx-font-size: 15;");

        Button agregar = new Button("🛒 Agregar al carrito");
        agregar.setMaxWidth(Double.MAX_VALUE);
        agregar.getStyleClass().add("btn-rosa");
        agregar.setStyle("-fx-font-size: 11; -fx-padding: 6 10 6 10;");
        agregar.setOnAction(e -> {
            carrito.agregarUnidad(p);
            try {
                carritoDAO.guardar(usuario, carrito);
                Navegador.actualizarContadorCarrito(
                        usuario, carrito.getProductos().size());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            agregar.setText("✓ Agregado");
            PauseTransition pausa = new PauseTransition(Duration.seconds(1.2));
            pausa.setOnFinished(ev -> agregar.setText("🛒 Agregar al carrito"));
            pausa.play();
        });

        tarjeta.getChildren().addAll(zonaImagen, nombre, marca, precio, agregar);
        return tarjeta;
    }

    private void guardarFavoritos() {
        try {
            favoritosDAO.guardar(usuario, favoritos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void explorarProductos() {
        Navegador.ir(Navegador.INICIO, contenedorFavoritos, usuario);
    }

}
