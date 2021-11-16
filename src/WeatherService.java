import javax.json.JsonNumber;
import javax.json.JsonObject;

/**
 * API key: '16a715ae23001ede6a0aba7c6d707daa'.
 */
public class WeatherService {
    /*public void getWeather(Player player) {

        try {

        } catch (Exception e) {
            System.out.println("Failure!");
        }
    }*/

/*    public void foo(Place place){
        String anfrage = "https://api.openweathermap.org/data/2.5/weather?q="
                + place.getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
        JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);
        jsonObj.entrySet().
                forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );
        JsonObject tempArray =  jsonObj.getJsonObject("main");


        double temp = Double.parseDouble(tempArray.getString("temp"));
        double celsius =  temp - 273.15;
        System.out.println("Temperature: " + celsius);
        place.setTemp(celsius);
    }*/

    public static boolean existingCity(Place place){
        boolean worked = true;
        try {
            String anfrage = "https://api.openweathermap.org/data/2.5/weather?q=" + place.getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
            JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);
            jsonObj.entrySet().forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );
            JsonObject tempArray =  jsonObj.getJsonObject("main");
            JsonObject coordArray =  jsonObj.getJsonObject("coord");

            JsonNumber lonJson = coordArray.getJsonNumber("lon");
            double lon = lonJson.doubleValue();
            JsonNumber latJson = coordArray.getJsonNumber("lat");
            double lat = latJson.doubleValue();
            JsonNumber tempJson = tempArray.getJsonNumber("temp");
            double temp = tempJson.doubleValue();

            place.setxVal(lon);
            place.setyVal(lat);
            double celsius =  temp - 273.15;
            place.setTemp(celsius);
        } catch (Exception e) {
            System.out.println("Stadt existiert nicht.");
            worked = false;
        }
        return worked;
    }

    public static void main(String[] args) {
        WeatherService weather = new WeatherService();
        weather.existingCity(new Place("Muenchen"));
    }
}
