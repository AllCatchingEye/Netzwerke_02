package abgabe4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Klasse fuer einen RemmotePlayer zum Uebermitteln des Namens und der STaedte.
 * @author Georg Lang, Nicolas Lerch.
 * @version 24.11.2021.
 */
public class GuesseltClient {
    /**
     * Information ob das Spiel noch laeuft.
     */
    private static boolean runningGame = true;
    /**
     * Information ob bereits ein Name abgefragt wurde.
     */
    private static boolean initialised = false;

    /**
     * Oeffnet eine Verbindung zum Spiel und uebermittelt die benoetigten Daten.
     * @param toSend Verbindungs URL.
     * @return true wenn der Verbindungsaufbau positiv verlaufen ist, false sonst.
     */
    private static boolean openConnection(String toSend) {
        System.out.println("trying connection...");
        boolean newRunningGame = true;
        HttpURLConnection con;
        StringBuilder index = new StringBuilder();
        try {
            con = (HttpURLConnection) new URL(toSend).openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    index.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newRunningGame;
    }

    /**
     * Main zum testen.
     * @param args args.
     */
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
