package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Configuración básica de la ventana
        setTitle("Sistema de Gestión - Florería");
        setSize(900, 600); // Ancho x Alto
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane();

        PanelProductos panelProductos = new PanelProductos();
        
        JPanel panelVentas = new JPanel();
        panelVentas.add(new JLabel("Acá va la pantalla de Ventas"));

        JPanel panelReportes = new JPanel();
        panelReportes.add(new JLabel("Acá van los Reportes y Alertas"));

        // Agregamos los paneles a las pestañas con un nombre
        pestañas.addTab("Productos", panelProductos);
        pestañas.addTab("Ventas", panelVentas);
        pestañas.addTab("Reportes y Alertas", panelReportes);

        // Agregamos las pestañas a la ventana principal
        add(pestañas, BorderLayout.CENTER);
    }

    // Método main para probar la ventana (después lo vamos a mover a una clase Main)
    public static void main(String[] args) {
        // Esto hace que la ventana se vea con el estilo del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mostramos la ventana
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}