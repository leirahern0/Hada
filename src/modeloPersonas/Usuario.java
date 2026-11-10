package modeloPersonas;

import modeloPersonas.Persona;
import modeloProductos.Carrito;
import modeloProductos.Favoritos;

public class Usuario extends Persona {

    private String direccion;
    private Carrito carrito;
    private Favoritos favoritos;

    public Usuario() {
        this.carrito = new Carrito();
        this.favoritos = new Favoritos();
    }

    public Usuario(String nombre, String contrasena, String telefono, String usuario, String direccion) {
        super(nombre, contrasena, telefono, usuario);
        this.direccion = direccion;
        this.carrito = new Carrito();
        this.favoritos = new Favoritos();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public Favoritos getFavoritos() {
        return favoritos;
    }
    
    @Override

    public boolean esAdministrador() {
    
        return false;

    }
    
    @Override
    public String toLinea() {
       
        return getNombre() + ";" + getContraseña() + ";" + getTelefono() + ";" 
                + getUsuario() + ";USUARIO;" + direccion;
    }

}
