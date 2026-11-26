package Controladoras;

import Controladoras.EntornoUsuario.ControladoraVistaProducto;
import Controladoras.EntornoUsuario.ConUsuario;
import datos.CarritoDAO;
import java.io.File;
import java.text.NumberFormat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import modeloPersonas.Usuario;
import modeloProductos.Producto;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Navegador {

    private static final List<BadgeCarrito> badgesCarrito = new ArrayList<>();

    private static final class BadgeCarrito {
        private final String correo;
        private final WeakReference<Label> etiqueta;

        private BadgeCarrito(String correo, Label etiqueta) {
            this.correo = correo;
            this.etiqueta = new WeakReference<>(etiqueta);
        }
    }

    public static final String INICIO = "/vista/vistaUsuarios/Usuarios.fxml";
    public static final String FAVORITOS = "/vista/vistaUsuarios/Favoritos.fxml";
    public static final String CARRITO = "/vista/vistaUsuarios/Carrito.fxml";
    public static final String TERMINAR = "/vista/vistaUsuarios/TerminarPedido.fxml";
    public static final String PERFIL = "/vista/vistaUsuarios/Perfil.fxml";
    public static final String MIS_PEDIDOS = "/vista/vistaUsuarios/MisPedidos.fxml";
    public static final String EDITAR_PERFIL = "/vista/vistaUsuarios/EditarPerfil.fxml";
    public static final String VISTA_PRODUCTO = "/vista/vistaUsuarios/VistaProducto.fxml";
    public static String textoBusqueda = null;   // guarda lo buscado desde otras ventanas

    public static void ir(String fxml, Node origen, Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(fxml));
            Parent root = loader.load();
            Object c = loader.getController();
            if (c instanceof ConUsuario cu && usuario != null) {
                cu.setUsuario(usuario);
            }
            origen.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Error", "No se pudo abrir la ventana: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public static void alerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    public static boolean confirmar(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        a.setTitle(titulo);
        a.setHeaderText(null);
        return a.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    public static String precio(double valor) {
        NumberFormat f = NumberFormat.getInstance(Locale.forLanguageTag("es-CO"));
        f.setMaximumFractionDigits(0);
        return "$ " + f.format(valor);
    }

    public static ImageView imagenProducto(Producto p, double tam) {
        ImageView iv = new ImageView();
        iv.setFitWidth(tam);
        iv.setFitHeight(tam);
        iv.setPreserveRatio(true);
        File f = new File("imagenes/productos", p.getImagen() == null ? "" : p.getImagen());
        if (f.exists()) {
            iv.setImage(new Image(f.toURI().toString()));
        }
        return iv;
    }

    private static ImageView icono(String ruta, double tam) {
        ImageView iv = new ImageView(new Image(Navegador.class.getResourceAsStream(ruta)));
        iv.setFitHeight(tam);
        iv.setPreserveRatio(true);
        return iv;
    }

    private static Button boton(String ruta, double tam, Runnable accion) {
        Button b = new Button();
        b.setGraphic(icono(ruta, tam));
        b.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        b.setOnAction(e -> accion.run());
        return b;
    }

    private static String correoClave(Usuario usuario) {
        return usuario == null || usuario.getCorreo() == null
                ? "" : usuario.getCorreo().trim().toLowerCase(Locale.ROOT);
    }

    private static Label crearBadgeCarrito(Usuario usuario, int unidades) {
        Label badge = new Label(String.valueOf(unidades));
        badge.setStyle("-fx-background-color: #ff4f9a; -fx-text-fill: white; -fx-font-size: 9; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 0 5 0 5;");
        badge.setMouseTransparent(true);
        badge.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        badgesCarrito.add(new BadgeCarrito(correoClave(usuario), badge));
        return badge;
    }

    /** Actualiza el contador visible en todos los encabezados actualmente creados para la cuenta. */
    public static void actualizarContadorCarrito(Usuario usuario, int unidades) {
        String correo = correoClave(usuario);
        Iterator<BadgeCarrito> iterador = badgesCarrito.iterator();
        while (iterador.hasNext()) {
            BadgeCarrito item = iterador.next();
            Label etiqueta = item.etiqueta.get();
            if (etiqueta == null) {
                iterador.remove();
            } else if (item.correo.equals(correo)) {
                etiqueta.setText(String.valueOf(Math.max(0, unidades)));
            }
        }
    }

    /** Coloca el contador sobre el gráfico del botón de carrito existente en una vista. */
    public static void agregarContadorCarrito(Button botonCarrito, Usuario usuario, int unidades) {
        if (botonCarrito == null || usuario == null) {
            return;
        }
        actualizarContadorCarrito(usuario, unidades);
        Node grafico = botonCarrito.getGraphic();
        botonCarrito.setGraphic(null);
        StackPane iconoConContador = new StackPane();
        if (grafico != null) {
            iconoConContador.getChildren().add(grafico);
        }
        Label badge = crearBadgeCarrito(usuario, unidades);
        iconoConContador.getChildren().add(badge);
        StackPane.setAlignment(badge, Pos.TOP_RIGHT);
        badge.setTranslateX(4);
        badge.setTranslateY(-3);
        botonCarrito.setGraphic(iconoConContador);
    }

    public static HBox encabezado(Usuario u) {
        HBox barra = new HBox(10);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(6, 20, 6, 20));
        barra.setStyle("-fx-background-color: #fff4e9; -fx-border-color: transparent transparent #e9cdbf transparent;");

        Button logo = boton("/imagenes/LogoHada.png", 56, () -> ir(INICIO, barra, u));

        TextField buscar = new TextField();
        buscar.setPromptText("¿Qué estás buscando?");
        buscar.setStyle("-fx-background-color: #ffeedd; -fx-border-color: #ff4f9a; -fx-border-radius: 30; -fx-background-radius: 30;");
        HBox.setHgrow(buscar, Priority.ALWAYS);

        // para que la barra de busqueda funcione
        buscar.setOnAction(e -> {
            textoBusqueda = buscar.getText().trim();
            ir(INICIO, barra, u);
        });

        int unidades = 0;
        try {
            unidades = new CarritoDAO().cargar(u).getProductos().size();
        } catch (Exception ignorada) {
        }
        actualizarContadorCarrito(u, unidades);
        Label badge = crearBadgeCarrito(u, unidades);
        StackPane carrito = new StackPane(boton("/imagenes/shopping-cart.png", 22, () -> ir(CARRITO, barra, u)), badge);
        StackPane.setAlignment(badge, Pos.TOP_RIGHT);

        barra.getChildren().addAll(logo, buscar,
                boton("/imagenes/heart.png", 22, () -> ir(FAVORITOS, barra, u)),
                boton("/imagenes/circle-user.png", 22, () -> ir(PERFIL, barra, u)),
                carrito);
        return barra;
    }

    public static void irAProducto(Producto producto, Node origen, Usuario usuario, String volverA) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource(VISTA_PRODUCTO));
            Parent root = loader.load();
            ControladoraVistaProducto c = loader.getController();
            c.setUsuario(usuario);                 // primero el usuario (pone el encabezado)
            c.setProducto(producto, volverA);      // luego el producto
            origen.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Error", "No se pudo abrir el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    

    public static void cerrarSesion(Node origen) {
        if (!confirmar("Cerrar sesión", "¿Estás segura que deseas salir de tu cuenta de Hada Beauty?")) {
            return;
        }
        try {
            Parent root = FXMLLoader.load(Navegador.class.getResource("/vista/Inicio.fxml"));
            Stage escenario = (Stage) origen.getScene().getWindow();
            double alto = Screen.getPrimary().getVisualBounds().getHeight() * 0.90;
            escenario.setScene(new Scene(root, 500, alto));
            escenario.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String iniciales(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "HB";
        }
        String[] p = nombre.trim().split("\\s+");
        String r = p[0].substring(0, 1);
        if (p.length > 1) {
            r += p[1].substring(0, 1);
        }
        return r.toUpperCase();
    }
}
