package dracula_punch.Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Client();
    }

    public Client() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", Server.PORT);

        // Create I/O streams
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

        Packet packet = new Packet("Hello");
        outStream.writeObject(packet);

        Packet recPacket = (Packet) inStream.readObject();
        System.out.println(recPacket.message);

        socket.close();
    }
}
