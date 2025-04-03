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
        
        setTitle("Registrar Lesión");
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
            // Configurar máscaras para las fechas
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            
            // ID de la lesión
            JLabel lblId = new JLabel("ID Lesión:");
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
            
            // Tipo de lesión
            JLabel lblLesion = new JLabel("Tipo de Lesión:");
            lblLesion.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblLesion);
            
            txtLesion = new JTextField();
            txtLesion.setFont(new Font("Arial", Font.PLAIN, 14));
            contentPanel.add(txtLesion);
            
            // Fecha de lesión
            JLabel lblFechaLesion = new JLabel("Fecha Lesión:");
            lblFechaLesion.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblFechaLesion);
            
            txtFechaLesion = new JFormattedTextField(dateMask);
            txtFechaLesion.setFont(new Font("Arial", Font.PLAIN, 14));
            txtFechaLesion.setToolTipText("Formato: DD/MM/AAAA");
            contentPanel.add(txtFechaLesion);
            
            // Fecha de recuperación
            JLabel lblFechaRecuperacion = new JLabel("Fecha Recuperación:");
            lblFechaRecuperacion.setFont(new Font("Arial", Font.BOLD, 14));
            contentPanel.add(lblFechaRecuperacion);
            
            txtFechaRecuperacion = new JFormattedTextField(dateMask);
            txtFechaRecuperacion.setFont(new Font("Arial", Font.PLAIN, 14));
            txtFechaRecuperacion.setToolTipText("Formato: DD/MM/AAAA");
            contentPanel.add(txtFechaRecuperacion);
            
            // Estado de lesión
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
            
            // Validar tipo de lesión
            String tipoLesion = txtLesion.getText().trim();
            if (tipoLesion.isEmpty()) {
                throw new Exception("Debe especificar el tipo de lesión");
            }
            
            // Validar y parsear fechas
            LocalDate fechaLesion = parseFecha(txtFechaLesion.getText().trim());
            LocalDate fechaRecuperacion = parseFecha(txtFechaRecuperacion.getText().trim());
            
            if (fechaRecuperacion.isBefore(fechaLesion)) {
                throw new Exception("La fecha de recuperación debe ser posterior a la de lesión");
            }
            
            boolean lesionado = chkLesionado.isSelected();
            
            // Crear y registrar la lesión
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
    
    private LocalDate parseFecha(String fechaStr) throws Exception {
        try {
            // Eliminar caracteres no numéricos y espacios
            String cleaned = fechaStr.replaceAll("[^0-9/]", "").trim();
            
            // Verificar formato básico
            if (!cleaned.matches("\\d{2}/\\d{2}/\\d{4}")) {
                throw new DateTimeParseException("Formato inválido", fechaStr, 0);
            }
            
            return LocalDate.parse(cleaned, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new Exception("Fecha inválida. Use formato DD/MM/AAAA");
        }
    }
}