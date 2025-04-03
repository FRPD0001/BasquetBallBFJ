package visual;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
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
        
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        contentPanel = new JPanel();
        contentPanel.setBackground(colorClaro);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            
            txtId = new JTextField("LES-" + SerieNacional.getInstance().getGenJugador());
            txtId.setEditable(false);
            
            cmbJugador = new JComboBox<>();
            cargarJugadores();
            
            txtLesion = new JTextField();
            
            txtFechaLesion = new JFormattedTextField(dateMask);
            txtFechaLesion.setToolTipText("Formato DD/MM/AAAA");
            
            txtFechaRecuperacion = new JFormattedTextField(dateMask);
            txtFechaRecuperacion.setToolTipText("Formato DD/MM/AAAA");
            
            chkLesionado = new JCheckBox();
            
        } catch (Exception e) {
            showError("Error al inicializar componentes: " + e.getMessage());
        }
    }

    private void setupLayout() {
        contentPanel.setLayout(new GridLayout(7, 2, 10, 10));
        
        addFormField("ID Lesi�n:", txtId);
        addFormField("Jugador:", cmbJugador);
        addFormField("Tipo de Lesi�n:", txtLesion);
        addFormField("Fecha Lesi�n (DD/MM/AAAA):", txtFechaLesion);
        addFormField("Fecha Recuperaci�n (DD/MM/AAAA):", txtFechaRecuperacion);
        addFormField("Actualmente Lesionado:", chkLesionado);

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(colorClaro);
        
        JButton btnGuardar = createButton("Guardar", e -> guardarLesion());
        JButton btnCancelar = createButton("Cancelar", e -> dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        contentPanel.add(lbl);
        contentPanel.add(field);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(colorOscuro);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(action);
        return btn;
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
                throw new Exception("Seleccione un jugador");
            }
            Jugador jugador = (Jugador) cmbJugador.getSelectedItem();
            
            // Validar tipo de lesi�n
            String tipoLesion = txtLesion.getText().trim();
            if (tipoLesion.isEmpty()) {
                throw new Exception("Ingrese el tipo de lesi�n");
            }
            
            // Validar y parsear fechas
            LocalDate fechaLesion = parseFecha(txtFechaLesion.getText());
            LocalDate fechaRecuperacion = parseFecha(txtFechaRecuperacion.getText());
            
            if (fechaRecuperacion.isBefore(fechaLesion)) {
                throw new Exception("La fecha de recuperaci�n debe ser posterior a la de lesi�n");
            }
            
            boolean lesionado = chkLesionado.isSelected();
            
            // Crear la lesi�n
            Lesion lesion = new Lesion(
                txtId.getText(),
                jugador,
                fechaRecuperacion,
                fechaLesion,
                tipoLesion,
                lesionado
            );
            
            // Registrar la lesi�n
            jugador.agregarLesion(lesion);
            jugador.setLesionado(lesionado);
            
            // Mostrar mensaje de �xito
            showSuccess("Lesi�n registrada exitosamente para " + jugador.getNombre());
            dispose();
            
        } catch (Exception ex) {
            showError("Error al registrar lesi�n: " + ex.getMessage());
            ex.printStackTrace(); // Para depuraci�n
        }
    }
    
    private LocalDate parseFecha(String fechaStr) throws Exception {
        try {
            String cleaned = fechaStr.replaceAll("[^0-9/]", "").trim();
            
            if (!cleaned.matches("\\d{2}/\\d{2}/\\d{4}")) {
                throw new Exception("Formato de fecha inv�lido");
            }
            
            return LocalDate.parse(cleaned, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new Exception("Fecha inv�lida. Use formato DD/MM/AAAA");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "�xito", JOptionPane.INFORMATION_MESSAGE);
    }
}