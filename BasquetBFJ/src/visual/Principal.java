package visual;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panelAjustes, panelEquipos, panelJugadores, panelCalendario, panelIzquierdo, panelDerecho, panelBotones;
    private JLabel lblImagen;
    private Color[] coloresOscuros = {new Color(147, 112, 219), Color.RED, new Color(17, 115, 68), new Color(100, 149, 237)};
    private Color[] coloresClaros = {new Color(216, 191, 216), new Color(255, 200, 200), new Color(200, 255, 200), new Color(200, 220, 255)};
    private String[] imagenesFondo = {
        "media/fondoProyecto5.jpeg",
        "media/fondoProyecto1.jpg",
        "media/fondoProyecto2.jpg",
        "media/fondoProyecto3.jpg",
        "media/fondoProyecto4.jpg",
    };
    private int colorIndex = 0;
    private int fondoIndex = 0;
    private JPanel panelActual;
    private JButton btnAgregarEquipo, btnListarEquipos, btnAgregarJugador, btnListarJugadores;
    private JButton btnGenerarCalendario, btnVerCalendario, btnEmpezarJuegos;

    // Clase para bordes redondeados
    class RoundedBorder implements Border {
        private int radius;
        
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }
        
        public boolean isBorderOpaque() {
            return true;
        }
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }

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

        JButton btnEquipos = crearBotonModerno("Equipos");
        JButton btnJugadores = crearBotonModerno("Jugadores");
        JButton btnCalendario = crearBotonModerno("Calendario");
        JButton btnAjustes = crearBotonModerno("Ajustes");

        JButton[] botones = {btnEquipos, btnJugadores, btnCalendario, btnAjustes};

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

        // Panel de Equipos (submenú)
        panelEquipos = new JPanel();
        panelEquipos.setLayout(new BoxLayout(panelEquipos, BoxLayout.Y_AXIS));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelEquipos.setBackground(coloresOscuros[colorIndex]);
        panelEquipos.setPreferredSize(new Dimension(350, getHeight()));

        btnAgregarEquipo = crearBotonSubmenuModerno("Agregar Equipo");
        btnListarEquipos = crearBotonSubmenuModerno("Listar Equipos");

        JButton[] botonesEquipos = {btnAgregarEquipo, btnListarEquipos};

        btnAgregarEquipo.addActionListener(e -> {
            RegEquipo regEquipo = new RegEquipo(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
            regEquipo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            regEquipo.setVisible(true);
        });

        btnListarEquipos.addActionListener(e -> {
            ListEquipo listEquipo = new ListEquipo(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
            listEquipo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            listEquipo.setVisible(true);
        });

        panelEquipos.add(Box.createVerticalStrut(200));
        panelEquipos.add(btnAgregarEquipo);
        panelEquipos.add(Box.createVerticalStrut(300));
        panelEquipos.add(btnListarEquipos);

        // Panel de Jugadores (submenú)
        panelJugadores = new JPanel();
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelJugadores.setBackground(coloresOscuros[colorIndex]);
        panelJugadores.setPreferredSize(new Dimension(350, getHeight()));

        btnAgregarJugador = crearBotonSubmenuModerno("Agregar Jugador");
        btnListarJugadores = crearBotonSubmenuModerno("Listar Jugadores");

        JButton btnAgregarLesion = crearBotonSubmenuModerno("Agregar Lesión");
        JButton btnListarLesiones = crearBotonSubmenuModerno("Listar Lesiones");

        JButton[] botonesJugadores = {btnAgregarJugador, btnListarJugadores, btnAgregarLesion, btnListarLesiones};

        btnAgregarJugador.addActionListener(e -> {
            RegJugador regJug = new RegJugador(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
            regJug.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            regJug.setVisible(true);
        });

        btnListarJugadores.addActionListener(e -> {
            ListJugador listJugador = new ListJugador(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
            listJugador.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            listJugador.setVisible(true);
        });
        
        btnAgregarLesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegLesion regLesion = new RegLesion(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
                regLesion.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                regLesion.setVisible(true);
            }
        });

        btnListarLesiones.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              /*  ListLesion listLesion = new ListLesion(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
                listLesion.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                listLesion.setVisible(true); */
            }
        });

        panelJugadores.add(Box.createVerticalStrut(60));
        panelJugadores.add(btnAgregarJugador);
        panelJugadores.add(Box.createVerticalStrut(160));
        panelJugadores.add(btnListarJugadores);
        panelJugadores.add(Box.createVerticalStrut(160));
        panelJugadores.add(btnAgregarLesion);
        panelJugadores.add(Box.createVerticalStrut(160));
        panelJugadores.add(btnListarLesiones);

        // Panel de Calendario (submenú)
        panelCalendario = new JPanel();
        panelCalendario.setLayout(new BoxLayout(panelCalendario, BoxLayout.Y_AXIS));
        panelCalendario.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelCalendario.setBackground(coloresOscuros[colorIndex]);
        panelCalendario.setPreferredSize(new Dimension(350, getHeight()));

        btnGenerarCalendario = crearBotonSubmenuModerno("Generar Calendario");
        btnVerCalendario = crearBotonSubmenuModerno("Ver Calendario");
        btnEmpezarJuegos = crearBotonSubmenuModerno("Empezar Juegos");

        JButton[] botonesCalendario = {btnGenerarCalendario, btnVerCalendario, btnEmpezarJuegos};

        btnGenerarCalendario.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Generar Calendario"));
        btnVerCalendario.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Ver Calendario"));
        btnEmpezarJuegos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Empezar Juegos"));

        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnGenerarCalendario);
        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnVerCalendario);
        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnEmpezarJuegos);

        // Panel de Ajustes (submenú)
        panelAjustes = new JPanel();
        panelAjustes.setLayout(new BoxLayout(panelAjustes, BoxLayout.Y_AXIS));
        panelAjustes.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelAjustes.setBackground(coloresOscuros[colorIndex]);
        panelAjustes.setPreferredSize(new Dimension(350, getHeight()));

        JButton btnCambiarColor = crearBotonSubmenuModerno("Cambiar Color");
        JButton btnCambiarFondo = crearBotonSubmenuModerno("Cambiar Fondo");

        JButton[] botonesAjustes = {btnCambiarColor, btnCambiarFondo};

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

    // Método para crear botones principales modernos
    private JButton crearBotonModerno(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                // Suavizado de bordes
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra
                g2.setColor(new Color(0, 0, 0, 255));
                g2.fillRoundRect(1, 3, getWidth()-2, getHeight()-2, 15, 15);
                
                // Fondo del botón
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        
        boton.setPreferredSize(new Dimension(280, 60));
        boton.setMaximumSize(new Dimension(280, 60));
        boton.setBackground(coloresOscuros[colorIndex]);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(coloresOscuros[colorIndex].darker());
                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(coloresOscuros[colorIndex]);
            }
        });
        
        return boton;
    }

    // Método para crear botones de submenú modernos
    private JButton crearBotonSubmenuModerno(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                // Suavizado de bordes
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo del botón
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        
        boton.setPreferredSize(new Dimension(260, 50));
        boton.setMaximumSize(new Dimension(260, 50));
        boton.setBackground(coloresClaros[colorIndex]);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setBorder(new RoundedBorder(10));
        
        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(coloresClaros[colorIndex].brighter());
                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(coloresClaros[colorIndex]);
            }
        });
        
        return boton;
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

    private void cambiarColor(JButton[] botones, JButton[] botonesAjustes, JButton[] botonesEquipos, JButton[] botonesJugadores, JButton[] botonesCalendario) {
        colorIndex = (colorIndex + 1) % coloresOscuros.length;
        
        // Actualizar botones principales
        for (JButton btn : botones) {
            btn.setBackground(coloresOscuros[colorIndex]);
            btn.setForeground(Color.WHITE);
        }
        
        // Actualizar botones de submenú
        for (JButton btn : botonesAjustes) {
            btn.setBackground(coloresClaros[colorIndex]);
            btn.setForeground(Color.BLACK);
        }
        
        for (JButton btn : botonesEquipos) {
            btn.setBackground(coloresClaros[colorIndex]);
            btn.setForeground(Color.BLACK);
        }
        
        for (JButton btn : botonesJugadores) {
            btn.setBackground(coloresClaros[colorIndex]);
            btn.setForeground(Color.BLACK);
        }
        
        for (JButton btn : botonesCalendario) {
            btn.setBackground(coloresClaros[colorIndex]);
            btn.setForeground(Color.BLACK);
        }
        
        // Actualizar fondos de los paneles
        panelIzquierdo.setBackground(coloresClaros[colorIndex]);
        panelBotones.setBackground(coloresClaros[colorIndex]);
        panelAjustes.setBackground(coloresOscuros[colorIndex]);
        panelEquipos.setBackground(coloresOscuros[colorIndex]);
        panelJugadores.setBackground(coloresOscuros[colorIndex]);
        panelCalendario.setBackground(coloresOscuros[colorIndex]);
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