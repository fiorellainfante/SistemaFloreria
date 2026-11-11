package vista;

import javax.swing.*;
import java.awt.*;

public class FormularioProducto extends JDialog {

    private JTextField txtNombre, txtPrecio, txtStock;
    private JComboBox<String> cbCategoria;
    private JButton btnGuardar, btnCancelar;
    private boolean guardadoExitoso = false;

    public FormularioProducto(JFrame padre) {
        super(padre, "Datos del Producto", true);
        setSize(350, 380);
        setLocationRelativeTo(padre);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Fila 1: Nombre ---
        JPanel filaNombre = new JPanel(new BorderLayout(5, 5));
        filaNombre.add(new JLabel("Nombre:"), BorderLayout.NORTH);
        txtNombre = new JTextField();
        filaNombre.add(txtNombre, BorderLayout.CENTER);
        panelPrincipal.add(filaNombre);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // --- Fila 2: Categoría ---
        JPanel filaCategoria = new JPanel(new BorderLayout(5, 5));
        filaCategoria.add(new JLabel("Categoría:"), BorderLayout.NORTH);
        cbCategoria = new JComboBox<>(new String[]{"PLANTA", "FLOR", "SEMILLA"});
        filaCategoria.add(cbCategoria, BorderLayout.CENTER);
        panelPrincipal.add(filaCategoria);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // --- Fila 3: Precio ---
        JPanel filaPrecio = new JPanel(new BorderLayout(5, 5));
        filaPrecio.add(new JLabel("Precio:"), BorderLayout.NORTH);
        txtPrecio = new JTextField();
        filaPrecio.add(txtPrecio, BorderLayout.CENTER);
        panelPrincipal.add(filaPrecio);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // --- Fila 4: Stock ---
        JPanel filaStock = new JPanel(new BorderLayout(5, 5));
        filaStock.add(new JLabel("Stock:"), BorderLayout.NORTH);
        txtStock = new JTextField();
        filaStock.add(txtStock, BorderLayout.CENTER);
        panelPrincipal.add(filaStock);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // --- Fila 5: Botones Guardar/Cancelar ---
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        filaBotones.add(btnGuardar);
        filaBotones.add(btnCancelar);
        panelPrincipal.add(filaBotones);

        add(panelPrincipal);

        // --- Eventos ---
        btnCancelar.addActionListener(e -> dispose()); 
        
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completá todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Double.parseDouble(txtPrecio.getText());
                Integer.parseInt(txtStock.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio y Stock deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            guardadoExitoso = true;
            dispose();
        });
    }

    // --- Getters ---
    public boolean isGuardadoExitoso() { return guardadoExitoso; }
    public String getNombreProducto() { return txtNombre.getText(); }
    public String getCategoriaSeleccionada() { return cbCategoria.getSelectedItem().toString(); }
    public double getPrecioProducto() { return Double.parseDouble(txtPrecio.getText()); }
    public int getStockProducto() { return Integer.parseInt(txtStock.getText()); }
}