package vista;

import modelo.Producto;
import servicio.InventarioService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class PanelVentas extends JPanel {

    private JComboBox<Producto> cmbProductos;
    private JSpinner spnCantidad;
    private JLabel lblPrecio;
    private JLabel lblStock;
    private JLabel lblTotal;
    private JButton btnRegistrar;

    private final InventarioService servicio;
    private final NumberFormat formatoMoneda;

    public PanelVentas() {
        servicio = new InventarioService();
        formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        setBackground(new Color(248, 248, 245));

        crearInterfaz();
        configurarEventos();
        cargarProductos();
    }

    private void crearInterfaz() {
        JLabel titulo = new JLabel("Registrar venta");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Seleccioná un producto e ingresá la cantidad.");
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

        lblPrecio = new JLabel("$0,00");
        lblStock = new JLabel("Stock disponible: -");
        lblTotal = new JLabel("$0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));
        lblTotal.setForeground(new Color(50, 120, 75));

        agregarCampo(formulario, gbc, 0, "Producto:", cmbProductos);
        agregarCampo(formulario, gbc, 1, "Cantidad:", spnCantidad);
        agregarCampo(formulario, gbc, 2, "Precio unitario:", lblPrecio);
        agregarCampo(formulario, gbc, 3, "Disponibilidad:", lblStock);
        agregarCampo(formulario, gbc, 4, "Total de la venta:", lblTotal);

        btnRegistrar = new JButton("Confirmar venta");
        btnRegistrar.setBackground(new Color(70, 130, 90));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setPreferredSize(new Dimension(180, 40));

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
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

        btnRegistrar.addActionListener(e -> registrarVenta());
    }

    private void cargarProductos() {
        cmbProductos.removeAllItems();

        List<Producto> productos = servicio.obtenerProductos();

        for (Producto producto : productos) {
            if (producto.getStock() > 0) {
                cmbProductos.addItem(producto);
            }
        }

        actualizarInformacion();
    }

    private void actualizarInformacionDesdeTexto() {
        JSpinner.DefaultEditor editor =
                (JSpinner.DefaultEditor) spnCantidad.getEditor();

        String texto = editor.getTextField().getText().trim();

        if (texto.isEmpty()) {
            actualizarTotal(null);
            return;
        }

        try {
            int cantidad = Integer.parseInt(texto);

            if (cantidad < 1 || cantidad > 999999) {
                actualizarTotal(null);
                return;
            }

            actualizarTotal(cantidad);

        } catch (NumberFormatException e) {
            actualizarTotal(null);
        }
    }

    private void actualizarInformacion() {
        actualizarTotal((int) spnCantidad.getValue());
    }

    private void actualizarTotal(Integer cantidad) {
        Producto producto = (Producto) cmbProductos.getSelectedItem();

        if (producto == null) {
            lblPrecio.setText("$0,00");
            lblStock.setText("Sin productos disponibles");
            lblTotal.setText("$0,00");
            btnRegistrar.setEnabled(false);
            return;
        }

        lblPrecio.setText(formatoMoneda.format(producto.getPrecio()));
        lblStock.setText("Stock disponible: " + producto.getStock());

        if (cantidad == null) {
            lblTotal.setText("-");
            btnRegistrar.setEnabled(false);
            return;
        }

        double total = producto.getPrecio() * cantidad;
        lblTotal.setText(formatoMoneda.format(total));

        btnRegistrar.setEnabled(true);
    }

    private void registrarVenta() {
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
                    "No hay productos disponibles para vender.");
            return;
        }

        int cantidad = (int) spnCantidad.getValue();

        if (cantidad > productoSeleccionado.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "No hay suficiente stock disponible.",
                    "Stock insuficiente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = productoSeleccionado.getPrecio() * cantidad;

        int respuesta = JOptionPane.showConfirmDialog(this,
                "Producto: " + productoSeleccionado.getNombre()
                + "\nCantidad: " + cantidad
                + "\nTotal: " + formatoMoneda.format(total)
                + "\n\n¿Confirmar la venta?",
                "Confirmar venta",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            if (servicio.registrarVenta(productoSeleccionado, cantidad)) {
                JOptionPane.showMessageDialog(this,
                        "Venta registrada correctamente.");

                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la venta. Verificá el stock disponible.",
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
