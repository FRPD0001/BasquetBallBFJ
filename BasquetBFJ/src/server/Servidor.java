package server; // O el paquete donde tengas tu lógica principal

import java.io.*;
import java.net.*;

public class Servidor extends Thread {
    private static final int PUERTO = 7000;
    private static final String ARCHIVO_RESPALDO = "Serie_Nacional_Respaldo.DAT";

    public static void  main(String args[]) {
        ServerSocket sfd = null;
        try {
            sfd = new ServerSocket(PUERTO);
            System.out.println("Servidor de respaldo iniciado en puerto " + PUERTO);

            while (true) {
                try {
                    Socket nsfd = sfd.accept();
                    System.out.println("Conexión aceptada de: " + nsfd.getInetAddress());
                    
                    // Recibir el archivo
                    try (DataInputStream ois = new DataInputStream(nsfd.getInputStream());
                         DataOutputStream fos = new DataOutputStream(new FileOutputStream(ARCHIVO_RESPALDO))) {
                        
                        int unByte;
                        while ((unByte = ois.read()) != -1) {
                            fos.write(unByte);
                        }
                        System.out.println("Respaldo recibido y guardado como: " + ARCHIVO_RESPALDO);
                    }
                } catch (IOException ioe) {
                    System.out.println("Error en conexión: " + ioe.getMessage());
                }
            }
        } catch (IOException ioe) {
            System.out.println("Error al iniciar servidor: " + ioe.getMessage());
        } finally {
            if (sfd != null) {
                try {
                    sfd.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar servidor: " + e.getMessage());
                }
            }
        }
    }
}