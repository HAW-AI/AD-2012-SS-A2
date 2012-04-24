package a2;

import java.util.*;
import java.util.Map.Entry;
/** 
 * Der Parkplatz ist ein Observer des Timers und hat eine Map, die Abbildet
 * Integer(Abfahrzeit) => {Car,Car,...}
 * Bei jedem update()-Aufruf werden alle Autos aus dieser Map in die outQueue
 * des Controlsystems eingereiht, die zu dem Zeitpunkt rausfahren wollen.
 * 
 * Der Parkplatz wird von Controlsystem verwaltet
 */
class Parking implements Observer {
    public final static int space = 150;
    private Map<Integer, Set<Car>> leaveMap;
    private final Controlsystem cs;
    
    /**
     * Standartkonstruktor der von Controlsystem bei der normalen Erstellung aufgerufen wird
     * Das Controlsystem trägt den Parkplatz im Timer als Observer ein
     * @param cs das Controlsystem, welches an diesen Parkplatz gebunden werden soll.
     */
    Parking(Controlsystem cs) {
        leaveMap = new HashMap<Integer, Set<Car>>();
        this.cs = cs;
    }
    
    /**
     * Copykonstrukor, der von Controlsystem aufgerufen wird, wenn es sich komplett 
     * kopieren soll.
     * @param p der alte Parkplatz, dessen Inhalt kopiert werden muss
     * @param cs das neue Controlsystem, an welches dieser neue Parkplatz gebunden werden soll
     */
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

    /** 
     * Diese Methode gibt die Anzahl der geparkten Autos zurück. Sie berücksichtigt
     * nicht die Autos in der outQueue vom Controlsystem. Diese Methode wird von
     * Controlsystem für den Algorithmus benötigt
     * @return die Anzhal der geparkten Autos
     */
    int getParkingCars() {
        int cars = 0;
        for(Entry<Integer,Set<Car>> e : leaveMap.entrySet()) {
            cars += e.getValue().size();
        }
        return cars;
    }

    /**
     * Diese Methode ordnet ein parkendes Auto in an die richtige Position in der 
     * Map ein. Autos sind in der Map nach Abfahrtzeiten angeordnet.
     * @param auto das zu parkende auto
     * @param leaveTime der !Zeitpunkt! zu dem das Auto den Parkplatz wieder verlassen soll.
     */
    void parkCar(Car auto, int leaveTime) {
        if (leaveMap.containsKey(leaveTime))
            leaveMap.get(leaveTime).add(auto);
        else { 
            HashSet<Car> set = new HashSet<Car>();
            set.add(auto);
            leaveMap.put(leaveTime, set);
        }
    }
}