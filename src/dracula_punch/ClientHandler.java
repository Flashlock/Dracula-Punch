package dracula_punch;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

/*
* This class is to handle multiple clients. It will run the process to accept clients on the server side
* And will be the middle man communicator from the client to the server and vice versa.
* */
public class ClientHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run(){
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            while (true) {
                String request = in.readLine();
                if (request.contains("character")) {
                    System.out.println("[SERVER] Sent character to" + client);
                    out.println(Server.selectRandomCharacter());
                } else {
                    out.println("Type 'character' to get a random character");
                }
            }
        } catch (IOException e) {
            System.err.println("IO exception in client handler");
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
