package datos;

import java.io.*;

public class DatosAdministrador {

    // RUTA DEL TXT DONDE SE GUARDA LA INFO DEL ADMIN 
    private static String RUTA = "datos" + File.separator + "perfil.txt";
    
    public static String nombre = "Valentina Mora";
    public static String correo = "valentina.admin@example.com";
    public static String telefono = "+57 300 000 0000";
    public static String documento = "Sin registrar";
    public static String ciudad = "Bogotá, Colombia";
    public static String rol = "Administradora general";
    public static String identificador = "HB-ADM-001";

    public static void cargar() {
        File archivo = new File(RUTA);
        if (!archivo.exists()) {
            System.out.println("No hay txt todavía, uso datos por defecto");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            nombre = br.readLine();
            correo = br.readLine();
            telefono = br.readLine();
            documento = br.readLine();
            ciudad = br.readLine();
            rol = br.readLine(); 
            identificador = br.readLine(); 

            if (nombre == null) nombre = "Valentina Mora";
            if (correo == null) correo = "";
            if (telefono == null) telefono = "";
            if (documento == null) documento = "";
            if (ciudad == null) ciudad = "";
            if (rol == null) rol = "Administradora general"; 
            if (identificador == null) identificador = "HB-ADM-001"; 

            System.out.println("TXT leído: " + nombre);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void guardar() {
        try {
            File carpeta = new File("datos");
            carpeta.mkdirs(); 

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
                bw.write(nombre);
                bw.newLine();
                bw.write(correo);
                bw.newLine();
                bw.write(telefono);
                bw.newLine();
                bw.write(documento);
                bw.newLine();
                bw.write(ciudad);
                bw.newLine();
                bw.write(rol); 
                bw.newLine();
                bw.write(identificador); 
                bw.newLine();
            }
            System.out.println("TXT guardado en: " + RUTA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}