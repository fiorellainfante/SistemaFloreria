package servicio;

import java.util.ArrayList;
import java.util.List;

import modelo.Producto;
import modelo.Movimiento;
import modelo.TipoMovimiento;

public class InventarioService {
	private List<Producto> productos;
	private List<Movimiento> movimientos;
	
	public InventarioService() {
	    productos = new ArrayList<>();
	    movimientos = new ArrayList<>();
	}
	
	public boolean agregarProducto(Producto producto) {

	    if (buscarProducto(producto.getId()) != null) {
	        return false;
	    }

	    productos.add(producto);
	    return true;
	}
	
	public List<Producto> obtenerProductos() {
	    return new ArrayList<>(productos);
	}
	
	public Producto buscarProducto(int id) {
	    for (Producto producto : productos) {
	        if (producto.getId() == id) {
	            return producto;
	        }
	    }

	    return null;
	}
	
	public boolean registrarVenta(Producto producto, int cantidad) {
		
		if (!productos.contains(producto)) {
		    return false;
		}

	    if (!producto.disminuirStock(cantidad)) {
	        return false;
	    }

	    Movimiento movimiento = new Movimiento(
	        movimientos.size() + 1,
	        producto,
	        TipoMovimiento.VENTA,
	        cantidad,
	        "Venta de producto"
	    );

	    movimientos.add(movimiento);

	    return true;
	}
	
	public boolean registrarReposicion(Producto producto, int cantidad) {
		
		if (!productos.contains(producto)) {
		    return false;
		}

	    if (cantidad <= 0) {
	        return false;
	    }

	    producto.aumentarStock(cantidad);

	    Movimiento movimiento = new Movimiento(
	        movimientos.size() + 1,
	        producto,
	        TipoMovimiento.REPOSICION,
	        cantidad,
	        "Reposicion de producto"
	    );

	    movimientos.add(movimiento);

	    return true;
	}
	
	public List<Movimiento> obtenerMovimientos() {
	    return new ArrayList<>(movimientos);
	}
	
	public List<Producto> obtenerProductosPorAgotarse() {
	    List<Producto> productosPorAgotarse = new ArrayList<>();

	    for (Producto producto : productos) {
	        if (producto.estaPorAgotarse()) {
	            productosPorAgotarse.add(producto);
	        }
	    }

	    return productosPorAgotarse;
	}

}