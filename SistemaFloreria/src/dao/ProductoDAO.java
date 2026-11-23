package dao;

import conexion.ConexionDB;
import modelo.Producto;
import modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public int guardar(Producto p) {
        try (Connection conexion = ConexionDB.conectar()) {
            if (conexion == null) {
                return -1;
            }
            return guardar(conexion, p);
        } catch (SQLException e) {
            System.out.println("Error al guardar producto: " + e.getMessage());
            return -1;
        }
    }

    public int guardar(Connection conexion, Producto p) throws SQLException {

        if (p == null || !p.esValido()) {
            return -1;
        }

        String sql = "INSERT INTO productos "
                + "(nombre, categoria, precio, stock, stock_minimo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria().name());
            pstmt.setDouble(3, p.getPrecio());
            pstmt.setInt(4, p.getStock());
            pstmt.setInt(5, p.getStockMinimo());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        p.setId(idGenerado);
                        return idGenerado;
                    }
                }
            }
        }

        return -1;
    }

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre COLLATE NOCASE";

        try (Connection conexion = ConexionDB.conectar()) {

            if (conexion == null) {
                return lista;
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    public boolean actualizar(Producto p) {
        try (Connection conexion = ConexionDB.conectar()) {
            if (conexion == null) {
                return false;
            }
            return actualizar(conexion, p);
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Connection conexion, Producto p) throws SQLException {

        if (p == null || !p.esValido() || p.getId() <= 0) {
            return false;
        }

        String sql = "UPDATE productos SET nombre = ?, categoria = ?, precio = ?, "
                + "stock = ?, stock_minimo = ? WHERE id = ?";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria().name());
            pstmt.setDouble(3, p.getPrecio());
            pstmt.setInt(4, p.getStock());
            pstmt.setInt(5, p.getStockMinimo());
            pstmt.setInt(6, p.getId());

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) {

        String sql = "DELETE FROM productos "
                + "WHERE id = ? "
                + "AND NOT EXISTS (SELECT 1 FROM movimientos WHERE id_producto = ?)";

        try (Connection conexion = ConexionDB.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                pstmt.setInt(2, id);

                return pstmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public Producto buscarPorId(int id) {

        String sql = "SELECT * FROM productos WHERE id = ?";

        try (Connection conexion = ConexionDB.conectar()) {

            if (conexion == null) {
                return null;
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearProducto(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }

        return null;
    }
    
    public Producto buscarPorId(Connection conexion, int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ?";

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        }

        return null;
    }

    public List<Producto> buscarPorNombre(String nombre) {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos "
                + "WHERE nombre LIKE ? COLLATE NOCASE "
                + "ORDER BY nombre COLLATE NOCASE";

        try (Connection conexion = ConexionDB.conectar()) {

            if (conexion == null) {
                return lista;
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

                pstmt.setString(1, "%" + nombre + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearProducto(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar productos: " + e.getMessage());
        }

        return lista;
    }

    public List<Producto> listarProductosConStockBajo() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos "
                + "WHERE stock <= stock_minimo "
                + "ORDER BY stock ASC";

        try (Connection conexion = ConexionDB.conectar()) {

            if (conexion == null) {
                return lista;
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar productos con stock bajo: " + e.getMessage());
        }

        return lista;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {

        Producto p = new Producto();

        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setStockMinimo(rs.getInt("stock_minimo"));

        return p;
    }
}
