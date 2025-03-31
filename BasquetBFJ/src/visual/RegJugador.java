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
    
    public static void main(String[] args) {
        try {
            RegJugador dialog = new RegJugador(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RegJugador(Jugador aux) {
        this.aux = aux;
        setTitle(aux == null ? "Registrar Jugador" : "Modificar Jugador");
        setModal(true);
        setBounds(100, 100, 480, 300);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(12, 13, 56, 16);
        contentPanel.add(lblID);

        txtID = new JTextField();
        txtID.setEditable(false);
        txtID.setBounds(105, 10, 116, 22);
        contentPanel.add(txtID);
        txtID.setColumns(10);
        txtID.setText(aux == null ? "J-" + SerieNacional.getGenJugador() : aux.getId());

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

        JButton okButton = new JButton(aux == null ? "Registrar" : "Modificar");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (aux == null) {
                    registrarJugador();
                } else {
                    modificarJugador();
                }
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancelar");
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

    private void modificarJugador() {
        if (!validarCampos()) return;

        try {
            aux.setNombre(txtNombre.getText().trim());
            aux.setPeso(Float.parseFloat(txtPeso.getText().trim()));
            aux.setAltura(Float.parseFloat(txtAltura.getText().trim()));
            aux.setEquipo((Equipo) cbxEquipos.getSelectedItem());

            JOptionPane.showMessageDialog(null, 
                "Jugador modificado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            // Aquí deberías actualizar la lista de jugadores si es necesario
            // ListadoJugador.loadJugadores();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Por favor ingrese valores numéricos válidos para peso y altura.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al modificar jugador: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText().trim();
        String peso = txtPeso.getText().trim();
        String altura = txtAltura.getText().trim();

        if (nombre.isEmpty() || peso.isEmpty() || altura.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Por favor, complete todos los campos.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            float pesoVal = Float.parseFloat(peso);
            float alturaVal = Float.parseFloat(altura);
            
            if (pesoVal <= 0 || pesoVal > 300) {
                JOptionPane.showMessageDialog(null, 
                    "El peso debe estar entre 0 y 300 kg.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (alturaVal <= 0 || alturaVal > 3) {
                JOptionPane.showMessageDialog(null, 
                    "La altura debe estar entre 0 y 3 metros.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Peso y altura deben ser valores numéricos.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clean() {
        txtNombre.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
        cbxEquipos.setSelectedIndex(0);
        txtID.setText("J-" + SerieNacional.getGenJugador());
    }
}