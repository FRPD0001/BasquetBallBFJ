package visual;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logico.Equipo;
import logico.SerieNacional;

public class RegEquipo extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTextField txtNombre;
    private JLabel lblColorSeleccionado;
    private Color colorSeleccionado;
    private JLabel lblId;
    private Equipo aux; // Para edición

    public RegEquipo( Color colorOscuro, Color colorClaro) {
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        this.aux = aux;
        setTitle(aux == null ? "Registrar Equipo" : "Modificar Equipo");
        setBounds(100, 100, 500, 250);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        // Aplicando colores personalizados
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(colorClaro);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // Configuración de componentes
        lblId = new JLabel("ID:");
        lblId.setBounds(30, 20, 50, 20);
        lblId.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblId);

        JLabel lblIdValue = new JLabel();
        lblIdValue.setBounds(70, 20, 150, 20);
        lblIdValue.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdValue.setText(aux == null ? "E-" + SerieNacional.getInstance().getGenEquipo() : aux.getId());
        contentPanel.add(lblIdValue);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(250, 20, 60, 20);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(310, 20, 150, 25);
        contentPanel.add(txtNombre);
        txtNombre.setColumns(10);

        JButton btnSeleccionarColor = new JButton("Seleccionar Color");
        btnSeleccionarColor.setBounds(50, 80, 170, 30);
        btnSeleccionarColor.setFont(new Font("Arial", Font.BOLD, 12));
        btnSeleccionarColor.setBackground(colorOscuro);
        btnSeleccionarColor.setForeground(Color.WHITE);
        contentPanel.add(btnSeleccionarColor);

        lblColorSeleccionado = new JLabel(" ");
        lblColorSeleccionado.setBounds(250, 80, 60, 30);
        lblColorSeleccionado.setOpaque(true);
        lblColorSeleccionado.setBackground(Color.WHITE);
        lblColorSeleccionado.setBorder(new LineBorder(Color.BLACK));
        contentPanel.add(lblColorSeleccionado);

        // Cargar datos si estamos editando
        if (aux != null) {
            txtNombre.setText(aux.getNombre());
            lblColorSeleccionado.setBackground(aux.getColor());
            colorSeleccionado = aux.getColor();
        }

        btnSeleccionarColor.addActionListener(e -> {
            colorSeleccionado = JColorChooser.showDialog(null, "Elige un color", Color.WHITE);
            if (colorSeleccionado != null) {
                lblColorSeleccionado.setBackground(colorSeleccionado);
            }
        });

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorClaro);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton(aux == null ? "Registrar" : "Modificar");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setBackground(new Color(34, 139, 34));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> {
            if (validarCampos()) {
                if (aux == null) {
                    registrarEquipo(lblIdValue.getText());
                } else {
                    modificarEquipo();
                }
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(178, 34, 34));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (colorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un color", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void registrarEquipo(String id) {
        try {
            Equipo nuevoEquipo = new Equipo(id, txtNombre.getText().trim(), colorSeleccionado);
            SerieNacional.getInstance().agregarEquipo(nuevoEquipo);

            JOptionPane.showMessageDialog(this,
                    "Equipo registrado exitosamente!\nID: " + id,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar equipo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarEquipo() {
        try {
            aux.setNombre(txtNombre.getText().trim());
            aux.setColor(colorSeleccionado);

            JOptionPane.showMessageDialog(this,
                    "Equipo modificado exitosamente!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar equipo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
