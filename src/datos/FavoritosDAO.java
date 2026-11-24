package datos;

import modeloProductos.*;
import modeloPersonas.Usuario;
import java.util.*;

public class FavoritosDAO {

    private String ruta = "datos/favoritos.txt";

    public FavoritosDAO() {
    }

    public Favoritos cargar(Usuario usuario) throws Exception {
        Favoritos fav = new Favoritos();
        ProductoDAO pdao = new ProductoDAO();
        for (String l : ArchivoUtil.leer(ruta)) {
            if (l.isBlank()) {
                continue;
            }
            String[] p = l.split("\\|");
            if (p[0].equals(usuario.getCorreo())) {
                Producto prod = pdao.buscarPorCodigo(p[1]);

                if (prod != null && !fav.esFavorito(prod)) {
                    fav.corazonFavoritos(prod);
                }
            }
        }
        return fav;
    }

    public void guardar(Usuario usuario, Favoritos fav) throws Exception {
        List<String> lineas = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) {
            if (!l.isBlank() && !l.split("\\|")[0].equals(usuario.getCorreo())) {
                lineas.add(l);
            }
        }
        for (Producto p : fav.getFavoritos()) {
            lineas.add(usuario.getCorreo() + "|" + p.getCodigo());
        }
        ArchivoUtil.reescribir(ruta, lineas);
    }
}
