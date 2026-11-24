package datos;

import modeloPersonas.Usuario;
import java.util.*;

public class UsuarioDAO {
    private String ruta = "datos/usuarios.txt";

    public UsuarioDAO() {
    }

    public List<Usuario> listar() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) if (!l.isBlank()) lista.add(Usuario.desdeLinea(l));
        return lista;
    }
    public void guardar(Usuario e) throws Exception { 
        ArchivoUtil.agregar(ruta, e.toLinea()); 
    }

    public void eliminar(String usuario) throws Exception {
        List<Usuario> lista = listar();
        lista.removeIf(p -> p.getCorreo().equals(usuario));
        guardarTodos(lista);
    }
    public void editar(Usuario nuevo) throws Exception {
        List<Usuario> lista = listar();
        for (int i = 0; i < lista.size(); i++) if (lista.get(i).getCorreo().equals(nuevo.getCorreo())) lista.set(i, nuevo);
        guardarTodos(lista);
    }
    private void guardarTodos(List<Usuario> lista) throws Exception {
        List<String> lineas = new ArrayList<>();
        for (Usuario p : lista) lineas.add(p.toLinea());
        ArchivoUtil.reescribir(ruta, lineas);
    }
    public Usuario buscarPorCorreo(String usuario) throws Exception {
        for (Usuario p : listar()) if (p.getCorreo().equals(usuario)) return p;
        return null;
    }
   
}
