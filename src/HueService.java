import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Klasse zur Komunikation mit der HueBridge.
 * @author Georg Lang, Nicolas Lerch.
 * @version 24.11.2021.
 */
public class HueService {
    /*
    URL /api/<username>/lights
    Method GET
    Version 1.0
    Permission Whitelist
     */
    /**
     * URL zur Kommunikation über API.
     */
    //private static final String LIGHT_URL = "http://10.28.209.13:9001/api/3dc1d8f23e55321f3c049c03ac88dff/lights/";
    private static final String LIGHT_URL = "http://localhost:8000/api/newdeveloper/lights/";

    /**
     * Setzt das Licht des jeweiligen Spilers in der passenden Farbe.
     * @param player der Spieler.
     */
    public static void setPlayerLight(Player player) {
        String hueString = "\"hue\": ";
        switch (player.getLives()) {
            case 1 -> hueString += 0;
            case 2 -> hueString += 10000;
            case 3 -> hueString += 25000;
            default -> {
                hueString += 0;
                Blinker blinker = new Blinker(player);
                blinker.start();
            }
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(LIGHT_URL + player.getId() + "/state")).openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write("{\"on\": true, " + hueString + "}");
            osw.flush();
            osw.close();
            //Aufruf von "conn.getResponseCode()" ist hier nötig keineAhnung warum?!
            String responseCode = String.valueOf(conn.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schaltet Lampen aus.
     * @param index Index der Lampe.
     */
    public static void allLightsOff(int index) {
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(LIGHT_URL + index + "/state")).openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write("{\"on\": false}");
            osw.flush();
            osw.close();
            String responseCode = String.valueOf(conn.getResponseCode());
            //System.err.println(conn.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setzt das Licht des Verlierers ueber Klasse-Blinker.
     * @param toSend Message.
     * @param loser Spieler ohne Leben.
     */
    public static void setLoserLight(String toSend, Player loser) {
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(LIGHT_URL + loser.getId() + "/state")).openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(toSend);
            osw.flush();
            osw.close();
            String response = String.valueOf(conn.getResponseCode());
            //System.err.println(conn.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setzt das Licht des Gewinners auf 'colorloop'.
     * @param player Gewinner.
     */
    public static void setWinnerLight(Player player) {
        if (player != null) {
            try {
                HttpURLConnection conn = (HttpURLConnection) (new URL(LIGHT_URL + player.getId() + "/state")).openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write("{\"on\":true,\"bri\":122,\"effect\":\"colorloop\"}");
                osw.flush();
                osw.close();
                conn.getResponseCode();
                //System.err.println(conn.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Main zum testen.
     * @param args args.
     */
    public static void main(String[] args) {
        HueService.setWinnerLight(new Player("Name", 1, false));
        HueService.setWinnerLight(new Player("Name", 2, false));
        HueService.setWinnerLight(new Player("Name", 3, false));
    }
}
