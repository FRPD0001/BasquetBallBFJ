package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import logico.SerieNacional;
import logico.Equipo;

public class ListEquipo extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTable tableEquipos;
    private DefaultTableModel tableModel;

    public ListEquipo(Color colorOscuro, Color colorClaro) {
        setTitle("Listado de Equipos");
        setBounds(100, 100, 900, 500);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        // Aplicando colores recibidos de Principal
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(colorClaro);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        // Modelo de la tabla
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

        // Alineación de celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableEquipos.getColumnCount(); i++) {
            tableEquipos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableEquipos);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        cargarEquipos();

        // Panel de botones
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorClaro);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = crearBoton("OK", new Color(34, 139, 34));
        okButton.addActionListener(e -> dispose());
        buttonPane.add(okButton);

        JButton cancelButton = crearBoton("Cancelar", new Color(178, 34, 34));
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }

    private void cargarEquipos() {
        tableModel.setRowCount(0);
        for (Equipo equipo : SerieNacional.getInstance().getMisEquipos()) {
            String id = equipo.getId();
            String nombre = equipo.getNombre();
            String colorPrincipal = String.format("RGB(%d, %d, %d)",
                    equipo.getColor().getRed(),
                    equipo.getColor().getGreen(),
                    equipo.getColor().getBlue());
            String cantJugadores = String.valueOf(equipo.getJugadores().size());
            String juegosGanados = String.valueOf(equipo.getWin());
            String juegosPerdidos = String.valueOf(equipo.getLose());

            tableModel.addRow(new Object[]{id, nombre, colorPrincipal, cantJugadores, juegosGanados, juegosPerdidos});
        }
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    public static void main(String[] args) {
        // Simulación de llamada con colores de Principal (Ejemplo de integración)
        Color colorOscuro = new Color(100, 149, 237); // Color por defecto
        Color colorClaro = new Color(200, 220, 255);  // Color claro por defecto

        try {
            ListEquipo dialog = new ListEquipo(colorOscuro, colorClaro);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
