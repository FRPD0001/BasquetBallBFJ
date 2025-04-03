package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.text.MaskFormatter;
import logico.SerieNacional;
import logico.Jugador;
import logico.Lesion;

public class RegLesion extends JDialog {
    private JPanel contentPanel;
    private JTextField txtId;
    private JComboBox<Jugador> cmbJugador;
    private JTextField txtLesion;
    private JFormattedTextField txtFechaLesion;
    private JFormattedTextField txtFechaRecuperacion;
    private JCheckBox chkLesionado;
    private Color colorOscuro;
    private Color colorClaro;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RegLesion(Color colorOscuro, Color colorClaro) {
        this.colorOscuro = colorOscuro;
        this.colorClaro = colorClaro;
        
        setTitle("Registrar Lesi�n");
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setBounds(100, 100, 500, 450);
        setLocationRelativeTo(null);
        setModal(true);
        
        contentPanel = new JPanel();
        contentPanel.setBackground(colorClaro);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new GridLayout(7, 2, 10, 15));
        
        try {
            // Configurar m�scaras para las fechas
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            
            // ID de la lesi�n
            JLabel lblId = new JLabel("ID Lesi�n:");
            lblId.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblId);
            
            txtId = new JTextField();
            txtId.setEditable(false);
            txtId.setText("LES-" + Jugador.getGenLesion());
            txtId.setFont(new Font("Arial", Font.PLAIN, 14));
            contentPanel.add(txtId);
            
            // Jugador afectado
            JLabel lblJugador = new JLabel("Jugador:");
            lblJugador.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblJugador);
            
            cmbJugador = new JComboBox<>();
            cmbJugador.setFont(new Font("Arial", Font.PLAIN, 14));
            cargarJugadores();
            contentPanel.add(cmbJugador);
            
            // Tipo de lesi�n
            JLabel lblLesion = new JLabel("Tipo de Lesi�n:");
            lblLesion.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblLesion);
            
            txtLesion = new JTextField();
            txtLesion.setFont(new Font("Arial", Font.PLAIN, 14));
            contentPanel.add(txtLesion);
            
            // Fecha de lesi�n
            JLabel lblFechaLesion = new JLabel("Fecha Lesi�n:");
            lblFechaLesion.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblFechaLesion);
            
            txtFechaLesion = new JFormattedTextField(dateMask);
            txtFechaLesion.setFont(new Font("Arial", Font.PLAIN, 14));
            txtFechaLesion.setToolTipText("Formato: DD/MM/AAAA");
            contentPanel.add(txtFechaLesion);
            
            // Fecha de recuperaci�n
            JLabel lblFechaRecuperacion = new JLabel("Fecha Recuperaci�n:");
            lblFechaRecuperacion.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblFechaRecuperacion);
            
            txtFechaRecuperacion = new JFormattedTextField(dateMask);
            txtFechaRecuperacion.setFont(new Font("Arial", Font.PLAIN, 14));
            txtFechaRecuperacion.setToolTipText("Formato: DD/MM/AAAA");
            contentPanel.add(txtFechaRecuperacion);
            
            // Estado de lesi�n
            JLabel lblLesionado = new JLabel("Actualmente Lesionado:");
            lblLesionado.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblLesionado);
            
            chkLesionado = new JCheckBox();
            contentPanel.add(chkLesionado);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al inicializar componentes: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(colorClaro);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(colorOscuro);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarLesion());
        buttonPanel.add(btnGuardar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(colorOscuro);
        btnGuardar.setForeground(Color.WHITE);
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
            // Validar jugador seleccionado
            if (cmbJugador.getSelectedItem() == null) {
                throw new Exception("Debe seleccionar un jugador");
            }
            
            Jugador jugador = (Jugador) cmbJugador.getSelectedItem();
            
            // Validar tipo de lesi�n
            String tipoLesion = txtLesion.getText().trim();
            if (tipoLesion.isEmpty()) {
                throw new Exception("Debe especificar el tipo de lesi�n");
            }
            
            // Validar y parsear fechas
            LocalDate fechaLesion = parseFecha(txtFechaLesion.getText().trim());
            LocalDate fechaRecuperacion = parseFecha(txtFechaRecuperacion.getText().trim());
            
            if (fechaRecuperacion.isBefore(fechaLesion)) {
                throw new Exception("La fecha de recuperaci�n debe ser posterior a la de lesi�n");
            }
            
            boolean lesionado = chkLesionado.isSelected();
            
            // Crear y registrar la lesi�n
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
                "Lesi�n registrada exitosamente para:\n" + jugador.getNombre(),
                "�xito", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar lesi�n:\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private LocalDate parseFecha(String fechaStr) throws Exception {
        try {
            // Eliminar caracteres no num�ricos y espacios
            String cleaned = fechaStr.replaceAll("[^0-9/]", "").trim();
            
            // Verificar formato b�sico
            if (!cleaned.matches("\\d{2}/\\d{2}/\\d{4}")) {
                throw new DateTimeParseException("Formato inv�lido", fechaStr, 0);
            }
            
            return LocalDate.parse(cleaned, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new Exception("Fecha inv�lida. Use formato DD/MM/AAAA");
        }
    }
}