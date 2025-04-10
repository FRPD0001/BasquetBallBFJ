package logico;

import java.io.Serializable;

public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String tipo;  
    private String userName;
    private String pass;
    
    public User(String tipo, String userName, String pass) {
        if (!tipo.equalsIgnoreCase("Administrador") &&  
            !tipo.equalsIgnoreCase("Anotador")) {
            throw new IllegalArgumentException("Tipo de usuario no válido: " + tipo);
        }
        this.tipo = tipo;
        this.userName = userName;
        this.pass = pass;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    public boolean esAdministrador() {
        return tipo.equalsIgnoreCase("Administrador");
    }


    public boolean esAnotador() {
        return tipo.equalsIgnoreCase("Anotador");
    }

    public boolean puedeModificar() {
        return !esAnotador(); 
    }

    @Override
    public String toString() {
        return userName + " (" + tipo + ")";
    }
}