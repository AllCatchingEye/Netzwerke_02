package abgabe4;

import javax.json.JsonObject;

/**
 * Klasse zur Ermittlung der Temperatur an einem bestimmten Ort.
 * @author Georg Lang, Nicolas Lerch.
 * @version 24.11.2021.
 * API key: '16a715ae23001ede6a0aba7c6d707daa'.
 */
public class WeatherService {
    /**
     * Prueft ob die gewaehlte Stadt existiert und setzt deren Temperatur.
     * @param place eine Stadt.
     * @return true wenn Stadt existiert, false sonst.
     */
    public static boolean existingCity(Place place){
        boolean worked = true;
        try {
            String anfrage = "https://api.openweathermap.org/data/2.5/weather?q=" + place.getName() + "&appid=16a715ae23001ede6a0aba7c6d707daa";
            JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);
            //jsonObj.entrySet().forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );

            JsonObject tempArray =  jsonObj.getJsonObject("main");
            JsonObject coordArray =  jsonObj.getJsonObject("coord");

            double lon = coordArray.getJsonNumber("lon").doubleValue();
            double lat = coordArray.getJsonNumber("lat").doubleValue();
            double kelvin = tempArray.getJsonNumber("temp").doubleValue();
            double celsius =  kelvin - 273.15;

            place.setLat(lat);
            place.setLon(lon);
            place.setTemp(celsius);
        } catch (Exception e) {
            System.out.println("Stadt existiert nicht.");
            worked = false;
        }
        return worked;
    }
}
