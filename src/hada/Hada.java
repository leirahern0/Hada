package hada;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Hada extends Application {

    @Override

    public void start(Stage primaryStage) throws Exception {

    String rutaFxml = "/vista/Inicio.fxml";

    Parent root = FXMLLoader.load(getClass().getResource(rutaFxml));
    Rectangle2D pantalla = Screen.getPrimary().getVisualBounds();
    double anchoVentana = Math.min(520, pantalla.getWidth() * 0.94);
    double altoVentana = Math.min(760, pantalla.getHeight() * 0.90);

    primaryStage.setTitle("Hada Beauty");
    primaryStage.setScene(new Scene(root, anchoVentana, altoVentana));
    primaryStage.initStyle(StageStyle.DECORATED);
    primaryStage.setMinWidth(Math.min(460, pantalla.getWidth() * 0.90));
    primaryStage.setMinHeight(Math.min(480, pantalla.getHeight() * 0.90));
    primaryStage.setResizable(true);
    primaryStage.centerOnScreen();
    primaryStage.show();
}
    public static void main(String[] args) {
        launch(args);
    }
}
