/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Componentes;

/**
 *
 * @author Usuario
 */
public class Productos {
    private String nombre;
    private String Id;
    private String Precio;
    private String Descripcion;
    private String Categoria;
    
    
    public Productos() {
        this.nombre = "";
        this.Id = "";
        this.Precio = "";
        this.Descripcion = "";
        this.Categoria = "";
    }
    
    public Productos(String nombre, String Id, String Precio, String Descripcion, String Categoria) {
        this.nombre = nombre;
        this.Id = Id;
        this.Precio = Precio;
        this.Descripcion = Descripcion;
        this.Categoria = Categoria;
    }
    
    
    

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return Id;
    }

    public String getPrecio() {
        return Precio;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getCategoria() {
        return Categoria;
    }

    
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setPrecio(String Precio) {
        this.Precio = Precio;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    
}
