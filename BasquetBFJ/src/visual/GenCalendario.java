package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import logico.Equipo;
import logico.Juego;
import logico.SerieNacional;
import com.toedter.calendar.JDateChooser;

public class GenCalendario extends JPanel {
    private JButton btnGenerarAleatorio;
    private JButton btnConfirmar;
    private JTable tablaEmparejamientos;
    private DefaultTableModel modelEmparejamientos;
    private ArrayList<ArrayList<Equipo>> emparejamientosActuales;
    private Color colorFondo;
    private Color colorBoton;
    private ArrayList<Equipo> equiposOriginales;

    public GenCalendario(Color colorFondo, Color colorBoton) {
        this.colorFondo = colorBoton;
        this.colorBoton = colorFondo;
        this.equiposOriginales = new ArrayList<>(SerieNacional.getInstance().getMisEquipos());
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        setBackground(colorFondo);
        setPreferredSize(new Dimension(900, 700));

        btnGenerarAleatorio = new JButton("Aleatorizar Orden");
        btnGenerarAleatorio.setBounds(220, 20, 220, 35);
        btnGenerarAleatorio.setBackground(colorBoton);
        btnGenerarAleatorio.setForeground(Color.BLACK);
        btnGenerarAleatorio.setFont(new Font("Arial", Font.BOLD, 12));
        btnGenerarAleatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Collections.shuffle(equiposOriginales);
                generarRoundRobin();
                actualizarTablaEmparejamientos();
            }
        });
        add(btnGenerarAleatorio);

        btnConfirmar = new JButton("Confirmar Calendario");
        btnConfirmar.setBounds(460, 20, 220, 35);
        btnConfirmar.setBackground(new Color(34, 139, 34));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarCalendario();
            }
        });
        add(btnConfirmar);

        modelEmparejamientos = new DefaultTableModel(
            new Object[]{"Jornada", "Local", "VS", "Visitante"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEmparejamientos = new JTable(modelEmparejamientos);
        tablaEmparejamientos.getTableHeader().setReorderingAllowed(false);
        tablaEmparejamientos.setRowHeight(40);
        tablaEmparejamientos.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JTableHeader header = tablaEmparejamientos.getTableHeader();
        header.setBackground(colorBoton);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        tablaEmparejamientos.getColumnModel().getColumn(1).setCellRenderer(new EquipoColorRenderer());
        tablaEmparejamientos.getColumnModel().getColumn(2).setCellRenderer(new VSRenderer());
        tablaEmparejamientos.getColumnModel().getColumn(3).setCellRenderer(new EquipoColorRenderer());
        
        tablaEmparejamientos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaEmparejamientos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaEmparejamientos.getColumnModel().getColumn(2).setPreferredWidth(50);
        tablaEmparejamientos.getColumnModel().getColumn(3).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(tablaEmparejamientos);
        scrollPane.setBounds(50, 70, 800, 550);
        add(scrollPane);

        if (SerieNacional.getInstance().esNumeroEquiposPar()) {
            generarRoundRobin();
            actualizarTablaEmparejamientos();
        } else {
            JOptionPane.showMessageDialog(this, 
                "El número de equipos debe ser par para generar un calendario.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            btnGenerarAleatorio.setEnabled(false);
            btnConfirmar.setEnabled(false);
        }
    }

    private void generarRoundRobin() {
        ArrayList<Equipo> equipos = new ArrayList<>(equiposOriginales);
        emparejamientosActuales = new ArrayList<>();
        int numEquipos = equipos.size();
        int numJornadas = numEquipos - 1;
        int partidosPorJornada = numEquipos / 2;
        
        for (int jornada = 0; jornada < numJornadas; jornada++) {
            ArrayList<Equipo> jornadaActual = new ArrayList<>();
            
            for (int i = 0; i < partidosPorJornada; i++) {
                Equipo local = equipos.get(i);
                Equipo visitante = equipos.get(numEquipos - 1 - i);
                jornadaActual.add(local);
                jornadaActual.add(visitante);
            }
            
            emparejamientosActuales.add(jornadaActual);
            
            Equipo ultimo = equipos.remove(numEquipos - 1);
            equipos.add(1, ultimo);
        }
    }

    private void actualizarTablaEmparejamientos() {
        modelEmparejamientos.setRowCount(0);
        
        for (int i = 0; i < emparejamientosActuales.size(); i++) {
            ArrayList<Equipo> jornada = emparejamientosActuales.get(i);
            boolean primeraFila = true;
            
            for (int j = 0; j < jornada.size(); j += 2) {
                Equipo local = jornada.get(j);
                Equipo visitante = jornada.get(j + 1);
                
                if (primeraFila) {
                    modelEmparejamientos.addRow(new Object[]{
                        "Jornada " + (i + 1), 
                        local, 
                        "VS", 
                        visitante
                    });
                    primeraFila = false;
                } else {
                    modelEmparejamientos.addRow(new Object[]{
                        "", 
                        local, 
                        "VS", 
                        visitante
                    });
                }
            }
            
            modelEmparejamientos.addRow(new Object[]{null, null, null, null});
        }
    }

    private class EquipoColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value == null) {
                return new JPanel();
            }
            
            if (value instanceof Equipo) {
                Equipo equipo = (Equipo) value;
                JPanel panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(equipo.getColor());
                        g.fillRect(5, 10, 20, 20);
                        g.setColor(Color.BLACK);
                        g.drawRect(5, 10, 20, 20);
                    }
                };
                panel.setBackground(table.getBackground());
                panel.setLayout(null);
                
                JLabel nombreLabel = new JLabel(equipo.getNombre());
                nombreLabel.setBounds(30, 0, 200, 40);
                nombreLabel.setFont(new Font("Arial", Font.BOLD, 12));
                panel.add(nombreLabel);
                
                return panel;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class VSRenderer extends DefaultTableCellRenderer {
        public VSRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Arial", Font.BOLD, 12));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value == null) {
                return new JPanel();
            }
            super.getTableCellRendererComponent(table, "VS", isSelected, hasFocus, row, column);
            return this;
        }
    }

    private void confirmarCalendario() {
        if (emparejamientosActuales == null || emparejamientosActuales.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Primero genere los emparejamientos.", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Juego> juegos = new ArrayList<>();
        LocalDate fechaBase = LocalDate.now();
        
        // Crear componentes para el diálogo de fecha
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy"); // Formato DD/MM/YYYY
        
        for (int i = 0; i < emparejamientosActuales.size(); i++) {
            ArrayList<Equipo> jornada = emparejamientosActuales.get(i);
            
            for (int j = 0; j < jornada.size(); j += 2) {
                Equipo local = jornada.get(j);
                Equipo visitante = jornada.get(j + 1);
                
                // Configurar fecha por defecto
                dateChooser.setDate(java.sql.Date.valueOf(fechaBase));
                
                int opcion = JOptionPane.showConfirmDialog(this, dateChooser, 
                    "Fecha para: " + local.getNombre() + " vs " + visitante.getNombre(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (opcion != JOptionPane.OK_OPTION) {
                    return; // El usuario canceló
                }
                
                // Obtener la fecha seleccionada
                java.util.Date fechaSeleccionada = dateChooser.getDate();
                LocalDate fecha = fechaSeleccionada.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
                
                Juego juego = new Juego(
                        "J" + (SerieNacional.getInstance().getMisJuegos().size() + 1),
                        local,
                        visitante,
                        fecha
                    );
                juegos.add(juego);
                fechaBase = fecha.plusDays(1); // Siguiente fecha por defecto
            }
        }
        
        // Guardar todos los juegos
        for (Juego juego : juegos) {
            SerieNacional.getInstance().agregarJuego(juego);
            juego.getLocal().getJuegos().add(juego);
            juego.getVisitante().getJuegos().add(juego);
        }
        
        JOptionPane.showMessageDialog(this, 
            "Calendario Round Robin generado exitosamente!\n" +
            "Total de partidos: " + juegos.size(), 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        if (this.getTopLevelAncestor() instanceof JDialog) {
            ((JDialog)this.getTopLevelAncestor()).dispose();
        }
    }
}