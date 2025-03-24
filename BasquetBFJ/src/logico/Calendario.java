package logico;
import java.time.LocalDate;

import java.util.ArrayList;

public class Calendario {
    private String idJuego;
    private ArrayList<LocalDate> fecJuego;
	private Calendario(String idJuego, ArrayList<LocalDate> fecJuego) {
		super();
		this.idJuego = idJuego;
		this.fecJuego = fecJuego;
	}
	
	public String getIdJuego() {
		return idJuego;
	}
	public void setIdJuego(String idJuego) {
		this.idJuego = idJuego;
	}
	public ArrayList<LocalDate> getFecJuego() {
		return fecJuego;
	}
	public void setFecJuego(ArrayList<LocalDate> fecJuego) {
		this.fecJuego = fecJuego;
	}
    

}
