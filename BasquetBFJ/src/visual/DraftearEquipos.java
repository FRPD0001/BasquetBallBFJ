package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import logico.Equipo;
import logico.Juego;
import logico.Jugador;

public class DraftearEquipos extends JFrame {

    private Juego juego;
    private Color colorFondo;
    private Color colorBoton;
    private JLabel[] slotsLocal;
    private JLabel[] slotsVisitante;
    private DragSource dragSource;

    public DraftearEquipos(Juego juego, Color colorFondo, Color colorBoton) {
        // Validaciones iniciales
        if (juego == null || juego.getLocal() == null || juego.getVisitante() == null) {
            throw new IllegalArgumentException("El juego debe tener equipos asignados");
        }
        if (juego.getLocal().getJugadores() == null || juego.getVisitante().getJugadores() == null) {
            throw new IllegalArgumentException("Los equipos deben tener jugadores asignados");
        }

        this.juego = juego;
        this.colorFondo = colorFondo;
        this.colorBoton = colorBoton;
        this.slotsLocal = new JLabel[5];
        this.slotsVisitante = new JLabel[5];
        this.dragSource = new DragSource();
        initUI();
    }

    private void initUI() {
        setTitle("Draftear Equipos - " + juego.getLocal().getNombre() + " vs " + juego.getVisitante().getNombre());
        setSize(1200, 750);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(colorFondo);

        try {
            setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        } catch (Exception e) {
            System.err.println("Error cargando icono: " + e.getMessage());
        }

        // Panel principal con cancha y jugadores
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel de cancha
        JPanel canchaPanel = new JPanel(null) {
            private Image fondo = new ImageIcon("media/Cancha.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        canchaPanel.setPreferredSize(new Dimension(800, 650));

        // Crear slots de posición
        crearSlotsPosiciones(canchaPanel);

        // Paneles de jugadores
        JPanel panelLocal = crearPanelJugadores(juego.getLocal().getJugadores(), true);
        JPanel panelVisitante = crearPanelJugadores(juego.getVisitante().getJugadores(), false);

        mainPanel.add(panelLocal, BorderLayout.WEST);
        mainPanel.add(canchaPanel, BorderLayout.CENTER);
        mainPanel.add(panelVisitante, BorderLayout.EAST);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(colorFondo);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // Botón Empezar Juego (verde)
        JButton btnEmpezar = new JButton("Empezar Juego");
        btnEmpezar.setBackground(new Color(0, 150, 0));
        btnEmpezar.setForeground(Color.WHITE);
        btnEmpezar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEmpezar.addActionListener(e -> verificarYEmpezarJuego());
        
        // Botón Cancelar (rojo)
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(150, 0, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> cancelarJuego());

        panelBotones.add(btnEmpezar);
        panelBotones.add(btnCancelar);

        add(mainPanel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void cancelarJuego() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cancelar el juego?", 
            "Confirmar cancelación", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void crearSlotsPosiciones(JPanel canchaPanel) {
        // Posiciones equipo local (izquierda)
        int[][] posicionesLocal = {
            {200, 300}, // Base
            {150, 200}, // Escolta
            {150, 400}, // Alero
            {100, 250}, // Ala-pívot
            {100, 350}  // Pívot
        };

        // Posiciones equipo visitante (derecha)
        int[][] posicionesVisitante = {
            {500, 300}, // Base
            {550, 200}, // Escolta
            {550, 400}, // Alero
            {600, 250}, // Ala-pívot
            {600, 350}  // Pívot
        };

        // Crear slots para local
        for (int i = 0; i < posicionesLocal.length; i++) {
            slotsLocal[i] = crearSlotPosicion();
            slotsLocal[i].setLocation(posicionesLocal[i][0], posicionesLocal[i][1]);
            canchaPanel.add(slotsLocal[i]);
        }

        // Crear slots para visitante
        for (int i = 0; i < posicionesVisitante.length; i++) {
            slotsVisitante[i] = crearSlotPosicion();
            slotsVisitante[i].setLocation(posicionesVisitante[i][0], posicionesVisitante[i][1]);
            canchaPanel.add(slotsVisitante[i]);
        }
    }

    private JLabel crearSlotPosicion() {
        JLabel slot = new JLabel("", SwingConstants.CENTER);
        slot.setSize(120, 40);
        slot.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        slot.setOpaque(true);
        slot.setBackground(new Color(255, 255, 255, 200));
        slot.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Configurar para aceptar drops
        slot.setTransferHandler(new TransferHandler("text") {
            @Override
            public boolean canImport(TransferSupport support) {
                // Solo permitir importar de jugadores a slots
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
            
            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    JLabel target = (JLabel)support.getComponent();
                    
                    // Verificar si el slot ya tiene un jugador
                    if (!target.getText().isEmpty()) {
                        return false;
                    }
                    
                    target.setText(data);
                    target.setBackground(new Color(200, 255, 200, 200));
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        
        // Permitir quitar jugador con doble clic
        slot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    slot.setText("");
                    slot.setBackground(new Color(255, 255, 255, 200));
                }
            }
        });
        
        return slot;
    }

    private JPanel crearPanelJugadores(List<Jugador> jugadores, boolean esLocal) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(colorFondo);
        panel.setPreferredSize(new Dimension(220, getHeight()));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(20, 10, 20, 10), 
            esLocal ? "Jugadores Locales" : "Jugadores Visitantes"));

        // Título del panel
        JLabel titulo = new JLabel(esLocal ? juego.getLocal().getNombre() : juego.getVisitante().getNombre());
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(colorBoton);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(15));

        // Verificar si hay jugadores
        if (jugadores == null || jugadores.isEmpty()) {
            JLabel vacio = new JLabel("No hay jugadores");
            vacio.setForeground(Color.RED);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(vacio);
            return panel;
        }

        // Añadir jugadores
        for (Jugador jugador : jugadores) {
            if (jugador != null) {
                JLabel labelJugador = crearLabelJugador(jugador, esLocal);
                panel.add(labelJugador);
                panel.add(Box.createVerticalStrut(8));
            }
        }

        return panel;
    }

    private JLabel crearLabelJugador(Jugador jugador, boolean esLocal) {
        String nombre = formatearNombreCompleto(jugador);
        JLabel label = new JLabel(nombre, SwingConstants.CENTER);
        
        label.setOpaque(true);
        label.setBackground(esLocal ? juego.getLocal().getColor() : juego.getVisitante().getColor());
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        label.setPreferredSize(new Dimension(200, 40));
        label.setMaximumSize(new Dimension(200, 40));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Configurar drag and drop
        label.setTransferHandler(new TransferHandler("text"));
        
        // Configurar el reconocimiento del gesto de arrastre
        dragSource.createDefaultDragGestureRecognizer(label, 
            DnDConstants.ACTION_COPY, new DragGestureListener() {
                @Override
                public void dragGestureRecognized(DragGestureEvent dge) {
                    // Crear el objeto transferible
                    Transferable transferable = new Transferable() {
                        @Override
                        public DataFlavor[] getTransferDataFlavors() {
                            return new DataFlavor[]{DataFlavor.stringFlavor};
                        }

                        @Override
                        public boolean isDataFlavorSupported(DataFlavor flavor) {
                            return flavor.equals(DataFlavor.stringFlavor);
                        }

                        @Override
                        public Object getTransferData(DataFlavor flavor) 
                            throws UnsupportedFlavorException, IOException {
                            if (isDataFlavorSupported(flavor)) {
                                return label.getText();
                            }
                            throw new UnsupportedFlavorException(flavor);
                        }
                    };
                    
                    // Iniciar el arrastre
                    dge.startDrag(null, transferable);
                }
            });
        
        return label;
    }

    private String formatearNombreCompleto(Jugador jugador) {
        if (jugador == null || jugador.getNombre() == null) {
            return "Jugador";
        }
        
        String[] partes = jugador.getNombre().split(" ");
        if (partes.length == 0) return jugador.getNombre();
        
        String nombre = partes[0];
        String inicial = (partes.length > 1) ? partes[1].substring(0, 1) + "." : "";
        return nombre + " " + inicial;
    }

    private String formatearNombre(Jugador jugador) {
        if (jugador == null || jugador.getNombre() == null) {
            return "Jugador";
        }
        
        String[] partes = jugador.getNombre().split(" ");
        if (partes.length == 0) return jugador.getNombre();
        
        String nombre = partes[0];
        String inicial = (partes.length > 1) ? partes[1].substring(0, 1) + "." : "";
        return nombre + " " + inicial;
    }
    
    private void verificarYEmpezarJuego() {
        try {
            // Limpiar listas previas
            juego.getActivosLocal().clear();
            juego.getActivosVisitante().clear();

            // Verificar slots locales y asignar jugadores
            for (int i = 0; i < slotsLocal.length; i++) {
                if (slotsLocal[i].getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Falta asignar jugador en posición " + (i+1) + " del equipo local", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean jugadorEncontrado = false;
                String nombreJugador = slotsLocal[i].getText();
                for (Jugador j : juego.getLocal().getJugadores()) {
                    if (formatearNombreCompleto(j).equals(nombreJugador)) {
                        juego.agregarJugadorLocal(j);
                        jugadorEncontrado = true;
                        break;
                    }
                }
                
                if (!jugadorEncontrado) {
                    JOptionPane.showMessageDialog(this, 
                        "No se encontró al jugador: " + nombreJugador + " en el equipo local",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Verificar slots visitante y asignar jugadores
            for (int i = 0; i < slotsVisitante.length; i++) {
                if (slotsVisitante[i].getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Falta asignar jugador en posición " + (i+1) + " del equipo visitante", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean jugadorEncontrado = false;
                String nombreJugador = slotsVisitante[i].getText();
                for (Jugador j : juego.getVisitante().getJugadores()) {
                    if (formatearNombreCompleto(j).equals(nombreJugador)) {
                        juego.agregarJugadorVisitante(j);
                        jugadorEncontrado = true;
                        break;
                    }
                }
                
                if (!jugadorEncontrado) {
                    JOptionPane.showMessageDialog(this, 
                        "No se encontró al jugador: " + nombreJugador + " en el equipo visitante",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Si todo está correcto
            juego.setDone(true);
            
            // Debug: Verificar jugadores asignados
            System.out.println("Jugadores locales activos: " + juego.getActivosLocal().size());
            System.out.println("Jugadores visitantes activos: " + juego.getActivosVisitante().size());
            
            // Abrir la ventana de simulación
            EventQueue.invokeLater(() -> {
                SimularJuego simulador = new SimularJuego(juego, colorFondo, colorBoton);
                simulador.setVisible(true);
            });
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al iniciar el juego: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}