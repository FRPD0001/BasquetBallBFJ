package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import logico.SerieNacional;
import logico.Jugador;
import logico.Lesion;

public class ListLesion extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableLesiones;
    private DefaultTableModel tableModel;
    private JComboBox<Jugador> cbJugadores;
    private JButton btnDeslesionar;
    private Color colorOscuro;
    private Color colorClaro;

    public ListLesion(Color colorOscuro, Color colorClaro) {
        this.colorOscuro = colorOscuro;
        this.colorClaro = colorClaro;
        
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setTitle("Historial de Lesiones");
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(colorClaro);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        // Panel superior para selección de jugador
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(colorClaro);
        
        JLabel lblJugador = new JLabel("Seleccionar Jugador:");
        lblJugador.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(lblJugador);
        
        cbJugadores = new JComboBox<>();
        cbJugadores.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarJugadores();
        cbJugadores.addActionListener(e -> cargarLesiones());
        panelSuperior.add(cbJugadores);
        
        contentPanel.add(panelSuperior, BorderLayout.NORTH);

        // Configuración de la tabla
        String[] columnNames = {"ID", "Tipo de Lesión", "Fecha Lesión", "Fecha Recuperación", "Actualmente Lesionado"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { // Columna de estado
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        
        tableLesiones = new JTable(tableModel);
        tableLesiones.setRowHeight(25);
        tableLesiones.setFont(new Font("Arial", Font.PLAIN, 14));
        tableLesiones.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableLesiones.getTableHeader().setBackground(colorOscuro);
        tableLesiones.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tableLesiones);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorClaro);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // Botón para deslesionar
        btnDeslesionar = new JButton("Deslesionar Jugador");
        btnDeslesionar.setFont(new Font("Arial", Font.BOLD, 12));
        btnDeslesionar.setBackground(new Color(220, 220, 220)); // Gris claro
        btnDeslesionar.setEnabled(false); // Inicialmente deshabilitado
        btnDeslesionar.addActionListener(e -> deslesionarJugador());
        buttonPane.add(btnDeslesionar);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setBackground(new Color(34, 139, 34)); // Verde
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dispose());
        buttonPane.add(okButton);
    }

    private void cargarJugadores() {
        cbJugadores.removeAllItems();
        
        // Añadir item de selección inicial
        cbJugadores.addItem(null); // Item nulo para "Seleccione"
        
        for (Jugador jugador : SerieNacional.getInstance().getMisJugadores()) {
            cbJugadores.addItem(jugador);
        }
        
        // Renderer personalizado para mostrar nombre y ID
        cbJugadores.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("<Seleccione>");
                } else if (value instanceof Jugador) {
                    Jugador j = (Jugador) value;
                    setText(j.getNombre() + " (" + j.getId() + ")");
                }
                setFont(new Font("Arial", Font.PLAIN, 14));
                return this;
            }
        });
    }

    private void cargarLesiones() {
        tableModel.setRowCount(0);
        btnDeslesionar.setEnabled(false);
        
        Jugador jugadorSeleccionado = (Jugador) cbJugadores.getSelectedItem();
        if (jugadorSeleccionado == null || jugadorSeleccionado.getMisLesiones() == null) {
            return;
        }
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Lesion lesion : jugadorSeleccionado.getMisLesiones()) {
            tableModel.addRow(new Object[]{
                lesion.getId(),
                lesion.getLesion(),
                lesion.getFecLes().format(dateFormatter),
                lesion.getFecRec().format(dateFormatter),
                lesion.isLesionado()
            });
        }
        
        // Habilitar botón si el jugador está lesionado
        btnDeslesionar.setEnabled(jugadorSeleccionado.isLesionado());
    }

    private void deslesionarJugador() {
        Jugador jugador = (Jugador) cbJugadores.getSelectedItem();
        if (jugador == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea marcar a " + jugador.getNombre() + " como no lesionado?",
            "Confirmar Deslesionar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            jugador.setLesionado(false);
            
            // Actualizar todas sus lesiones como no activas
            for (Lesion lesion : jugador.getMisLesiones()) {
                lesion.setLesionado(false);
            }
            
            JOptionPane.showMessageDialog(this, 
                jugador.getNombre() + " ha sido marcado como no lesionado",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Actualizar la tabla y deshabilitar el botón
            cargarLesiones();
        }
    }
}