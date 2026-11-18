package modelo;

public class Producto {
	
	private int id;
    private String nombre;
    private Categoria categoria;
    private double precio;
    private int stock;
    private int stockMinimo;
    
    
    public Producto() {
    }
    
    public Producto(String nombre, Categoria categoria, double precio, int stock, int stockMinimo) {
        setNombre(nombre);
        setCategoria(categoria);
        setPrecio(precio);
        setStock(stock);
        setStockMinimo(stockMinimo);
    }    
    public Producto(int id, String nombre, Categoria categoria,
            double precio, int stock, int stockMinimo) {

        setId(id);
        setNombre(nombre);
        setCategoria(categoria);
        setPrecio(precio);
        setStock(stock);
        setStockMinimo(stockMinimo);
    }
	
	public void setId(int id) {
	    if (id <= 0) {
	        throw new IllegalArgumentException("El ID debe ser positivo.");
	    }
	    this.id = id;
	}
	
	public void setNombre(String nombre) {
	    if (nombre == null || nombre.trim().isEmpty()) {
	        throw new IllegalArgumentException("El nombre no puede estar vacío.");
	    }
	    this.nombre = nombre;
	}

	public void setCategoria(Categoria categoria) {
	    if (categoria == null) {
	        throw new IllegalArgumentException("La categoría no puede ser nula.");
	    }
	    this.categoria = categoria;
	}

	public void setPrecio(double precio) {
	    if (!Double.isFinite(precio) || precio < 0) {
	        throw new IllegalArgumentException("El precio no es válido.");
	    }
	    this.precio = precio;
	}

	public void setStock(int stock) {
	    if (stock < 0) {
	        throw new IllegalArgumentException("El stock no puede ser negativo.");
	    }
	    this.stock = stock;
		}

	public void setStockMinimo(int stockMinimo) {
	    if (stockMinimo < 0) {
	        throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
	    }
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
	
	public boolean aumentarStock(int cantidad) {
	    if (cantidad <= 0 || cantidad > Integer.MAX_VALUE - stock) {
	        return false;
	    }

	    stock += cantidad;
	    return true;
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
	
	public boolean esValido() {
	    return id > 0
	        && nombre != null
	        && !nombre.trim().isEmpty()
	        && categoria != null
	        && Double.isFinite(precio)
	        && precio >= 0
	        && stock >= 0
	        && stockMinimo >= 0;
	}
}


