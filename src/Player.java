public class Player {
    private int lives = 3;
    private int id;
    private String name;
    private Place place1 = new Place("");
    private Place place2 = new Place("");
    private double diff;
    private boolean remote;

    public Player(String name, int identifier, boolean remote) {
        this.name = name;
        this.id = identifier;
        this.remote = remote;
    }

    public Place getPlace1() {
        return place1;
    }

    public int getId() {
        return id;
    }

    public void setPlace1(Place pla1) {
        place1 = pla1;
    }

    public Place getPlace2() {
        return place2;
    }

    public void setPlace2(Place pla2) {
        place2 = pla2;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
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

    public boolean getRemote(){ return remote; }
}

