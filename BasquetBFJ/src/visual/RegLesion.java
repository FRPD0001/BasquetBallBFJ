package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.border.BevelBorder;
import javax.swing.text.MaskFormatter;
import logico.SerieNacional;
import logico.Jugador;
import logico.Lesion;
import com.toedter.calendar.JDateChooser;

public class RegLesion extends JDialog {
    private JPanel contentPanel;
    private JTextField txtId;
    private JComboBox<Jugador> cmbJugador;
    private JTextField txtLesion;
    private JDateChooser dateChooserLesion;
    private JDateChooser dateChooserRecuperacion;
    private JCheckBox chkLesionado;
    private Color colorOscuro;
    private Color colorClaro;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RegLesion(Color colorOscuro, Color colorClaro) {
        this.colorOscuro = colorOscuro;
        this.colorClaro = colorClaro;
        
        setTitle("Registrar Lesión");
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setBounds(100, 100, 500, 450);
        setLocationRelativeTo(null);
        setModal(true);
        
        contentPanel = new JPanel();
        contentPanel.setBackground(colorClaro);
        contentPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new GridLayout(7, 2, 10, 15));
        
        JLabel lblId = new JLabel("ID Lesión:");
        lblId.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(lblId);
        
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setText("LES-" + Jugador.getGenLesion());
        txtId.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(txtId);
        
        JLabel lblJugador = new JLabel("Jugador:");
        lblJugador.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(lblJugador);
        
        cmbJugador = new JComboBox<>();
        cmbJugador.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarJugadores();
        contentPanel.add(cmbJugador);
        
        JLabel lblLesion = new JLabel("Tipo de Lesión:");
        lblLesion.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(lblLesion);
        
        txtLesion = new JTextField();
        txtLesion.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(txtLesion);
        
        // Fecha de lesión con JDateChooser
        JLabel lblFechaLesion = new JLabel("Fecha Lesión:");
        lblFechaLesion.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(lblFechaLesion);
        
        dateChooserLesion = new JDateChooser();
        dateChooserLesion.setDateFormatString("dd/MM/yyyy");
        dateChooserLesion.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel panelFechaLesion = new JPanel(new BorderLayout());
        panelFechaLesion.add(dateChooserLesion);
        contentPanel.add(panelFechaLesion);
        
        // Fecha de recuperación con JDateChooser
        JLabel lblFechaRecuperacion = new JLabel("Fecha Recuperación:");
        lblFechaRecuperacion.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(lblFechaRecuperacion);
        
        dateChooserRecuperacion = new JDateChooser();
        dateChooserRecuperacion.setDateFormatString("dd/MM/yyyy");
        dateChooserRecuperacion.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel panelFechaRecuperacion = new JPanel(new BorderLayout());
        panelFechaRecuperacion.add(dateChooserRecuperacion);
        contentPanel.add(panelFechaRecuperacion);
        
        // Estado de lesión
        JLabel lblLesionado = new JLabel("Actualmente Lesionado:");
        lblLesionado.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(lblLesionado);
        
        chkLesionado = new JCheckBox();
        contentPanel.add(chkLesionado);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        buttonPanel.setBackground(Color.WHITE);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(34, 139, 34)); 
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarLesion());
        buttonPanel.add(btnGuardar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(178, 34, 34));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());
        buttonPanel.add(btnCancelar);
    }
    
    private void cargarJugadores() {
        cmbJugador.removeAllItems();
        for (Jugador jugador : SerieNacional.getInstance().getMisJugadores()) {
            cmbJugador.addItem(jugador);
        }
        cmbJugador.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Jugador) {
                    Jugador j = (Jugador) value;
                    setText(j.getNombre() + " (" + j.getId() + ")");
                }
                return this;
            }
        });
    }
    
    private void guardarLesion() {
        try {
            if (cmbJugador.getSelectedItem() == null) {
                throw new Exception("Debe seleccionar un jugador");
            }
            
            Jugador jugador = (Jugador) cmbJugador.getSelectedItem();
            
            String tipoLesion = txtLesion.getText().trim();
            if (tipoLesion.isEmpty()) {
                throw new Exception("Debe especificar el tipo de lesión");
            }
            
            if (dateChooserLesion.getDate() == null || dateChooserRecuperacion.getDate() == null) {
                throw new Exception("Debe seleccionar ambas fechas");
            }
            
            LocalDate fechaLesion = dateChooserLesion.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
                
            LocalDate fechaRecuperacion = dateChooserRecuperacion.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
            
            if (fechaRecuperacion.isBefore(fechaLesion)) {
                throw new Exception("La fecha de recuperación debe ser posterior a la de lesión");
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
            
            jugador.agregarLesion(lesion);
            jugador.setLesionado(lesionado);
            
            JOptionPane.showMessageDialog(this, 
                "Lesión registrada exitosamente para:\n" + jugador.getNombre(),
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar lesión:\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}