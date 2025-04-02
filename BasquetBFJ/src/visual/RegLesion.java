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
        
        setTitle("Registrar Lesi�n");
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setBounds(100, 100, 500, 400);
        setLocationRelativeTo(null);
        setModal(true);
        
        contentPanel = new JPanel();
        contentPanel.setBackground(colorClaro);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new GridLayout(6, 2, 10, 15));
        
        // ID de la lesi�n
        JLabel lblId = new JLabel("ID Lesi�n:");
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
        
        // Tipo de lesi�n
        JLabel lblLesion = new JLabel("Tipo de Lesi�n:");
        lblLesion.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblLesion);
        
        txtLesion = new JTextField();
        contentPanel.add(txtLesion);
        
        JLabel lblFechaLesion = new JLabel("Fecha Lesi�n (DD/MM/YYYY):");
        lblFechaLesion.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblFechaLesion);
        
        txtFechaLesion = new JTextField();
        contentPanel.add(txtFechaLesion);
        
        // Fecha de recuperaci�n
        JLabel lblFechaRecuperacion = new JLabel("Fecha Recuperaci�n (DD/MM/YYYY):");
        lblFechaRecuperacion.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblFechaRecuperacion);
        
        txtFechaRecuperacion = new JTextField();
        contentPanel.add(txtFechaRecuperacion);
        
        // Estado de lesi�n
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
            
            // Validar que la fecha de recuperaci�n sea posterior a la de lesi�n
            if (fechaRecuperacion.isBefore(fechaLesion)) {
                throw new Exception("La fecha de recuperaci�n debe ser posterior a la fecha de lesi�n");
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
                "Lesi�n registrada exitosamente para " + jugador.getNombre(), 
                "�xito", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha inv�lido. Use DD/MM/YYYY", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar la lesi�n: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}