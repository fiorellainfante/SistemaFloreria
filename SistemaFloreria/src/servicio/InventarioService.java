package servicio;

import conexion.ConexionDB;
import dao.ProductoDAO;
import dao.MovimientoDAO;
import modelo.Producto;
import modelo.Movimiento;
import modelo.TipoMovimiento;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InventarioService {

    private final ProductoDAO productoDAO;
    private final MovimientoDAO movimientoDAO;

    public InventarioService() {
        productoDAO = new ProductoDAO();
        movimientoDAO = new MovimientoDAO();
    }

    public boolean agregarProducto(Producto producto) {

        if (producto == null || !producto.esValido()) {
            return false;
        }

        return productoDAO.guardar(producto) > 0;
    }

    public List<Producto> obtenerProductos() {
        return productoDAO.listarTodos();
    }

    public Producto buscarProducto(int id) {
        return productoDAO.buscarPorId(id);
    }

    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }

    public boolean modificarProducto(Producto producto) {

        if (producto == null || !producto.esValido() || producto.getId() <= 0) {
            return false;
        }

        return productoDAO.actualizar(producto);
    }

    public boolean eliminarProducto(int id) {
        return productoDAO.eliminar(id);
    }

    public boolean registrarVenta(Producto producto, int cantidad) {

        if (producto == null) {
            return false;
        }

        return registrarMovimientoStock(
                producto.getId(),
                cantidad,
                TipoMovimiento.VENTA,
                "Venta de producto"
        );
    }

    public boolean registrarReposicion(Producto producto, int cantidad) {

        if (producto == null) {
            return false;
        }

        return registrarMovimientoStock(
                producto.getId(),
                cantidad,
                TipoMovimiento.REPOSICION,
                "Reposicion de producto"
        );
    }

    public boolean registrarDeterioro(Producto producto, int cantidad) {

        if (producto == null) {
            return false;
        }

        return registrarMovimientoStock(
                producto.getId(),
                cantidad,
                TipoMovimiento.DETERIORO,
                "Deterioro de producto"
        );
    }

    private boolean registrarMovimientoStock(int idProducto, int cantidad,
            TipoMovimiento tipo, String observacion) {

        if (idProducto <= 0 || cantidad <= 0 || tipo == null) {
            return false;
        }

        try (Connection conn = ConexionDB.conectar()) {

            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try {

                Producto producto = productoDAO.buscarPorId(conn, idProducto);

                if (producto == null) {
                    conn.rollback();
                    return false;
                }

                boolean stockModificado;

                if (tipo == TipoMovimiento.REPOSICION) {
                    stockModificado = producto.aumentarStock(cantidad);
                } else {
                    stockModificado = producto.disminuirStock(cantidad);
                }

                if (!stockModificado) {
                    conn.rollback();
                    return false;
                }

                if (!productoDAO.actualizar(conn, producto)) {
                    conn.rollback();
                    return false;
                }

                double precioUnitario = tipo == TipoMovimiento.VENTA
                        ? producto.getPrecio() : 0;

                Movimiento movimiento = new Movimiento(
                        producto,
                        tipo,
                        cantidad,
                        observacion,
                        precioUnitario
                );

                if (!movimientoDAO.registrar(conn, movimiento)) {
                    conn.rollback();
                    return false;
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                System.out.println("Error al registrar movimiento: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error en la transaccion: " + e.getMessage());
            return false;
        }
    }

    public List<Movimiento> obtenerMovimientos() {
        return movimientoDAO.listarTodos();
    }

    public List<Producto> obtenerProductosPorAgotarse() {
        return productoDAO.listarProductosConStockBajo();
    }

    public List<Movimiento> obtenerMovimientosPorFecha(LocalDate fecha) {
        return movimientoDAO.listarPorFecha(fecha);
    }

    public List<Movimiento> obtenerMovimientosPorTipo(TipoMovimiento tipo) {
        return movimientoDAO.listarPorTipo(tipo);
    }

    public List<Movimiento> obtenerMovimientosPorProducto(int idProducto) {
        return movimientoDAO.listarPorProducto(idProducto);
    }

    public double obtenerTotalVentasDelDia(LocalDate fecha) {

        double total = 0;

        List<Movimiento> movimientos = obtenerMovimientosPorFecha(fecha);

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo() == TipoMovimiento.VENTA) {
                total += movimiento.getImporteTotal();
            }
        }

        return total;
    }

    public int obtenerCantidadVentasDelDia(LocalDate fecha) {

        int cantidad = 0;

        List<Movimiento> movimientos = obtenerMovimientosPorFecha(fecha);

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo() == TipoMovimiento.VENTA) {
                cantidad += movimiento.getCantidad();
            }
        }

        return cantidad;
    }

    public int obtenerCantidadReposicionesDelDia(LocalDate fecha) {

        int cantidad = 0;

        List<Movimiento> movimientos = obtenerMovimientosPorFecha(fecha);

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo() == TipoMovimiento.REPOSICION) {
                cantidad += movimiento.getCantidad();
            }
        }

        return cantidad;
    }
}
