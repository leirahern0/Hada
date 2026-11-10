/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import modeloPersonas.Persona;
import java.util.*;

public class UsuarioDAO {
    private String ruta = "datos/usuarios.txt";

    public List<Persona> listar() throws Exception {
        List<Persona> lista = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) if (!l.isBlank()) lista.add(Persona.desdeLinea(l));
        return lista;
    }
    public void guardar(Persona p) throws Exception { ArchivoUtil.agregar(ruta, p.toLinea()); }

    public void eliminar(String usuario) throws Exception {
        List<Persona> lista = listar();
        lista.removeIf(p -> p.getUsuario().equals(usuario));
        guardarTodos(lista);
    }
    public void editar(Persona nuevo) throws Exception {
        List<Persona> lista = listar();
        for (int i = 0; i < lista.size(); i++) if (lista.get(i).getUsuario().equals(nuevo.getUsuario())) lista.set(i, nuevo);
        guardarTodos(lista);
    }
    private void guardarTodos(List<Persona> lista) throws Exception {
        List<String> lineas = new ArrayList<>();
        for (Persona p : lista) lineas.add(p.toLinea());
        ArchivoUtil.reescribir(ruta, lineas);
    }
    public Persona buscarPorUsuario(String usuario) throws Exception {
        for (Persona p : listar()) if (p.getUsuario().equals(usuario)) return p;
        return null;
    }
}