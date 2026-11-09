package modeloPersonas;

public abstract class Persona {

    private String nombre;
    private String contrasena;
    private String telefono;
    private String usuario;

    public Persona() {
    }

    public Persona(String nombre, String contrasena, String telefono, String usuario) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.usuario = usuario;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contrasena;
    }

    public void setContraseña(String contraseña) {
        this.contrasena = contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
