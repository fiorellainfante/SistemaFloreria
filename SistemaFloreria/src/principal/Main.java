package principal;

import conexion.ConexionDB;
import javax.swing.SwingUtilities;
import vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {

        ConexionDB.inicializarBaseDeDatos();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
