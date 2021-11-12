/**
 * Guesselt Programm.
 * @author Georg Lang, Nicolas Lerch.
 * @version 11.11.2021.
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Guesselt {
    private int actualValue;
    public List<Player> players = new LinkedList<Player>();

    /**
     * Prueft ob ein Spieler keine Leben mehr hat.
     * @return true wenn ein Spieler keine Leben mehr hat, false sont.
     */
    public boolean someOneDead() {
        for (Player person : players) {
            if (person.getLives() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Webservice zur bestimmen der geographischen Lage der Stadte.
     * @return
     */
    private static boolean getCoordinates(Place place) {
        if (place.getName().equals("")) {
            return false;
        }
        // Wenn Ort nicht vorhanden print("Try again.")
        //TODO: Webservice Koordinaten
        return true;
    }

    /**
     * Setzt den Differenzwert des Spielers auf die Differenz zwischen dem
     * Temperaturunterschied der gewaehlten Orte und dem zufaelligen Wert.
     * @param person ein Spieler.
     */
    public void webWeather(Player person) {
        //TODO: Webservice Wetter
        int realTemp = 0;
        person.setDiff(realTemp);
    }
    /**
     * Setzt den Differenzwert des Spielers auf die Differenz der Route zwischen den
     * gewaehlten Orten und dem zufaelligen Wert.
     * @param person ein Spieler.
     */
    public void webRoute(Player person) {
        //TODO: Webservice Route
        int realDist = 0;
        person.setDiff(realDist);
    }

    /**
     * liest die gewaelten Staedte des Spielers ein
     * @param player Spieler am Zug.
     * @param scanner Scanner Objekt zum einlesen der Staedtenamen.
     */
    public static void getCity(Player player, Scanner scanner) {
        while (!getCoordinates(player.getPlace1())) {
            System.out.println(player.getName() + " type in the first place:");
            player.setPlace1(new Place(scanner.nextLine()));
        }
        while (!getCoordinates(player.getPlace2())) {
            System.out.println(player.getName() + " type in the second place:");
            player.setPlace2(new Place(scanner.nextLine()));
        }
    }

    /**
     * Bestimmt den Rundengewinner
     * @param list alle Spieler.
     * @return den Spieler, der gewonnen hat.
     */
    public Player checkPlayerForWin(List<Player> list) {
        for (Player person : list) {
            person.setPlace1(new Place(""));
            person.setPlace2(new Place(""));
        }
        return null;
    }

    private void setLamps(){
        final String LIGHT_URL = "http://localhost:9001/api/newdeveloper/lights/";

    }

    public void setBackCities() {
        for (Player player : players) {
            player.getPlace1().setName("");
            player.getPlace2().setName("");
        }
    }
}
