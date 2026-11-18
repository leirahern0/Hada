/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;

public class ControladoraGestionProductos {

    @FXML private VBox contenedorProductos;
    @FXML private Label lblContador;
    @FXML private TextField txtBuscar;
    @FXML private Button btnFiltroTodos, btnFiltroRostro, btnFiltroLabios, btnFiltroOjos, btnFiltroBrochas;

    private ControladoraAdmin admin;
    public void setAdmin(ControladoraAdmin admin) { this.admin = admin; }

    @FXML
    private void nuevoProducto() {
        
    }
    
     @FXML
    private void filtroTodos() {
        
    }
    
    @FXML
    private void filtroRostro() {
        
    }
    
    @FXML
    private void filtroLabios() {
        
    }
    
    @FXML
    private void filtroOjos() {
        
    }
    
    public void agregarProducto(String nombre, String descripcion, String categoria, String precio, String urlImagen) {
        
    }

    
    @FXML
    public void buscarProducto(KeyEvent event) {
 

            
    }
}
