package dao;

import conexion.ConexionDB;
import modelo.Movimiento;
import modelo.Producto;
import modelo.TipoMovimiento;
import modelo.Categoria;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    public boolean registrar(Movimiento m) {

        try (Connection conn = ConexionDB.conectar()) {

            if (conn == null) {
                return false;
            }

            return registrar(conn, m);

        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
            return false;
        }
    }

    public boolean registrar(Connection conn, Movimiento m) throws SQLException {

        if (m == null || m.getProducto() == null
                || m.getProducto().getId() <= 0) {
            return false;
        }

        String sql = "INSERT INTO movimientos "
                + "(id_producto, tipo_movimiento, cantidad, fecha_hora, observacion, precio_unitario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, m.getProducto().getId());
            pstmt.setString(2, m.getTipo().name());
            pstmt.setInt(3, m.getCantidad());
            pstmt.setString(4, m.getFecha().toString());
            pstmt.setString(5, m.getObservacion());
            pstmt.setDouble(6, m.getPrecioUnitario());

            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Movimiento> listarTodos() {

        String sql = consultaBase()
                + " ORDER BY m.fecha_hora DESC";

        return listar(sql, null);
    }

    public List<Movimiento> listarPorTipo(TipoMovimiento tipo) {

        String sql = consultaBase()
                + " WHERE m.tipo_movimiento = ?"
                + " ORDER BY m.fecha_hora DESC";

        return listar(sql, tipo == null ? null : tipo.name());
    }

    public List<Movimiento> listarPorFecha(LocalDate fecha) {

        String sql = consultaBase()
                + " WHERE DATE(m.fecha_hora) = ?"
                + " ORDER BY m.fecha_hora DESC";

        return listar(sql, fecha == null ? null : fecha.toString());
    }

    public List<Movimiento> listarPorProducto(int idProducto) {

        String sql = consultaBase()
                + " WHERE m.id_producto = ?"
                + " ORDER BY m.fecha_hora DESC";

        return listar(sql, idProducto);
    }

    private List<Movimiento> listar(String sql, Object parametro) {

        List<Movimiento> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.conectar()) {

            if (conn == null) {
                return lista;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                if (parametro != null) {
                    if (parametro instanceof Integer) {
                        pstmt.setInt(1, (Integer) parametro);
                    } else {
                        pstmt.setString(1, parametro.toString());
                    }
                }

                try (ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        lista.add(mapearMovimiento(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }

        return lista;
    }

    private String consultaBase() {

        return "SELECT m.*, "
                + "p.nombre, p.categoria, p.precio, p.stock, "
                + "p.stock_minimo, p.ruta_imagen "
                + "FROM movimientos m "
                + "INNER JOIN productos p ON m.id_producto = p.id";
    }

    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {

        Producto p = new Producto();

        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setRutaImagen(rs.getString("ruta_imagen"));

        String fechaString = rs.getString("fecha_hora");
        LocalDateTime fecha = LocalDateTime.parse(fechaString);

        Movimiento m = new Movimiento(
                rs.getInt("id"),
                p,
                TipoMovimiento.valueOf(rs.getString("tipo_movimiento")),
                rs.getInt("cantidad"),
                fecha,
                rs.getString("observacion"),
                rs.getDouble("precio_unitario")
        );

        return m;
    }
}
