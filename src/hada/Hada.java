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

    // Aquí pones cuál vista vas a abrir
    String rutaFxml = "/vista/Inicio.fxml";

    Parent root = FXMLLoader.load(getClass().getResource(rutaFxml));

    // Aquí mismo decides el ancho, sin otro método
    Rectangle2D pantalla = Screen.getPrimary().getVisualBounds();
    double anchoVentana;
    double altoventana= pantalla.getHeight() * 0.90;

    if (rutaFxml.contains("Inicio")) {
        anchoVentana = 500;
    } else {
        anchoVentana = 1140;
    }

    primaryStage.setTitle("Hada Beauty");
    primaryStage.setScene(new Scene(root, anchoVentana, altoventana));
    primaryStage.initStyle(StageStyle.DECORATED);
    primaryStage.setResizable(false);
    primaryStage.centerOnScreen();
    primaryStage.show();
}
    public static void main(String[] args) {
        launch(args);
    }
}
