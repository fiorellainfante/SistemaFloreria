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
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.observacion = observacion;
        this.fecha = LocalDateTime.now();
    }

    public Movimiento(int id, Producto producto, TipoMovimiento tipo, int cantidad, String observacion) {
        this.id = id;
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.observacion = observacion;
        this.fecha = LocalDateTime.now();
    }

    public void setId(int id) { this.id = id; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setObservacion(String observacion) { this.observacion = observacion; }


    public int getId() { return id; }
    public Producto getProducto() { return producto; }
    public TipoMovimiento getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public LocalDateTime getFecha() { return fecha; }
    public String getObservacion() { return observacion; }
}