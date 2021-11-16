import javax.json.JsonNumber;
import javax.json.JsonObject;

/**
 * API key: '16a715ae23001ede6a0aba7c6d707daa'.
 */
public class WeatherService {
    public static boolean existingCity(Place place){
        boolean worked = true;
        try {
            String anfrage = "https://api.openweathermap.org/data/2.5/weather?q=" + place.getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
            JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);

            JsonObject tempArray =  jsonObj.getJsonObject("main");
            JsonObject coordArray =  jsonObj.getJsonObject("coord");

            double lon = coordArray.getJsonNumber("lon").doubleValue();
            double lat = coordArray.getJsonNumber("lat").doubleValue();
            double kelvin = tempArray.getJsonNumber("temp").doubleValue();
            double celsius =  kelvin - 273.15;

            place.setxVal(lon);
            place.setyVal(lat);
            place.setTemp(celsius);
        } catch (Exception e) {
            System.out.println("Stadt existiert nicht.");
            worked = false;
        }
        return worked;
    }
}
