package Net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static final int PORT = 8080;
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>(); // список всех нитей - экземпляров
    // сервера, слушающих каждый своего клиента
    public static int ID = 0;

    /**
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server Started");
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                checkSockets();
                Socket socket = server.accept();
                try {
                    ID++;
                    serverList.add(new ServerSomthing(socket));// добавить новое соединенние в список
                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    public static int getNewID() { return ID; }

    private static void checkSockets() {
        for(ServerSomthing vr : serverList) {
            if(!vr.getSocket().isConnected() || vr.isInterrupted()) {
                vr.interrupt();
                serverList.remove(vr);
            }
        }
    }

    public static void removeSocket(Socket socket) {
        for (ServerSomthing vr : serverList) {
            if (vr.getSocket().equals(socket)) {
                vr.interrupt();
                serverList.remove(vr);
            }
        }
    }
}