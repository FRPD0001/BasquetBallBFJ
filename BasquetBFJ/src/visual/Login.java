package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import logico.SerieNacional;

public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldUsuario;
    private JTextField textFieldPassword;

    public static void main(String[] args) {
        // Cargar datos al iniciar
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
        setTitle("Login - Sistema de Gestión Deportiva");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(3, 2, 10, 10));
        contentPane.add(panelCentral, BorderLayout.CENTER);

        // Componentes UI
        JLabel lblUsuario = new JLabel("Usuario:");
        panelCentral.add(lblUsuario);

        textFieldUsuario = new JTextField();
        panelCentral.add(textFieldUsuario);
        textFieldUsuario.setColumns(10);

        JLabel lblPassword = new JLabel("Contraseña:");
        panelCentral.add(lblPassword);

        textFieldPassword = new JTextField();
        panelCentral.add(textFieldPassword);
        textFieldPassword.setColumns(10);

        // Panel inferior para botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        contentPane.add(panelInferior, BorderLayout.SOUTH);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });
        panelInferior.add(btnLogin);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panelInferior.add(btnSalir);
    }

    private void validarLogin() {
        String usuario = textFieldUsuario.getText().trim();
        String password = textFieldPassword.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe completar ambos campos", 
                "Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (SerieNacional.getInstance().autenticarUsuario(usuario, password)) {
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