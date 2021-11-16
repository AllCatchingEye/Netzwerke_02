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
    public int actualValue;
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
     * liest die gewaelten Staedte des Spielers ein
     * @param player Spieler am Zug.
     * @param scanner Scanner Objekt zum einlesen der Staedtenamen.
     */
    public static void getCity(Player player, Scanner scanner) {
        while (player.getPlace1().getName().equals("") || !WeatherService.existingCity(player.getPlace1())) {
            System.out.println(player.getName() + " type in the first place:");
            player.setPlace1(new Place(scanner.nextLine()));
        }
        while (player.getPlace2().getName().equals("") || !WeatherService.existingCity(player.getPlace2())) {
            System.out.println(player.getName() + " type in the second place:");
            player.setPlace2(new Place(scanner.nextLine()));
        }
    }

    /**
     * Bestimmt den Rundengewinner
     * @return den Spieler, der gewonnen hat.
     */
    public Player checkPlayerForWin() {
        double minDist = 0.0;
        for (Player person : players) {
            person.setPlace1(new Place(""));
            person.setPlace2(new Place(""));
            person.setDiff(Math.abs(actualValue - person.getDiff()));
        }
        double minDiff = Math.min(players.get(0).getDiff(), players.get(1).getDiff());
        if (players.size() > 2) {
            minDiff = Math.min(minDiff, players.get(2).getDiff());
        }
        //Sehr haesslich :(
        Player retPlayer = null;
        for (Player e : players) {
            if (e.getDiff() == minDiff) {
                retPlayer = e;
            }
        }
        for (Player person : players) {
            if (!person.equals(retPlayer)) {
                person.setLives(person.getLives() - 1);
            }
        }
        return retPlayer;
    }

    public void calcDiff(Player player) {
        double temp1 = player.getPlace1().getTemp();
        double temp2 = player.getPlace2().getTemp();
        double diff = Math.max(temp1, temp2) - Math.min(temp1, temp2);
        player.setDiff(diff);
    }

    public void setBackCities() {
        for (Player player : players) {
            player.getPlace1().setName("");
            player.getPlace2().setName("");
        }
    }

    public Player absoluteWinner() {
        return null;
    }
}
