package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import logico.SerieNacional;
import logico.Equipo;
import logico.User;

public class ListEquipo extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableEquipos;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbFiltro;
    private JButton btnModificar;
    private List<Equipo> listaEquipos;
    private List<Equipo> listaOriginal;
    private int lastSelectedRow = -1;
    private boolean esAnotador;

    public ListEquipo(Color colorOscuro, Color colorClaro) {
        // Verificar tipo de usuario al inicio
        User usuarioActual = SerieNacional.getUsuarioActual();
        esAnotador = usuarioActual != null && usuarioActual.esAnotador();
        
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setTitle(esAnotador ? "Listado de Equipos (Solo lectura)" : "Listado de Equipos");
        setBounds(100, 100, 900, 500);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(colorClaro);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nombre", "Color Principal", "Cant. Jugadores", "J. Ganados", "J. Perdidos"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableEquipos = new JTable(tableModel);
        tableEquipos.setRowHeight(25);
        tableEquipos.setFont(new Font("Arial", Font.PLAIN, 14));
        tableEquipos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableEquipos.getTableHeader().setBackground(colorOscuro);
        tableEquipos.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tableEquipos);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        listaEquipos = new ArrayList<>(SerieNacional.getInstance().getMisEquipos());
        listaOriginal = new ArrayList<>(listaEquipos);
        cargarEquipos();

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorClaro);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        cbFiltro = new JComboBox<>(new String[]{"Filtrar", "Nombre (A-Z)", "Cant. Jugadores (Mayor-Menor)", "J. Ganados (Mayor-Menor)", "J. Perdidos (Mayor-Menor)"});
        cbFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarTabla((String) cbFiltro.getSelectedItem());
            }
        });
        buttonPane.add(cbFiltro);

        // Configuración del botón Modificar
        btnModificar = new JButton("Modificar");
        btnModificar.setFont(new Font("Arial", Font.BOLD, 12));
        btnModificar.setBackground(Color.LIGHT_GRAY);
        btnModificar.setForeground(Color.BLACK);
        btnModificar.setFocusPainted(false);
        btnModificar.setEnabled(false);
        
        if (esAnotador) {
            btnModificar.setToolTipText("Requiere permisos de administrador");
        }

        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
 
                int selectedRow = tableEquipos.getSelectedRow();
                if (selectedRow != -1) {
                    String idEquipo = tableModel.getValueAt(selectedRow, 0).toString();
                    Equipo equipoSeleccionado = SerieNacional.getInstance().buscarEquipoPorId(idEquipo);
                    if (equipoSeleccionado != null) {
                        RegEquipo ventanaModificar = new RegEquipo(colorOscuro, colorClaro, equipoSeleccionado);
                        ventanaModificar.setVisible(true);
                        cargarEquipos();
                    }
                }
                
            }
        });
        buttonPane.add(btnModificar);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setBackground(new Color(34, 139, 34));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dispose());
        buttonPane.add(okButton);

        tableEquipos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableEquipos.rowAtPoint(e.getPoint());

                if (selectedRow == lastSelectedRow) {
                    tableEquipos.clearSelection();
                    btnModificar.setEnabled(false);
                    btnModificar.setBackground(Color.LIGHT_GRAY);
                    lastSelectedRow = -1;
                } else {
                    lastSelectedRow = selectedRow;
                    btnModificar.setEnabled(!esAnotador); // Solo habilitar si no es anotador
                    btnModificar.setBackground(!esAnotador ? new Color(30, 144, 255) : Color.LIGHT_GRAY);
                }
            }
        });
    }

    private void cargarEquipos() {
        tableModel.setRowCount(0);
        tableEquipos.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
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

        for (Equipo equipo : listaEquipos) {
            tableModel.addRow(new Object[]{
                equipo.getId(),
                equipo.getNombre(),
                equipo.getColor(),
                equipo.getJugadores().size(),
                equipo.getWin(),
                equipo.getLose()
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
        } else if (criterio.equals("J. Ganados (Mayor-Menor)")) {
            Collections.sort(listaEquipos, new Comparator<Equipo>() {
                @Override
                public int compare(Equipo e1, Equipo e2) {
                    return Integer.compare(e2.getWin(), e1.getWin());
                }
            });
        } else if (criterio.equals("J. Perdidos (Mayor-Menor)")) {
            Collections.sort(listaEquipos, new Comparator<Equipo>() {
                @Override
                public int compare(Equipo e1, Equipo e2) {
                    return Integer.compare(e2.getLose(), e1.getLose());
                }
            });
        }
        cargarEquipos();
    }
}