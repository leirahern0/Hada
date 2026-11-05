package modeloPersonas;

public class Administrador extends Persona {

    private String codigoAdministrador;

    public Administrador() {
    }

    public Administrador(String nombre, String contrasena, String telefono, String usuario, int edad, String sexo) {
        super(nombre, contrasena, telefono, usuario, edad, sexo);
    }

    public String getCodigoAdministrador() {
        return codigoAdministrador;
    }

    public void setCodigoAdministrador(String codigoAdministrador) {
        this.codigoAdministrador = codigoAdministrador;
    }

}
