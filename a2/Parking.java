package a2;

import java.util.*;
import java.util.Map.Entry;

public interface Parking extends Observer {
    public void parkCar(Car auto, int abfahrtZeit);
    public int getParkingCars(); //Gibt nur die Anzahl der parkenden Autos zurück. Ausfahrende Autos werden nicht gezählt
}

/** Package-Private implementierung des Parkplatzes
 * 
 */
class ParkplatzImpl implements Parking {
    private Map<Integer, Set<Car>> leaveMap;
    private final Controlsystem ls;
    
    ParkplatzImpl(Controlsystem ls) {
        leaveMap = new HashMap<Integer, Set<Car>>();
        this.ls = ls;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (o == ls && arg != null && arg instanceof Integer) {
            Integer timestamp = (Integer) arg;
            if (leaveMap.containsKey(timestamp)) {
                for(Car auto : leaveMap.get(timestamp))
                    ls.addToExitQueue(auto);
                leaveMap.remove(timestamp);
            }
        }
    }

    @Override
    public int getParkingCars() {
        int cars = 0;
        for(Entry<Integer,Set<Car>> e : leaveMap.entrySet()) {
            cars += e.getValue().size();
        }
        return cars;
    }

    @Override
    public void parkCar(Car auto, int abfahrtZeit) {
        if (leaveMap.containsKey(abfahrtZeit))
            leaveMap.get(abfahrtZeit).add(auto);
        else { 
            HashSet<Car> set = new HashSet<Car>();
            set.add(auto);
            leaveMap.put(abfahrtZeit, set);
        }
    }
    
    
}