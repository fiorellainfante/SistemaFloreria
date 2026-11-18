package servicio;
import modelo.Producto;
import modelo.Categoria;
import modelo.Movimiento;
import java.util.List;

public class PruebaInventario {

	public static void main(String[] args) {

	    InventarioService inventario = new InventarioService();

	    Producto rosa = new Producto(
	        1, "Rosa", Categoria.FLOR, 1500, 20, 5
	    );

	    Producto girasol = new Producto(
	        2, "Girasol", Categoria.FLOR, 2000, 10, 3
	    );
	    
	    boolean agregadoRosa = inventario.agregarProducto(rosa);
	    boolean agregadoGirasol = inventario.agregarProducto(girasol);

	    System.out.println("Rosa agregada: " + agregadoRosa);
	    System.out.println("Girasol agregado: " + agregadoGirasol);
	    
	    System.out.println("Cantidad de productos: " + inventario.obtenerProductos().size());
	    
	    Producto encontrado = inventario.buscarProducto(1);

	    if (encontrado != null) {
	        System.out.println("Producto encontrado: " + encontrado.getNombre());
	    } else {
	        System.out.println("Producto no encontrado.");
	    }
	    
	    Producto inexistente = inventario.buscarProducto(99);

	    System.out.println("Búsqueda de ID 99: " + inexistente);
	    
	    System.out.println("Stock de rosas antes de la venta: " + rosa.getStock());

	    boolean ventaExitosa = inventario.registrarVenta(rosa, 5);

	    System.out.println("Venta realizada: " + ventaExitosa);
	    System.out.println("Stock de rosas después de la venta: " + rosa.getStock());
	    
	    boolean ventaFallida = inventario.registrarVenta(rosa, 100);

	    System.out.println("Venta de 100 rosas: " + ventaFallida);
	    System.out.println("Stock actual: " + rosa.getStock());
	    
	    System.out.println("Stock antes de la reposición: " + rosa.getStock());

	    boolean reposicionExitosa = inventario.registrarReposicion(rosa, 10);

	    System.out.println("Reposición realizada: " + reposicionExitosa);
	    System.out.println("Stock después de la reposición: " + rosa.getStock());
	    
	    boolean reposicionFallida = inventario.registrarReposicion(rosa, -5);

	    System.out.println("Reposición inválida: " + reposicionFallida);
	    System.out.println("Stock actual: " + rosa.getStock());
	    
	    System.out.println("Stock antes del deterioro: " + rosa.getStock());

	    boolean deterioroExitoso = inventario.registrarDeterioro(rosa, 3);

	    System.out.println("Deterioro registrado: " + deterioroExitoso);
	    System.out.println("Stock después del deterioro: " + rosa.getStock());
	    
	    boolean deterioroFallido = inventario.registrarDeterioro(rosa, 50);

	    System.out.println("Deterioro inválido: " + deterioroFallido);
	    System.out.println("Stock actual: " + rosa.getStock());
	    
	    System.out.println("\n--- HISTORIAL DE MOVIMIENTOS ---");

	    for (Movimiento movimiento : inventario.obtenerMovimientos()) {

	        System.out.println("ID: " + movimiento.getId());
	        System.out.println("Producto: " + movimiento.getProducto().getNombre());
	        System.out.println("Tipo: " + movimiento.getTipo());
	        System.out.println("Cantidad: " + movimiento.getCantidad());
	        System.out.println("Fecha: " + movimiento.getFecha());
	        System.out.println("Observación: " + movimiento.getObservacion());

	        System.out.println("----------------------");
	    }
	    
	    System.out.println("\n--- PRODUCTOS POR AGOTARSE ---");

	    List<Producto> porAgotarse = inventario.obtenerProductosPorAgotarse();

	    for (Producto producto : porAgotarse) {
	        System.out.println(producto.getNombre() + " - Stock: " + producto.getStock());
	    }

	    System.out.println("Cantidad de productos por agotarse: " + porAgotarse.size());
	    
	    inventario.registrarVenta(rosa, 19);

	    System.out.println("\n--- PRODUCTOS POR AGOTARSE ---");

	    for (Producto producto : inventario.obtenerProductosPorAgotarse()) {
	        System.out.println(producto.getNombre() + " - Stock: " + producto.getStock());
	    }
	    
	    System.out.println("\n--- PRUEBA DE PRECIOS ---");

	    try {
	        rosa.setPrecio(-100);
	        System.out.println("Error: se aceptó un precio negativo.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Correcto: se rechazó el precio negativo.");
	    }
	    
	    Producto rosaDuplicada = new Producto(
	    	    1, "Rosa roja", Categoria.FLOR, 1800, 15, 4
	    	);

	    	boolean agregado = inventario.agregarProducto(rosaDuplicada);

	    	System.out.println("¿Se agregó el producto duplicado? " + agregado);
	    	
	    	System.out.println("\n--- PRUEBA DE PRODUCTO INCOMPLETO ---");

	    	Producto productoVacio = new Producto();

	    	boolean agregadoVacio = inventario.agregarProducto(productoVacio);

	    	System.out.println("¿Se agregó el producto incompleto? " + agregadoVacio);
	    	
	    	System.out.println("\n--- PRUEBA DE CANTIDADES INVÁLIDAS ---");

	    	System.out.println("Venta con -2 unidades: "
	    	    + inventario.registrarVenta(rosa, -2));

	    	System.out.println("Reposición con 0 unidades: "
	    	    + inventario.registrarReposicion(rosa, 0));

	    	System.out.println("Deterioro con -3 unidades: "
	    	    + inventario.registrarDeterioro(rosa, -3));
	    	
	    	System.out.println("\n--- PRUEBA DE LÍMITE DE STOCK ---");

	    	rosa.setStock(Integer.MAX_VALUE);

	    	boolean reposicionLimite = rosa.aumentarStock(1);

	    	System.out.println("¿Se pudo aumentar el stock? " + reposicionLimite);
	    	System.out.println("Stock actual: " + rosa.getStock());

	}

}
