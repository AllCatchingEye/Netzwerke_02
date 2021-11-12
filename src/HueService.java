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

    public JsonObject setPlayerLight(Player player) {

        // Convert RGB to HSB


        String hueString = "\"hue\": ";
        switch (player.getLives()) {
            case 1: {
//                https://examples.javacodegeeks.com/desktop-java/awt/rgb-to-hsb-and-vice-versa-color-conversion/
//                int red = 255;
//                int green = 0;
//                int blue = 0;
//                float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                hueString += 0;
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                hueString += 25000;
                break;
            }
            default: {
                hueString += 0 + ", \"effect\": colorloop";
            };
        }

        try {
            String LIGHT_URL = "http://localhost:8000/api/newdeveloper/lights/";
            HttpURLConnection conn = (HttpURLConnection) (new URL(LIGHT_URL + player.getId() + "/state")).openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write("{\"on\": true, " + hueString + "}");
            osw.flush();
            osw.close();
            System.err.println(conn.getResponseCode());
            /*try( JsonReader jsonRdr = Json.createReader( (InputStream) conn.getContent() ) ) {
                return jsonRdr.readObject();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObject allLightsOff(int index) {
        String LIGHT_URL = "http://localhost:8000/api/newdeveloper/lights/";
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
            System.err.println(conn.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
