import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

/**
 * Klasse fuer GeoServeice.
 * @author Georg Lang, Nicolas Lerch.
 * @version 11.11.2021.
 * API Key: API Key: jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY.
 */
public class RouteService {

    public static void getRoute(Player player) {
        String origin = player.getPlace1().getLat() + "," + player.getPlace2().getLon();
        String destination = player.getPlace2().getLat() + "," + player.getPlace2().getLon();
        String url = "https://router.hereapi.com/v8/routes?transportMode=car&origin=" + origin
                + "&destination=" + destination + "&return=summary&apiKey=jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY";

        try {
            JsonObject jsonObj = JsonObjectFromUrlUtil.getJsonObjectFromUrl(url);
            jsonObj.entrySet().forEach( e -> System.out.println( "key=" + e.getKey() + ", val=" + e.getValue() + "\n" ) );
            JsonArray routeArray = jsonObj.getJsonArray("items");

        } catch (Exception e) {
            System.out.println("Route Failure!");
        }
    }

    public RouteService(){

    }

    public void getCoordinates(Place place) {
        String anfrage = "https://geocode.search.hereapi.com/v1/geocode?q=" + place.getName() + "&apiKey=jDBuS4GoWg4hNum0o5Jb25l5FDJTpd-S7Ku_MbYSPuY";
        HttpURLConnection con;
        StringBuilder index = new StringBuilder();
        try {
            con = (HttpURLConnection) new URL(anfrage).openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                //TODO:
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
        RouteService route = new RouteService();
        route.getCoordinates(new Place("Wolfsburg"));
    }
}
