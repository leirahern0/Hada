/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladoras;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modeloProductos.Producto;

public class ControladoraAdmin {

    @FXML private Label lblAdmin;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnNuevoProducto;
    @FXML private TextField txtBuscar;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Void> colImagen;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Void> colAcciones;

    @FXML
    private void irProductos() {
       
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