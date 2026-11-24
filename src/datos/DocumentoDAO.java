package datos;

import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {

    private String ruta = "datos/documentos.txt";

    // Devuelve true si ese documento ya pertenece a otra cuenta
    public boolean usadoPorOtro(String documento, String correo) throws Exception {
        for (String l : ArchivoUtil.leer(ruta)) {
            if (l.isBlank()) {
                continue;
            }
            String[] p = l.split("\\|", -1);
            if (p.length >= 3 && p[2].equals(documento) && !p[0].equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    // Guarda  el documento de este usuario
    public void guardar(String correo, String tipo, String documento) throws Exception {
        List<String> lineas = new ArrayList<>();
        for (String l : ArchivoUtil.leer(ruta)) {
            if (!l.isBlank() && !l.split("\\|", -1)[0].equalsIgnoreCase(correo)) {
                lineas.add(l);
            }
        }
        lineas.add(correo + "|" + tipo + "|" + documento);
        ArchivoUtil.reescribir(ruta, lineas);
    }
}
