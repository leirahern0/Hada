/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author Usuario
 */

public class ArchivoUtil {
    
    public static List<String> leer(String ruta) throws Exception {
        Path p = Paths.get(ruta);
        if (!Files.exists(p)) return new ArrayList<>();
        return Files.readAllLines(p);
    }
    public static void agregar(String ruta, String linea) throws Exception {
        Path p = Paths.get(ruta);
        Files.createDirectories(p.getParent());
        Files.writeString(p, linea + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    public static void reescribir(String ruta, List<String> lineas) throws Exception {
        Path p = Paths.get(ruta);
        Files.createDirectories(p.getParent());
        Files.write(p, lineas);
    }
}