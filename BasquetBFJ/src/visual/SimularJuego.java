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
        this.juego = juego;
        this.colorFondo = colorFondo;
        this.colorBoton = colorBoton;
        
        setTitle("Simular Juego - " + juego.getLocal().getNombre() + " vs " + juego.getVisitante().getNombre());
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(colorFondo);
        
        initUI();
    }
    
    private void initUI() {
        // Panel superior con marcador
        JPanel panelMarcador = new JPanel(new GridLayout(1, 3));
        panelMarcador.setBackground(colorFondo);
        panelMarcador.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Equipo local
        JPanel panelLocal = new JPanel();
        panelLocal.setBackground(colorFondo);
        panelLocal.setLayout(new BoxLayout(panelLocal, BoxLayout.Y_AXIS));
        
        JLabel lblNombreLocal = new JLabel(juego.getLocal().getNombre(), SwingConstants.CENTER);
        lblNombreLocal.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombreLocal.setForeground(colorBoton);
        
        lblPuntosLocal = new JLabel("0", SwingConstants.CENTER);
        lblPuntosLocal.setFont(new Font("Arial", Font.BOLD, 36));
        lblPuntosLocal.setForeground(Color.WHITE);
        
        panelLocal.add(lblNombreLocal);
        panelLocal.add(lblPuntosLocal);
        
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
        JPanel panelVisitante = new JPanel();
        panelVisitante.setBackground(colorFondo);
        panelVisitante.setLayout(new BoxLayout(panelVisitante, BoxLayout.Y_AXIS));
        
        JLabel lblNombreVisitante = new JLabel(juego.getVisitante().getNombre(), SwingConstants.CENTER);
        lblNombreVisitante.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombreVisitante.setForeground(colorBoton);
        
        lblPuntosVisitante = new JLabel("0", SwingConstants.CENTER);
        lblPuntosVisitante.setFont(new Font("Arial", Font.BOLD, 36));
        lblPuntosVisitante.setForeground(Color.WHITE);
        
        panelVisitante.add(lblNombreVisitante);
        panelVisitante.add(lblPuntosVisitante);
        
        panelMarcador.add(panelLocal);
        panelMarcador.add(panelTiempo);
        panelMarcador.add(panelVisitante);
        
        // Panel central con opciones de puntuación
        JPanel panelOpciones = new JPanel(new GridLayout(2, 3, 10, 10));
        panelOpciones.setBackground(colorFondo);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] tiposPuntos = {"Tiro Libre (1pt)", "Tiro de Campo (2pts)", "Triple (3pts)"};
        for (String tipo : tiposPuntos) {
            JButton btnLocal = new JButton("Local: " + tipo);
            btnLocal.setBackground(juego.getLocal().getColor());
            btnLocal.setForeground(Color.WHITE);
            btnLocal.addActionListener(e -> agregarPuntos(true, tipo));
            
            JButton btnVisitante = new JButton("Visitante: " + tipo);
            btnVisitante.setBackground(juego.getVisitante().getColor());
            btnVisitante.setForeground(Color.WHITE);
            btnVisitante.addActionListener(e -> agregarPuntos(false, tipo));
            
            panelOpciones.add(btnLocal);
            panelOpciones.add(btnVisitante);
        }
        
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
    
    private void agregarPuntos(boolean esLocal, String tipoPunto) {
        int puntos = 0;
        switch(tipoPunto) {
            case "Tiro Libre (1pt)": puntos = 1; break;
            case "Tiro de Campo (2pts)": puntos = 2; break;
            case "Triple (3pts)": puntos = 3; break;
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
        for (Jugador jugador : equipo.getJugadores()) {
            StatsJugador stats = jugador.getEstadistica();
            if (stats != null) {
                stats.setAsistenciasPorPartido(stats.getAsistenciasPorPartido() + 1);
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
        }
    }
    
    private void terminarJuego() {
        // Determinar ganador
        if (puntosLocal > puntosVisitante) {
            juego.actualizarResultado(puntosLocal, puntosVisitante);
            JOptionPane.showMessageDialog(this, 
                "¡" + juego.getLocal().getNombre() + " gana el juego!\n" +
                "Resultado: " + puntosLocal + " - " + puntosVisitante,
                "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
        } else if (puntosVisitante > puntosLocal) {
            juego.actualizarResultado(puntosLocal, puntosVisitante);
            JOptionPane.showMessageDialog(this, 
                "¡" + juego.getVisitante().getNombre() + " gana el juego!\n" +
                "Resultado: " + puntosLocal + " - " + puntosVisitante,
                "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "¡Empate!\n" +
                "Resultado: " + puntosLocal + " - " + puntosVisitante,
                "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
        }
        
        dispose();
    }
}