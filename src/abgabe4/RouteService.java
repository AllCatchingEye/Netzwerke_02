package abgabe4;

import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Klasse fuer GeoServeice.
 * @author Georg Lang, Nicolas Lerch.
 * @version 24.11.2021.
 * API Key: API Key: jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY.
 */

public class RouteService {

    /**
     * Ermittelt die Laenge der Route zwischen den Orten des Spielers.
     * @param player der Spieler.
     */
    public static void getRoute(Player player) {
        String origin = player.getPlace1().getLat() + "," + player.getPlace1().getLon();
        String destination = player.getPlace2().getLat() + "," + player.getPlace2().getLon();
        String url = "https://router.hereapi.com/v8/routes?transportMode=car&origin=" + origin
                + "&destination=" + destination + "&return=summary&apiKey=jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY";

        try {
            JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(url);
            //jsonObj.entrySet().forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );
            JsonArray routesArray = jsonObj.getJsonArray("routes");
            JsonObject routesObject = routesArray.getJsonObject(0);
            JsonArray sectionsArray = routesObject.getJsonArray("sections");
            JsonObject sectionsObject = sectionsArray.getJsonObject(0);
            JsonObject summeryObject = sectionsObject.getJsonObject("summary");
            int length = summeryObject.getJsonNumber("length").intValue();
            player.setDiff((double)length / 1000);
            System.out.println("Laenge: " + length);

        } catch (Exception e) {
            System.out.println("Route Failure!");
        }
    }

    /**
     * Eine einfache Main zum testen.
     * @param args args.
     */
    public static void main(String[] args) {
        Player player =  new Player("Name", 1, false);
        player.setPlace1(new Place("Muenchen"));
        player.setPlace2(new Place("Berlin"));
        WeatherService.existingCity(player.getPlace1());
        WeatherService.existingCity(player.getPlace2());
        RouteService.getRoute(player);
    }
}
