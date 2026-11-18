package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión - Florería");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "Sistema de Gestión de Florería",
                SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();

        PanelProductos panelProductos = new PanelProductos();
        PanelVentas panelVentas = new PanelVentas();
        PanelReposiciones panelReposiciones = new PanelReposiciones();
        PanelReportes panelReportes = new PanelReportes();

        pestanas.addTab("Productos", panelProductos);
        pestanas.addTab("Ventas", panelVentas);
        pestanas.addTab("Reposiciones", panelReposiciones);
        pestanas.addTab("Reportes y alertas", panelReportes);

        pestanas.addChangeListener(e -> {
            Component panelSeleccionado = pestanas.getSelectedComponent();

            if (panelSeleccionado instanceof PanelProductos) {
                panelProductos.actualizarDatos();
            } else if (panelSeleccionado instanceof PanelVentas) {
                panelVentas.actualizarDatos();
            } else if (panelSeleccionado instanceof PanelReposiciones) {
                panelReposiciones.actualizarDatos();
            } else if (panelSeleccionado instanceof PanelReportes) {
                panelReportes.actualizarDatos();
            }
        });

        add(pestanas, BorderLayout.CENTER);
    }
}
