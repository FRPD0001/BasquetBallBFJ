package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panelAjustes, panelEquipos, panelJugadores, panelCalendario, panelIzquierdo, panelDerecho, panelBotones;
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
    private JButton btnAgregarEquipo, btnListarEquipos, btnAgregarJugador, btnListarJugadores;
    private JButton btnGenerarCalendario, btnVerCalendario, btnEmpezarJuegos;

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

        // Panel de Jugadores
        panelJugadores = new JPanel();
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelJugadores.setBackground(coloresClaros[colorIndex]);
        panelJugadores.setPreferredSize(new Dimension(350, getHeight()));

        btnAgregarJugador = new JButton("Agregar Jugador");
        btnListarJugadores = new JButton("Listar Jugadores");

        JButton[] botonesJugadores = {btnAgregarJugador, btnListarJugadores};

        for (JButton btn : botonesJugadores) {
            configurarBoton(btn);
        }

        btnAgregarJugador.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Agregar Jugador"));
        btnListarJugadores.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Listar Jugadores"));

        panelJugadores.add(Box.createVerticalStrut(200));
        panelJugadores.add(btnAgregarJugador);
        panelJugadores.add(Box.createVerticalStrut(300));
        panelJugadores.add(btnListarJugadores);

        // Panel de Calendario (nuevo)
        panelCalendario = new JPanel();
        panelCalendario.setLayout(new BoxLayout(panelCalendario, BoxLayout.Y_AXIS));
        panelCalendario.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelCalendario.setBackground(coloresClaros[colorIndex]);
        panelCalendario.setPreferredSize(new Dimension(350, getHeight()));

        btnGenerarCalendario = new JButton("Generar Calendario");
        btnVerCalendario = new JButton("Ver Calendario");
        btnEmpezarJuegos = new JButton("Empezar Juegos");

        JButton[] botonesCalendario = {btnGenerarCalendario, btnVerCalendario, btnEmpezarJuegos};

        for (JButton btn : botonesCalendario) {
            configurarBoton(btn);
        }

        btnGenerarCalendario.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Generar Calendario"));
        btnVerCalendario.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Ver Calendario"));
        btnEmpezarJuegos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Empezar Juegos"));

        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnGenerarCalendario);
        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnVerCalendario);
        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnEmpezarJuegos);

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

        btnCambiarColor.addActionListener(e -> cambiarColor(botones, botonesAjustes, botonesEquipos, botonesJugadores, botonesCalendario));
        btnCambiarFondo.addActionListener(e -> cambiarImagenFondo());

        panelAjustes.add(Box.createVerticalStrut(200));
        panelAjustes.add(btnCambiarColor);
        panelAjustes.add(Box.createVerticalStrut(300));
        panelAjustes.add(btnCambiarFondo);

        // Configuración de los botones principales
        btnEquipos.addActionListener(e -> mostrarSubmenu(panelEquipos));
        btnJugadores.addActionListener(e -> mostrarSubmenu(panelJugadores));
        btnCalendario.addActionListener(e -> mostrarSubmenu(panelCalendario));
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

    private void cambiarColor(JButton[] botones, JButton[] botonesAjustes, JButton[] botonesEquipos, JButton[] botonesJugadores, JButton[] botonesCalendario) {
        colorIndex = (colorIndex + 1) % coloresOscuros.length;
        
        // Actualizar botones principales
        for (JButton btn : botones) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        
        // Actualizar botones de ajustes
        for (JButton btn : botonesAjustes) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        
        // Actualizar botones de equipos
        for (JButton btn : botonesEquipos) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        
        // Actualizar botones de jugadores
        for (JButton btn : botonesJugadores) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        
        // Actualizar botones de calendario
        for (JButton btn : botonesCalendario) {
            btn.setBackground(coloresOscuros[colorIndex]);
        }
        
        // Actualizar fondos de los paneles
        panelIzquierdo.setBackground(coloresClaros[colorIndex]);
        panelBotones.setBackground(coloresClaros[colorIndex]);
        panelAjustes.setBackground(coloresClaros[colorIndex]);
        panelEquipos.setBackground(coloresClaros[colorIndex]);
        panelJugadores.setBackground(coloresClaros[colorIndex]);
        panelCalendario.setBackground(coloresClaros[colorIndex]);
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