/**
 * Klasse zum Speichern von Spielern mit zugehoereigen Daten.
 * @author Georg Lang, Nicolas Lerch.
 * @version 24.11.2021.
 */
public class Player {
    /**
     * Anzahl Leben.
     */
    private int lives = 3;
    /**
     * ID-Nummer des Spielers.
     */
    private int id;
    /**
     * Name des Spielers.
     */
    private String name;
    /**
     * erster Ort des Spielers.
     */
    private Place place1 = new Place("");
    /**
     * zweiter Ort des Spielers.
     */
    private Place place2 = new Place("");
    /**
     * Differenzwert des Spielers, zur ermittlung eines Gewinners gespeichert.
     */
    private double diff;
    /**
     * Information ob Spieler remote spielt.
     */
    private final boolean remote;

    /**
     * Konstruktor fuer einen Spieler
     * @param name Name des Spielers.
     * @param identifier ID-Nummer des Spielers.
     * @param remote ist Spieler remote.
     */
    public Player(String name, int identifier, boolean remote) {
        this.name = name;
        this.id = identifier;
        this.remote = remote;
    }

    /**
     * Konstruktor fuer einen Spieler
     * @param name Name des Spielers.
     * @param remote ist Spieler remote.
     */
    public Player(String name, boolean remote) {
        this.name = name;
        this.remote = remote;
    }

    /**
     * Im Folgenden:
     * Getter & Setter der Attribute.
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

