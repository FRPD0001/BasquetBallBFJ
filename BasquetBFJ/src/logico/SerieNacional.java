package logico;

import java.util.ArrayList;
import java.time.LocalDate;

public class SerieNacional {
    
    private ArrayList<Equipo> misEquipos;
    private static int genEquipo;
    private ArrayList<Jugador> misJugadores;
    private static int genJugador;
    private ArrayList<Juego> misJuegos;
    private static int genJuego;
    private static SerieNacional serie;

    private SerieNacional() {
    	genEquipo = 1;
    	genJugador = 1;
    	genJuego = 1;
    	
        misEquipos = new ArrayList<>();
        misJugadores = new ArrayList<>();
        misJuegos = new ArrayList<>();
    }

    public static SerieNacional getInstance() {
        if (serie == null) {
            serie = new SerieNacional();
        }
        return serie;
    }

    public ArrayList<Equipo> getMisEquipos() {
        return misEquipos;
    }

    public void setMisEquipos(ArrayList<Equipo> misEquipos) {
        this.misEquipos = misEquipos;
    }

    public static int getGenEquipo() {
        return genEquipo;
    }

    public static void setGenEquipo(int genEquipo) {
        SerieNacional.genEquipo = genEquipo;
    }

    public ArrayList<Jugador> getMisJugadores() {
        return misJugadores;
    }

    public void setMisJugadores(ArrayList<Jugador> misJugadores) {
        this.misJugadores = misJugadores;
    }

    public static int getGenJugador() {
        return genJugador;
    }

    public static void setGenJugador(int genJugador) {
        SerieNacional.genJugador = genJugador;
    }

    public ArrayList<Juego> getMisJuegos() {
        return misJuegos;
    }

    public void setMisJuegos(ArrayList<Juego> misJuegos) {
        this.misJuegos = misJuegos;
    }

    public static int getGenJuego() {
        return genJuego;
    }

    public static void setGenJuego(int genJuego) {
        SerieNacional.genJuego = genJuego;
    }

    public void agregarEquipo(Equipo equipo) {
        misEquipos.add(equipo);
        genEquipo++;
    }

    public void agregarJugador(Jugador jugador) {
        misJugadores.add(jugador);
        genJugador++;
    }

    public void agregarJuego(Juego juego) {
        misJuegos.add(juego);
        genJuego++;
    }

    public float Winrate(Equipo equipo) {
        if (equipo.getWin() + equipo.getLose() == 0) {
            return 0;
        }
        return (float) equipo.getWin() / (equipo.getWin() + equipo.getLose()) * 100;
    }
}