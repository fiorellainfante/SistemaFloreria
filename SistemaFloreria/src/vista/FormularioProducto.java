package vista;

import modelo.Categoria;
import modelo.Producto;

import javax.swing.*;
import java.awt.*;

public class FormularioProducto extends JDialog {
	
	private static final long serialVersionUID = 1L;
    private JTextField txtNombre;
    private JComboBox<Categoria> cmbCategoria;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtStockMinimo;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private Producto productoOriginal;
    private Producto productoResultado;
    private boolean confirmado;

    public FormularioProducto(JFrame ventanaPadre, Producto producto) {
        super(ventanaPadre, true);

        this.productoOriginal = producto;
        this.confirmado = false;

        setTitle(producto == null ? "Agregar producto" : "Editar producto");
        setSize(500, 400);
        setLocationRelativeTo(ventanaPadre);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        crearFormulario();
        crearBotones();

        if (producto != null) {
            cargarDatosProducto();
        }
    }

    private void crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(20);
        cmbCategoria = new JComboBox<>(Categoria.values());
        txtPrecio = new JTextField(20);
        txtStock = new JTextField(20);
        txtStockMinimo = new JTextField(20);

        agregarCampo(panel, gbc, 0, "Nombre:", txtNombre);
        agregarCampo(panel, gbc, 1, "Categoría:", cmbCategoria);
        agregarCampo(panel, gbc, 2, "Precio:", txtPrecio);
        agregarCampo(panel, gbc, 3, "Stock:", txtStock);
        agregarCampo(panel, gbc, 4, "Stock mínimo:", txtStockMinimo);

        add(panel, BorderLayout.CENTER);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc,
            int fila, String texto, JComponent campo) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;

        panel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(campo, gbc);
    }

    private void crearBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarProducto());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatosProducto() {
        txtNombre.setText(productoOriginal.getNombre());
        cmbCategoria.setSelectedItem(productoOriginal.getCategoria());
        txtPrecio.setText(String.valueOf(productoOriginal.getPrecio()));
        txtStock.setText(String.valueOf(productoOriginal.getStock()));
        txtStockMinimo.setText(String.valueOf(productoOriginal.getStockMinimo()));
    }

    private void guardarProducto() {
        try {
            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingresá el nombre del producto.");
                return;
            }

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim().replace(",", "."));

            int stock = Integer.parseInt(txtStock.getText().trim());
            int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());

            if (!Double.isFinite(precio) || precio < 0
                    || stock < 0 || stockMinimo < 0) {
                JOptionPane.showMessageDialog(this,
                        "Los valores numéricos no pueden ser negativos.");
                return;
            }

            Producto producto = new Producto(
                    nombre,
                    (Categoria) cmbCategoria.getSelectedItem(),
                    precio,
                    stock,
                    stockMinimo
            );

            if (productoOriginal != null) {
                producto.setId(productoOriginal.getId());
            }

            productoResultado = producto;
            confirmado = true;

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingresá valores numéricos válidos para precio y stock.",
                    "Datos incorrectos",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Datos incorrectos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Producto getProducto() {
        return productoResultado;
    }
}
