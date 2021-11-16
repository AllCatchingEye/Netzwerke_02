public class Blinker extends Thread{
    public Player player;

    public Blinker(Player player) {
        this.player = player;
    }

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

    private void switchLight(String toSend, Player player) {
        HueService.setLoserLight(toSend, player);
    }
}
