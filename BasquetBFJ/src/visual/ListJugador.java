package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import logico.SerieNacional;
import logico.Jugador;

public class ListJugador extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTable tableJugadores;
    private DefaultTableModel tableModel;

    public ListJugador(Color colorOscuro, Color colorClaro) {
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setTitle("Listado de todos Jugadores");
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(colorClaro); // Color de fondo dinámico
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        tableModel = new DefaultTableModel();
        tableJugadores = new JTable(tableModel);

        String[] columnNames = {"ID", "Nombre", "Equipo"};
        tableModel.setColumnIdentifiers(columnNames);

        cargarJugadores();

        JScrollPane scrollPane = new JScrollPane(tableJugadores);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        tableJugadores.getTableHeader().setBackground(colorOscuro);
        tableJugadores.getTableHeader().setForeground(Color.WHITE);

        // Panel de botones con color dinámico
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorClaro);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton closeButton = new JButton("OK");
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.setBackground(new Color(34, 139, 34));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose());
        buttonPane.add(closeButton);
    }

    private void cargarJugadores() {
        tableModel.setRowCount(0);

        for (Jugador jugador : SerieNacional.getInstance().getMisJugadores()) {
            String id = String.valueOf(jugador.getId());
            String nombre = jugador.getNombre();
            String equipo = jugador.getEquipo() != null ? jugador.getEquipo().getNombre() : "Sin equipo";

            tableModel.addRow(new Object[]{id, nombre, equipo});
        }
    }
}
