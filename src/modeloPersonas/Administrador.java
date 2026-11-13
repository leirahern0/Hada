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
        return getNombre() + "|" + getContraseña() + "|" + getTelefono() + "|" + getUsuario() + "|ADMIN|" 
                + codigoAdministrador;
    }
    
    public static Administrador desdeLinea(String linea) {
    String[] p = linea.split("\\|", -1);
    // p[0]=usuario, p[1]=nombre, p[2]=correo, p[3]=clave, p[5]=telefono
    return new Administrador(p[0], p[1], p[2], p[3], p[5]);
}
}
