package logico;

import java.time.LocalDate;

public class Lesion {
	
    private String id;
    private Jugador jugador;
    private LocalDate fecRec;
    private LocalDate fecLes;
    private String lesion;
    private boolean lesionado;
    
	private Lesion(String id, Jugador jugador, LocalDate fecRec, LocalDate fecLes, String lesion, boolean lesionado) {
		super();
		this.id = id;
		this.jugador = jugador;
		this.fecRec = fecRec;
		this.fecLes = fecLes;
		this.lesion = lesion;
		this.lesionado = lesionado;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Jugador getJugador() {
		return jugador;
	}
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
	public LocalDate getFecRec() {
		return fecRec;
	}
	public void setFecRec(LocalDate fecRec) {
		this.fecRec = fecRec;
	}
	public LocalDate getFecLes() {
		return fecLes;
	}
	public void setFecLes(LocalDate fecLes) {
		this.fecLes = fecLes;
	}
	public String getLesion() {
		return lesion;
	}
	public void setLesion(String lesion) {
		this.lesion = lesion;
	}
	public boolean isLesionado() {
		return lesionado;
	}
	public void setLesionado(boolean lesionado) {
		this.lesionado = lesionado;
	}
    
    

}
