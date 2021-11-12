import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * API key: '16a715ae23001ede6a0aba7c6d707daa'.
 */
public class WetherService {
    private static void getWeather(Place place) {
        String anfrage = "https://api.openweathermap.org/data/2.5/weather?q="+ place.getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
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

    public static void main(String[] args) {
        Place place = new Place("Muenchen");
        getWeather(place);
        System.out.println("finish");
    }
}
