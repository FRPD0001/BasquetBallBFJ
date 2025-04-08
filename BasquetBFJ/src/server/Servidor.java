package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{
    private static final int PUERTO = 7001;
    private static final String ARCHIVO_RESPALDO = "Serie_Nacional.DAT";

    public static void main(String[] args) {
        System.out.println("Servidor de respaldo simplificado iniciado...");
        System.out.println("Esperando conexiones en el puerto " + PUERTO);
        
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                    
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                    
                    // Leer solicitud del cliente
                    String solicitud = ois.readUTF();
                    
                    if ("SOLICITAR_RESPALDO".equals(solicitud)) {
                        enviarRespaldo(oos);
                    } else {
                        oos.writeUTF("Comando no reconocido. Use 'SOLICITAR_RESPALDO'");
                    }
                    
                } catch (IOException e) {
                    System.err.println("Error en la conexión con el cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    private static void enviarRespaldo(ObjectOutputStream oos) throws IOException {
        File archivo = new File(ARCHIVO_RESPALDO);
        
        if (!archivo.exists()) {
            oos.writeUTF("ERROR: Archivo de respaldo no encontrado");
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(archivo);
             ObjectInputStream fileIn = new ObjectInputStream(fis)) {
            
            Object datos = fileIn.readObject();
            oos.writeUTF("RESPALDO_OK");
            oos.writeObject(datos);
            System.out.println("Respaldo enviado exitosamente");
            
        } catch (ClassNotFoundException e) {
            oos.writeUTF("ERROR: Formato de archivo inválido");
        } catch (IOException e) {
            oos.writeUTF("ERROR: No se pudo leer el archivo de respaldo");
        }
    }
}