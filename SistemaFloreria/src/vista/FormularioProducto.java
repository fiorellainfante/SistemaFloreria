package vista;

import modelo.Categoria;
import modelo.Producto;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FormularioProducto extends JDialog {

    private JTextField txtNombre;
    private JComboBox<Categoria> cmbCategoria;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtStockMinimo;
    private JTextField txtRutaImagen;

    private JButton btnSeleccionarImagen;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private JLabel lblImagen;

    private Producto productoOriginal;
    private Producto productoResultado;
    private boolean confirmado;

    private File archivoImagenSeleccionado;

    public FormularioProducto(JFrame ventanaPadre, Producto producto) {
        super(ventanaPadre, true);

        this.productoOriginal = producto;
        this.confirmado = false;

        setTitle(producto == null ? "Agregar producto" : "Editar producto");
        setSize(500, 520);
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
        txtRutaImagen = new JTextField(20);
        txtRutaImagen.setEditable(false);

        btnSeleccionarImagen = new JButton("Seleccionar imagen");

        lblImagen = new JLabel("Sin imagen seleccionada",
                SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(150, 100));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        agregarCampo(panel, gbc, 0, "Nombre:", txtNombre);
        agregarCampo(panel, gbc, 1, "Categoría:", cmbCategoria);
        agregarCampo(panel, gbc, 2, "Precio:", txtPrecio);
        agregarCampo(panel, gbc, 3, "Stock:", txtStock);
        agregarCampo(panel, gbc, 4, "Stock mínimo:", txtStockMinimo);
        agregarCampo(panel, gbc, 5, "Imagen:", txtRutaImagen);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(btnSeleccionarImagen, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(lblImagen, gbc);

        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

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

        txtRutaImagen.setText(productoOriginal.getRutaImagen() == null
                ? "" : productoOriginal.getRutaImagen());

        actualizarVistaPrevia();
    }

    private void seleccionarImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar imagen del producto");
        selector.setFileFilter(new FileNameExtensionFilter(
                "Imágenes (JPG, JPEG, PNG, GIF, BMP)",
                "jpg", "jpeg", "png", "gif", "bmp"));

        int resultado = selector.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoImagenSeleccionado = selector.getSelectedFile();

            txtRutaImagen.setText(
                    archivoImagenSeleccionado.getAbsolutePath());

            actualizarVistaPrevia();
        }
    }

    private void actualizarVistaPrevia() {
        String ruta = txtRutaImagen.getText();

        if (ruta == null || ruta.trim().isEmpty()) {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen seleccionada");
            return;
        }

        File archivo = new File(ruta);

        if (!archivo.isAbsolute()) {
            archivo = Paths.get(ruta).toFile();
        }

        if (!archivo.exists()) {
            lblImagen.setIcon(null);
            lblImagen.setText("Imagen no encontrada");
            return;
        }

        ImageIcon imagen = new ImageIcon(archivo.getAbsolutePath());

        if (imagen.getIconWidth() <= 0) {
            lblImagen.setIcon(null);
            lblImagen.setText("No se pudo cargar la imagen");
            return;
        }

        Image imagenEscalada = imagen.getImage().getScaledInstance(
                150, 100, Image.SCALE_SMOOTH);

        lblImagen.setText("");
        lblImagen.setIcon(new ImageIcon(imagenEscalada));
    }

    private String copiarImagenAlProyecto(File archivo)
            throws IOException {

        Path carpetaImagenes = Paths.get("imagenes");
        Files.createDirectories(carpetaImagenes);

        String nombre = txtNombre.getText().trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9áéíóúñ]+", "_")
                .replaceAll("^_|_$", "");

        String extension = obtenerExtension(archivo.getName());

        String nombreArchivo = nombre + "_"
                + UUID.randomUUID().toString().substring(0, 8)
                + "." + extension;

        Path destino = carpetaImagenes.resolve(nombreArchivo);

        Files.copy(
                archivo.toPath(),
                destino,
                StandardCopyOption.REPLACE_EXISTING
        );

        return "imagenes/" + nombreArchivo;
    }

    private String obtenerExtension(String nombreArchivo) {
        int posicion = nombreArchivo.lastIndexOf('.');

        if (posicion == -1) {
            return "png";
        }

        return nombreArchivo.substring(posicion + 1).toLowerCase();
    }

    private String obtenerRutaImagenParaGuardar() throws IOException {
        if (archivoImagenSeleccionado != null) {
            return copiarImagenAlProyecto(archivoImagenSeleccionado);
        }

        if (productoOriginal == null
                || productoOriginal.getRutaImagen() == null) {
            return null;
        }

        String rutaOriginal = productoOriginal.getRutaImagen();
        Path ruta = Paths.get(rutaOriginal);

        if (ruta.isAbsolute() && Files.exists(ruta)) {
            return copiarImagenAlProyecto(ruta.toFile());
        }

        return rutaOriginal;
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

            producto.setRutaImagen(obtenerRutaImagenParaGuardar());

            productoResultado = producto;
            confirmado = true;

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingresá valores numéricos válidos para precio y stock.",
                    "Datos incorrectos",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo copiar la imagen a la carpeta del proyecto: "
                    + e.getMessage(),
                    "Error con la imagen",
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
