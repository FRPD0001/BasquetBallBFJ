package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import logico.Equipo;
import logico.Juego;
import logico.Jugador;

public class DraftearEquipos extends JFrame {

    private Juego juego;
    private Color colorFondo;
    private Color colorBoton;

    public DraftearEquipos(Juego juego, Color colorFondo, Color colorBoton) {
        this.juego = juego;
        this.colorFondo = colorFondo;
        this.colorBoton = colorBoton;
        initUI();
    }

    private void initUI() {
        setTitle("Draftear Equipos");
        setSize(1200, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(colorFondo);


        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());

        JPanel canchaPanel = new JPanel(null) {
            private Image fondo = new ImageIcon("media/Cancha.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        canchaPanel.setPreferredSize(new Dimension(800, 700));

        // Crear slots de posición
        crearSlotsPosiciones(canchaPanel);

        // Paneles de jugadores - VERIFICAR JUGADORES ANTES
        System.out.println("Jugadores Local: " + juego.getLocal().getJugadores().size());
        System.out.println("Jugadores Visitante: " + juego.getVisitante().getJugadores().size());

        JPanel panelLocal = crearPanelJugadores(juego.getLocal().getJugadores(), true);
        JPanel panelVisitante = crearPanelJugadores(juego.getVisitante().getJugadores(), false);

        add(panelLocal, BorderLayout.WEST);
        add(canchaPanel, BorderLayout.CENTER);
        add(panelVisitante, BorderLayout.EAST);

        revalidate();
        repaint();
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
        for (int[] pos : posicionesLocal) {
            JLabel slot = crearSlotPosicion();
            slot.setLocation(pos[0], pos[1]);
            canchaPanel.add(slot);
        }

        // Crear slots para visitante
        for (int[] pos : posicionesVisitante) {
            JLabel slot = crearSlotPosicion();
            slot.setLocation(pos[0], pos[1]);
            canchaPanel.add(slot);
        }
    }

    private JLabel crearSlotPosicion() {
        JLabel slot = new JLabel("", SwingConstants.CENTER);
        slot.setSize(80, 30);
        slot.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        slot.setOpaque(true);
        slot.setBackground(new Color(255, 255, 255, 200));
        return slot;
    }

    private JPanel crearPanelJugadores(List<Jugador> jugadores, boolean esLocal) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(colorFondo);
        panel.setPreferredSize(new Dimension(200, getHeight()));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Título del panel
        JLabel titulo = new JLabel(esLocal ? juego.getLocal().getNombre() : juego.getVisitante().getNombre());
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(colorBoton);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        // Verificar si hay jugadores
        if (jugadores == null || jugadores.isEmpty()) {
            JLabel vacio = new JLabel("No hay jugadores");
            vacio.setForeground(Color.RED);
            panel.add(vacio);
            return panel;
        }

        // Añadir jugadores
        for (Jugador jugador : jugadores) {
            if (jugador != null) { // Verificación adicional
                JLabel labelJugador = crearLabelJugador(jugador, esLocal);
                panel.add(labelJugador);
                panel.add(Box.createVerticalStrut(10));
            }
        }

        return panel;
    }

    private JLabel crearLabelJugador(Jugador jugador, boolean esLocal) {
        String nombre = formatearNombre(jugador);
        JLabel label = new JLabel(nombre, SwingConstants.CENTER);
        
        label.setOpaque(true);
        label.setBackground(esLocal ? juego.getLocal().getColor() : juego.getVisitante().getColor());
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        label.setMaximumSize(new Dimension(180, 30));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return label;
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
}