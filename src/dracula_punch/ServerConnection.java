package dracula_punch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable{
    private Socket server;
    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection(Socket s) throws IOException {
        server=s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));    // only responsible for handling input for the server
    }


    @Override
    public void run() {
            try {
                while(true) {
                    String serverResponse = in.readLine();
                    if(serverResponse == null){ break; }
                    System.out.println("Server says: " + serverResponse);
                    System.out.print("> ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
