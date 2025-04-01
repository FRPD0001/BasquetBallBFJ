package logico;

public class StatsJugador {
	
    private String jugador;
    private float puntosPorPartido;
    private float rebotesPorPartido;
    private float asistenciasPorPartido;
    private float porcentajeTirosLibres;
    private float porcentajeTirosCampo;
    private float porcentajeTriples;
    private float coefEficiencia;

    public StatsJugador() {
    }

    public StatsJugador(String jugador, float puntosPorPartido, float rebotesPorPartido, 
                       float asistenciasPorPartido, float porcentajeTirosLibres, 
                       float porcentajeTirosCampo, float porcentajeTriples) {
    	
        this.jugador = jugador;
        this.puntosPorPartido = puntosPorPartido;
        this.rebotesPorPartido = rebotesPorPartido;
        this.asistenciasPorPartido = asistenciasPorPartido;
        this.porcentajeTirosLibres = porcentajeTirosLibres;
        this.porcentajeTirosCampo = porcentajeTirosCampo;
        this.porcentajeTriples = porcentajeTriples;
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
    
    public float calcularCoeficienteEfectividad() {
        // Fórmula de eficiencia 
        float efectividad = 
            puntosPorPartido + 
            (rebotesPorPartido * 0.7f) + 
            (asistenciasPorPartido * 0.7f) +
            ((porcentajeTirosCampo / 100) * 50) + 
            ((porcentajeTriples / 100) * 30) -
            (1 - (porcentajeTirosLibres / 100)) * 20; 
        
        return Math.max(efectividad/100.0f, 0); // Evitar valores negativos
    }
}