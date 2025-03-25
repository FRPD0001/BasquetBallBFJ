package logico;

public class StatsJugador {
	
    private int anio;
    private String jugador;
    private float puntosPorPartido;
    private float rebotesPorPartido;
    private float asistenciasPorPartido;
    private float porcentajeTirosLibres;
    private float porcentajeTirosCampo;
    private float porcentajeTriples;

    public StatsJugador() {
    }

    public StatsJugador(int anio, String jugador, float puntosPorPartido, float rebotesPorPartido, 
                       float asistenciasPorPartido, float porcentajeTirosLibres, 
                       float porcentajeTirosCampo, float porcentajeTriples) {
    	
        this.anio = anio;
        this.jugador = jugador;
        this.puntosPorPartido = puntosPorPartido;
        this.rebotesPorPartido = rebotesPorPartido;
        this.asistenciasPorPartido = asistenciasPorPartido;
        this.porcentajeTirosLibres = porcentajeTirosLibres;
        this.porcentajeTirosCampo = porcentajeTirosCampo;
        this.porcentajeTriples = porcentajeTriples;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public float getPuntosPorPartido() {
        return puntosPorPartido;
    }

    public void setPuntosPorPartido(float puntosPorPartido) {
        this.puntosPorPartido = puntosPorPartido;
    }

    public float getRebotesPorPartido() {
        return rebotesPorPartido;
    }

    public void setRebotesPorPartido(float rebotesPorPartido) {
        this.rebotesPorPartido = rebotesPorPartido;
    }

    public float getAsistenciasPorPartido() {
        return asistenciasPorPartido;
    }

    public void setAsistenciasPorPartido(float asistenciasPorPartido) {
        this.asistenciasPorPartido = asistenciasPorPartido;
    }

    public float getPorcentajeTirosLibres() {
        return porcentajeTirosLibres;
    }

    public void setPorcentajeTirosLibres(float porcentajeTirosLibres) {
        this.porcentajeTirosLibres = porcentajeTirosLibres;
    }

    public float getPorcentajeTirosCampo() {
        return porcentajeTirosCampo;
    }

    public void setPorcentajeTirosCampo(float porcentajeTirosCampo) {
        this.porcentajeTirosCampo = porcentajeTirosCampo;
    }

    public float getPorcentajeTriples() {
        return porcentajeTriples;
    }

    public void setPorcentajeTriples(float porcentajeTriples) {
        this.porcentajeTriples = porcentajeTriples;
    }
}