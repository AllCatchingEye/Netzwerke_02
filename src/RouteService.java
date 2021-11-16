import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Klasse fuer GeoServeice.
 * @author Georg Lang, Nicolas Lerch.
 * @version 11.11.2021.
 * API Key: API Key: jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY.
 */
public class RouteService {

    public static void getRoute(Player player) {
        String origin = player.getPlace1().getLat() + "," + player.getPlace1().getLon();
        String destination = player.getPlace2().getLat() + "," + player.getPlace2().getLon();
        String url = "https://router.hereapi.com/v8/routes?transportMode=car&origin=" + origin
                + "&destination=" + destination + "&return=summary&apiKey=jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY";

        try {
            JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(url);
            jsonObj.entrySet().forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );
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

    public RouteService(){

    }

    /*public static void getCoordinates(Place place) {
        String anfrage = "https://geocode.search.hereapi.com/v1/geocode?q=" + place.getName() + "&apiKey=jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY";
        String response = """
                key=items, val=[{"title":"M?nchen, Bayern, Deutschland","id":"here:cm:namedplace:20177269","resultType":"locality","localityType":"city",
                "address":{"label":"M?nchen, Bayern, Deutschland","countryCode":"DEU","countryName":"Deutschland","stateCode":"BY","state":"Bayern",
                "countyCode":"M","county":"M?nchen (Stadt)","city":"M?nchen","postalCode":"80331"},"position":{"lat":48.13641,"lng":11.57754},
                "mapView":{"west":11.36084,"south":48.06175,"east":11.72291,"north":48.24824},
                "scoring":{"queryScore":1.0,"fieldScore":{"city":1.0}}}]
        """;
        try {
            JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(anfrage);
        } catch (Exception e) {
            System.out.println("Route Failure!");
        }
    }*/

    public static void main(String[] args) {
        Player player =  new Player("Name", 1);
        player.setPlace1(new Place("M?nchen"));
        player.setPlace2(new Place("Wien"));
        WeatherService.existingCity(player.getPlace1());
        WeatherService.existingCity(player.getPlace2());
        RouteService.getRoute(player);
    }
}
