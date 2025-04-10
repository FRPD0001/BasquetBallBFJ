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
    private int tiempoTranscurrido = 0;
    
    private JLabel lblPuntosLocal;
    private JLabel lblPuntosVisitante;
    private JLabel lblTiempo;
    private JButton btnTerminarJuego;
    private JButton btnSiguienteTiempo;
    
    public SimularJuego(Juego juego, Color colorFondo, Color colorBoton) {
    	
        ImageIcon icon = new ImageIcon("media/LogoProyecto.png");
        setIconImage(icon.getImage());
        
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
        JPanel panelMarcador = new JPanel(new GridLayout(1, 3));
        panelMarcador.setBackground(colorFondo);
        panelMarcador.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        btnSiguienteTiempo = new JButton("Finalizar Tiempo");
        btnSiguienteTiempo.setBackground(colorBoton);
        btnSiguienteTiempo.setForeground(Color.WHITE);
        btnSiguienteTiempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siguienteTiempo();
            }
        });
        
        JPanel panelLocal = crearPanelEquipo(juego.getLocal(), true);
        
        
        JPanel panelTiempo = new JPanel();
        panelTiempo.setBackground(colorFondo);
        panelTiempo.setLayout(new BoxLayout(panelTiempo, BoxLayout.Y_AXIS));
        
        lblTiempo = new JLabel("PRIMER TIEMPO", SwingConstants.CENTER);
        lblTiempo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTiempo.setForeground(colorBoton);
        
        panelTiempo.add(lblTiempo);
        panelTiempo.add(Box.createVerticalStrut(20));
        panelTiempo.add(btnSiguienteTiempo);
        
        JPanel panelVisitante = crearPanelEquipo(juego.getVisitante(), false);
        
        panelMarcador.add(panelLocal);
        panelMarcador.add(panelTiempo);
        panelMarcador.add(panelVisitante);
        
        JPanel panelOpciones = crearPanelOpcionesPuntuacion();
        
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(colorFondo);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        btnTerminarJuego = new JButton("Terminar Juego");
        btnTerminarJuego.setBackground(new Color(150, 0, 0));
        btnTerminarJuego.setForeground(Color.WHITE);
        btnTerminarJuego.setEnabled(false);
        btnTerminarJuego.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminarJuego();
            }
        });

        
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER));  
        panelInferior.add(btnTerminarJuego);
        
        add(panelMarcador, BorderLayout.NORTH);
        add(panelOpciones, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelEquipo(Equipo equipo, boolean esLocal) {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        String nombreEquipo = equipo.getNombre();
        if (esLocal) {
            nombreEquipo += " (L)";
        } else {
            nombreEquipo += " (V)";
        }
        
        JLabel lblNombre = new JLabel(nombreEquipo, SwingConstants.CENTER);
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

        JPanel panelLocal = new JPanel(new GridLayout(1, 3)); 
        JPanel panelVisitante = new JPanel(new GridLayout(1, 3));

        for (String tipo : tiposPuntos) {
        	
            JButton btnLocal = new JButton("Local: " + tipo);
            btnLocal.setBackground(juego.getLocal().getColor());
            btnLocal.setForeground(Color.WHITE);
            btnLocal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        agregarPuntos(true, tipo);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(SimularJuego.this,
                                "Error al agregar puntos: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });


            JButton btnVisitante = new JButton("Visitante: " + tipo);
            btnVisitante.setBackground(juego.getVisitante().getColor());
            btnVisitante.setForeground(Color.WHITE);
            btnVisitante.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        agregarPuntos(false, tipo);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(SimularJuego.this,
                                "Error al agregar puntos: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });


            panelLocal.add(btnLocal);
            panelVisitante.add(btnVisitante);
        }


        panelOpciones.add(panelLocal);
        panelOpciones.add(panelVisitante);

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
        } else if (tiempoTranscurrido >= 2) {
            lblTiempo.setText("ULTIMO TIEMPO");
            btnTerminarJuego.setEnabled(true);
            btnSiguienteTiempo.setEnabled(false);
            
        }
    }
    
    private void disableAllButtons() {
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                disableButtons((JPanel) component);
            }
        }

        btnTerminarJuego.setEnabled(true);
    }

    private void disableButtons(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton && component != btnTerminarJuego) {
                component.setEnabled(false);
            } else if (component instanceof JPanel) {
                disableButtons((JPanel) component);
            }
        }
    }
    
    private void terminarJuego() {
        try {

        	String mensaje;
            if (puntosLocal > puntosVisitante) {
                juego.actualizarResultado(puntosLocal, puntosVisitante);
                mensaje = "¡" + juego.getLocal().getNombre() + " gana el juego!\n" +
                          "Resultado final: " + puntosLocal + " - " + puntosVisitante;
            } else if (puntosVisitante > puntosLocal) {
                juego.actualizarResultado(puntosLocal, puntosVisitante);
                mensaje = "¡" + juego.getVisitante().getNombre() + " gana el juego!\n" +
                          "Resultado final: " + puntosLocal + " - " + puntosVisitante;
            } else {
                juego.actualizarResultado(puntosLocal, puntosVisitante);
                mensaje = "¡Empate!\nResultado final: " + puntosLocal + " - " + puntosVisitante;
            }
            
            JOptionPane.showMessageDialog(this, 
                mensaje,
                "Juego Terminado", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al terminar el juego: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}