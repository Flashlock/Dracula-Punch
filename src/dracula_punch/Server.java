package dracula_punch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        System.out.println("[SERVER] Waiting for client conenction...");
        while(true) {
            Socket client = listener.accept();          // make the connection
            System.out.println("[SERVER] Connected to client: " + client);
            ClientHandler clientThread = new ClientHandler(client); // creating possible multiple clients
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

