package dracula_punch.Networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static String[] characters = {"Austin", "Amanda", "Ritta"};
    private static String[] adjs = {"The Brave", "The Wicked", "The Sorcerer"};
    private static final int PORT = 9090;

    // a way to store our clients
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        while(true) {
            if(clients.size() < 3) {
                System.out.println("[SERVER] Waiting for client conenction...");
            }
            Socket client = listener.accept();          // make the connection
            System.out.println("[SERVER] Connected to client: " + client);
            ClientHandler clientThread = new ClientHandler(client, clients); // creating possible multiple clients
            clients.add(clientThread);

            // ask executor to run
            pool.execute(clientThread);
        }
    }

    public static String selectRandomCharacter() {
        String character = characters[(int)(Math.random()*characters.length)];
        String adj = adjs[(int)(Math.random()*adjs.length)];
        return character + " " + adj;
    }
}

