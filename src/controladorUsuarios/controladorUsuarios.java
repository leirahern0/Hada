/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorUsuarios;

import java.util.ArrayList;
import modeloPersonas.Usuario;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class controladorUsuarios {

    private List<Usuario> usuarios;

    public controladorUsuarios() {
        usuarios = new ArrayList<>();
    }

    public boolean registro(Usuario nuevo) {

        for (Usuario u : usuarios) {

            if (u.getUsuario().equalsIgnoreCase(nuevo.getUsuario())) {

                return false;

            }

        }

        usuarios.add(nuevo);

        return true;

    }

    public Usuario buscarUsuario(String correo) {

        for (Usuario u : usuarios) {

            if (u.getUsuario().equalsIgnoreCase(correo)) {
                return u;
            }
        }

        return null;
    }

    
    public boolean InicioSesion(String correo, String clave) {

        Usuario encontrado = buscarUsuario(correo);

        if (encontrado != null
                && encontrado.getContraseña().equals(clave)) {

            return true;
        }

        return false;
    }

}
