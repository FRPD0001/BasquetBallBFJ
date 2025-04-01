package logico;
import java.awt.Color;
import java.util.ArrayList;
public class Equipo {

	private String id;
    private String nombre;
    private String ciudad;
    private ArrayList<Juego> juegos;
    private ArrayList<Jugador> jugadores;
    private int win;
    private int lose;
    private float nomina;
    private Color color;
    //aqui esta el color

    public Equipo(String id, String nombre, Color color) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.ciudad = "";
        this.juegos = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.win = 0;
        this.lose = 0;
        this.nomina = 0.0f;
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

    public ArrayList<Juego> getJuegos() {
        return juegos;
    }

    public void setJuegos(ArrayList<Juego> juegos) {
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
    
    public float getNomina() {
		return nomina;
	}

	public void setNomina(float nomina) {
		this.nomina = nomina;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float CalcularNomina() {
    	float nomina = 0;
    	
    	for(Jugador jugador : jugadores) {
    		nomina+=jugador.calcularSalario();
    	}
    	return nomina;
    }
    
}