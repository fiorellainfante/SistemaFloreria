package modelo;

public class Producto {
	
	private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
    private int stockMinimo;
    
public Producto(int id, String nombre, String categoria,
		double precio, int stock, int stockMinimo) {

	this.id = id;
	this.nombre = nombre;
	this.categoria = categoria;
	this.precio = precio;
	this.stock = stock;
	this.stockMinimo = stockMinimo;
}

//GETTERS

public int getId() {
 return id;
}

public String getNombre() {
 return nombre;
}

public String getCategoria() {
 return categoria;
}

public double getPrecio() {
 return precio;
}

public int getStock() {
 return stock;
}

public int getStockMinimo() {
 return stockMinimo;
}

//SETTERS

public void setNombre(String nombre) {
 this.nombre = nombre;
}

public void setCategoria(String categoria) {
 this.categoria = categoria;
}

public void setPrecio(double precio) {
 this.precio = precio;
}

public void setStockMinimo(int stockMinimo) {
 this.stockMinimo = stockMinimo;
}

}
