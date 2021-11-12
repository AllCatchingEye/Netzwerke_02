public class Player {
    private int lives = 3;
    private final String name;
    private Place place1;
    private Place place2;
    private int diff;

    public Place getPlace1() {
        return place1;
    }

    public void setPlace1(Place place1) {
        this.place1 = place1;
    }

    public Place getPlace2() {
        return place2;
    }

    public void setPlace2(Place place2) {
        this.place2 = place2;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public Player(String name) {
        this.name = name;
    }

    public int getLives() {
        return lives;
    }

    public String getName() {
        return name;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}

