package modelo;

public class Producto {
	
	private int id;
    private String nombre;
    private Categoria categoria;
    private double precio;
    private int stock;
    private int stockMinimo;
    
    public Producto(int id, String nombre, Categoria categoria,
    		double precio, int stock, int stockMinimo) {

	this.id = id;
	this.nombre = nombre;
	this.categoria = categoria;
	this.precio = precio;
	this.stock = stock;
	this.stockMinimo = stockMinimo;
}
	
	public int getId() {
	 return id;
	}
	
	public String getNombre() {
	 return nombre;
	}
	
	public Categoria getCategoria() {
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
	
	public void setNombre(String nombre) {
	 this.nombre = nombre;
	}
	
	public void setCategoria(Categoria categoria) {
	 this.categoria = categoria;
	}
	
	public void setPrecio(double precio) {
	 this.precio = precio;
	}
	
	public void setStockMinimo(int stockMinimo) {
	 this.stockMinimo = stockMinimo;
	}
	
	public void aumentarStock(int cantidad) {
	    if (cantidad <= 0) {
	        System.out.println("La cantidad debe ser positiva.");
	    } else {
	        stock += cantidad;
	    }
	}
	
	public boolean disminuirStock(int cantidad) {
	    if (cantidad <= 0) {
	        return false;
	    } 
	    else if (cantidad > stock) {
	        return false;
	    } 
	    else {
	        stock -= cantidad;
	        return true;
	    }
	}
	
	public boolean estaPorAgotarse() {
	    return stock <= stockMinimo;
	}
}
