import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class GuesseltClient {
    private static boolean runningGame = true;
    private static boolean initialised = false;

    private static boolean openConnection(String toSend) {
        System.out.println("trying connection...");
        boolean newRunningGame = true;
        HttpURLConnection con;
        StringBuilder index = new StringBuilder();
        try {
            con = (HttpURLConnection) new URL(toSend).openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    index.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newRunningGame;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You are remote player. Wait your teammates to give you instruction.");
        while (runningGame) {
            String toSend = "http://localhost:8082/";
            if (!initialised) {
                System.out.println("Type in your name: ");
                String name = scanner.nextLine();
                toSend += "name=" + name;
                runningGame = openConnection(toSend);
                if (runningGame) {
                    initialised = true;
                }
            } else {
                System.out.println("Type in your first city: ");
                String place1 = scanner.nextLine();
                System.out.println("Type in your second city: ");
                String place2 = scanner.nextLine();
                toSend += "place1=" + place1 + "&place2=" + place2;
                runningGame = openConnection(toSend);
            }
        }
    }
}
