import javax.json.JsonObject;
import javax.json.JsonString;
import java.util.concurrent.atomic.AtomicReference;

/**
 * API key: '16a715ae23001ede6a0aba7c6d707daa'.
 */
public class WeatherService {
    public void getWeather(Player player) {
        String anfrage = "https://api.openweathermap.org/data/2.5/weather?q=" + player.getPlace1().getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
        JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);
        //jsonObj.entrySet().forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );
        //System.out.println("***************************************");
        JsonObject tempArray =  jsonObj.getJsonObject("main");
        //tempArray.entrySet().forEach(e -> System.out.println("key:" + e.getKey() + ", val=" + e.getValue() + "\n") );
        //System.out.println("***************************************");
        int temp = tempArray.getInt("temp");
        double celsius =  temp - 273.15;
        System.out.println("Temperature: " + celsius);

        String anfrage2 = "https://api.openweathermap.org/data/2.5/weather?q=" + player.getPlace2().getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
        JsonObject jsonObj2 = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage2);
        JsonObject tempArray2 =  jsonObj2.getJsonObject("main");
        int temp2 = tempArray2.getInt("temp");
        double celsius2 =  temp2 - 273.15;
        System.out.println("Temperature: " + celsius2);
        player.setDiff(celsius - celsius2);
    }
}
