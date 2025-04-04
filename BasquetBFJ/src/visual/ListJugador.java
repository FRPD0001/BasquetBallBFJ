package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
import logico.Jugador;

public class ListJugador extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTable tableJugadores;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbFiltro;
    private List<Jugador> listaJugadores;
    private List<Jugador> listaOriginal;

    public ListJugador(Color colorOscuro, Color colorClaro) {
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setTitle("Listado de Jugadores");
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        contentPanel.setBackground(colorClaro); 

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        tableModel = new DefaultTableModel();
        tableJugadores = new JTable(tableModel);

        String[] columnNames = {"ID", "Nombre", "Equipo"};
        tableModel.setColumnIdentifiers(columnNames);

        listaJugadores = new ArrayList<>(SerieNacional.getInstance().getMisJugadores());
        listaOriginal = new ArrayList<>(listaJugadores);
        cargarJugadores();

        JScrollPane scrollPane = new JScrollPane(tableJugadores);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        tableJugadores.getTableHeader().setBackground(colorOscuro);
        tableJugadores.getTableHeader().setForeground(Color.WHITE);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorClaro);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        cbFiltro = new JComboBox<>(new String[]{"Filtrar", "Nombre (A-Z)", "Equipo (A-Z)"});
        cbFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarTabla((String) cbFiltro.getSelectedItem());
            }
        });
        buttonPane.add(cbFiltro);

        JButton closeButton = new JButton("OK");
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.setBackground(new Color(34, 139, 34));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose());
        buttonPane.add(closeButton);
    }

    private void cargarJugadores() {
        tableModel.setRowCount(0);
        for (Jugador jugador : listaJugadores) {
            String id = String.valueOf(jugador.getId());
            String nombre = jugador.getNombre();
            String equipo = jugador.getEquipo() != null ? jugador.getEquipo().getNombre() : "Sin equipo";
            tableModel.addRow(new Object[]{id, nombre, equipo});
        }
    }

    private void ordenarTabla(String criterio) {
        if (criterio.equals("Filtrar")) {
            listaJugadores = new ArrayList<>(listaOriginal);
        } else if (criterio.equals("Nombre (A-Z)")) {
            Collections.sort(listaJugadores, new Comparator<Jugador>() {
                @Override
                public int compare(Jugador j1, Jugador j2) {
                    return j1.getNombre().compareTo(j2.getNombre());
                }
            });
        } else if (criterio.equals("Equipo (A-Z)")) {
            Collections.sort(listaJugadores, new Comparator<Jugador>() {
                @Override
                public int compare(Jugador j1, Jugador j2) {
                    String equipo1 = (j1.getEquipo() != null && j1.getEquipo().getNombre() != null) ? j1.getEquipo().getNombre() : "";
                    String equipo2 = (j2.getEquipo() != null && j2.getEquipo().getNombre() != null) ? j2.getEquipo().getNombre() : "";
                    return equipo1.compareTo(equipo2);
                }
            });
        }
        cargarJugadores();
    }

}
