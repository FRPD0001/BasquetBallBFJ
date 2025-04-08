package logico;

import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.util.Collections;

public class SerieNacional implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private ArrayList<Equipo> misEquipos;
    private static int genEquipo = 1;
    private ArrayList<Jugador> misJugadores;
    private static int genJugador = 1;
    private ArrayList<Juego> misJuegos;
    private static int genJuego = 1;
    private static SerieNacional serie;
    private static String FILE_NAME = "Serie_Nacional.DAT";

    private int savedGenEquipo = 1;
    private int savedGenJugador = 1;
    private int savedGenJuego = 1;

    private SerieNacional() {
        misEquipos = new ArrayList<>();
        misJugadores = new ArrayList<>();
        misJuegos = new ArrayList<>();
    }

    public static SerieNacional getInstance() {
        if (serie == null) {
            serie = new SerieNacional();
        }
        return serie;
    }

    public void guardarFileTest() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        
        try {
            // Sincronizamos los contadores estáticos con las variables de instancia antes de guardar
            this.savedGenEquipo = genEquipo;
            this.savedGenJugador = genJugador;
            this.savedGenJuego = genJuego;
            
            fos = new FileOutputStream(FILE_NAME);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void cargarFicheroTest() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            guardarFileTest();
            return;
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            SerieNacional loaded = (SerieNacional) ois.readObject();
            
            this.misEquipos = loaded.misEquipos;
            this.misJugadores = loaded.misJugadores;
            this.misJuegos = loaded.misJuegos;
            genEquipo = loaded.savedGenEquipo;
            genJugador = loaded.savedGenJugador;
            genJuego = loaded.savedGenJuego;
            serie = this;
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void agregarEquipo(Equipo equipo) {
        equipo.setId("EQ-" + genEquipo);
        misEquipos.add(equipo);
        genEquipo++;
    }

    public void agregarJugador(Jugador jugador) {
        jugador.setId("J-" + genJugador);
        misJugadores.add(jugador);
        genJugador++;
    }

    public void agregarJuego(Juego juego) {
        juego.setId("JG-" + genJuego);
        misJuegos.add(juego);
        genJuego++;
    }

    public Equipo buscarEquipoPorId(String id) {
        for (Equipo eq : misEquipos) {
            if (eq.getId().equals(id)) {
                return eq;
            }
        }
        return null;
    }

    public Jugador buscarJugadorPorId(String id) {
        for (Jugador jugador : misJugadores) {
            if (jugador.getId().equals(id)) {
                return jugador;
            }
        }
        return null;
    }

    // Métodos para generar round robin
    public boolean esNumeroEquiposPar() {
        return misEquipos.size() % 2 == 0;
    }

    public ArrayList<ArrayList<Equipo>> generarRoundRobin() {
        if (!esNumeroEquiposPar()) {
            return new ArrayList<>();
        }
        
        ArrayList<Equipo> equipos = new ArrayList<>(misEquipos);
        ArrayList<ArrayList<Equipo>> todasJornadas = new ArrayList<>();
        int numEquipos = equipos.size();
        int numJornadas = numEquipos - 1;
        int partidosPorJornada = numEquipos / 2;
        
        for (int jornada = 0; jornada < numJornadas; jornada++) {
            ArrayList<Equipo> jornadaActual = new ArrayList<>();
            
            for (int i = 0; i < partidosPorJornada; i++) {
                Equipo local = equipos.get(i);
                Equipo visitante = equipos.get(numEquipos - 1 - i);
                jornadaActual.add(local);
                jornadaActual.add(visitante);
            }
            
            todasJornadas.add(jornadaActual);
            
            // Rotar equipos (excepto el primero)
            Equipo ultimo = equipos.remove(numEquipos - 1);
            equipos.add(1, ultimo);
        }
        
        return todasJornadas;
    }

    // Getters y setters
    public ArrayList<Equipo> getMisEquipos() {
        return misEquipos;
    }

    public ArrayList<Jugador> getMisJugadores() {
        return misJugadores;
    }

    public ArrayList<Juego> getMisJuegos() {
        return misJuegos;
    }

    public static int getGenEquipo() {
        return genEquipo;
    }

    public static int getGenJugador() {
        return genJugador;
    }

    public static int getGenJuego() {
        return genJuego;
    }

    // Métodos para estadísticas
    public float Winrate(Equipo equipo) {
        if (equipo.getWin() + equipo.getLose() == 0) {
            return 0;
        }
        return (float) equipo.getWin() / (equipo.getWin() + equipo.getLose()) * 100;
    }
}