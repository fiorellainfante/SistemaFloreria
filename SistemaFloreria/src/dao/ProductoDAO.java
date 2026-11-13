package dao;

import conexion.ConexionDB;
import modelo.Producto;
import modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public int guardar(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, stock, stock_minimo) VALUES (?, ?, ?, ?, ?)";
        int idGenerado = -1; // -1 significa que falló
        
        // Usamos RETURN_GENERATED_KEYS para pedirle a SQLite el ID que asignó
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria().name());
            pstmt.setDouble(3, p.getPrecio());
            pstmt.setInt(4, p.getStock());
            pstmt.setInt(5, p.getStockMinimo());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Leemos el ID que generó la base de datos
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        p.setId(idGenerado); // Se lo asignamos al objeto en memoria
                    }
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al guardar producto: " + e.getMessage());
        }
        return idGenerado;
    }
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, categoria = ?, precio = ?, stock = ?, stock_minimo = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria().name());
            pstmt.setDouble(3, p.getPrecio());
            pstmt.setInt(4, p.getStock());
            pstmt.setInt(5, p.getStockMinimo());
            pstmt.setInt(6, p.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0; 
            
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Producto p = null;
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setStockMinimo(rs.getInt("stock_minimo"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return p;
    }
}