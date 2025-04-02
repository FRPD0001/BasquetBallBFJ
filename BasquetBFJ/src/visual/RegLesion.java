package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import logico.SerieNacional;
import logico.Jugador;
import logico.Lesion;

public class RegLesion extends JDialog {
    private JPanel contentPanel;
    private JTextField txtId;
    private JComboBox<Jugador> cmbJugador;
    private JTextField txtLesion;
    private JTextField txtFechaLesion;
    private JTextField txtFechaRecuperacion;
    private JCheckBox chkLesionado;
    private Color colorOscuro;
    private Color colorClaro;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RegLesion(Color colorOscuro, Color colorClaro) {
        this.colorOscuro = colorOscuro;
        this.colorClaro = colorClaro;
        
        setTitle("Registrar Lesión");
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setBounds(100, 100, 500, 400);
        setLocationRelativeTo(null);
        setModal(true);
        
        contentPanel = new JPanel();
        contentPanel.setBackground(colorClaro);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new GridLayout(6, 2, 10, 15));
        
        // ID de la lesión
        JLabel lblId = new JLabel("ID Lesión:");
        lblId.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblId);
        
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setText("LES-" + SerieNacional.getInstance().getGenJugador());
        contentPanel.add(txtId);
        
        // Jugador afectado
        JLabel lblJugador = new JLabel("Jugador:");
        lblJugador.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblJugador);
        
        cmbJugador = new JComboBox<>();
        cargarJugadores();
        contentPanel.add(cmbJugador);
        
        // Tipo de lesión
        JLabel lblLesion = new JLabel("Tipo de Lesión:");
        lblLesion.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblLesion);
        
        txtLesion = new JTextField();
        contentPanel.add(txtLesion);
        
        JLabel lblFechaLesion = new JLabel("Fecha Lesión (DD/MM/YYYY):");
        lblFechaLesion.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblFechaLesion);
        
        txtFechaLesion = new JTextField();
        contentPanel.add(txtFechaLesion);
        
        // Fecha de recuperación
        JLabel lblFechaRecuperacion = new JLabel("Fecha Recuperación (DD/MM/YYYY):");
        lblFechaRecuperacion.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblFechaRecuperacion);
        
        txtFechaRecuperacion = new JTextField();
        contentPanel.add(txtFechaRecuperacion);
        
        // Estado de lesión
        JLabel lblLesionado = new JLabel("Actualmente Lesionado:");
        lblLesionado.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblLesionado);
        
        chkLesionado = new JCheckBox();
        contentPanel.add(chkLesionado);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(colorClaro);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(colorOscuro);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarLesion());
        buttonPanel.add(btnGuardar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(colorOscuro);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());
        buttonPanel.add(btnCancelar);
    }
    
    private void cargarJugadores() {
        cmbJugador.removeAllItems();
        for (Jugador jugador : SerieNacional.getInstance().getMisJugadores()) {
            cmbJugador.addItem(jugador);
        }
    }
    
    private void guardarLesion() {
        try {
            // Validar campos obligatorios
            if (cmbJugador.getSelectedItem() == null || txtLesion.getText().trim().isEmpty() ||
                txtFechaLesion.getText().trim().isEmpty() || txtFechaRecuperacion.getText().trim().isEmpty()) {
                throw new Exception("Todos los campos son obligatorios");
            }
            
            Jugador jugador = (Jugador) cmbJugador.getSelectedItem();
            String tipoLesion = txtLesion.getText().trim();
            
            // Parsear fechas con formato DD/MM/YYYY
            LocalDate fechaLesion = LocalDate.parse(txtFechaLesion.getText().trim(), dateFormatter);
            LocalDate fechaRecuperacion = LocalDate.parse(txtFechaRecuperacion.getText().trim(), dateFormatter);
            
            // Validar que la fecha de recuperación sea posterior a la de lesión
            if (fechaRecuperacion.isBefore(fechaLesion)) {
                throw new Exception("La fecha de recuperación debe ser posterior a la fecha de lesión");
            }
            
            boolean lesionado = chkLesionado.isSelected();
            
            Lesion lesion = new Lesion(
                txtId.getText(),
                jugador,
                fechaRecuperacion,
                fechaLesion,
                tipoLesion,
                lesionado
            );
            
            jugador.getMisLesiones().add(lesion);
            jugador.setLesionado(lesionado);
            
            JOptionPane.showMessageDialog(this, 
                "Lesión registrada exitosamente para " + jugador.getNombre(), 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha inválido. Use DD/MM/YYYY", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar la lesión: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}