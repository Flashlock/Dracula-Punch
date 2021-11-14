package dracula_punch.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP =  "24.22.56.165"; // "24.22.56.165"; public address for desktop at home
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

        ServerConnection serverConnection = new ServerConnection(socket);
        // We will probably not use BufferedReader, but keyboard inputs to send to server.
        // I used bufferedReader for the tutorial and understand how sockets work.
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // flush sockets = true

        // we don't want a pool of threads, just multiple single threads
        new Thread(serverConnection).start();


        while(true) {
            System.out.print("> ");
            String command = keyboard.readLine();

            if(command.equals("quit")){ break; }

            out.println(command);
        }
        socket.close();
        System.exit(0);
    }
}
