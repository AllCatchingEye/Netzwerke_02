import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Remote {
    private boolean initialised = false;

    public void startServer() {
        while (true) {
            try (ServerSocket servSock = new ServerSocket(8082)) {
                System.out.println("Server started, waiting for clients...");
                try (Socket s = servSock.accept();
                     BufferedReader fromClient = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
                     BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"))) {
                    System.out.println("Got client connection!");

                    if (initialised) {
                        toClient.write("Type in your name: ");
                        toClient.flush();
                    } else {
                        toClient.write("Type in your City");
                        toClient.flush();
                    }

                    String RES = "";
                    for (String line = fromClient.readLine(); line != null
                            && line.length() > 0; line = fromClient.readLine()) {
                        if (line.contains("GET")) {
                            String[] parts = line.split(" ");
                            if (!parts[1].equals("/")) {
                                RES = parts[1];
                            }
                            System.out.println("Found GET: " + RES);
                        }
                        System.out.println("Client says: " + line);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPlayerName(){
        return null;
    }

    public static String getPlace1(){
        return null;
    }

    public static String getPlace2(){
        return null;
    }


    public static void main(String[] args) {
        Remote remote = new Remote();
        remote.startServer();
    }

}
