package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:floreria.db";

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(URL);

            try (Statement stmt = conexion.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }

            return conexion;

        } catch (SQLException e) {
            System.out.println("Error al conectar a SQLite: " + e.getMessage());
            return null;
        }
    }

    public static void inicializarBaseDeDatos() {

        String sqlProductos = "CREATE TABLE IF NOT EXISTS productos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "categoria TEXT NOT NULL, "
                + "precio REAL NOT NULL, "
                + "stock INTEGER NOT NULL, "
                + "stock_minimo INTEGER NOT NULL, "
                + "ruta_imagen TEXT"
                + ")";

        String sqlMovimientos = "CREATE TABLE IF NOT EXISTS movimientos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_producto INTEGER NOT NULL, "
                + "tipo_movimiento TEXT NOT NULL, "
                + "cantidad INTEGER NOT NULL, "
                + "fecha_hora TEXT NOT NULL, "
                + "observacion TEXT, "
                + "precio_unitario REAL NOT NULL DEFAULT 0, "
                + "FOREIGN KEY(id_producto) REFERENCES productos(id)"
                + ")";

        try (Connection conn = conectar()) {

            if (conn == null) {
                return;
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlProductos);
                stmt.execute(sqlMovimientos);
            }

            agregarColumnaSiNoExiste(
                    conn, "productos", "ruta_imagen", "TEXT");

            agregarColumnaSiNoExiste(
                    conn, "movimientos", "precio_unitario",
                    "REAL NOT NULL DEFAULT 0");

            insertarProductosIniciales(conn);

            System.out.println("Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: "
                    + e.getMessage());
        }
    }

    private static void insertarProductosIniciales(Connection conn)
            throws SQLException {

        String sqlCantidad = "SELECT COUNT(*) FROM productos";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCantidad)) {

            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }

        String sql = "INSERT INTO productos "
                + "(id, nombre, categoria, precio, stock, stock_minimo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            insertarProducto(pstmt, 1, "Rosa", "FLOR", 2500, 20, 5);
            insertarProducto(pstmt, 2, "Girasol", "FLOR", 1800, 20, 5);
            insertarProducto(pstmt, 3, "Lavanda", "FLOR", 2200, 20, 5);
            insertarProducto(pstmt, 4, "Margarita", "FLOR", 2000, 20, 5);

            insertarProducto(pstmt, 5, "Semillas de girasol",
                    "SEMILLA", 900, 20, 5);
            insertarProducto(pstmt, 6, "Semillas de albahaca",
                    "SEMILLA", 750, 20, 5);
            insertarProducto(pstmt, 7, "Semillas de manzanilla",
                    "SEMILLA", 700, 20, 5);

            insertarProducto(pstmt, 8, "Helecho", "PLANTA", 4500, 20, 5);
            insertarProducto(pstmt, 9, "Aloe vera", "PLANTA", 3800, 20, 5);
            insertarProducto(pstmt, 10, "Potus", "PLANTA", 3200, 20, 5);
        }
    }

    private static void insertarProducto(PreparedStatement pstmt,
            int id, String nombre, String categoria,
            double precio, int stock, int stockMinimo)
            throws SQLException {

        pstmt.setInt(1, id);
        pstmt.setString(2, nombre);
        pstmt.setString(3, categoria);
        pstmt.setDouble(4, precio);
        pstmt.setInt(5, stock);
        pstmt.setInt(6, stockMinimo);

        pstmt.executeUpdate();
    }

    private static void agregarColumnaSiNoExiste(Connection conn,
            String tabla, String columna, String tipo)
            throws SQLException {

        boolean existe = false;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "PRAGMA table_info(" + tabla + ")")) {

            while (rs.next()) {
                if (columna.equalsIgnoreCase(rs.getString("name"))) {
                    existe = true;
                    break;
                }
            }
        }

        if (!existe) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("ALTER TABLE " + tabla
                        + " ADD COLUMN " + columna + " " + tipo);
            }
        }
    }
}
