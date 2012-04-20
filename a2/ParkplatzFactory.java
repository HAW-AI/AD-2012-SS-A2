package a2;

/**Factoryklasse zum Erstellen des Parkplatzes
 */

public abstract class ParkplatzFactory {
    public static Parkplatz getParking(Leitsystem ls, Timer t) {
        Parkplatz p = new ParkplatzImpl(ls);
        t.addObserver(p);
        return p;
    }
}
