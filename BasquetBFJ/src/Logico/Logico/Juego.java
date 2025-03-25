package logico;

import java.util.ArrayList;

public class Juego {
    private String id;
    private Equipo local;
    private Equipo visitante;
    private ArrayList<StatsJugador> estadisticas;
    private int puntosLocal;
    private int puntosVisitante;
    private String winner;

    public Juego() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Equipo getLocal() {
        return local;
    }

    public void setLocal(Equipo local) {
        this.local = local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public void setVisitante(Equipo visitante) {
        this.visitante = visitante;
    }

    public ArrayList<StatsJugador> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(ArrayList<StatsJugador> estadisticas) {
        this.estadisticas = estadisticas;
    }

    public int getPuntosLocal() {
        return puntosLocal;
    }

    public void setPuntosLocal(int puntosLocal) {
        this.puntosLocal = puntosLocal;
    }

    public int getPuntosVisitante() {
        return puntosVisitante;
    }

    public void setPuntosVisitante(int puntosVisitante) {
        this.puntosVisitante = puntosVisitante;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}