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
    private boolean lesionado;
    private float salario;
    private static int genLesion = 1;
    private static float salarioMax = 500000.00f;
    private static float salarioMin = 250000.00f;

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
    
    public float calcularSalario() {
    	return Math.max(getEstadistica().calcularCoeficienteEfectividad()*salarioMax, salarioMin);
    	
    }

    public Jugador(String id, String nombre, float peso, float altura, Equipo equipo) {
        this.id = id;
        this.nombre = nombre;
        this.peso = peso;
        this.altura = altura;
        this.equipo = equipo;
        this.setLesionado(false);
        this.misLesiones = new ArrayList<Lesion>(); 
        this.estadistica = new StatsJugador(
            nombre,                // jugador
            0.0f,                  // puntosPorPartido
            0.0f,                  // rebotesPorPartido
            0.0f,                  // asistenciasPorPartido
            0.0f,                  // porcentajeTirosLibres
            0.0f,                  // porcentajeTirosCampo
            0.0f                   // porcentajeTriples
        );
        this.setSalario(salarioMin); // Calcula salario inicial basado en stats
    }

	public boolean isLesionado() {
		return lesionado;
	}

	public void setLesionado(boolean lesionado) {
		this.lesionado = lesionado;
	}

	public float getSalario() {
		return salario;
	}

	public void setSalario(float salario) {
		this.salario = salario;
	}
	
	public void agregarLesion(Lesion lesion) {
	    if (misLesiones == null) {
	        misLesiones = new ArrayList<>();
	    }
	    misLesiones.add(lesion);
	    setGenLesion(getGenLesion() + 1);
	}

	public static int getGenLesion() {
		return genLesion;
	}

	public static void setGenLesion(int genLesion) {
		Jugador.genLesion = genLesion;
	}
	
	public Lesion buscarLesionPorId(String id) {
	    for (Lesion lesion : misLesiones) {
	        if (lesion.getId().equals(id)) {
	            return lesion;
	        }
	    }
	    return null;
	}
}
