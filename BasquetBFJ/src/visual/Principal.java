package visual;

import javax.swing.*;
import javax.swing.border.Border;

import logico.SerieNacional;
import logico.User;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panelAjustes, panelEquipos, panelJugadores, panelCalendario, panelIzquierdo, panelDerecho, panelBotones;
    private JLabel lblImagen;
    private Color[] coloresOscuros = {new Color(147, 112, 219), new Color(100, 149, 237), Color.RED, new Color(17, 115, 68)};
    private Color[] coloresClaros = {new Color(216, 191, 216), new Color(200, 220, 255), new Color(255, 200, 200), new Color(200, 255, 200)};
    private String[] imagenesFondo = {
        "media/fondoProyecto5.jpeg",
        "media/fondoProyecto1.jpg",
        "media/fondoProyecto2.jpg",
        "media/fondoProyecto3.jpg",
    };
    
    private int colorIndex = 0;
    private int fondoIndex = 0;
    private JPanel panelActual;
    private JButton btnAgregarEquipo, btnListarEquipos, btnAgregarJugador, btnListarJugadores;
    private JButton btnGenerarCalendario, btnVerCalendario, btnEmpezarJuegos;
    private JButton btnAgregarUsuario;

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
    	
    	
    	if (SerieNacional.getUsuarioActual() == null) {
            JOptionPane.showMessageDialog(null, 
                "Debe iniciar sesión primero", 
                "Acceso no autorizado", 
                JOptionPane.ERROR_MESSAGE);
            this.dispose(); // Cierra la ventana si se crea sin autenticación
            return;
        }
    	
        setTitle("Basketball Manager - Usuario: " + SerieNacional.getUsuarioActual().getUserName());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());

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

        JButton btnCerrar = new JButton("") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 255));
                g2.fillRoundRect(1, 3, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        ImageIcon iconoCloseOriginal = new ImageIcon("media/Close.png");

        Image imagenCloseEscalada = iconoCloseOriginal.getImage().getScaledInstance(
            btnCerrar.getPreferredSize().width - 5,
            btnCerrar.getPreferredSize().height + 20,
            Image.SCALE_SMOOTH
        );

        ImageIcon iconoCloseEscalado = new ImageIcon(imagenCloseEscalada);
        btnCerrar.setIcon(iconoCloseEscalado);
        
        btnCerrar.setToolTipText("Cerrar programa");
        
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setOpaque(false);
        btnCerrar.setBackground(new Color(211, 47, 47)); 
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setPreferredSize(new Dimension(60, 60));
        btnCerrar.setMaximumSize(new Dimension(60, 60));
        
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(new Color(211, 47, 47).darker());
                btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(new Color(211, 47, 47));
            }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton btnLogout = new JButton("") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 255));
                g2.fillRoundRect(1, 3, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(Principal.this, 
                    "¿Está seguro que desea cerrar sesión?", 
                    "Confirmar", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    SerieNacional.cerrarSesion();
                    Principal.this.dispose();
                    new Login().setVisible(true); // Vuelve a mostrar el login
                }
            }
        });
        
        ImageIcon iconoLogoutOriginal = new ImageIcon("media/Logout.png");

     Image imagenLogoutEscalada = iconoLogoutOriginal.getImage().getScaledInstance(
         btnLogout.getPreferredSize().width - 1, 
         btnLogout.getPreferredSize().height + 20, 
         Image.SCALE_SMOOTH
     );

     ImageIcon iconoLogoutEscalado = new ImageIcon(imagenLogoutEscalada);
     btnLogout.setIcon(iconoLogoutEscalado);
        btnLogout.setIcon(new ImageIcon(imagenLogoutEscalada));
        btnLogout.setToolTipText("Cerrar sesión");
        
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setOpaque(false);
        btnLogout.setBackground(new Color(245, 124, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(60, 60));
        btnLogout.setMaximumSize(new Dimension(60, 60));
        
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(245, 124, 0).darker());
                btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(245, 124, 0));
            }
        });
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Principal.this, 
                    "Funcionalidad de logout no implementada aún", 
                    "Logout", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnGuardar = new JButton("") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 255));
                g2.fillRoundRect(1, 3, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        ImageIcon icono = new ImageIcon("media/Save.png");
        Image imagenEscalada = icono.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH);
        btnGuardar.setIcon(new ImageIcon(imagenEscalada));
        btnGuardar.setToolTipText("Guardar");
        
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        btnGuardar.setIcon(iconoEscalado);
        
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(false);
        btnGuardar.setBackground(new Color(52, 199, 89));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(60, 60));
        btnGuardar.setMaximumSize(new Dimension(60, 60));
        
        btnGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnGuardar.setBackground(new Color(52, 199, 89).darker());
                btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnGuardar.setBackground(new Color(52, 199, 89));
            }
        });
        
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Principal.this, 
                    "Funcionalidad de guardar implementada aquí", 
                    "Guardar", JOptionPane.INFORMATION_MESSAGE);
                SerieNacional.getInstance().guardarFileTest();
            }
        });

        JButton btnRespaldo = new JButton("") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 255));
                g2.fillRoundRect(1, 3, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        ImageIcon iconoBackupOriginal = new ImageIcon("media/Backup.png");

     Image imagenBackupEscalada = iconoBackupOriginal.getImage().getScaledInstance(
         btnRespaldo.getPreferredSize().width - 5, 
         btnRespaldo.getPreferredSize().height + 25, 
         Image.SCALE_SMOOTH
    );

     	ImageIcon iconoBackupEscalado = new ImageIcon(imagenBackupEscalada);
     	btnRespaldo.setIcon(iconoBackupEscalado);
        
        btnRespaldo.setToolTipText("Respaldar información");
        
        btnRespaldo.setContentAreaFilled(false);
        btnRespaldo.setBorderPainted(false);
        btnRespaldo.setFocusPainted(false);
        btnRespaldo.setOpaque(false);
        btnRespaldo.setFont(new Font("Arial", Font.BOLD, 24));
        btnRespaldo.setBackground(new Color(41, 98, 255));
        btnRespaldo.setForeground(Color.WHITE);
        btnRespaldo.setPreferredSize(new Dimension(60, 60));
        btnRespaldo.setMaximumSize(new Dimension(60, 60));
        
        btnRespaldo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRespaldo.setBackground(new Color(41, 98, 255).darker());
                btnRespaldo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnRespaldo.setBackground(new Color(41, 98, 255));
            }
        });
        
        btnRespaldo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarRespaldoRemotoSimple();
            }
        });

        JPanel panelBotonesInferiores = new JPanel();
        panelBotonesInferiores.setLayout(new BoxLayout(panelBotonesInferiores, BoxLayout.X_AXIS));
        panelBotonesInferiores.setBackground(coloresClaros[colorIndex]);
        panelBotonesInferiores.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));
        
        panelBotonesInferiores.add(btnCerrar);
        panelBotonesInferiores.add(Box.createHorizontalStrut(10));
        panelBotonesInferiores.add(btnLogout);
        panelBotonesInferiores.add(Box.createHorizontalStrut(10));
        panelBotonesInferiores.add(btnGuardar);
        panelBotonesInferiores.add(Box.createHorizontalStrut(10));
        panelBotonesInferiores.add(btnRespaldo);
        panelBotonesInferiores.add(Box.createHorizontalGlue());
        
        panelIzquierdo.add(panelBotonesInferiores, BorderLayout.SOUTH);

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

        panelEquipos = new JPanel();
        panelEquipos.setLayout(new BoxLayout(panelEquipos, BoxLayout.Y_AXIS));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelEquipos.setBackground(coloresOscuros[colorIndex]);
        panelEquipos.setPreferredSize(new Dimension(350, getHeight()));

        btnAgregarEquipo = crearBotonSubmenuModerno("Agregar Equipo");
        btnListarEquipos = crearBotonSubmenuModerno("Listar Equipos");

        JButton[] botonesEquipos = {btnAgregarEquipo, btnListarEquipos};

        btnAgregarEquipo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegEquipo regEquipo = new RegEquipo(coloresOscuros[colorIndex], coloresClaros[colorIndex], null);
                regEquipo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                regEquipo.setVisible(true);
            }
        });

        btnListarEquipos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListEquipo listEquipo = new ListEquipo(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
                listEquipo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                listEquipo.setVisible(true);
            }
        });

        panelEquipos.add(Box.createVerticalStrut(200));
        panelEquipos.add(btnAgregarEquipo);
        panelEquipos.add(Box.createVerticalStrut(300));
        panelEquipos.add(btnListarEquipos);

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

        btnAgregarJugador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegJugador regJug = new RegJugador(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
                regJug.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                regJug.setVisible(true);
            }
        });

        btnListarJugadores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListJugador listJugador = new ListJugador(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
                listJugador.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                listJugador.setVisible(true);
            }
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
                ListLesion listLesion = new ListLesion(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
                listLesion.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                listLesion.setVisible(true); 
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

        panelCalendario = new JPanel();
        panelCalendario.setLayout(new BoxLayout(panelCalendario, BoxLayout.Y_AXIS));
        panelCalendario.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelCalendario.setBackground(coloresOscuros[colorIndex]);
        panelCalendario.setPreferredSize(new Dimension(350, getHeight()));

        btnGenerarCalendario = crearBotonSubmenuModerno("Generar Calendario");
        btnVerCalendario = crearBotonSubmenuModerno("Ver Calendario");
        btnEmpezarJuegos = crearBotonSubmenuModerno("Empezar Juegos");

        JButton[] botonesCalendario = {btnGenerarCalendario, btnVerCalendario, btnEmpezarJuegos};

        btnGenerarCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenCalendario genCalendario = new GenCalendario(coloresOscuros[colorIndex], coloresClaros[colorIndex]);
                
                JDialog dialog = new JDialog(Principal.this, "Generar Calendario", true);
                dialog.getContentPane().add(genCalendario);
                dialog.pack();
                dialog.setSize(915, 700);
                dialog.setLocationRelativeTo(Principal.this);
                dialog.setVisible(true);
            }
        });

        btnVerCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SerieNacional.getInstance().getMisJuegos().isEmpty()) {
                    JOptionPane.showMessageDialog(Principal.this, 
                        "No hay juegos programados. Genere primero un calendario.", 
                        "Calendario Vacío", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    VerCalendario.mostrarDialog(Principal.this, coloresClaros[colorIndex], coloresOscuros[colorIndex]);
                }
            }
        });

        btnEmpezarJuegos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                if (SerieNacional.getInstance().getMisJuegos().isEmpty()) {
                    JOptionPane.showMessageDialog(Principal.this, 
                        "No hay juegos programados. Genere primero un calendario.", 
                        "Calendario Vacío", JOptionPane.INFORMATION_MESSAGE);
                    
                } else {
                	
                }
                EmpezarJuegos.mostrarDialog(Principal.this, coloresClaros[colorIndex], coloresOscuros[colorIndex]);
            }
        });

        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnGenerarCalendario);
        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnVerCalendario);
        panelCalendario.add(Box.createVerticalStrut(150));
        panelCalendario.add(btnEmpezarJuegos);

        panelAjustes = new JPanel();
        panelAjustes.setLayout(new BoxLayout(panelAjustes, BoxLayout.Y_AXIS));
        panelAjustes.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelAjustes.setBackground(coloresOscuros[colorIndex]);
        panelAjustes.setPreferredSize(new Dimension(350, getHeight()));

        JButton btnCambiarColor = crearBotonSubmenuModerno("Cambiar Color");
        JButton btnCambiarFondo = crearBotonSubmenuModerno("Cambiar Fondo");
         btnAgregarUsuario = crearBotonSubmenuModerno("Agregar Usuario"); // Nuevo botón

        
        
        JButton[] botonesAjustes = {btnCambiarColor, btnCambiarFondo, btnAgregarUsuario}; // Añádelo al array
        
        btnAgregarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solo permite agregar usuarios si el usuario actual es Administrador
                if (SerieNacional.getUsuarioActual() != null && 
                    SerieNacional.getUsuarioActual().getTipo().equals("Administrador")) {
                    RegUser regUser = new RegUser();
                    regUser.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    regUser.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(Principal.this,
                        "Solo los administradores pueden agregar usuarios",
                        "Acceso denegado",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnCambiarColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarColor(botones, botonesAjustes, botonesEquipos, botonesJugadores, botonesCalendario);
            }
        });
        
        btnCambiarFondo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarImagenFondo();
            }
        });

        panelAjustes.add(Box.createVerticalStrut(100));  // Espacio inicial reducido
        panelAjustes.add(btnCambiarColor);
        panelAjustes.add(Box.createVerticalStrut(150));
        panelAjustes.add(btnCambiarFondo);
        panelAjustes.add(Box.createVerticalStrut(150));
        panelAjustes.add(btnAgregarUsuario);  // Nuevo botón añadido
        
        configurarPermisosUsuario();

        btnEquipos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSubmenu(panelEquipos);
            }
        });

        btnJugadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSubmenu(panelJugadores);
            }
        });

        btnCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSubmenu(panelCalendario);
            }
        });

        btnAjustes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSubmenu(panelAjustes);
            }
        });
        
        configurarPermisosUsuario();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, null);
        splitPane.setDividerLocation(350);
        splitPane.setEnabled(false);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane, panelDerecho);
        mainSplitPane.setDividerLocation(700);
        mainSplitPane.setEnabled(false);

        getContentPane().add(mainSplitPane);
    }

    
    private void configurarPermisosUsuario() {
        User usuario = SerieNacional.getUsuarioActual();
        if (usuario == null) return;
        
        boolean esAnotador = "Anotador".equals(usuario.getTipo());
        
        JButton[] botonesRestringidos = {
            btnAgregarEquipo, 
            btnAgregarJugador, 
            btnGenerarCalendario, 
            btnAgregarUsuario
        };
        
        for (JButton boton : botonesRestringidos) {
            boton.setEnabled(!esAnotador);
            boton.setToolTipText(esAnotador ? 
                "Función restringida para anotadores" : 
                boton.getToolTipText());
        }
    }
    
    private JButton crearBotonModerno(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 255));
                g2.fillRoundRect(1, 3, getWidth()-2, getHeight()-2, 15, 15);
                
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

    private JButton crearBotonSubmenuModerno(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
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
        
        for (JButton btn : botones) {
            btn.setBackground(coloresOscuros[colorIndex]);
            btn.setForeground(Color.WHITE);
        }
        
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
        
        panelIzquierdo.setBackground(coloresClaros[colorIndex]);
        panelBotones.setBackground(coloresClaros[colorIndex]);
        panelAjustes.setBackground(coloresOscuros[colorIndex]);
        panelEquipos.setBackground(coloresOscuros[colorIndex]);
        panelJugadores.setBackground(coloresOscuros[colorIndex]);
        panelCalendario.setBackground(coloresOscuros[colorIndex]);
        
        JPanel panelBotonesInferiores = (JPanel) panelIzquierdo.getComponent(1);
        panelBotonesInferiores.setBackground(coloresClaros[colorIndex]);
        
        // Actualizar color del botón de guardar
        Component[] components = panelBotonesInferiores.getComponents();
        for (Component c : components) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                if (btn.getText().equals("\uD83D\uDCBE")) { // Botón de guardar
                    btn.setBackground(coloresOscuros[colorIndex]);
                }
            }
        }
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
    
    private void realizarRespaldoRemotoSimple() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Desea crear un respaldo remoto de los datos?",
            "Confirmar Respaldo",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try (Socket sfd = new Socket("localhost", 7000);
             DataInputStream fis = new DataInputStream(new FileInputStream("Serie_Nacional.DAT"));
             DataOutputStream os = new DataOutputStream(sfd.getOutputStream())) {
            
            int unByte;
            while ((unByte = fis.read()) != -1) {
                os.write(unByte);
                os.flush();
            }
            
            JOptionPane.showMessageDialog(this,
                "Respaldo remoto creado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (UnknownHostException uhe) {
            JOptionPane.showMessageDialog(this,
                "No se puede acceder al servidor: " + uhe.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                "Error en comunicación: " + ioe.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
}