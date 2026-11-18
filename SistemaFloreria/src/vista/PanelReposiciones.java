package vista;

import modelo.Producto;
import servicio.InventarioService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

public class PanelReposiciones extends JPanel {

    private JComboBox<Producto> cmbProductos;
    private JSpinner spnCantidad;
    private JLabel lblStockActual;
    private JLabel lblStockFinal;
    private JButton btnRegistrar;

    private final InventarioService servicio;

    public PanelReposiciones() {
        servicio = new InventarioService();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        setBackground(new Color(248, 248, 245));

        crearInterfaz();
        configurarEventos();
        cargarProductos();
    }

    private void crearInterfaz() {
        JLabel titulo = new JLabel("Registrar reposición");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitulo = new JLabel(
                "Ingresá mercadería para actualizar el inventario.");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 5, 5));
        encabezado.setBackground(getBackground());
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        add(encabezado, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        cmbProductos = new JComboBox<>();
        cmbProductos.setPreferredSize(new Dimension(300, 32));

        cmbProductos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value instanceof Producto) {
                    Producto producto = (Producto) value;
                    setText(producto.getNombre() + " - Stock: "
                            + producto.getStock());
                }

                return this;
            }
        });

        spnCantidad = new JSpinner(
                new SpinnerNumberModel(1, 1, 999999, 1));

        lblStockActual = new JLabel("-");
        lblStockFinal = new JLabel("-");
        lblStockFinal.setFont(new Font("Arial", Font.BOLD, 20));
        lblStockFinal.setForeground(new Color(50, 130, 80));

        agregarCampo(formulario, gbc, 0, "Producto:", cmbProductos);
        agregarCampo(formulario, gbc, 1, "Cantidad a reponer:", spnCantidad);
        agregarCampo(formulario, gbc, 2, "Stock actual:", lblStockActual);
        agregarCampo(formulario, gbc, 3, "Stock después de reponer:", lblStockFinal);

        btnRegistrar = new JButton("Confirmar reposición");
        btnRegistrar.setBackground(new Color(70, 130, 90));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setPreferredSize(new Dimension(200, 40));

        gbc.gridx = 1;
        gbc.gridy = 4;
        formulario.add(btnRegistrar, gbc);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(getBackground());
        contenedor.add(formulario);

        add(contenedor, BorderLayout.CENTER);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc,
            int fila, String texto, JComponent componente) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(componente, gbc);
    }

    private void configurarEventos() {
        cmbProductos.addActionListener(e -> actualizarInformacion());

        spnCantidad.addChangeListener(e -> actualizarInformacion());

        JSpinner.DefaultEditor editor =
                (JSpinner.DefaultEditor) spnCantidad.getEditor();

        editor.getTextField().getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarInformacionDesdeTexto();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarInformacionDesdeTexto();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarInformacionDesdeTexto();
            }
        });

        btnRegistrar.addActionListener(e -> registrarReposicion());
    }

    private void cargarProductos() {
        cmbProductos.removeAllItems();

        List<Producto> productos = servicio.obtenerProductos();

        for (Producto producto : productos) {
            cmbProductos.addItem(producto);
        }

        actualizarInformacion();
    }

    private void actualizarInformacionDesdeTexto() {
        JSpinner.DefaultEditor editor =
                (JSpinner.DefaultEditor) spnCantidad.getEditor();

        String texto = editor.getTextField().getText().trim();

        if (texto.isEmpty()) {
            actualizarStockResultante(null);
            return;
        }

        try {
            int cantidad = Integer.parseInt(texto);

            if (cantidad < 1 || cantidad > 999999) {
                actualizarStockResultante(null);
                return;
            }

            actualizarStockResultante(cantidad);

        } catch (NumberFormatException e) {
            actualizarStockResultante(null);
        }
    }

    private void actualizarInformacion() {
        actualizarStockResultante((int) spnCantidad.getValue());
    }

    private void actualizarStockResultante(Integer cantidad) {
        Producto producto = (Producto) cmbProductos.getSelectedItem();

        if (producto == null) {
            lblStockActual.setText("-");
            lblStockFinal.setText("-");
            btnRegistrar.setEnabled(false);
            return;
        }

        lblStockActual.setText(String.valueOf(producto.getStock()));

        if (cantidad == null) {
            lblStockFinal.setText("-");
            btnRegistrar.setEnabled(false);
            return;
        }

        long stockFinal = (long) producto.getStock() + cantidad;

        if (stockFinal > Integer.MAX_VALUE) {
            lblStockFinal.setText("Cantidad no válida");
            btnRegistrar.setEnabled(false);
            return;
        }

        lblStockFinal.setText(String.valueOf(stockFinal));
        btnRegistrar.setEnabled(true);
    }

    private void registrarReposicion() {
        try {
            spnCantidad.commitEdit();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingresá una cantidad válida.",
                    "Cantidad incorrecta",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto productoSeleccionado =
                (Producto) cmbProductos.getSelectedItem();

        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos registrados.");
            return;
        }

        int cantidad = (int) spnCantidad.getValue();

        if ((long) productoSeleccionado.getStock() + cantidad
                > Integer.MAX_VALUE) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad supera el stock máximo permitido.",
                    "Cantidad no válida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        long stockFinal = (long) productoSeleccionado.getStock() + cantidad;

        int respuesta = JOptionPane.showConfirmDialog(this,
                "Producto: " + productoSeleccionado.getNombre()
                + "\nCantidad a reponer: " + cantidad
                + "\nStock actual: " + productoSeleccionado.getStock()
                + "\nStock resultante: " + stockFinal
                + "\n\n¿Confirmar la reposición?",
                "Confirmar reposición",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            if (servicio.registrarReposicion(productoSeleccionado, cantidad)) {
                JOptionPane.showMessageDialog(this,
                        "Reposición registrada correctamente.");

                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la reposición.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

                cargarProductos();
            }
        }
    }

    public void actualizarDatos() {
        cargarProductos();
    }
}
