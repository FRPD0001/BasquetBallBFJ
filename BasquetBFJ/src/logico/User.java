package logico;

import java.io.Serializable;

public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String tipo;  // Valores posibles: "Administrador", "Entrenador", "Scout", "Anotador"
    private String userName;  // Nombre de usuario (no afecta permisos)
    private String pass;
    
    public User(String tipo, String userName, String pass) {
        // Validación para asegurar que el tipo sea válido (opcional pero recomendado)
        if (!tipo.equalsIgnoreCase("Administrador") &&  
            !tipo.equalsIgnoreCase("Anotador")) {
            throw new IllegalArgumentException("Tipo de usuario no válido: " + tipo);
        }
        this.tipo = tipo;
        this.userName = userName;  // El username no se usa para permisos
        this.pass = pass;
    }

    // --- Getters y Setters (se mantienen igual) ---
    public String getTipo() {
        return tipo;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    // --- Métodos para verificar el tipo (basados SOLO en el campo `tipo`) ---
    public boolean esAdministrador() {
        return tipo.equalsIgnoreCase("Administrador");
    }


    // Método clave: Verifica si es anotador (usa SOLO el campo `tipo`)
    public boolean esAnotador() {
        return tipo.equalsIgnoreCase("Anotador");
    }

    // Método general para permisos de edición (opcional)
    public boolean puedeModificar() {
        return !esAnotador(); // Todos pueden modificar excepto anotadores
    }

    @Override
    public String toString() {
        return userName + " (" + tipo + ")";
    }
}