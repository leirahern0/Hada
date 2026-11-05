package modeloPersonas;

public abstract class Persona {

    private String nombre;
    private String contrasena;
    private String telefono;
    private String usuario;
    private int edad;
    private String sexo;

    public Persona() {
    }

    public Persona(String nombre, String contrasena, String telefono, String usuario, int edad, String sexo) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.usuario = usuario;
        this.edad = edad;
        this.sexo = sexo;
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

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

}
