package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelProductos extends JPanel {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEditar, btnEliminar;

    public PanelProductos() {
        setLayout(new BorderLayout());

        // 1. Configurar la Tabla
        String[] columnas = {"ID", "Nombre", "Categoría", "Precio", "Stock", "Stock Mín."};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir editar haciendo doble clic
            }
        };
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // 2. Configurar el panel de botones (abajo)
        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar Producto");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        // 3. Acción del botón Agregar (Abre la ventanita)
        btnAgregar.addActionListener(e -> {
            JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(this);
            FormularioProducto form = new FormularioProducto(ventanaPadre);
            form.setVisible(true);

            if (form.isGuardadoExitoso()) {
                int nuevoId = modeloTabla.getRowCount() + 1;
                modeloTabla.addRow(new Object[]{
                    nuevoId,
                    form.getNombreProducto(),
                    form.getCategoriaSeleccionada(),
                    form.getPrecioProducto(),
                    form.getStockProducto(),
                    5 // Stock mínimo por defecto
                });
            }
        });

        // 4. Cargar datos de prueba
        cargarDatosDePrueba();

        // 5. Pintar de rojo las filas con stock bajo
        tablaProductos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Traemos los valores de las columnas Stock (4) y Stock Mín. (5)
                int stock = (int) table.getValueAt(row, 4);
                int stockMinimo = (int) table.getValueAt(row, 5);

                // Si la fila no está seleccionada, aplicamos los colores
                if (!isSelected) {
                    if (stock <= stockMinimo) {
                        c.setBackground(new Color(255, 102, 102)); // Rojo
                        c.setForeground(Color.WHITE); // Letra blanca
                    } else {
                        c.setBackground(Color.WHITE); // Normal
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });
    }

    // Método para llenar la tabla con datos de prueba
    private void cargarDatosDePrueba() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        modeloTabla.addRow(new Object[]{1, "Rosa Roja", "FLOR", 1500.0, 20, 5});
        modeloTabla.addRow(new Object[]{2, "Aloé Vera", "PLANTA", 800.0, 3, 5}); // Este va a salir rojo
        modeloTabla.addRow(new Object[]{3, "Semillas de Girasol", "SEMILLA", 200.0, 50, 10});
    }
}