package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panelAjustes, panelEquipos, panelIzquierdo, panelDerecho, panelBotones;
    private JLabel lblImagen;
    private Color[] coloresOscuros = {new Color(100, 149, 237), Color.RED, Color.GREEN, Color.YELLOW};
    private Color[] coloresClaros = {new Color(200, 220, 255), new Color(255, 200, 200), new Color(200, 255, 200), new Color(255, 255, 200)};
    private String[] imagenesFondo = {
        "media/fondoProyecto1.jpg",
        "media/fondoProyecto2.jpg",
        "media/fondoProyecto3.jpg",
        "media/fondoProyecto4.jpg"
    };
    private int colorIndex = 0;
    private int fondoIndex = 0;
    private JPanel panelActual;
    private JButton btnAgregarEquipo, btnListarEquipos; // Botones de Equipos como variables de clase

    public Principal() {
        setTitle("Basketball Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());

        // Panel izquierdo (Menú principal)
        panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(coloresClaros[colorIndex]);
        panelIzquierdo.setPreferredSize(new Dimension(350, getHeight()));

        panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelBotones.setBackground(coloresClaros[colorIndex]);

        JButton btnEquipos = new JButton("Equipos");
        JButton btnJugadores = new JButton("Jugadores");
        JButton btnCalendario = new JButton("Calendario");
        JButton btnAjustes = new JButton("Ajustes");

        Dimension buttonSize = new Dimension(280, 80);
        JButton[] botones = {btnEquipos, btnJugadores, btnCalendario, btnAjustes};

        for (JButton btn : botones) {
            btn.setPreferredSize(buttonSize);
            btn.setMaximumSize(buttonSize);
            btn.setBackground(coloresOscuros[colorIndex]);
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

        // Panel derecho (Imagen de fondo)
        panelDerecho = new JPanel(new BorderLayout());
        lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.add(lblImagen, BorderLayout.CENTER);
        actualizarImagenFondo();

        panelDerecho.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarImagenFondo();
            }
        });

        // Panel de Equipos
        panelEquipos = new JPanel();
        panelEquipos.setLayout(new BoxLayout(panelEquipos, BoxLayout.Y_AXIS));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelEquipos.setBackground(coloresClaros[colorIndex]);
        panelEquipos.setPreferredSize(new Dimension(350, getHeight()));

        btnAgregarEquipo = new JButton("Agregar Equipo");
        btnListarEquipos = new JButton("Listar Equipos");

        JButton[] botonesEquipos = {btnAgregarEquipo, btnListarEquipos};

        for (JButton btn : botonesEquipos) {
            configurarBoton(btn);
        }

        btnAgregarEquipo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Agregar Equipo"));
        btnListarEquipos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Listar Equipos"));

        panelEquipos.add(Box.createVerticalStrut(200));
        panelEquipos.add(btnAgregarEquipo);
        panelEquipos.add(Box.createVerticalStrut(300));
        panelEquipos.add(btnListarEquipos);

        // Panel de Ajustes
        panelAjustes = new JPanel();
        panelAjustes.setLayout(new BoxLayout(panelAjustes, BoxLayout.Y_AXIS));
        panelAjustes.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelAjustes.setBackground(coloresClaros[colorIndex]);
        panelAjustes.setPreferredSize(new Dimension(350, getHeight()));

        JButton btnCambiarColor = new JButton("Cambiar Color");
        JButton btnCambiarFondo = new JButton("Cambiar Fondo");

        JButton[] botonesAjustes = {btnCambiarColor, btnCambiarFondo};

        for (JButton btn : botonesAjustes) {
            configurarBoton(btn);
        }

        // Modificado para incluir los botones de Equipos
        btnCambiarColor.addActionListener(e -> cambiarColor(botones, botonesAjustes, botonesEquipos));
        btnCambiarFondo.addActionListener(e -> cambiarImagenFondo());

        panelAjustes.add(Box.createVerticalStrut(200));
        panelAjustes.add(btnCambiarColor);
        panelAjustes.add(Box.createVerticalStrut(300));
        panelAjustes.add(btnCambiarFondo);

        // Configuración de los botones principales
        btnEquipos.addActionListener(e -> mostrarSubmenu(panelEquipos));
        btnAjustes.addActionListener(e -> mostrarSubmenu(panelAjustes));

        // Estructura inicial de los paneles
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, null);
        splitPane.setDividerLocation(350);
        splitPane.setEnabled(false);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, panelDerecho);
        mainSplitPane.setDividerLocation(700);
        mainSplitPane.setEnabled(false);

        getContentPane().add(mainSplitPane);
    }

    private void mostrarSubmenu(JPanel panel) {
        JSplitPane splitPane = (JSplitPane) panelIzquierdo.getParent();
        
        if (panelActual == panel && splitPane.getRightComponent() != null) {
            splitPane.setRightComponent(null);
            panelActual = null;
        } else {
            splitPane.setRightComponent(panel);
            splitPane.setDividerLocation(350);
            panelActual = panel;
        }
        
        splitPane.revalidate();
        splitPane.repaint();
    }

    private void configurarBoton(JButton boton) {
        boton.setPreferredSize(new Dimension(280, 80));
        boton.setMaximumSize(new Dimension(280, 80));
        boton.setBackground(coloresOscuros[colorIndex]);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // Método modificado para incluir botones de Equipos
    private void cambiarColor(JButton[] botones, JButton[] botonesAjustes, JButton[] botonesEquipos) {
        colorIndex = (colorIndex + 1) % coloresOscuros.length;
        
        for (JButton btn : botones) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        for (JButton btn : botonesAjustes) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        for (JButton btn : botonesEquipos) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        
        panelIzquierdo.setBackground(coloresClaros[colorIndex]);
        panelBotones.setBackground(coloresClaros[colorIndex]);
        panelAjustes.setBackground(coloresClaros[colorIndex]);
        panelEquipos.setBackground(coloresClaros[colorIndex]);
    }

    private void cambiarImagenFondo() {
        fondoIndex = (fondoIndex + 1) % imagenesFondo.length;
        actualizarImagenFondo();
    }

    private void actualizarImagenFondo() {
        try {
            File imagenFile = new File(imagenesFondo[fondoIndex]);
            ImageIcon imagenOriginal = new ImageIcon(imagenFile.getAbsolutePath());
            Image img = imagenOriginal.getImage();
            int ancho = panelDerecho.getWidth();
            int alto = panelDerecho.getHeight();
            if (ancho > 0 && alto > 0) {
                Image imgEscalada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception e) {
            lblImagen.setText("Error cargando imagen");
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Principal ventana = new Principal();
                ventana.setVisible(true);
            }
        });
    }
}