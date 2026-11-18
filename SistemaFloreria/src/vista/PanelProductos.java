package vista;

import modelo.Producto;
import servicio.InventarioService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PanelProductos extends JPanel {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JLabel lblCantidad;
    private JLabel lblVistaPrevia;

    private final InventarioService servicio;
    private List<Producto> productosActuales = new ArrayList<>();

    public PanelProductos() {
        servicio = new InventarioService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(248, 248, 245));

        crearEncabezado();
        crearTabla();
        crearBotones();
        configurarEventos();

        cargarProductos();
    }

    private void crearEncabezado() {
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(getBackground());

        JLabel titulo = new JLabel("Gestión de productos");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(getBackground());

        JLabel lblBuscar = new JLabel("Buscar producto:");
        txtBuscar = new JTextField(20);

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);

        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void crearTabla() {
        String[] columnas = {
            "ID", "Imagen", "Nombre", "Categoría", "Precio", "Stock"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return ImageIcon.class;
                    case 4:
                        return Double.class;
                    case 5:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(50);
        tablaProductos.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaProductos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setAutoCreateRowSorter(true);

        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(45);
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(55);

        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(65);
        tablaProductos.getColumnModel().getColumn(1).setMaxWidth(75);

        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(150);

        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(75);
        tablaProductos.getColumnModel().getColumn(3).setMaxWidth(85);

        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(4).setMaxWidth(90);

        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(55);
        tablaProductos.getColumnModel().getColumn(5).setMaxWidth(65);

        tablaProductos.getColumnModel().getColumn(1)
                .setCellRenderer(new ImageCellRenderer());
        
        tablaProductos.getColumnModel().getColumn(0)
        .setCellRenderer(centrarContenido());

        tablaProductos.getColumnModel().getColumn(4)
        .setCellRenderer(centrarContenido());

        tablaProductos.getColumnModel().getColumn(5)
        .setCellRenderer(centrarContenido());

        tablaProductos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component componente = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                int filaModelo = table.convertRowIndexToModel(row);

                if (filaModelo < productosActuales.size()) {
                    Producto producto = productosActuales.get(filaModelo);

                    if (!isSelected) {
                        if (producto.getStock() <= producto.getStockMinimo()) {
                            componente.setBackground(new Color(255, 220, 220));
                            componente.setForeground(new Color(150, 30, 30));
                        } else {
                            componente.setBackground(Color.WHITE);
                            componente.setForeground(Color.BLACK);
                        }
                    }
                }

                return componente;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        lblVistaPrevia = new JLabel("Sin selección", SwingConstants.CENTER);
        lblVistaPrevia.setPreferredSize(new Dimension(280, 280));
        lblVistaPrevia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        lblVistaPrevia.setBackground(Color.WHITE);
        lblVistaPrevia.setOpaque(true);

        JPanel panelVistaPrevia = new JPanel(new BorderLayout(5, 5));
        panelVistaPrevia.setBackground(getBackground());
        panelVistaPrevia.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel lblTituloPrevia = new JLabel("Vista previa");
        lblTituloPrevia.setFont(new Font("Arial", Font.BOLD, 14));

        panelVistaPrevia.add(lblTituloPrevia, BorderLayout.NORTH);
        panelVistaPrevia.add(lblVistaPrevia, BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBackground(getBackground());
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        panelCentro.add(panelVistaPrevia, BorderLayout.EAST);

        add(panelCentro, BorderLayout.CENTER);
    }

    private void crearBotones() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(getBackground());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBotones.setBackground(getBackground());

        btnAgregar = new JButton("Agregar producto");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(70, 130, 90));
        btnAgregar.setForeground(Color.WHITE);

        btnEditar.setBackground(new Color(70, 110, 160));
        btnEditar.setForeground(Color.WHITE);

        btnEliminar.setBackground(new Color(180, 70, 70));
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        lblCantidad = new JLabel("Productos: 0");
        lblCantidad.setFont(new Font("Arial", Font.BOLD, 12));

        panelInferior.add(panelBotones, BorderLayout.WEST);
        panelInferior.add(lblCantidad, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            actualizarVistaPrevia();
        });

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                cargarProductos();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cargarProductos();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cargarProductos();
            }
        });
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);

        String busqueda = txtBuscar == null ? "" : txtBuscar.getText().trim();

        if (busqueda.isEmpty()) {
            productosActuales = servicio.obtenerProductos();
        } else {
            productosActuales = servicio.buscarProductosPorNombre(busqueda);
        }

        for (Producto producto : productosActuales) {
            modeloTabla.addRow(new Object[]{
                producto.getId(),
                cargarMiniatura(producto.getRutaImagen(), 45, 45),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock()
            });
        }

        lblCantidad.setText("Productos: " + productosActuales.size());
        actualizarVistaPrevia();
    }

    private ImageIcon cargarMiniatura(String rutaImagen, int ancho, int alto) {
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            return null;
        }

        File archivo = new File(rutaImagen);

        if (!archivo.exists()) {
            return null;
        }

        ImageIcon original = new ImageIcon(archivo.getAbsolutePath());

        if (original.getIconWidth() <= 0) {
            return null;
        }

        Image escalada = original.getImage()
                .getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

        return new ImageIcon(escalada);
    }

    private void actualizarVistaPrevia() {
        int filaSeleccionada = tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            lblVistaPrevia.setIcon(null);
            lblVistaPrevia.setText("Sin selección");
            return;
        }

        int filaModelo = tablaProductos.convertRowIndexToModel(filaSeleccionada);

        if (filaModelo >= productosActuales.size()) {
            lblVistaPrevia.setIcon(null);
            lblVistaPrevia.setText("Sin selección");
            return;
        }

        Producto producto = productosActuales.get(filaModelo);
        ImageIcon icono = cargarMiniatura(producto.getRutaImagen(), 250, 250);

        if (icono != null) {
            lblVistaPrevia.setIcon(icono);
            lblVistaPrevia.setText("");
        } else {
            lblVistaPrevia.setIcon(null);
            lblVistaPrevia.setText("Sin imagen");
        }
    }

    private void agregarProducto() {
        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(this);

        FormularioProducto formulario = new FormularioProducto(ventanaPadre, null);
        formulario.setVisible(true);

        if (formulario.isConfirmado()) {
            Producto producto = formulario.getProducto();

            if (servicio.agregarProducto(producto)) {
                JOptionPane.showMessageDialog(this,
                        "Producto agregado correctamente.");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo agregar el producto.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProducto() {
        Producto producto = obtenerProductoSeleccionado();

        if (producto == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná un producto para editar.");
            return;
        }

        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(this);

        FormularioProducto formulario = new FormularioProducto(ventanaPadre, producto);
        formulario.setVisible(true);

        if (formulario.isConfirmado()) {
            Producto productoModificado = formulario.getProducto();

            if (servicio.modificarProducto(productoModificado)) {
                JOptionPane.showMessageDialog(this,
                        "Producto modificado correctamente.");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo modificar el producto.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        Producto producto = obtenerProductoSeleccionado();

        if (producto == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná un producto para eliminar.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar el producto "
                + producto.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            if (servicio.eliminarProducto(producto.getId())) {
                JOptionPane.showMessageDialog(this,
                        "Producto eliminado correctamente.");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el producto. "
                        + "Puede tener movimientos registrados.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Producto obtenerProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            return null;
        }

        int filaModelo = tablaProductos.convertRowIndexToModel(filaSeleccionada);

        if (filaModelo >= productosActuales.size()) {
            return null;
        }

        return productosActuales.get(filaModelo);
    }

    public void actualizarDatos() {
        cargarProductos();
    }
    
    private DefaultTableCellRenderer centrarContenido() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        return renderer;
    }

    private static class ImageCellRenderer extends DefaultTableCellRenderer {

        private final JLabel label = new JLabel();

        public ImageCellRenderer() {
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            } else {
                int filaModelo = table.convertRowIndexToModel(row);
                label.setBackground(table.getBackground());
                label.setForeground(table.getForeground());
            }

            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
                label.setText(null);
            } else {
                label.setIcon(null);
                label.setText("—");
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }

            return label;
        }
    }
}