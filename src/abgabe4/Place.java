package abgabe4;

/**
 * Klasse zum Speichern von Orten mit zugehoereigen Daten.
 * @author Georg Lang, Nicolas Lerch.
 * @version 24.11.2021.
 */
public class Place {
    /**
     * Ortsname.
     */
    private String name;
    /**
     * Breitengrad.
     */
    private double lat;
    /**
     * Laengengrad.
     */
    private double lon;
    /**
     * aktuelle Temperatur.
     */
    private double temp;

    /**
     * Im Folgenden:
     * Getter & Setter der Attribute.
     */

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public Place(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
