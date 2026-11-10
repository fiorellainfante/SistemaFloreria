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

        // Creamos el panel de pestañas
        JTabbedPane pestañas = new JTabbedPane();

        // --- CREAMOS PANELES "DE MENTIRA" POR AHORA ---
        // Después vamos a crear clases separadas para cada uno de estos paneles
        JPanel panelProductos = new JPanel();
        panelProductos.add(new JLabel("Acá va la tabla de Productos"));
        
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
        // Esto hace que la ventana se vea con el estilo del sistema operativo (más lindo)
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