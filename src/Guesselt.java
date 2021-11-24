import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Hauptklasse mit allen Methoden fuer eine Partie Guesselt.
 * @author Georg Lang, Nicolas Lerch.
 * @version 24.11.2021.
 */

public class Guesselt {
    public int actualValue;
    public Player winner;
    public List<Player> players = new LinkedList<>();

    /**
     * Prueft ob ein Spieler keine Leben mehr hat.
     * @return true wenn ein Spieler keine Leben mehr hat, false sonst.
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
     * liest die gewaelten Staedte des Spielers ein.
     * @param players Liste der Spieler.
     * @param scanner Scanner Objekt zum einlesen der Staedtenamen.
     */
    public void getCity(List<Player> players, Scanner scanner, Remote remoteServer) {
        for (Player player : players) {
            if (player.getRemote()) {
                remoteServer.startServer();
            } else {
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
    }

    /**
     * Prueft ob die gewaehlte Statd schon ein anderer Spieler ausgesucht hat.
     * @param players alle Spieler.
     * @param city neue ausgewaelthe Stadt.
     * @return true falls die Stadt noch nicht belegt, sonst false.
     */
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
     * Bestimmt den Rundengewinner.
     * @return den Spieler, der gewonnen hat.
     */
    public Player checkPlayerForWin() {
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

    /**
     * Berechnet die Differenz zwischen den Spieler-Orts-Paaren.
     * @param player ein Spieler.
     */
    public void calcDiff(Player player) {
        double temp1 = player.getPlace1().getTemp();
        double temp2 = player.getPlace2().getTemp();
        double diff = Math.max(temp1, temp2) - Math.min(temp1, temp2);
        player.setDiff(diff);
    }

    /**
     * Setzt die Staedte aller Spieler zurueck.
     */
    public void setBackCities() {
        for (Player player : players) {
            player.getPlace1().setName("");
            player.getPlace2().setName("");
        }
    }

    /**
     * Setzt die neue Lichtfarbe und gibt den Gewinner der aktuellen Spielrunde aus.
     */
    public void setLightsNew() {
        Player winner = checkPlayerForWin();
        System.out.println("Congratulations " + winner.getName() + " you won this round!");
        for (Player player : players) {
            HueService.setPlayerLight(player);
        }
    }

    /**
     * Ermittelt den Gewinner des Spiels.
     */
    public void absoluteWinner() {
        Player absWinner = null;
        int count = 0;
        for (Player player : players) {
            if (player.getLives() == 0) {
                 count++;
            } else {
                absWinner = player;
            }
        }
        if (players.size() == 3 && count == 2) {
            winner = absWinner;
        } else if (players.size() == 2 && count == 1) {
            winner = absWinner;
        }
    }
}
