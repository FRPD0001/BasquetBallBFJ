package logico;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

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

    // ------------------------- CONSTRUCTORES -------------------------
    public Juego() {
        this("", null, null, LocalDate.now());
    }

    public Juego(String id, Equipo local, Equipo visitante, LocalDate fechaJuego) {
        this.id = Objects.requireNonNull(id, "ID no puede ser null");
        this.local = local;
        this.visitante = visitante;
        this.activosLocal = new ArrayList<>();
        this.activosVisitante = new ArrayList<>();
        this.puntosLocal = 0;
        this.puntosVisitante = 0;
        this.winner = null;
        this.fechaJuego = Objects.requireNonNull(fechaJuego, "Fecha no puede ser null");
        this.isDone = false;
    }

    public void actualizarResultado(int puntosLocal, int puntosVisitante) {
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;
        determinarGanador();
    }

    private void determinarGanador() {
        if (puntosLocal > puntosVisitante) {
            setWinner(local);
        } else if (puntosVisitante > puntosLocal) {
            setWinner(visitante);
        } else {
            setWinner(null); // Empate
        }
    }

    public void setWinner(Equipo nuevoGanador) {
        // Revertir estadísticas del ganador anterior
        if (this.winner != null) {
            if (this.winner.equals(local)) {
                local.setWin(local.getWin() - 1);
                visitante.setLose(visitante.getLose() - 1);
            } else if (this.winner.equals(visitante)) {
                visitante.setWin(visitante.getWin() - 1);
                local.setLose(local.getLose() - 1);
            }
        }
        
        // Actualizar nuevo ganador
        this.winner = nuevoGanador;
        this.isDone = (nuevoGanador != null);
        
        // Aplicar nuevas estadísticas
        if (nuevoGanador != null) {
            if (nuevoGanador.equals(local)) {
                local.setWin(local.getWin() + 1);
                visitante.setLose(visitante.getLose() + 1);
            } else if (nuevoGanador.equals(visitante)) {
                visitante.setWin(visitante.getWin() + 1);
                local.setLose(local.getLose() + 1);
            }
        }
    }

   
    public ArrayList<Jugador> getActivosLocal() {
        if (activosLocal == null) {
            activosLocal = new ArrayList<>();
        }
        return activosLocal;
    }

    public ArrayList<Jugador> getActivosVisitante() {
        if (activosVisitante == null) {
            activosVisitante = new ArrayList<>();
        }
        return activosVisitante;
    }

    // ------------------------- SETTERS PROTEGIDOS -------------------------
    public void setActivosLocal(ArrayList<Jugador> activosLocal) {
        this.activosLocal = activosLocal != null ? new ArrayList<>(activosLocal) : new ArrayList<>();
    }

    public void setActivosVisitante(ArrayList<Jugador> activosVisitante) {
        this.activosVisitante = activosVisitante != null ? new ArrayList<>(activosVisitante) : new ArrayList<>();
    }

    // ------------------------- MANEJO DE JUGADORES -------------------------
    public boolean agregarJugadorLocal(Jugador jugador) {
        Objects.requireNonNull(jugador, "Jugador no puede ser null");
        if (!getActivosLocal().contains(jugador)) {
            return getActivosLocal().add(jugador);
        }
        return false;
    }

    public boolean agregarJugadorVisitante(Jugador jugador) {
        Objects.requireNonNull(jugador, "Jugador no puede ser null");
        if (!getActivosVisitante().contains(jugador)) {
            return getActivosVisitante().add(jugador);
        }
        return false;
    }

    public boolean removerJugadorLocal(Jugador jugador) {
        return getActivosLocal().remove(jugador);
    }

    public boolean removerJugadorVisitante(Jugador jugador) {
        return getActivosVisitante().remove(jugador);
    }

    public String getId() { return id; }
    public Equipo getLocal() { return local; }
    public Equipo getVisitante() { return visitante; }
    public int getPuntosLocal() { return puntosLocal; }
    public int getPuntosVisitante() { return puntosVisitante; }
    public Equipo getWinner() { return winner; }
    public LocalDate getFechaJuego() { return fechaJuego; }
    public boolean isDone() { return isDone; }

    public void setId(String id) { this.id = Objects.requireNonNull(id); }
    public void setLocal(Equipo local) { this.local = local; }
    public void setVisitante(Equipo visitante) { this.visitante = visitante; }
    public void setPuntosLocal(int puntosLocal) { this.puntosLocal = puntosLocal; }
    public void setPuntosVisitante(int puntosVisitante) { this.puntosVisitante = puntosVisitante; }
    public void setFechaJuego(LocalDate fechaJuego) { this.fechaJuego = Objects.requireNonNull(fechaJuego); }
    public void setDone(boolean isDone) { 
        this.isDone = isDone;
        if (!isDone) setWinner(null);
    }

    public boolean tieneEquiposValidos() {
        return local != null && visitante != null && !local.equals(visitante);
    }

    public boolean tieneJugadoresSuficientes() {
        return getActivosLocal().size() == 5 && getActivosVisitante().size() == 5;
    }
}