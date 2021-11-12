import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HueService {
    public class WetherService {
        private static void getWeather(Place place) {
            String anfrage = "/api/nikgeorg/lights/<id>/state";
            HttpURLConnection con;
            StringBuilder index = new StringBuilder();
            try {
                con = (HttpURLConnection) new URL(anfrage).openConnection();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        index.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(index);
        }
    }

    public static void main(String[] args) {

    }
}
