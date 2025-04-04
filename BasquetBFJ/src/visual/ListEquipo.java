package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import logico.SerieNacional;
import logico.Equipo;

public class ListEquipo extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableEquipos;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbFiltro;
    private List<Equipo> listaEquipos;
    private List<Equipo> listaOriginal;

    public ListEquipo(Color colorOscuro, Color colorClaro) {
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setTitle("Listado de Equipos");
        setBounds(100, 100, 1000, 500);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(colorClaro);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nombre", "Pais", "Color", "Cant. Jugadores", "Winrate %"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) { // Columna de Color
                    return Color.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        
        tableEquipos = new JTable(tableModel);
        tableEquipos.setRowHeight(25);
        tableEquipos.setFont(new Font("Arial", Font.PLAIN, 14));
        tableEquipos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableEquipos.getTableHeader().setBackground(colorOscuro);
        tableEquipos.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                if (column == 5) {
                    if (value instanceof Float) {
                        float winrate = (Float) value;
                        if (winrate == 0.0f) {
                            value = "N/A";
                        } else {
                            value = String.format("%.2f%%", winrate);
                        }
                    }
                }

                else if (column == 4) {
                    if (value instanceof Integer && (Integer) value == 0) {
                        value = "N/A";
                    }
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };

        tableEquipos.getColumnModel().getColumn(4).setCellRenderer(customRenderer);
        tableEquipos.getColumnModel().getColumn(5).setCellRenderer(customRenderer);

        tableEquipos.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Color) {
                    JLabel lblColor = new JLabel();
                    lblColor.setOpaque(true);
                    lblColor.setBackground((Color) value);
                    lblColor.setPreferredSize(new Dimension(30, 20));
                    lblColor.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    return lblColor;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableEquipos);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        listaEquipos = new ArrayList<>(SerieNacional.getInstance().getMisEquipos());
        listaOriginal = new ArrayList<>(listaEquipos);
        cargarEquipos();

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorClaro);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        cbFiltro = new JComboBox<>(new String[]{"Filtrar", "Nombre (A-Z)", "Cant. Jugadores (Mayor-Menor)", "Winrate (Mayor-Menor)"});
        cbFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarTabla((String) cbFiltro.getSelectedItem());
            }
        });
        buttonPane.add(cbFiltro);

        JButton okButton = crearBoton("OK", new Color(34, 139, 34));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(okButton);
    }

    private void cargarEquipos() {
        tableModel.setRowCount(0);

        for (Equipo equipo : listaEquipos) {
            // Calcular valores
            String cantJugadores = equipo.getJugadores().size() == 0 ? "N/A" : String.valueOf(equipo.getJugadores().size());
            float winrate = SerieNacional.getInstance().Winrate(equipo);
            String winrateStr = winrate == 0.0f ? "N/A" : String.valueOf(winrate);
            
            tableModel.addRow(new Object[]{
                equipo.getId(),
                equipo.getNombre(),
                equipo.getCiudad(),
                equipo.getColor(),

                String.valueOf(equipo.getJugadores().size()), // El renderer se encargará de mostrarlo como "N/A" si es 0
                winrate // El renderer se encargará de mostrarlo como "N/A" si es 0.0
            });
        }
    }

    private void ordenarTabla(String criterio) {
        if (criterio.equals("Filtrar")) {
            listaEquipos = new ArrayList<>(listaOriginal);
        } else if (criterio.equals("Nombre (A-Z)")) {
            Collections.sort(listaEquipos, new Comparator<Equipo>() {
                @Override
                public int compare(Equipo e1, Equipo e2) {
                    return e1.getNombre().compareTo(e2.getNombre());
                }
            });
        } else if (criterio.equals("Cant. Jugadores (Mayor-Menor)")) {
            Collections.sort(listaEquipos, new Comparator<Equipo>() {
                @Override
                public int compare(Equipo e1, Equipo e2) {
                    return Integer.compare(e2.getJugadores().size(), e1.getJugadores().size());
                }
            });
        } else if (criterio.equals("Winrate (Mayor-Menor)")) {
            Collections.sort(listaEquipos, new Comparator<Equipo>() {
                @Override
                public int compare(Equipo e1, Equipo e2) {
                    return Float.compare(
                        SerieNacional.getInstance().Winrate(e2),
                        SerieNacional.getInstance().Winrate(e1)
                    );
                }
            });
        }
        cargarEquipos();
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }
}