package a2;

import java.util.*;
import java.util.Map.Entry;

public interface Parkplatz extends Observer {
    public void parkCar(Auto auto, int abfahrtZeit);
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
    public void update(Observable o, Object arg) {
        if (o == ls && arg != null && arg instanceof Integer) {
            Integer timestamp = (Integer) arg;
            if (leaveMap.containsKey(timestamp)) {
                for(Auto auto : leaveMap.get(timestamp))
                    ls.addToExitQueue(auto);
                leaveMap.remove(timestamp);
            }
        }
    }

    @Override
    public int getParkingCars() {
        int cars = 0;
        for(Entry<Integer,Set<Auto>> e : leaveMap.entrySet()) {
            cars += e.getValue().size();
        }
        return cars;
    }

    @Override
    public void parkCar(Auto auto, int abfahrtZeit) {
        if (leaveMap.containsKey(abfahrtZeit))
            leaveMap.get(abfahrtZeit).add(auto);
        else { 
            HashSet<Auto> set = new HashSet<Auto>();
            set.add(auto);
            leaveMap.put(abfahrtZeit, set);
        }
    }
    
    
}