package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import logico.Juego;
import logico.Jugador;
import logico.StatsJugador;
import logico.Equipo;

public class SimularJuego extends JFrame {
    
    private Juego juego;
    private Color colorFondo;
    private Color colorBoton;
    
    private int puntosLocal = 0;
    private int puntosVisitante = 0;
    private int tiempoTranscurrido = 0; // 0 = Primer tiempo, 1 = Segundo tiempo, 2 = Finalizado
    
    private JLabel lblPuntosLocal;
    private JLabel lblPuntosVisitante;
    private JLabel lblTiempo;
    private JButton btnTerminarJuego;
    
    public SimularJuego(Juego juego, Color colorFondo, Color colorBoton) {
        // Validaciones iniciales
        if (juego == null) {
            throw new IllegalArgumentException("El juego no puede ser null");
        }
        if (juego.getLocal() == null || juego.getVisitante() == null) {
            throw new IllegalArgumentException("Los equipos no pueden ser null");
        }
        if (colorFondo == null || colorBoton == null) {
            throw new IllegalArgumentException("Los colores no pueden ser null");
        }

        this.juego = juego;
        this.colorFondo = colorFondo;
        this.colorBoton = colorBoton;
        
        setTitle("Simular Juego - " + juego.getLocal().getNombre() + " vs " + juego.getVisitante().getNombre());
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(colorFondo);
        
        try {
            initUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar la interfaz: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    private void initUI() {
        // Panel superior con marcador
        JPanel panelMarcador = new JPanel(new GridLayout(1, 3));
        panelMarcador.setBackground(colorFondo);
        panelMarcador.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Equipo local
        JPanel panelLocal = crearPanelEquipo(juego.getLocal(), true);
        
        // Tiempo
        JPanel panelTiempo = new JPanel();
        panelTiempo.setBackground(colorFondo);
        panelTiempo.setLayout(new BoxLayout(panelTiempo, BoxLayout.Y_AXIS));
        
        lblTiempo = new JLabel("PRIMER TIEMPO", SwingConstants.CENTER);
        lblTiempo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTiempo.setForeground(colorBoton);
        
        JButton btnSiguienteTiempo = new JButton("Finalizar Tiempo");
        btnSiguienteTiempo.setBackground(colorBoton);
        btnSiguienteTiempo.setForeground(Color.WHITE);
        btnSiguienteTiempo.addActionListener(e -> siguienteTiempo());
        
        panelTiempo.add(lblTiempo);
        panelTiempo.add(Box.createVerticalStrut(20));
        panelTiempo.add(btnSiguienteTiempo);
        
        // Equipo visitante
        JPanel panelVisitante = crearPanelEquipo(juego.getVisitante(), false);
        
        panelMarcador.add(panelLocal);
        panelMarcador.add(panelTiempo);
        panelMarcador.add(panelVisitante);
        
        // Panel central con opciones de puntuación
        JPanel panelOpciones = crearPanelOpcionesPuntuacion();
        
        // Panel inferior con botón terminar
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(colorFondo);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        btnTerminarJuego = new JButton("Terminar Juego");
        btnTerminarJuego.setBackground(new Color(150, 0, 0));
        btnTerminarJuego.setForeground(Color.WHITE);
        btnTerminarJuego.setEnabled(false);
        btnTerminarJuego.addActionListener(e -> terminarJuego());
        
        panelInferior.add(btnTerminarJuego);
        
        add(panelMarcador, BorderLayout.NORTH);
        add(panelOpciones, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelEquipo(Equipo equipo, boolean esLocal) {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel lblNombre = new JLabel(equipo.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombre.setForeground(colorBoton);
        
        JLabel lblPuntos = new JLabel("0", SwingConstants.CENTER);
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 36));
        lblPuntos.setForeground(Color.WHITE);
        
        if (esLocal) {
            lblPuntosLocal = lblPuntos;
        } else {
            lblPuntosVisitante = lblPuntos;
        }
        
        panel.add(lblNombre);
        panel.add(lblPuntos);
        
        return panel;
    }
    
    private JPanel crearPanelOpcionesPuntuacion() {
        JPanel panelOpciones = new JPanel(new GridLayout(2, 3, 10, 10));
        panelOpciones.setBackground(colorFondo);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] tiposPuntos = {"Tiro Libre (1pt)", "Tiro de Campo (2pts)", "Triple (3pts)"};
        
        for (String tipo : tiposPuntos) {
            // Botón para equipo local
            JButton btnLocal = new JButton("Local: " + tipo);
            btnLocal.setBackground(juego.getLocal().getColor());
            btnLocal.setForeground(Color.WHITE);
            btnLocal.addActionListener(e -> {
                try {
                    agregarPuntos(true, tipo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Error al agregar puntos: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            // Botón para equipo visitante
            JButton btnVisitante = new JButton("Visitante: " + tipo);
            btnVisitante.setBackground(juego.getVisitante().getColor());
            btnVisitante.setForeground(Color.WHITE);
            btnVisitante.addActionListener(e -> {
                try {
                    agregarPuntos(false, tipo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Error al agregar puntos: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            panelOpciones.add(btnLocal);
            panelOpciones.add(btnVisitante);
        }
        
        return panelOpciones;
    }
    
    private void agregarPuntos(boolean esLocal, String tipoPunto) {
        int puntos = 0;
        switch(tipoPunto) {
            case "Tiro Libre (1pt)": puntos = 1; break;
            case "Tiro de Campo (2pts)": puntos = 2; break;
            case "Triple (3pts)": puntos = 3; break;
            default: throw new IllegalArgumentException("Tipo de punto no válido");
        }
        
        if (esLocal) {
            puntosLocal += puntos;
            lblPuntosLocal.setText(String.valueOf(puntosLocal));
            asignarAsistencias(juego.getLocal());
        } else {
            puntosVisitante += puntos;
            lblPuntosVisitante.setText(String.valueOf(puntosVisitante));
            asignarAsistencias(juego.getVisitante());
        }
    }
    
    private void asignarAsistencias(Equipo equipo) {
        if (equipo == null || equipo.getJugadores() == null) return;
        
        for (Jugador jugador : equipo.getJugadores()) {
            if (jugador != null) {
                StatsJugador stats = jugador.getEstadistica();
                if (stats != null) {
                    stats.setAsistenciasPorPartido(
                        stats.getAsistenciasPorPartido() + 1);
                }
            }
        }
    }
    
    private void siguienteTiempo() {
        tiempoTranscurrido++;
        
        if (tiempoTranscurrido == 1) {
            lblTiempo.setText("SEGUNDO TIEMPO");
        } else if (tiempoTranscurrido == 2) {
            lblTiempo.setText("JUEGO TERMINADO");
            btnTerminarJuego.setEnabled(true);
            
            // Deshabilitar botones de puntuación
            Component[] components = getContentPane().getComponents();
            for (Component component : components) {
                if (component instanceof JPanel) {
                    disableButtons((JPanel) component);
                }
            }
        }
    }
    
    private void disableButtons(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(false);
            } else if (component instanceof JPanel) {
                disableButtons((JPanel) component);
            }
        }
    }
    
    private void terminarJuego() {
        try {
            // Determinar ganador
            String mensaje;
            if (puntosLocal > puntosVisitante) {
                juego.actualizarResultado(puntosLocal, puntosVisitante);
                mensaje = "¡" + juego.getLocal().getNombre() + " gana el juego!\n" +
                          "Resultado: " + puntosLocal + " - " + puntosVisitante;
            } else if (puntosVisitante > puntosLocal) {
                juego.actualizarResultado(puntosLocal, puntosVisitante);
                mensaje = "¡" + juego.getVisitante().getNombre() + " gana el juego!\n" +
                          "Resultado: " + puntosLocal + " - " + puntosVisitante;
            } else {
                mensaje = "¡Empate!\nResultado: " + puntosLocal + " - " + puntosVisitante;
            }
            
            JOptionPane.showMessageDialog(this, mensaje, 
                "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al terminar el juego: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}