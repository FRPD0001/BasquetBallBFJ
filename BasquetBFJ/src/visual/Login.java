package visual;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import logico.SerieNacional;
import logico.User;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldUsuario;
    private JPasswordField textFieldPassword;
    private String tipoAcceso = "";
    private Color[] coloresOscuros = {new Color(147, 112, 219), new Color(100, 149, 237), Color.RED, new Color(17, 115, 68)};
    private Color[] coloresClaros = {new Color(216, 191, 216), new Color(200, 220, 255), new Color(255, 200, 200), new Color(200, 255, 200)};
    private int colorIndex = 0;
    private JButton btnAdmin;
    private JButton btnAnotador;

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

    public static void main(String[] args) {
        File archivo = new File("Serie_Nacional.DAT");
        if (archivo.exists()) {
            SerieNacional.getInstance().cargarFicheroTest();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setTitle("Login - Basketball Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 800);
        setLocationRelativeTo(null);
        
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(coloresClaros[colorIndex]);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Panel principal con logo y formulario
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(coloresClaros[colorIndex]);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(panelPrincipal, BorderLayout.CENTER);

        // Logo
        JLabel lblLogo = new JLabel(new ImageIcon("media/LogoProyecto.png"));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblLogo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(10, 30)));

        // Panel de tipo de acceso con botones estilo Principal
        JPanel panelTipoAcceso = new JPanel();
        panelTipoAcceso.setLayout(new GridLayout(1, 2, 15, 15));
        panelTipoAcceso.setBackground(coloresClaros[colorIndex]);
        panelTipoAcceso.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panelTipoAcceso.setMaximumSize(new Dimension(400, 60));
        
        
        Color colorSeleccion = new Color(
                Math.min(coloresOscuros[colorIndex].getRed() + 40, 255),
                Math.min(coloresOscuros[colorIndex].getGreen() + 40, 255),
                Math.min(coloresOscuros[colorIndex].getBlue() + 40, 255)
            );

        btnAdmin = crearBotonModerno("Administrador");
        btnAdmin.addActionListener(e -> {
            tipoAcceso = "admin";
            // Resaltar botón seleccionado
            btnAdmin.setBackground(colorSeleccion);
            btnAdmin.setForeground(Color.WHITE);
            // Restaurar botón no seleccionado
            btnAnotador.setBackground(coloresOscuros[colorIndex]);
            btnAnotador.setForeground(Color.WHITE);
        });

        btnAnotador = crearBotonModerno("Anotador");
        btnAnotador.addActionListener(e -> {
            tipoAcceso = "anotador";
            // Resaltar botón seleccionado
            btnAnotador.setBackground(colorSeleccion);
            btnAnotador.setForeground(Color.WHITE);
            // Restaurar botón no seleccionado
            btnAdmin.setBackground(coloresOscuros[colorIndex]);
            btnAdmin.setForeground(Color.WHITE);
        });

        configurarHoverBoton(btnAdmin, coloresOscuros[colorIndex], colorSeleccion);
        configurarHoverBoton(btnAnotador, coloresOscuros[colorIndex], colorSeleccion);
        
        panelTipoAcceso.add(btnAdmin);
        panelTipoAcceso.add(btnAnotador);
        panelPrincipal.add(panelTipoAcceso);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel de formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(4, 1, 5, 10));
        panelFormulario.setBackground(coloresClaros[colorIndex]);
        panelFormulario.setMaximumSize(new Dimension(400, 200));

        // Campo Usuario
        JPanel panelUsuario = new JPanel(new BorderLayout(5, 5));
        panelUsuario.setBackground(coloresClaros[colorIndex]);
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsuario.setForeground(coloresOscuros[colorIndex]);
        panelUsuario.add(lblUsuario, BorderLayout.NORTH);
        textFieldUsuario = new JTextField();
        textFieldUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        textFieldUsuario.setBorder(new RoundedBorder(10));
        panelUsuario.add(textFieldUsuario, BorderLayout.CENTER);
        panelFormulario.add(panelUsuario);

        // Campo Contraseña
        JPanel panelPassword = new JPanel(new BorderLayout(5, 5));
        panelPassword.setBackground(coloresClaros[colorIndex]);
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setForeground(coloresOscuros[colorIndex]);
        panelPassword.add(lblPassword, BorderLayout.NORTH);
        textFieldPassword = new JPasswordField();
        textFieldPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        textFieldPassword.setBorder(new RoundedBorder(10));
        panelPassword.add(textFieldPassword, BorderLayout.CENTER);
        panelFormulario.add(panelPassword);

        panelPrincipal.add(panelFormulario);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel de botones inferiores
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(coloresClaros[colorIndex]);

        JButton btnLogin = crearBotonModerno("Iniciar Sesión");
        btnLogin.setBackground(new Color(52, 199, 89)); // Verde similar al botón de guardar
        btnLogin.addActionListener(e -> validarLogin());
        panelBotones.add(btnLogin);

        JButton btnSalir = crearBotonModerno("Salir");
        btnSalir.setBackground(new Color(211, 47, 47)); // Rojo similar al botón de cerrar
        btnSalir.addActionListener(e -> System.exit(0));
        panelBotones.add(btnSalir);

        contentPane.add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarHoverBoton(JButton boton, Color colorNormal, Color colorSeleccion) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!boton.getBackground().equals(colorSeleccion)) {
                    boton.setBackground(colorNormal.darker());
                }
                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!boton.getBackground().equals(colorSeleccion)) {
                    boton.setBackground(colorNormal);
                }
            }
        });
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
        
        boton.setPreferredSize(new Dimension(180, 45));
        boton.setBackground(coloresOscuros[colorIndex]);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        
        
        return boton;
    }

    private void validarLogin() {
        String usuario = textFieldUsuario.getText().trim();
        String password = new String(textFieldPassword.getPassword()).trim();

        if (tipoAcceso.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un tipo de acceso", 
                "Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe completar ambos campos", 
                "Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (SerieNacional.getInstance().autenticarUsuario(usuario, password)) {
            User usuarioActual = SerieNacional.getUsuarioActual();
            
            if (("admin".equals(tipoAcceso) && !"Administrador".equals(usuarioActual.getTipo())) ||
                ("anotador".equals(tipoAcceso) && "Administrador".equals(usuarioActual.getTipo()))) {
                
                JOptionPane.showMessageDialog(this, 
                    "Acceso denegado: No tiene permisos para ingresar como " + 
                    ("admin".equals(tipoAcceso) ? "Administrador" : "Anotador"), 
                    "Error de autenticación", 
                    JOptionPane.ERROR_MESSAGE);
                SerieNacional.cerrarSesion();
                return;
            }

            SerieNacional.getInstance().cargarFicheroTest();
            Principal principal = new Principal();
            principal.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Credenciales incorrectas", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            textFieldPassword.setText("");
            textFieldUsuario.requestFocus();
        }
    }
}