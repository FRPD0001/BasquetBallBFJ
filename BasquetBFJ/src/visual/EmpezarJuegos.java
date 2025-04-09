package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class EmpezarJuegos {
    private JDialog dialog;
    private JTable tablaJuegos;
    private DefaultTableModel modelJuegos;
    private JComboBox<String> comboJornadas;
    private List<Juego> juegosDisponibles;
    private Color colorFondo;
    private Color colorBoton;

    public static void mostrarDialog(java.awt.Frame parent, Color colorFondo, Color colorBoton) {
        if (SerieNacional.getInstance().getMisJuegos().isEmpty()) {
            JOptionPane.showMessageDialog(parent, 
                "No hay juegos programados. Genere primero un calendario.", 
                "Juegos Vacíos", JOptionPane.INFORMATION_MESSAGE);
        } else {
            EmpezarJuegos dialog = new EmpezarJuegos(colorFondo, colorBoton);
            dialog.mostrar();
        }
    }

    private EmpezarJuegos(Color colorFondo, Color colorBoton) {
        this.colorFondo = colorFondo;
        this.colorBoton = colorBoton;
        juegosDisponibles = new ArrayList<>(SerieNacional.getInstance().getMisJuegos());
        initComponents();
    }

    private void initComponents() {
        dialog = new JDialog();
        dialog.setTitle("Seleccionar Juego");
        dialog.setSize(800, 500);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(colorFondo);
        
        // Establecer icono de la ventana
        try {
            ImageIcon icon = new ImageIcon("media/LogoProyecto.png");
            Image image = icon.getImage();
            dialog.setIconImage(image);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
        
        // Panel superior con filtros
        JPanel panelFiltros = new JPanel();
        panelFiltros.setBackground(colorFondo);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblJornada = new JLabel("Filtrar por Jornada:");
        lblJornada.setFont(new Font("Arial", Font.BOLD, 12));
        lblJornada.setForeground(colorBoton);
        
        comboJornadas = new JComboBox<>();
        comboJornadas.setBackground(Color.WHITE);
        comboJornadas.setFont(new Font("Arial", Font.PLAIN, 12));
        comboJornadas.addItem("Todas las jornadas");
        
        // Obtener jornadas únicas
        List<String> jornadas = new ArrayList<>();
        for (Juego juego : juegosDisponibles) {
            String jornada = juego.getId().substring(0, juego.getId().indexOf('-'));
            if (!jornadas.contains(jornada)) {
                jornadas.add(jornada);
                comboJornadas.addItem(jornada);
            }
        }
        
        comboJornadas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarJuegos();
            }
        });
        
        panelFiltros.add(lblJornada);
        panelFiltros.add(comboJornadas);
        dialog.add(panelFiltros, BorderLayout.NORTH);
        
        // Modelo y tabla de juegos
        modelJuegos = new DefaultTableModel(
            new Object[]{"Jornada", "Local", "VS", "Visitante", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaJuegos = new JTable(modelJuegos);
        tablaJuegos.setBackground(Color.WHITE);
        tablaJuegos.setForeground(Color.BLACK);
        tablaJuegos.setGridColor(Color.LIGHT_GRAY);
        tablaJuegos.getTableHeader().setReorderingAllowed(false);
        tablaJuegos.getTableHeader().setBackground(colorBoton);
        tablaJuegos.getTableHeader().setForeground(Color.WHITE);
        tablaJuegos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaJuegos.setRowHeight(40);
        tablaJuegos.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Renderers personalizados
        tablaJuegos.getColumnModel().getColumn(1).setCellRenderer(new EquipoColorRenderer());
        tablaJuegos.getColumnModel().getColumn(2).setCellRenderer(new VSRenderer());
        tablaJuegos.getColumnModel().getColumn(3).setCellRenderer(new EquipoColorRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tablaJuegos);
        scrollPane.getViewport().setBackground(Color.WHITE);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(colorFondo);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnSeleccionar = new JButton("Seleccionar Juego");
        btnSeleccionar.setBackground(colorBoton);
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setFont(new Font("Arial", Font.BOLD, 12));
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarJuego();
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(colorBoton);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        
        // Cargar todos los juegos inicialmente
        cargarJuegos(juegosDisponibles);
    }
    
    private void cargarJuegos(List<Juego> juegos) {
        modelJuegos.setRowCount(0);
        
        for (Juego juego : juegos) {
            modelJuegos.addRow(new Object[]{
                juego.getId().substring(0, juego.getId().indexOf('-')),
                juego.getLocal(),
                "VS",
                juego.getVisitante(),
                juego.getFechaJuego().toString()
            });
        }
    }
    
    private void filtrarJuegos() {
        String jornadaSeleccionada = (String) comboJornadas.getSelectedItem();
        
        if (jornadaSeleccionada.equals("Todas las jornadas")) {
            cargarJuegos(juegosDisponibles);
        } else {
            List<Juego> juegosFiltrados = new ArrayList<>();
            for (Juego juego : juegosDisponibles) {
                if (juego.getId().startsWith(jornadaSeleccionada)) {
                    juegosFiltrados.add(juego);
                }
            }
            cargarJuegos(juegosFiltrados);
        }
    }
    
    private void seleccionarJuego() {
        int filaSeleccionada = tablaJuegos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(dialog, 
                "Por favor seleccione un juego de la tabla", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigoJornada = (String) modelJuegos.getValueAt(filaSeleccionada, 0);
        Equipo local = (Equipo) modelJuegos.getValueAt(filaSeleccionada, 1);
        Equipo visitante = (Equipo) modelJuegos.getValueAt(filaSeleccionada, 3);
        
        // Buscar el juego correspondiente
        Juego juegoSeleccionado = null;
        for (Juego juego : juegosDisponibles) {
            if (juego.getLocal().equals(local) && juego.getVisitante().equals(visitante) &&
                juego.getId().startsWith(codigoJornada)) {
                juegoSeleccionado = juego;
                break;
            }
        }
        
        if (juegoSeleccionado != null) {
        	dialog.dispose();
        	DraftearEquipos ventanaDraft = new DraftearEquipos(juegoSeleccionado, colorFondo, colorBoton);
        	ventanaDraft.setVisible(true);
        }
    }
    
    private void mostrar() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    // Renderers para mostrar los equipos con sus colores
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
                panel.setBackground(Color.WHITE);
                panel.setLayout(null);
                
                JLabel nombreLabel = new JLabel(equipo.getNombre());
                nombreLabel.setBounds(30, 0, 200, 40);
                nombreLabel.setFont(new Font("Arial", Font.BOLD, 12));
                nombreLabel.setForeground(Color.BLACK);
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
            setForeground(Color.BLACK);
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
}