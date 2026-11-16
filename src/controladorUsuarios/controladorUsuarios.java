/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorUsuarios;
import datos.UsuarioDAO;
import modeloPersonas.Persona;
import modeloPersonas.Usuario;
import java.util.List;

public class controladorUsuarios {

    private final UsuarioDAO usuarioDAO;

    public controladorUsuarios() {
        usuarioDAO = new UsuarioDAO();
    }

    public boolean registro(Usuario nuevo) {
        try {
            if (usuarioDAO.buscarPorCorreo(nuevo.getCorreo()) != null) {
                return false;
            }

            usuarioDAO.guardar(nuevo);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarUsuario(String correo) {
        try {
            Persona persona = usuarioDAO.buscarPorCorreo(correo);

            if (persona instanceof Usuario && !persona.esAdministrador()) {
                return (Usuario) persona;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean InicioSesion(String correo, String clave) {
        try {
            Usuario encontrado = buscarUsuario(correo);

            return encontrado != null
                    && encontrado.getContraseña().equals(clave);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}