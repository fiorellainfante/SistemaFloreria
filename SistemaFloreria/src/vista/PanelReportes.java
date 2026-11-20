package vista;

import modelo.Movimiento;
import modelo.Producto;
import modelo.TipoMovimiento;
import servicio.InventarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class PanelReportes extends JPanel {

	private static final long serialVersionUID = 1L;
    private final InventarioService servicio;
    private final NumberFormat formatoMoneda;
    private final DateTimeFormatter formatoFecha;

    private JTable tablaAlertas;
    private JTable tablaMovimientos;

    private DefaultTableModel modeloAlertas;
    private DefaultTableModel modeloMovimientos;

    private JSpinner selectorFecha;
    private JLabel lblTotalVentas;
    private JLabel lblCantidadVentas;
    private JLabel lblCantidadReposiciones;

    private JComboBox<String> cmbFiltroTipo;

    public PanelReportes() {
        servicio = new InventarioService();
        formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "AR"));
        formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        setLayout(new BorderLayout());
        setBackground(new Color(248, 248, 245));

        crearInterfaz();
        cargarAlertas();
        cargarMovimientos();
        actualizarReporte();
    }

    private void crearInterfaz() {
        JLabel titulo = new JLabel("Reportes y alertas");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        add(titulo, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();

        pestanas.addTab("Stock bajo", crearPanelAlertas());
        pestanas.addTab("Movimientos", crearPanelMovimientos());
        pestanas.addTab("Reporte diario", crearPanelReporteDiario());

        add(pestanas, BorderLayout.CENTER);
    }

    private JPanel crearPanelAlertas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel descripcion = new JLabel(
                "Productos cuyo stock está por debajo o igual al mínimo establecido.");

        String[] columnas = {
            "ID", "Producto", "Categoría", "Stock actual", "Stock mínimo"
        };

        modeloAlertas = crearModeloTabla(columnas);
        tablaAlertas = new JTable(modeloAlertas);
        tablaAlertas.setRowHeight(27);

        JButton btnActualizar = new JButton("Actualizar alertas");
        btnActualizar.addActionListener(e -> cargarAlertas());

        panel.add(descripcion, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaAlertas), BorderLayout.CENTER);
        panel.add(btnActualizar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelMovimientos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        filtros.add(new JLabel("Tipo de movimiento:"));

        cmbFiltroTipo = new JComboBox<>(new String[]{
            "Todos", "VENTA", "REPOSICION", "DETERIORO"
        });

        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnActualizar = new JButton("Actualizar");

        filtros.add(cmbFiltroTipo);
        filtros.add(btnFiltrar);
        filtros.add(btnActualizar);

        String[] columnas = {
            "ID", "Fecha", "Producto", "Tipo", "Cantidad",
            "Precio unitario", "Importe"
        };

        modeloMovimientos = crearModeloTabla(columnas);
        tablaMovimientos = new JTable(modeloMovimientos);
        tablaMovimientos.setRowHeight(27);
        tablaMovimientos.setAutoCreateRowSorter(true);

        btnFiltrar.addActionListener(e -> filtrarMovimientos());
        btnActualizar.addActionListener(e -> cargarMovimientos());

        panel.add(filtros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMovimientos), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelReporteDiario() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT));

        selector.add(new JLabel("Seleccionar fecha:"));

        selectorFecha = new JSpinner(new SpinnerDateModel());
        selectorFecha.setEditor(new JSpinner.DateEditor(
                selectorFecha, "dd/MM/yyyy"));

        JButton btnConsultar = new JButton("Consultar reporte");
        btnConsultar.addActionListener(e -> actualizarReporte());

        selector.add(selectorFecha);
        selector.add(btnConsultar);

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 15, 15));

        lblTotalVentas = new JLabel("$0,00", SwingConstants.CENTER);
        lblCantidadVentas = new JLabel("0 unidades", SwingConstants.CENTER);
        lblCantidadReposiciones = new JLabel("0 unidades", SwingConstants.CENTER);

        tarjetas.add(crearTarjeta("Total vendido", lblTotalVentas));
        tarjetas.add(crearTarjeta("Productos vendidos", lblCantidadVentas));
        tarjetas.add(crearTarjeta("Productos repuestos", lblCantidadReposiciones));

        panel.add(selector, BorderLayout.NORTH);
        panel.add(tarjetas, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjeta(String titulo, JLabel valor) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        JLabel etiqueta = new JLabel(titulo, SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 14));

        valor.setFont(new Font("Arial", Font.BOLD, 21));
        valor.setForeground(new Color(60, 120, 80));

        tarjeta.add(etiqueta, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);

        return tarjeta;
    }

    private DefaultTableModel crearModeloTabla(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void cargarAlertas() {
        modeloAlertas.setRowCount(0);

        List<Producto> productos = servicio.obtenerProductosPorAgotarse();

        for (Producto producto : productos) {
            modeloAlertas.addRow(new Object[]{
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getStock(),
                producto.getStockMinimo()
            });
        }
    }

    private void cargarMovimientos() {
        modeloMovimientos.setRowCount(0);

        List<Movimiento> movimientos = servicio.obtenerMovimientos();

        for (Movimiento movimiento : movimientos) {
            agregarMovimientoATabla(movimiento);
        }
    }

    private void filtrarMovimientos() {
        modeloMovimientos.setRowCount(0);

        String tipoSeleccionado = (String) cmbFiltroTipo.getSelectedItem();

        if ("Todos".equals(tipoSeleccionado)) {
            cargarMovimientos();
            return;
        }

        TipoMovimiento tipo = TipoMovimiento.valueOf(tipoSeleccionado);

        List<Movimiento> movimientos =
                servicio.obtenerMovimientosPorTipo(tipo);

        for (Movimiento movimiento : movimientos) {
            agregarMovimientoATabla(movimiento);
        }
    }

    private void agregarMovimientoATabla(Movimiento movimiento) {
        modeloMovimientos.addRow(new Object[]{
            movimiento.getId(),
            movimiento.getFecha().format(formatoFecha),
            movimiento.getProducto().getNombre(),
            movimiento.getTipo(),
            movimiento.getCantidad(),
            formatoMoneda.format(movimiento.getPrecioUnitario()),
            formatoMoneda.format(movimiento.getImporteTotal()),
            movimiento.getObservacion()
        });
    }

    private void actualizarReporte() {
        java.util.Date fechaSeleccionada =
                (java.util.Date) selectorFecha.getValue();

        LocalDate fecha = fechaSeleccionada.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        double totalVentas = servicio.obtenerTotalVentasDelDia(fecha);
        int cantidadVentas = servicio.obtenerCantidadVentasDelDia(fecha);
        int cantidadReposiciones =
                servicio.obtenerCantidadReposicionesDelDia(fecha);

        lblTotalVentas.setText(formatoMoneda.format(totalVentas));
        lblCantidadVentas.setText(cantidadVentas + " unidades");
        lblCantidadReposiciones.setText(cantidadReposiciones + " unidades");
    }
    
    public void actualizarDatos() {
        cargarAlertas();
        cargarMovimientos();
        actualizarReporte();
    }
}