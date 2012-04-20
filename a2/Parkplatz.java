package a2;

import java.util.*;

public interface Parkplatz extends Observer {
    public void parkCar(Auto auto);
    public int getParkingCars(); //Gibt nur die Anzahl der parkenden Autos zurück. Ausfahrende Autos werden nicht gezählt
}

/** Package-Private implementierung des Parkplatzes
 * 
 */
class ParkplatzImpl implements Parkplatz {
    private Map<Integer, Set<Auto>> leaveMap;
    private final Leitsystem ls;
    
    ParkplatzImpl(Leitsystem ls) {
        leaveMap = new HashMap<>();
        this.ls = ls;
    }
    
    @Override
    public void parkCar(Auto auto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Observable o, Object arg) {
        
    }

    @Override
    public int getParkingCars() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}