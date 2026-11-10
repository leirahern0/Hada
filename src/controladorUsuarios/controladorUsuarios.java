/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladorUsuarios;

import java.util.ArrayList;
import modeloPersonas.Usuario;
import java.util.List;
import datos.UsuarioDAO;
import modeloPersonas.Persona;

/**
 *
 * @author Usuario
 */
public class controladorUsuarios {
    
    private UsuarioDAO dao = new UsuarioDAO();

    public controladorUsuarios() {
        
    }

    public boolean registro(Usuario nuevo) {
        try {
       
            if (dao.buscarPorUsuario(nuevo.getUsuario()) != null) {
                return false;
            }
          
            dao.guardar(nuevo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean InicioSesion(String correo, String clave) {
        try {
            Persona encontrado = dao.buscarPorUsuario(correo);
            if (encontrado != null && encontrado.getContraseña().equals(clave)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Usuario buscarUsuario(String correo) {
    
        try {
        
            Persona encontrado = dao.buscarPorUsuario(correo);
        
            if (encontrado != null && !encontrado.esAdministrador()) {
            
                return (Usuario) encontrado;
        
            }
        
            return null;
    
        } 
    
        catch (Exception e) {
        
            e.printStackTrace();
        
            return null;
    
    
        }

    }


 


    
    

}
