import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.json.*;
import java.io.*;

public class HueService {
    /*
    URL /api/<username>/lights
    Method GET
    Version 1.0
    Permission Whitelist
     */
    //private static final String LIGHT_URL = "http://10.28.209.13:9001/api/3dc1d8f23e55321f3c049c03ac88dff/lights/";
    private static final String LIGHT_URL = "http://localhost:8000/api/newdeveloper/lights/";

    public static JsonObject setPlayerLight(Player player) {
        String hueString = "\"hue\": ";
        switch (player.getLives()) {
            case 1: {
                hueString += 0;
                break;
            }
            case 2: {
                hueString += 10000;
                break;
            }
            case 3: {
                hueString += 25000;
                break;
            }
            default: {
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
            //System.err.println(conn.getResponseCode());
            /*try( JsonReader jsonRdr = Json.createReader( (InputStream) conn.getContent() ) ) {
                return jsonRdr.readObject();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObject allLightsOff(int index) {
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
        return null;
    }

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
                //osw.write("{\"on\":true,\"bri\":122,\"hue\": 12000}");
                //osw.write("\"on\": false");
                osw.flush();
                osw.close();
                System.err.println(conn.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        HueService.setWinnerLight(new Player("Name", 1, false));
        HueService.setWinnerLight(new Player("Name", 2, false));
        HueService.setWinnerLight(new Player("Name", 3, false));
    }
}
