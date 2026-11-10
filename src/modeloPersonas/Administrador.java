package modeloPersonas;

public class Administrador extends Persona {

    private String codigoAdministrador;

    public Administrador() {
    }

    public Administrador(String nombre, String contrasena, String telefono,
            String usuario, String codigoAdministrador) {
        super(nombre, contrasena, telefono, usuario);
        this.codigoAdministrador = codigoAdministrador;
    }

    public String getCodigoAdministrador() {
        return codigoAdministrador;
    }

    public void setCodigoAdministrador(String codigoAdministrador) {
        this.codigoAdministrador = codigoAdministrador;
    }
    
    @Override

    public boolean esAdministrador() {
    
        return true;

    }
    
    @Override
    public String toLinea() {
        return getNombre() + ";" + getContraseña() + ";" + getTelefono() + ";" + getUsuario() + ";ADMIN;" 
                + codigoAdministrador;
    }
}
