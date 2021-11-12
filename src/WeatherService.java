import com.sun.tools.javac.Main;

import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * API key: '16a715ae23001ede6a0aba7c6d707daa'.
 */
public class WeatherService {
    public void getWeather(Place place) {
        String city = "Paris"; //TODO: can#t access parameter place Why?!
        String anfrage = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=16a715ae23001ede6a0aba7c6d707daa";
        HttpURLConnection con;
        StringBuilder index = new StringBuilder();
        try {
            con = (HttpURLConnection) new URL(anfrage).openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                /*for (String line = br.readLine(); line != null; line = br.readLine()) {
                    index.append(line);
                }*/
                JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(place.getName() + ":");
        System.out.println(index);
    }
}
