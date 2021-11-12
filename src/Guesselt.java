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

    /**
     * Prueft ob ein Spieler keine Leben mehr hat.
     * @param players Liste von Spielern.
     * @return true wenn ein Spieler keine Leben mehr hat, false sont.
     */
    private static boolean someOneDead(List<Player> players) {
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
    private static boolean getCoordinates(Player person) {
        if (person.getPlace1().getName().equals("") || person.getPlace2().getName().equals("")) {
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
    private void webWeather(Player person) {
        //TODO: Webservice Wetter
        int realTemp = 0;
        person.setDiff(realTemp);
    }
    /**
     * Setzt den Differenzwert des Spielers auf die Differenz der Route zwischen den
     * gewaehlten Orten und dem zufaelligen Wert.
     * @param person ein Spieler.
     */
    private void webRoute(Player person) {
        //TODO: Webservice Route
        int realDist = 0;
        person.setDiff(realDist);
    }

    /**
     * liest die gewaelten Staedte des Spielers ein
     * @param player Spieler am Zug.
     * @param scanner Scanner Objekt zum einlesen der Staedtenamen.
     */
    private static void getCity(Player player, Scanner scanner) {
        while (!getCoordinates(player)) {
            System.out.println(player.getName() + " type in the first place:");
            player.setPlace1(new Place(scanner.nextLine()));
        }
        while (!getCoordinates(player)) {
            System.out.println(player.getName() + " type in the second place:");
            player.setPlace2(new Place(scanner.nextLine()));
        }
    }

    /**
     * Bestimmt den Rundengewinner
     * @param list alle Spieler.
     * @return den Spieler, der gewonnen hat.
     */
    private Player checkPlayerForWin(List<Player> list) {
        //TODO: Bestimmen welcher Spieler die Runde geqwonnen hat
        for (Player person : list) {
            person.setPlace1(new Place(""));
            person.setPlace2(new Place(""));
        }
        return null;
    }

    private void setLamps(){
        final String LIGHT_URL = "http://localhost:9001/api/newdeveloper/lights/";

    }

    /**
     * Fuehrt eine Partie Gueasselt aus.
     * @param args Argumente.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Player> players = new LinkedList<>();
        String tmp = "";
        while (tmp != null) {
            System.out.println("Type in new player name (nothing for finished): ");
            tmp = scanner.nextLine();
            if (!tmp.equals("")) {
                players.add(new Player(tmp));
                System.out.println("Welcome " + tmp + "!");
            } else {
                tmp = null;
            }
        }
        System.out.println("Let the game begin!");
        Random random = new Random();
        while (!someOneDead(players)) {
            System.out.println("**********************\nGuess Distance\n**********************\n");
            int randomDist = random.nextInt(990) + 10;
            System.out.println("The random distance is" + randomDist);
            for (Player person : players) {
                System.out.println("It's your turn " + person.getName());
                getCity(person, scanner);
            }
            //TODO: check what Player has the best Distance Guess
            System.out.println("**********************\nGuess Temperature\n**********************\n");
            int randomTemp = random.nextInt(19) + 1;
            System.out.println("The random temperature is " + randomTemp);
            for (Player person : players) {
                System.out.println("It's your turn " + person.getName());
                getCity(person, scanner);
            }
            //TODO: check what Player has the best Tamperature Guess
        }
    }
}
