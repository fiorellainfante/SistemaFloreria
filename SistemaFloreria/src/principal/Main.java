package principal;

import conexion.ConexionDB;
import dao.ProductoDAO;
import dao.MovimientoDAO;
import modelo.Producto;
import modelo.Movimiento;
import modelo.Categoria;
import modelo.TipoMovimiento;

public class Main {
    public static void main(String[] args) {
        ConexionDB.inicializarBaseDeDatos();
        
        ProductoDAO prodDAO = new ProductoDAO();
        MovimientoDAO movDAO = new MovimientoDAO();
        
        // 1. Guardar un producto nuevo y obtener su ID directamente
        System.out.println("--- GUARDANDO PRODUCTO ---");
        Producto lavanda = new Producto("Lavanda Premium", Categoria.PLANTA, 1500.0, 20, 5);
        
        int idLavanda = prodDAO.guardar(lavanda); // El DAO ahora devuelve el ID
        System.out.println("Producto guardado con ID: " + idLavanda);
        
        // 2. Registrar una venta
        if (idLavanda != -1) {
            // Volvemos a buscar el producto por ID para asegurarnos de tener sus datos actuales
            Producto prodVender = prodDAO.buscarPorId(idLavanda);
            
            System.out.println("\n--- REGISTRANDO VENTA DE 4 UNIDADES ---");
            if (prodVender.disminuirStock(4)) {
                prodDAO.actualizar(prodVender); // Actualiza stock en BD
                
                Movimiento venta = new Movimiento(prodVender, TipoMovimiento.VENTA, 4, "Venta de prueba");
                movDAO.registrar(venta);
                System.out.println("Venta registrada con éxito.");
            }
        }
        
        // 3. Mostrar historial
        System.out.println("\n--- HISTORIAL DE MOVIMIENTOS ---");
        for (Movimiento m : movDAO.listarTodos()) {
            // Mostramos la fecha formateada (solo fecha y hora, sin los nanosegundos)
            java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            System.out.println(m.getFecha().format(formato) + " | " + m.getTipo() + " | " + 
                               m.getProducto().getNombre() + " | Cant: " + m.getCantidad() + 
                               " | Obs: " + m.getObservacion());
        }
    }
}