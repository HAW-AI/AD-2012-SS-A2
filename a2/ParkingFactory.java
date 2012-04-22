package a2;

/**Factoryklasse zum Erstellen des Parkplatzes
 */

public abstract class ParkingFactory {
    public static Parking getParking(Controlsystem ls, Timer t) {
        Parking p = new ParkplatzImpl(ls);
        t.addObserver(p);
        return p;
    }
}
