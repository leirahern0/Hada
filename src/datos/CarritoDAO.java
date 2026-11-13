/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import modeloProductos.*;
import modeloPersonas.Usuario;
import java.util.*;

public class CarritoDAO {
    private String ruta = "datos/carrito.txt";

    public CarritoDAO() {
    }

   
    public Carrito cargar(Usuario usuario) throws Exception {
        Carrito carrito = new Carrito();
        ProductoDAO pdao = new ProductoDAO();
        for (String l : ArchivoUtil.leer(ruta)) {
            if (l.isBlank()) continue;
            String[] p = l.split("\\|");
            if (p[0].equals(usuario.getUsuario())) {
                Producto prod = pdao.buscarPorCodigo(p[1]);
                if (prod != null) carrito.agregarUnidad(prod);
            }
        }
        return carrito;
    }

    
    public void guardar(Usuario usuario, Carrito carrito) throws Exception {
        List<String> lineas = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) {
            if (!l.isBlank() && !l.split("\\|")[0].equals(usuario.getUsuario())) lineas.add(l);
        }
        for (Producto p : carrito.getProductos()) {
            lineas.add(usuario.getUsuario() + "|" + p.getCodigo());
        }
        ArchivoUtil.reescribir(ruta, lineas);
    }
}