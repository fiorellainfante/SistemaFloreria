package modelo;

import java.time.LocalDateTime;

public class Movimiento {

    private int id;
    private Producto producto;
    private TipoMovimiento tipo;
    private int cantidad;
    private LocalDateTime fecha;
    private String observacion;

    public Movimiento() {
    }

    public Movimiento(Producto producto, TipoMovimiento tipo, int cantidad, String observacion) {
        setProducto(producto);
        setTipo(tipo);
        setCantidad(cantidad);
        setObservacion(observacion);
        setFecha(LocalDateTime.now());
    }

    public Movimiento(int id, Producto producto, TipoMovimiento tipo, int cantidad, String observacion) {
        setId(id);
        setProducto(producto);
        setTipo(tipo);
        setCantidad(cantidad);
        setObservacion(observacion);
        setFecha(LocalDateTime.now());
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo.");
        }
        this.id = id;
    }

    public void setProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        this.producto = producto;
    }

    public void setTipo(TipoMovimiento tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de movimiento no puede ser nulo.");
        }
        this.tipo = tipo;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }
        this.cantidad = cantidad;
    }

    public void setFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.fecha = fecha;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }


    public int getId() { return id; }
    public Producto getProducto() { return producto; }
    public TipoMovimiento getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public LocalDateTime getFecha() { return fecha; }
    public String getObservacion() { return observacion; }
}