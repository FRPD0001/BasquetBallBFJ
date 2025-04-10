package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import logico.SerieNacional;
import logico.User;

public class RegUser extends JDialog {

    private JTextField txtUsuario;
    private JTextField txtPassword;
    private JTextField txtConfirmar;
    private JComboBox<String> cbTipo;
    private Color[] coloresOscuros;
    private Color[] coloresClaros;
    private int colorIndex; // Índice de color

    public RegUser(Color[] coloresOscuros, Color[] coloresClaros, int colorIndex) {
        this.coloresOscuros = coloresOscuros;
        this.coloresClaros = coloresClaros;
        this.colorIndex = colorIndex;
        initComponents();
    }

    private void initComponents() {
        setTitle("Registro de Nuevo Usuario");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        // Establecer icono
        ImageIcon icon = new ImageIcon("media/LogoProyecto.png");
        setIconImage(icon.getImage());

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        
        // Usar colorIndex para seleccionar el color
        panel.setBackground(coloresClaros[colorIndex]);  // Fondo con el color claro seleccionado
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        JLabel lblTipo = new JLabel("Tipo de usuario:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTipo, gbc);

        cbTipo = new JComboBox<>(new String[]{"Administrador", "Anotador"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(cbTipo, gbc);

        JLabel lblUsuario = new JLabel("Nombre de usuario:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtUsuario, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPassword, gbc);

        txtPassword = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtPassword, gbc);

        JLabel lblConfirmar = new JLabel("Confirmar contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblConfirmar, gbc);

        txtConfirmar = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(txtConfirmar, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
        panelBotones.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnCancelar);

        // Configuración final
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private void registrarUsuario() {
        String tipo = cbTipo.getSelectedItem().toString();
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmacion = txtConfirmar.getText().trim();

        // Validaciones
        if (usuario.isEmpty() || password.isEmpty() || confirmacion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Todos los campos son obligatorios",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmacion)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtConfirmar.setText("");
            txtPassword.requestFocus();
            return;
        }

        if (SerieNacional.getInstance().existeUsuario(usuario)) {
            JOptionPane.showMessageDialog(this,
                "El nombre de usuario ya existe",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            txtUsuario.requestFocus();
            return;
        }

        // Crear y guardar usuario
        User nuevoUsuario = new User(tipo, usuario, password);
        SerieNacional.getInstance().agregarUsuario(nuevoUsuario);
        SerieNacional.getInstance().guardarFileTest(); // Guardar cambios en disco

        JOptionPane.showMessageDialog(this,
            "Usuario registrado exitosamente",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);

        dispose(); // Cerrar ventana
    }
}

