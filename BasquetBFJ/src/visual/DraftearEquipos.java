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
    private JLabel[] slotsLocal;
    private JLabel[] slotsVisitante;

    public DraftearEquipos(Juego juego, Color colorFondo, Color colorBoton) {
        this.juego = juego;
        this.colorFondo = colorFondo;
        this.colorBoton = colorBoton;
        this.slotsLocal = new JLabel[5];
        this.slotsVisitante = new JLabel[5];
        initUI();
    }

    private void initUI() {
        setTitle("Draftear Equipos");
        setSize(1200, 750); // Aumentado para el panel inferior
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

    private void verificarYEmpezarJuego() {
        // Verificar slots locales
        for (int i = 0; i < slotsLocal.length; i++) {
            if (slotsLocal[i].getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Falta asignar jugador en posición " + (i+1) + " del equipo local", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Verificar slots visitante
        for (int i = 0; i < slotsVisitante.length; i++) {
            if (slotsVisitante[i].getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Falta asignar jugador en posición " + (i+1) + " del equipo visitante", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Si todo está correcto
        JOptionPane.showMessageDialog(this, 
            "¡Juego iniciado correctamente!", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
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
            if (jugador != null) {
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
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        label.setPreferredSize(new Dimension(180, 30));
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