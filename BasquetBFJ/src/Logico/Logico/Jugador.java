package logico;

import java.util.ArrayList;

public class Jugador {
	
    private String id;
    private String nombre;
    private float peso;
    private float altura;
    private ArrayList<Lesion> misLesiones;
    private Equipo equipo;
    private StatsJugador estadistica;

    public Jugador() {
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

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public ArrayList<Lesion> getMisLesiones() {
        return misLesiones;
    }

    public void setMisLesiones(ArrayList<Lesion> misLesiones) {
        this.misLesiones = misLesiones;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public StatsJugador getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(StatsJugador estadistica) {
        this.estadistica = estadistica;
    }
}
