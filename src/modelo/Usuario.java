package modelo;

public class Usuario extends Persona {

    private String direccion;

    public Usuario() {
    }

    public Usuario(String nombre, String contraseña, String telefono, String usuario, int edad, String sexo, String direccion) {
        super(nombre, contraseña, telefono, usuario, edad, sexo);
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
