package visual;

import java.awt.*;
import java.beans.*;
import java.time.*;
import java.util.Date;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import logico.*;
import com.toedter.calendar.*;

public class VerCalendario extends JPanel {
    private JCalendar calendar;
    private JTable tablaJuegos;
    private DefaultTableModel modeloTabla;
    private Color colorFondo;
    private Color colorBoton;
    private ArrayList<Juego> todosJuegos;

    public VerCalendario(Color colorFondo, Color colorBoton) {
        this.colorFondo = colorFondo;
        this.colorBoton = colorBoton;
        this.todosJuegos = new ArrayList<Juego>();
        initComponents();
        cargarJuegos();
        mostrarTodosJuegos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(colorFondo);

        // Configuración del calendario
        calendar = new JCalendar();
        calendar.setDecorationBackgroundColor(colorBoton);
        calendar.setDecorationBackgroundVisible(true);
        calendar.setWeekOfYearVisible(false);

        // Permitir cambiar mes y año
        calendar.getMonthChooser().setEnabled(true);
        calendar.getYearChooser().setEnabled(true);

        // Evitar que se pueda cambiar la fecha seleccionada
        calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                calendar.setDate((Date) evt.getOldValue());
            }
        });

        // Configuración de la tabla
        modeloTabla = new DefaultTableModel(new Object[]{"Fecha", "Local", "VS", "Visitante"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaJuegos = new JTable(modeloTabla);
        tablaJuegos.setRowHeight(30);
        tablaJuegos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaJuegos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaJuegos.getTableHeader().setBackground(colorBoton);
        tablaJuegos.getTableHeader().setForeground(Color.WHITE);

        // Centrar contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablaJuegos.getColumnCount(); i++) {
            tablaJuegos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Organización de componentes
        JScrollPane scrollTabla = new JScrollPane(tablaJuegos);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenedor.setBackground(colorFondo);
        contenedor.add(calendar, BorderLayout.NORTH);
        contenedor.add(scrollTabla, BorderLayout.CENTER);

        add(contenedor, BorderLayout.CENTER);
    }

    private void cargarJuegos() {
        todosJuegos.clear();
        todosJuegos.addAll(SerieNacional.getInstance().getMisJuegos());
        calendar.updateUI();
    }

    private void mostrarTodosJuegos() {
        modeloTabla.setRowCount(0);
        for (Juego juego : todosJuegos) {
            modeloTabla.addRow(new Object[]{
                juego.getFechaJuego().toString(),
                juego.getLocal().getNombre(),
                "VS",
                juego.getVisitante().getNombre()
            });
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static void mostrarDialog(Component parent, Color colorFondo, Color colorBoton) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Calendario de Juegos");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(new VerCalendario(colorFondo, colorBoton));
        dialog.pack();
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}