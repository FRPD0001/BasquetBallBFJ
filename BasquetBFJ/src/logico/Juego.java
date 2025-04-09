package logico;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.ArrayList;

public class Juego implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String id;
    private Equipo local;
    private Equipo visitante;
    private ArrayList<Jugador> activosLocal;
    private ArrayList<Jugador> activosVisitante;
    private int puntosLocal;
    private int puntosVisitante;
    private Equipo winner;
    private LocalDate fechaJuego;
    private boolean isDone;

    // Constructor completo que inicializa todos los campos
    public Juego(String id, Equipo local, Equipo visitante, LocalDate fechaJuego) {
        this.id = id;
        this.local = local;
        this.visitante = visitante;
        this.activosLocal = new ArrayList<>();
        this.activosVisitante = new ArrayList<>();
        this.puntosLocal = 0;
        this.puntosVisitante = 0;
        this.winner = null;
        this.fechaJuego = fechaJuego;
        this.isDone = false;
    }

    // Método para actualizar resultado del juego
    public void actualizarResultado(int puntosLocal, int puntosVisitante) {
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;
        
        if (puntosLocal > puntosVisitante) {
            setWinner(local);
        } else if (puntosVisitante > puntosLocal) {
            setWinner(visitante);
        } else {
            setWinner(null);
        }
    }

    // ============ GETTERS ============
    public String getId() {
        return id;
    }

    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public ArrayList<Jugador> getActivosLocal() {
        return activosLocal;
    }

    public ArrayList<Jugador> getActivosVisitante() {
        return activosVisitante;
    }

    public int getPuntosLocal() {
        return puntosLocal;
    }

    public int getPuntosVisitante() {
        return puntosVisitante;
    }

    public Equipo getWinner() {
        return winner;
    }

    public LocalDate getFechaJuego() {
        return fechaJuego;
    }

    public boolean isDone() {
        return isDone;
    }

    // ============ SETTERS ============
    public void setId(String id) {
        this.id = id;
    }

    public void setLocal(Equipo local) {
        this.local = local;
    }

    public void setVisitante(Equipo visitante) {
        this.visitante = visitante;
    }

    public void setActivosLocal(ArrayList<Jugador> activosLocal) {
        if (activosLocal != null) {
            this.activosLocal = activosLocal;
        }
    }

    public void setActivosVisitante(ArrayList<Jugador> activosVisitante) {
        if (activosVisitante != null) {
            this.activosVisitante = activosVisitante;
        }
    }

    public void setPuntosLocal(int puntosLocal) {
        this.puntosLocal = puntosLocal;
    }

    public void setPuntosVisitante(int puntosVisitante) {
        this.puntosVisitante = puntosVisitante;
    }

    public void setWinner(Equipo winner) {
        // Si había un ganador previo, revertir estadísticas
        if (this.winner != null) {
            if (this.winner.equals(local)) {
                local.setWin(local.getWin() - 1);
                visitante.setLose(visitante.getLose() - 1);
            } else if (this.winner.equals(visitante)) {
                visitante.setWin(visitante.getWin() - 1);
                local.setLose(local.getLose() - 1);
            }
        }
        
        this.winner = winner;
        this.isDone = (winner != null);
        
        // Aplicar nuevas estadísticas
        if (winner != null) {
            if (winner.equals(local)) {
                local.setWin(local.getWin() + 1);
                visitante.setLose(visitante.getLose() + 1);
            } else if (winner.equals(visitante)) {
                visitante.setWin(visitante.getWin() + 1);
                local.setLose(local.getLose() + 1);
            }
        }
    }

    public void setFechaJuego(LocalDate fechaJuego) {
        this.fechaJuego = fechaJuego;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
        if (!isDone) {
            this.winner = null;
        }
    }

    // Métodos para manejar jugadores activos
    public void agregarJugadorLocal(Jugador jugador) {
        if (jugador != null && !activosLocal.contains(jugador)) {
            activosLocal.add(jugador);
        }
    }

    public void agregarJugadorVisitante(Jugador jugador) {
        if (jugador != null && !activosVisitante.contains(jugador)) {
            activosVisitante.add(jugador);
        }
    }

    public void removerJugadorLocal(Jugador jugador) {
        activosLocal.remove(jugador);
    }

    public void removerJugadorVisitante(Jugador jugador) {
        activosVisitante.remove(jugador);
    }
}