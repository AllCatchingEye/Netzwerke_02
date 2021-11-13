import java.util.Random;
import java.util.Scanner;

public class PlayGuesselt {
    /**
     * Fuehrt eine Partie Gueasselt aus.
     * @param args Argumente.
     */
    public static void main(String[] args) {
        Guesselt guesselt = new Guesselt();
        HueService hue = new HueService();
        Scanner scanner = new Scanner(System.in);
        String tmp = "";
        int num = 1;
        while (tmp != null && guesselt.players.size() < 3) {
            System.out.println("Type in new player name (nothing for finished): ");
            tmp = scanner.nextLine();
            if (!tmp.equals("")) {
                guesselt.players.add(new Player(tmp, num));
                System.out.println("Welcome " + tmp + "!");
                num++;
            } else {
                if (guesselt.players.size() < 2) {
                    System.out.println("There have to be minimum two players.");
                } else {
                    tmp = null;
                }
            }
        }
        for (int i = 1; i <= 3; i++) {
            hue.allLightsOff(i);
        }
        for (Player player: guesselt.players) {
            hue.setPlayerLight(player);
        }
        System.out.println("Let the game begin!");
        Random random = new Random();
        while (!guesselt.someOneDead()) {
            /*System.out.println("**********************\nGuess Distance\n**********************\n");
            int randomDist = random.nextInt(990) + 10;
            System.out.println("The random distance is " + randomDist);
            for (Player person : guesselt.players) {
                System.out.println("It's your turn " + person.getName());
                guesselt.getCity(person, scanner);
            }
            //TODO: check what Player has the best Distance Guess
            guesselt.setBackCities();*/
            System.out.println("**********************\nGuess Temperature\n**********************\n");
            int randomTemp = random.nextInt(19) + 1;
            guesselt.actualValue = randomTemp;
            System.out.println("The random temperature is " + randomTemp);
            for (Player person : guesselt.players) {
                System.out.println("It's your turn " + person.getName());
                guesselt.getCity(person, scanner);
            }
            WeatherService weather = new WeatherService();
            for (Player player : guesselt.players) {
                weather.getWeather(player);
                System.out.println(player.getName() + ": " + player.getPlace1().getName() + " und " + player.getPlace2().getName());
                System.out.println(player.getDiff());
                guesselt.calcDiff(player);
            }
            Player winner = guesselt.checkPlayerForWin();
            System.out.println("Congratulations " + winner.getName() + " you won this round!");
            for (Player player : guesselt.players) {
                hue.setPlayerLight(player);
            }
            guesselt.setBackCities();
        }

    }
}
