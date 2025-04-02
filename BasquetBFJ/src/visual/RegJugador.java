package visual;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import logico.Jugador;
import logico.SerieNacional;
import logico.Equipo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RegJugador extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtID;
    private JTextField txtNombre;
    private JTextField txtPeso;
    private JTextField txtAltura;
    private JComboBox<Equipo> cbxEquipos;
    private Jugador aux; // Para saber si estamos modificando o registrando

    public RegJugador(Color colorPrincipal, Color colorSecundario) {

        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());

        setTitle(aux == null ? "Registrar Jugador" : "Modificar Jugador");
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
        txtID.setText(aux == null ? "J-" + SerieNacional.getInstance().getGenJugador() : aux.getId());

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(12, 42, 56, 16);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(105, 39, 197, 22);
        contentPanel.add(txtNombre);
        txtNombre.setColumns(10);

        JLabel lblPeso = new JLabel("Peso (kg):");
        lblPeso.setBounds(12, 71, 70, 16);
        contentPanel.add(lblPeso);

        txtPeso = new JTextField();
        txtPeso.setBounds(105, 68, 116, 22);
        contentPanel.add(txtPeso);
        txtPeso.setColumns(10);

        JLabel lblAltura = new JLabel("Altura (m):");
        lblAltura.setBounds(12, 100, 70, 16);
        contentPanel.add(lblAltura);

        txtAltura = new JTextField();
        txtAltura.setBounds(105, 97, 116, 22);
        contentPanel.add(txtAltura);
        txtAltura.setColumns(10);

        JLabel lblEquipo = new JLabel("Equipo:");
        lblEquipo.setBounds(12, 129, 56, 16);
        contentPanel.add(lblEquipo);

        cbxEquipos = new JComboBox<>();
        cbxEquipos.setBounds(105, 126, 197, 22);
        cargarEquipos();
        contentPanel.add(cbxEquipos);

        JPanel buttonPane = new JPanel();
        buttonPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // Botón "Registrar" o "Modificar" (Verde fijo)
        JButton okButton = new JButton(aux == null ? "Registrar" : "Modificar");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setBackground(new Color(34, 139, 34));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {registrarJugador();}
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        // Botón "Cancelar" (Rojo fijo)
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(178, 34, 34)); // Rojo
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        loadJugador(aux);
    }

    private void cargarEquipos() {
        ArrayList<Equipo> equipos = SerieNacional.getInstance().getMisEquipos();
        cbxEquipos.addItem(null); // Opción vacía
        for (Equipo equipo : equipos) {
            cbxEquipos.addItem(equipo);
        }
    }

    private void loadJugador(Jugador aux) {
        if (aux != null) {
            txtNombre.setText(aux.getNombre());
            txtPeso.setText(String.valueOf(aux.getPeso()));
            txtAltura.setText(String.valueOf(aux.getAltura()));
            
            // Seleccionar el equipo actual del jugador
            if (aux.getEquipo() != null) {
                cbxEquipos.setSelectedItem(aux.getEquipo());
            }
        }
    }

    private void registrarJugador() {
        if (!validarCampos()) return;

        try {
            String id = txtID.getText();
            String nombre = txtNombre.getText().trim();
            float peso = Float.parseFloat(txtPeso.getText().trim());
            float altura = Float.parseFloat(txtAltura.getText().trim());
            Equipo equipo = (Equipo) cbxEquipos.getSelectedItem();

            Jugador nuevoJugador = new Jugador();
            nuevoJugador.setId(id);
            nuevoJugador.setNombre(nombre);
            nuevoJugador.setPeso(peso);
            nuevoJugador.setAltura(altura);
            nuevoJugador.setEquipo(equipo);

            SerieNacional.getInstance().agregarJugador(nuevoJugador);

            JOptionPane.showMessageDialog(null, 
                "Jugador registrado exitosamente.\n" +
                "ID: " + id + "\n" +
                "Nombre: " + nombre, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            clean();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Por favor ingrese valores numéricos válidos para peso y altura.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al registrar jugador: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        return !txtNombre.getText().trim().isEmpty() &&
               !txtPeso.getText().trim().isEmpty() &&
               !txtAltura.getText().trim().isEmpty();
    }

    private void clean() {
        txtNombre.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
        cbxEquipos.setSelectedIndex(0);
        txtID.setText("J-" + SerieNacional.getGenJugador());
    }
}
