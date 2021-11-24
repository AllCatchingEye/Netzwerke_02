/**
 * Klasse Blinker, fuer das Blinken der Spielerlampen.
 * @author Nicolas Lerch.
 * @version 24.11.2021.
 */
public class Blinker extends Thread{
    /**
     * Spieler ohne Leben.
     */
    public Player player;

    /**
     * Konstruktor fuer den Blinker.
     * @param player Spieler ohne Leben
     */
    public Blinker(Player player) {
        this.player = player;
    }

    /**
     * Override Methode run.
     */
    @Override public void run(){
        System.out.println(Thread.currentThread());
        try {
            while(!interrupted()) {
                switchLight("{\"on\": true, \"hue\": 0}", player);
                sleep(1000);
                switchLight("{\"on\": false}", player);
                sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("{\"on\": false}");
    }

    /**
     * Komunikation ueber den HueService.
     * @param toSend Message.
     * @param player Spieler, dessen Lampe gesetzt werden soll.
     */
    private void switchLight(String toSend, Player player) {
        HueService.setLoserLight(toSend, player);
    }
}
