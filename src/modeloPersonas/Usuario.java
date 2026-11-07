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

    public Usuario(String nombre, String contraseña, String telefono, String usuario, int edad, String sexo, String direccion) {
        super(nombre, contraseña, telefono, usuario, edad, sexo);
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

}
