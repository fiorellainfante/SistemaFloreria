package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    
	private static final String URL = "jdbc:sqlite:floreria.db";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error al conectar a SQLite: " + e.getMessage());
        }
        return conexion;
    }

    public static void inicializarBaseDeDatos() {
        String sqlProductos = "CREATE TABLE IF NOT EXISTS productos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "categoria TEXT NOT NULL, " +
                "precio REAL NOT NULL, " +
                "stock INTEGER NOT NULL, " +
                "stock_minimo INTEGER NOT NULL);";

        String sqlMovimientos = "CREATE TABLE IF NOT EXISTS movimientos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_producto INTEGER NOT NULL, " +
                "tipo_movimiento TEXT NOT NULL, " +
                "cantidad INTEGER NOT NULL, " +
                "fecha_hora TEXT NOT NULL, " +
                "observacion TEXT, " +
                "FOREIGN KEY(id_producto) REFERENCES productos(id));";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sqlProductos);
                stmt.execute(sqlMovimientos);
                System.out.println("Base de datos y tablas listas.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }
}