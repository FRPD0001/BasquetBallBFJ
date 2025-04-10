package visual;

import java.awt.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import logico.Equipo;
import logico.SerieNacional;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;  // Para el selector de color
import javax.swing.BorderFactory;  // Para los bordes
import javax.swing.ImageIcon;      // Para el icono de la ventana

public class RegEquipo extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtNombre;
    private JLabel lblColorSeleccionado;
    private Color colorSeleccionado;
    private JTextField txtID;
    private JComboBox<String> cbxCiudad;
    private Equipo aux;
    private ImageIcon appIcon;

    private String[] CIUDADES = {
        "Seleccionar Pais", "Estados Unidos", "Canada", "Mexico", "Republica Dominicana",
        "Puerto Rico", "Cuba", "Argentina", "Venezuela", "Chile", "Brazil"
    };

    public RegEquipo(Color colorPrincipal, Color colorSecundario, Equipo equipoAModificar) {
        this.aux = equipoAModificar;
        appIcon = new ImageIcon("media/LogoProyecto.png");
        setIconImage(appIcon.getImage());
        setTitle(aux == null ? "Registrar Equipo" : "Modificar Equipo");
        setModal(true);
        setBounds(100, 100, 480, 300);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        contentPanel.setBackground(colorSecundario);

        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(12, 13, 56, 16);
        contentPanel.add(lblID);

        txtID = new JTextField();
        txtID.setEditable(false);
        txtID.setBounds(105, 10, 116, 22);
        contentPanel.add(txtID);
        txtID.setColumns(10);
        txtID.setText(aux == null ? "E-" + SerieNacional.getInstance().getGenEquipo() : aux.getId());

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(12, 42, 56, 16);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(105, 39, 197, 22);
        contentPanel.add(txtNombre);
        txtNombre.setColumns(10);

        JLabel lblCiudad = new JLabel("País:");
        lblCiudad.setBounds(12, 71, 70, 16);
        contentPanel.add(lblCiudad);

        cbxCiudad = new JComboBox<>(CIUDADES);
        cbxCiudad.setBounds(105, 68, 197, 22);
        contentPanel.add(cbxCiudad);

        JLabel lblColor = new JLabel("Color:");
        lblColor.setBounds(12, 100, 70, 16);
        contentPanel.add(lblColor);
        

        JButton btnColor = new JButton("Seleccionar");
        btnColor.setBounds(105, 97, 116, 22);
        btnColor.setBackground(colorPrincipal);
        btnColor.setForeground(Color.WHITE);
        btnColor.addActionListener(e -> mostrarSelectorColor());
        contentPanel.add(btnColor);

        lblColorSeleccionado = new JLabel(" ");
        lblColorSeleccionado.setBounds(233, 97, 22, 22);
        lblColorSeleccionado.setOpaque(true);
        lblColorSeleccionado.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblColorSeleccionado.setBackground(Color.WHITE);
        contentPanel.add(lblColorSeleccionado);

        JPanel buttonPane = new JPanel();
        buttonPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton(aux == null ? "Registrar" : "Modificar");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setBackground(new Color(34, 139, 34));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (aux == null) {
                    registrarEquipo();
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

        if (aux != null) {
            cargarDatosEquipo();
        }
    }

    private void cargarDatosEquipo() {
        txtID.setText(aux.getId());
        txtNombre.setText(aux.getNombre());
        colorSeleccionado = aux.getColor();
        lblColorSeleccionado.setBackground(colorSeleccionado);

        for (int i = 0; i < CIUDADES.length; i++) {
            if (CIUDADES[i].equals(aux.getCiudad())) {
                cbxCiudad.setSelectedIndex(i);
                break;
            }
        }
    }

    private void mostrarSelectorColor() {
        // Inicializar colorSeleccionado si es null
        if (colorSeleccionado == null) {
            colorSeleccionado = Color.WHITE;
        }
        
        JColorChooser chooser = new JColorChooser(colorSeleccionado);
        
        // Configurar paneles (mantener solo Swatches)


        
        // Crear diálogo
        JDialog dialog = JColorChooser.createDialog(
            this,
            "Seleccionar Color",
            true,
            chooser,
            e -> {
                colorSeleccionado = chooser.getColor();
                lblColorSeleccionado.setBackground(colorSeleccionado);
                lblColorSeleccionado.repaint(); // Añadir esto para refrescar
            },
            null
        );
        
        dialog.setVisible(true);
    }

    private void registrarEquipo() {
        if (!validarCampos()) return;

        try {
            String id = txtID.getText();
            String nombre = txtNombre.getText().trim();
            String ciudad = (String) cbxCiudad.getSelectedItem();

            if (existeEquipoConNombre(nombre)) {
                JOptionPane.showMessageDialog(this, 
                    "Ya existe un equipo con ese nombre", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (existeEquipoConColor(colorSeleccionado)) {
                JOptionPane.showMessageDialog(this, 
                    "Ya existe un equipo con ese color", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Equipo nuevoEquipo = new Equipo(id, nombre, colorSeleccionado);
            nuevoEquipo.setCiudad(ciudad);
            SerieNacional.getInstance().agregarEquipo(nuevoEquipo);

            JOptionPane.showMessageDialog(this, 
                "Equipo registrado exitosamente.\n" +
                "ID: " + id + "\n" +
                "Nombre: " + nombre, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar equipo: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarEquipo() {
        if (!validarCampos()) return;

        try {
            String nombre = txtNombre.getText().trim();
            String ciudad = (String) cbxCiudad.getSelectedItem();

            for (Equipo equipo : SerieNacional.getInstance().getMisEquipos()) {
                if (equipo.getNombre().equalsIgnoreCase(nombre) && !equipo.getId().equals(aux.getId())) {
                    JOptionPane.showMessageDialog(this, 
                        "Ya existe otro equipo con ese nombre", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            for (Equipo equipo : SerieNacional.getInstance().getMisEquipos()) {
                if (equipo.getColor().equals(colorSeleccionado) && !equipo.getId().equals(aux.getId())) {
                    JOptionPane.showMessageDialog(this, 
                        "Ya existe otro equipo con ese color", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            aux.setNombre(nombre);
            aux.setColor(colorSeleccionado);
            aux.setCiudad(ciudad);

            JOptionPane.showMessageDialog(this, 
                "Equipo modificado exitosamente.\n" +
                "ID: " + aux.getId() + "\n" +
                "Nombre: " + aux.getNombre(), 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al modificar equipo: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        cbxCiudad.setSelectedIndex(0);
        colorSeleccionado = null;
        lblColorSeleccionado.setBackground(Color.WHITE);
        txtID.setText("E-" + SerieNacional.getInstance().getGenEquipo());
        txtNombre.requestFocus();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (colorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un color", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cbxCiudad.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un país", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean existeEquipoConNombre(String nombre) {
        for (Equipo equipo : SerieNacional.getInstance().getMisEquipos()) {
            if (equipo.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    private boolean existeEquipoConColor(Color color) {
        for (Equipo equipo : SerieNacional.getInstance().getMisEquipos()) {
            if (equipo.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }
}