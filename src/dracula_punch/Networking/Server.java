package dracula_punch.Networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static int PORT = 4999;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Server();
    }

    public Server() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();

        // Create I/O streams
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

        Packet recPacket = (Packet) inStream.readObject();

        System.out.println(recPacket.message);
        Packet packet = new Packet("Hi from Server");
        outStream.writeObject(packet);

        socket.close();
        serverSocket.close();
    }
}
