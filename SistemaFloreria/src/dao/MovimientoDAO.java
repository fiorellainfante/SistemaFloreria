package dao;

import conexion.ConexionDB;
import modelo.Movimiento;
import modelo.Producto;
import modelo.TipoMovimiento;
import modelo.Categoria;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    public boolean registrar(Movimiento m) {
        String sql = "INSERT INTO movimientos (id_producto, tipo_movimiento, cantidad, fecha_hora, observacion) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, m.getProducto().getId());
            pstmt.setString(2, m.getTipo().name());
            pstmt.setInt(3, m.getCantidad());
            pstmt.setString(4, m.getFecha().toString()); 
            pstmt.setString(5, m.getObservacion());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
            return false;
        }
    }

    public List<Movimiento> listarTodos() {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT m.*, p.nombre, p.categoria, p.precio, p.stock, p.stock_minimo " +
                     "FROM movimientos m " +
                     "INNER JOIN productos p ON m.id_producto = p.id " +
                     "ORDER BY m.fecha_hora DESC";
        
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId(rs.getInt("id"));
                m.setTipo(TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
                m.setCantidad(rs.getInt("cantidad"));
                
                String fechaString = rs.getString("fecha_hora");
                if (fechaString != null && fechaString.contains(".")) {
                     fechaString = fechaString.substring(0, fechaString.indexOf("."));
                }
                m.setFecha(LocalDateTime.parse(fechaString));
                
                m.setObservacion(rs.getString("observacion"));

                Producto p = new Producto();
                p.setId(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                
                m.setProducto(p);
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }
}