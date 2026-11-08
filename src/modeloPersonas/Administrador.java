package modeloPersonas;

public class Administrador extends Persona {

    private String codigoAdministrador;

    public Administrador() {
    }

    public Administrador(String nombre, String contrasena, String telefono,
            String usuario, int edad, String sexo, String codigoAdministrador) {
        super(nombre, contrasena, telefono, usuario, edad, sexo);
        this.codigoAdministrador = codigoAdministrador;
    }

    public String getCodigoAdministrador() {
        return codigoAdministrador;
    }

    public void setCodigoAdministrador(String codigoAdministrador) {
        this.codigoAdministrador = codigoAdministrador;
    }
}
