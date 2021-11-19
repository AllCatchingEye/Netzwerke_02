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
                        if (line.contains("name")) {
                            String[] parts = line.split("=| ");
                            String name = parts[2];
                            System.out.println("Found name: " + name);
                            player = new Player(name, true);
                        } else if (line.contains("place1")) {
                            String[] parts = line.split("=|&| ");
                            String place1 = parts[2];
                            String place2 = parts[4];
                            System.out.println("Found place1: " + place1);
                            System.out.println("Found place2: " + place2);
                            player.setPlace1(new Place(place1));
                            player.setPlace2(new Place(place2));
                            WeatherService.existingCity(player.getPlace1());
                            WeatherService.existingCity(player.getPlace2());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    //}

    public static String getPlayerName(){
        return null;
    }

    public static String getPlace1(){
        return getPlace1();
    }

    public static String getPlace2(){
        return getPlace2();
    }


    public static void main(String[] args) {
        Remote remote = new Remote();
        remote.startServer();
    }
}
