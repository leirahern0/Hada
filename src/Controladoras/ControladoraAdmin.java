/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import modeloProductos.Producto;

public class ControladoraAdmin {

    @FXML private Label lblAdmin;
    @FXML private Button btnCerrarSesion;
    @FXML private StackPane contenedorVistas;
    
    
    private void cargarVista(String nombreFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/" + nombreFxml));
            Node vista = loader.load();
            contenedorVistas.getChildren().clear(); 
            contenedorVistas.getChildren().add(vista); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void initialize (){
        cargarVista("GestionProductos.fxml");
    }

    @FXML
    private void irProductos() {
       
    }
    
     @FXML
    private void irInicio() {
     
    }
    
    

    @FXML
    private void irPedidos() {
     
    }

    @FXML
    private void irUsuarios() {
       
    }

    @FXML
    private void irMiPerfil() {
      
    }

    @FXML
    private void verTienda() {
       
    }

    @FXML
    private void cerrarSesion() {
    
    }

    @FXML
    private void nuevoProducto() {
       
    }

    @FXML
    private void buscarProducto() {
        
    }
}