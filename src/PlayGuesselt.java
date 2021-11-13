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
            System.out.println("The random temperature is " + randomTemp);
            for (Player person : guesselt.players) {
                System.out.println("It's your turn " + person.getName());
                guesselt.getCity(person, scanner);
            }
            //TODO: check what Player has the best Tamperature Guess
            WeatherService weather = new WeatherService();
            weather.getWeather(guesselt.players.get(0));
            System.out.println(guesselt.players.get(0).getDiff());
            System.out.println(guesselt.players.get(0).getName() + ": " + guesselt.players.get(0).getPlace1().getName() + " und " + guesselt.players.get(0).getPlace2().getName());
            guesselt.setBackCities();
        }
    }
}
