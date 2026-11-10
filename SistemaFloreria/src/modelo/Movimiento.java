package modelo;

import java.time.LocalDateTime;

public class Movimiento {

    private int id;
    private Producto producto;
    private TipoMovimiento tipo;
    private int cantidad;
    private LocalDateTime fecha;
    private String observacion;

    public Movimiento(int id, Producto producto, TipoMovimiento tipo,
    		int cantidad, String observacion) {

	this.id = id;
	this.producto = producto;
	this.tipo = tipo;
	this.cantidad = cantidad;
	this.fecha = LocalDateTime.now();
	this.observacion = observacion;
    }

	public int getId() {
	    return id;
	}
	
	public Producto getProducto() {
	    return producto;
	}
	
	public TipoMovimiento getTipo() {
	    return tipo;
	}
	
	public int getCantidad() {
	    return cantidad;
	}
	
	public LocalDateTime getFecha() {
	    return fecha;
	}
	
	public String getObservacion() {
	    return observacion;
	}
}