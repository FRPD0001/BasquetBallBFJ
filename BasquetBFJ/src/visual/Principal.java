package visual;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Principal extends JFrame {
    
    private static final long serialVersionUID = 1L;

    public Principal() {
        setTitle("Basketball Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BorderLayout());
        panelIzquierdo.setBackground(new Color(230, 240, 250));
        panelIzquierdo.setPreferredSize(new Dimension(350, getHeight()));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelBotones.setBackground(new Color(200, 220, 240));

        JButton btnEquipos = new JButton("Equipos");
        JButton btnJugadores = new JButton("Jugadores");
        JButton btnCalendario = new JButton("Calendario");
        JButton btnAjustes = new JButton("Ajustes");

        Dimension buttonSize = new Dimension(280, 80);
        Color azulClaro = new Color(100, 149, 237);
        JButton[] botones = {btnEquipos, btnJugadores, btnCalendario, btnAjustes};

        for (JButton btn : botones) {
            btn.setPreferredSize(buttonSize);
            btn.setMaximumSize(buttonSize);
            btn.setBackground(azulClaro);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        
        panelBotones.add(Box.createVerticalStrut(50));
        panelBotones.add(btnEquipos);
        panelBotones.add(Box.createVerticalStrut(150));
        panelBotones.add(btnJugadores);
        panelBotones.add(Box.createVerticalStrut(150));
        panelBotones.add(btnCalendario);
        panelBotones.add(Box.createVerticalStrut(150));
        panelBotones.add(btnAjustes);

        panelIzquierdo.add(panelBotones, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout());

        try {
            File imagenFile = new File("media/fondoProyecto.png");
            ImageIcon imagenOriginal = new ImageIcon(imagenFile.getAbsolutePath());
            Image img = imagenOriginal.getImage();
            Image imgEscalada = img.getScaledInstance(-1, -1, Image.SCALE_SMOOTH);

            JLabel lblImagen = new JLabel(new ImageIcon(imgEscalada));
            lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
            lblImagen.setVerticalAlignment(SwingConstants.CENTER);

            panelDerecho.setBackground(new Color(230, 240, 250));
            panelDerecho.add(lblImagen, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            panelDerecho.add(new JLabel("Error cargando imagen", SwingConstants.CENTER));
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(350);
        splitPane.setEnabled(false);

        getContentPane().add(splitPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Principal ventana = new Principal();
            ventana.setVisible(true);
        });
    }
}
