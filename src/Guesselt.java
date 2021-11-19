/**
 * Guesselt Programm.
 *
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
     * @param players Liste der Spieler.
     * @param scanner Scanner Objekt zum einlesen der Staedtenamen.
     */
    public static void getCity(List<Player> players, Scanner scanner) {
        for (Player player : players) {
            System.out.println("It's your turn " + player.getName());
            while (player.getPlace1().getName().equals("") || !WeatherService.existingCity(player.getPlace1())) {
                System.out.println(player.getName() + " type in the first place:");
                Place newCity = new Place(scanner.nextLine());
                if (checkCityChosen(players, newCity)) {
                    player.setPlace1(newCity);
                }
            }
            while (player.getPlace2().getName().equals("") || !WeatherService.existingCity(player.getPlace2())) {
                System.out.println(player.getName() + " type in the second place:");
                Place newCity = new Place(scanner.nextLine());
                if (checkCityChosen(players, newCity)) {
                    player.setPlace2(newCity);
                }
            }
        }
    }

    private static boolean checkCityChosen(List<Player> players, Place city) {
        for (Player player : players) {
            if (player.getPlace1().getName().equals(city.getName()) ||
                    player.getPlace2().getName().equals(city.getName())) {
                System.out.println("Another player already selected this city.");
                return false;
            }
        }
        return true;
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

    public void setLightsNew() {
        Player winner = checkPlayerForWin();
        System.out.println("Congratulations " + winner.getName() + " you won this round!");
        for (Player player : players) {
            HueService.setPlayerLight(player);
        }
    }

    public Player absoluteWinner() {
        return null;
    }
}
