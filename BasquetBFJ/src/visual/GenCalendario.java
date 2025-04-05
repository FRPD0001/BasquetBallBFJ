package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import logico.Equipo;
import logico.Juego;
import logico.SerieNacional;

public class GenCalendario extends JPanel {
    private JButton btnGenerarAleatorio;
    private JButton btnConfirmar;
    private JTable tablaEmparejamientos;
    private DefaultTableModel modelEmparejamientos;
    private ArrayList<ArrayList<Equipo>> emparejamientosActuales;
    private Color colorFondo;
    private Color colorBoton;

    public GenCalendario(Color colorFondo, Color colorBoton) {
        this.colorFondo = colorBoton;
        this.colorBoton = colorFondo;
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        setBackground(colorFondo);
        setPreferredSize(new Dimension(900, 700));

        // Botón "Aleatorizar Orden"
        btnGenerarAleatorio = new JButton("Aleatorizar Orden");
        btnGenerarAleatorio.setBounds(120, 20, 220, 35);
        btnGenerarAleatorio.setBackground(colorBoton);
        btnGenerarAleatorio.setForeground(Color.BLACK);
        btnGenerarAleatorio.setFont(new Font("Arial", Font.BOLD, 12));
        btnGenerarAleatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarEmparejamientosAleatorios();
            }
        });
        add(btnGenerarAleatorio);

        // Botón "Confirmar Calendario"
        btnConfirmar = new JButton("Confirmar Calendario");
        btnConfirmar.setBounds(360, 20, 220, 35);
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

        // Configuración de la tabla
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
        
        // Renderizadores personalizados
        tablaEmparejamientos.getColumnModel().getColumn(1).setCellRenderer(new EquipoColorRenderer());
        tablaEmparejamientos.getColumnModel().getColumn(2).setCellRenderer(new VSRenderer());
        tablaEmparejamientos.getColumnModel().getColumn(3).setCellRenderer(new EquipoColorRenderer());
        
        // Ajuste de ancho de columnas
        tablaEmparejamientos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaEmparejamientos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaEmparejamientos.getColumnModel().getColumn(2).setPreferredWidth(50);
        tablaEmparejamientos.getColumnModel().getColumn(3).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(tablaEmparejamientos);
        scrollPane.setBounds(50, 70, 800, 550);
        add(scrollPane);

        // Generar el round robin inicial
        if (SerieNacional.getInstance().esNumeroEquiposPar()) {
            generarEmparejamientosAleatorios();
        } else {
            JOptionPane.showMessageDialog(this, 
                "El número de equipos debe ser par para generar un calendario.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            btnGenerarAleatorio.setEnabled(false);
            btnConfirmar.setEnabled(false);
        }
    }

    // Renderizador para equipos con color
    private class EquipoColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
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

    // Renderizador para "VS"
    private class VSRenderer extends DefaultTableCellRenderer {
        public VSRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Arial", Font.BOLD, 12));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, "VS", isSelected, hasFocus, row, column);
            return this;
        }
    }

    private void generarEmparejamientosAleatorios() {
        emparejamientosActuales = SerieNacional.getInstance().generarRoundRobinAleatorio();
        actualizarTablaEmparejamientos();
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
            
            // Añadir fila vacía como separador
            modelEmparejamientos.addRow(new Object[]{"", "", "", ""});
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
        int diasEntreJornadas = 7;
        
        for (int i = 0; i < emparejamientosActuales.size(); i++) {
            ArrayList<Equipo> jornada = emparejamientosActuales.get(i);
            LocalDate fechaBase = LocalDate.now().plusDays(i * diasEntreJornadas);
            
            for (int j = 0; j < jornada.size(); j += 2) {
                Equipo local = jornada.get(j);
                Equipo visitante = jornada.get(j + 1);
                
                String fechaStr = JOptionPane.showInputDialog(this,
                    "Fecha para el partido:\n" + 
                    "Jornada " + (i + 1) + " - " + local.getNombre() + " vs " + visitante.getNombre() + 
                    "\n(Formato YYYY-MM-DD, sugerido: " + fechaBase + "):",
                    fechaBase.toString());
                
                try {
                    LocalDate fecha = LocalDate.parse(fechaStr);
                    Juego juego = new Juego(
                        "J" + (SerieNacional.getInstance().getMisJuegos().size() + 1),
                        local,
                        visitante,
                        new ArrayList<>(),
                        0,
                        0,
                        null,
                        fecha
                    );
                    juegos.add(juego);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Formato de fecha inválido. Use YYYY-MM-DD.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        // Agregar todos los juegos
        for (Juego juego : juegos) {
            SerieNacional.getInstance().agregarJuego(juego);
            juego.getLocal().getJuegos().add(juego);
            juego.getVisitante().getJuegos().add(juego);
        }
        
        JOptionPane.showMessageDialog(this, 
            "Calendario Round Robin generado exitosamente!\n" +
            "Total de partidos: " + juegos.size() + "\n" +
            "Jornadas programadas: " + emparejamientosActuales.size(), 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        if (this.getTopLevelAncestor() instanceof JDialog) {
            ((JDialog)this.getTopLevelAncestor()).dispose();
        }
    }
}