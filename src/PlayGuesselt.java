import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class PlayGuesselt {
    /**
     * Fuehrt eine Partie Gueasselt aus.
     * @param args Argumente.
     */
    public static void main(String[] args) {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("java -jar /Users/nicolaslerch/IdeaProjects/abgabe2-grp2-02/Hue-Emulator-master/HueEmulator-v0.8.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Guesselt guesselt = new Guesselt();
        HueService hue = new HueService();
        Scanner scanner = new Scanner(System.in);
        String input = "";
        int numPlayers = 1;
        Remote remoteServer = new Remote();
        // Anmeldung der Spieler. Mindestens zwei und maximal drei.
        while (input != null && guesselt.players.size() < 3) {
            System.out.println("Do you want to play remote? (y/n) (q for quit): ");
            String wantRemote = scanner.nextLine();
            if (wantRemote.equals("n")) {
                System.out.println("Type in new player name: ");
                input = scanner.nextLine();
                if (!input.equals("")) {
                    guesselt.players.add(new Player(input, numPlayers, false));
                }
                numPlayers++;
            } else if (wantRemote.equals("q")){
                input = null;
            } else {
                remoteServer.startServer();
                Player remotePlayer = Remote.getPlayer();
                remotePlayer.setId(numPlayers);
                guesselt.players.add(remotePlayer);
                input = remotePlayer.getName();
                numPlayers++;
            }
            checkPlayerCount(input, numPlayers, guesselt);
        }
        // Schalte alle Lichter aus.
        for (int i = 1; i <= 3; i++) {
            hue.allLightsOff(i);
        }
        // Schalte Lichter fuer angemeldete Spieler ein
        for (Player player: guesselt.players) {
            hue.setPlayerLight(player);
        }

        System.out.println("Let the game begin!");
        Random random = new Random();
        Player winner = null;
        int switcher = 1;
        while (guesselt.winner == null) {
            if (switcher%2 == 1) {
                playDistance(random, guesselt, scanner, remoteServer);
            } else {
                playTemperature(random, guesselt, scanner, remoteServer);
            }
            for (Player player : guesselt.players) {
                System.out.println(player.getName() + ": " + player.getPlace1().getName() + " und " + player.getPlace2().getName());
                System.out.println(player.getDiff());
                RouteService.getRoute(player);
            }
            switcher++;
            guesselt.setLightsNew();
            guesselt.setBackCities();
            guesselt.absoluteWinner();
        }
        HueService.setWinnerLight(guesselt.winner);
        String end = scanner.nextLine();
        if (end != null) {
            proc.destroy();
            System.exit(0);
        }
    }

    private static void playTemperature(Random random, Guesselt guesselt, Scanner scanner, Remote remoteServer) {
        System.out.println("**********************\nGuess Temperature\n**********************\n");
        int randomTemp = random.nextInt(19) + 1;
        guesselt.actualValue = randomTemp;
        System.out.println("The random temperature is " + randomTemp);
        guesselt.getCity(guesselt.players, scanner, remoteServer);
    }

    private static void playDistance(Random random, Guesselt guesselt, Scanner scanner, Remote remoteServer) {
        System.out.println("**********************\nGuess Distance\n**********************\n");
        int randomDist = random.nextInt(990) + 10;
        System.out.println("The random distance is " + randomDist);
        guesselt.getCity(guesselt.players, scanner, remoteServer);
    }

    private static void checkPlayerCount(String name, int num, Guesselt guesselt) {
        if (name != null && !name.equals("")) {
            System.out.println("Welcome " + name + "!");
            num++;
        } else {
            if (guesselt.players.size() < 2) {
                System.out.println("There have to be minimum two players.");
            } else {
                name = null;
            }
        }
    }
}
