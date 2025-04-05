package logico;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Collections;
import logico.Juego;

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
    
    public Jugador buscarJugadorPorId(String id) {
        for (Jugador jugador : misJugadores) {
            if (jugador.getId().equals(id)) {
                return jugador;
            }
        }
        return null;
    }
    
    // Nuevos métodos para el calendario
    public boolean esNumeroEquiposPar() {
        return misEquipos.size() % 2 == 0;
    }

    public ArrayList<ArrayList<Equipo>> generarRoundRobin() {
        if (!esNumeroEquiposPar()) {
            return new ArrayList<>(); // Retorna lista vacía si no es par
        }
        
        ArrayList<Equipo> equipos = new ArrayList<>(misEquipos);
        ArrayList<ArrayList<Equipo>> todasJornadas = new ArrayList<>();
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
            
            todasJornadas.add(jornadaActual);
            
            // Rotar equipos (excepto el primero)
            Equipo ultimo = equipos.remove(numEquipos - 1);
            equipos.add(1, ultimo);
        }
        
        return todasJornadas;
    }
    
    public ArrayList<ArrayList<Equipo>> generarRoundRobinAleatorio() {
        ArrayList<Equipo> equipos = new ArrayList<>(misEquipos);
        Collections.shuffle(equipos);
        return generarRoundRobin();
    }
}