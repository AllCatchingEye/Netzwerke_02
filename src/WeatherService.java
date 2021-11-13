import javax.json.JsonObject;
import javax.json.JsonString;
import java.util.concurrent.atomic.AtomicReference;

/**
 * API key: '16a715ae23001ede6a0aba7c6d707daa'.
 */
public class WeatherService {
    public void getWeather(Player player) {
        //TODO: hier keine exception mehr
        try {
        String anfrage = "https://api.openweathermap.org/data/2.5/weather?q=" + player.getPlace1().getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
        JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);
        //jsonObj.entrySet().forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );
        //System.out.println("***************************************");
        JsonObject tempArray =  jsonObj.getJsonObject("main");
        //tempArray.entrySet().forEach(e -> System.out.println("key:" + e.getKey() + ", val=" + e.getValue() + "\n") );
        //System.out.println("***************************************");
        //TODO: hier waere ein double toll!
        int temp = tempArray.getInt("temp");
        double celsius =  temp - 273.15;
        System.out.println("Temperature: " + celsius);
        player.getPlace1().setTemp(celsius);

        String anfrage2 = "https://api.openweathermap.org/data/2.5/weather?q=" + player.getPlace2().getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
        JsonObject jsonObj2 = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage2);
        JsonObject tempArray2 =  jsonObj2.getJsonObject("main");
        int temp2 = tempArray2.getInt("temp");
        double celsius2 =  temp2 - 273.15;
        System.out.println("Temperature: " + celsius2);
        player.getPlace2().setTemp(celsius2);
        } catch (Exception e) {
            System.out.println("Failure!");;
        }
    }
}
