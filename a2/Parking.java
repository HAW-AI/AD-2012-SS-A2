package a2;

import java.util.*;
import java.util.Map.Entry;
/** Package-Private implementierung des Parkplatzes
 * 
 */
class Parking implements Observer {
    public final static int space = 150;
    private Map<Integer, Set<Car>> leaveMap;
    private final Controlsystem cs;
    
    Parking(Controlsystem cs) {
        leaveMap = new HashMap<Integer, Set<Car>>();
        this.cs = cs;
    }
    
    Parking(Parking p, Controlsystem cs) {
        leaveMap = new HashMap<Integer, Set<Car>>();
        this.cs = cs;
        
        for(Entry<Integer, Set<Car>> entry : p.leaveMap.entrySet()) {
            leaveMap.put(entry.getKey(), new HashSet<Car>(entry.getValue()));
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Integer) {
            Integer timestamp = (Integer) arg;
            if (leaveMap.containsKey(timestamp)) {
                for(Car auto : leaveMap.get(timestamp))
                    cs.addToExitQueue(auto);
                leaveMap.remove(timestamp);
            }
        }
    }

    public int getParkingCars() {
        int cars = 0;
        for(Entry<Integer,Set<Car>> e : leaveMap.entrySet()) {
            cars += e.getValue().size();
        }
        return cars;
    }

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