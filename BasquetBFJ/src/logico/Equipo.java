package logico;

import java.util.ArrayList;

public class Equipo {
	
    private String id;
    private String nombre;
    private String ciudad;
    private ArrayList<Integer> juegos;
    private ArrayList<Jugador> jugadores;
    private int win;
    private int lose;

    public Equipo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public ArrayList<Integer> getJuegos() {
        return juegos;
    }

    public void setJuegos(ArrayList<Integer> juegos) {
        this.juegos = juegos;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }
}