package a2;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Diese Klasse repräsentiert die Hauptstraße von der in einem bestimmten Intervall
 * neue Autos ankommen.
 * Die Erzeugung der Autos geschieht im Interval [a1,a2].
 * Der daraus ermittelte zufällige Wert wird linear mit der Tageszeit verrechnet.
 * => Morgens kommen mehr Autos, als kurz vor Ladenschluss
 */
class Street implements Observer {
    
    Controlsystem cs;
    private final int createStart, createEnd, minParkduration, maxParkduration;
    private final Random rand = new Random();
    private int nextCar = 1;
    
    /**
     * 
     * @param cs das Controlsystem an welches die Hauptstraße gebunden werden soll
     * @param createStart = a1
     * @param createEnd = a2
     * @param minParkduration = e1
     * @param maxParkduration = e2
     */
    Street(Controlsystem cs, int createStart/*a1*/, int createEnd/*a2*/, int minParkduration/*e1*/, int maxParkduration/*e2*/){
        this.cs = cs;
        this.createStart = createStart;
        this.createEnd = createEnd;
        this.minParkduration = minParkduration;
        this.maxParkduration = maxParkduration;
    }
    
    /**
     * Privater Copy-Konstruktor, der von clone() benötigt wird.
     * @param s 
     */
    private Street(Street s) {
        cs = null;
        createStart = s.createStart;
        createEnd = s.createEnd;
        minParkduration = s.minParkduration;
        maxParkduration = s.maxParkduration;              
        nextCar = s.nextCar;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Timer && !(arg == null) && arg instanceof Integer && (Integer)arg <= 36000 && (Integer)arg >= 0){
           int timestamp = (Integer) arg;
           if (timestamp >= nextCar) {
               if (timestamp != 1){
                cs.addToEntryQueue(new Car(rand.nextInt(maxParkduration-minParkduration+1) + minParkduration-1));
               }
               nextCar = createRandInt(timestamp) + timestamp;
           }
       }
    }
    /** Wählt zufällig die Dauer aus dem gegebenen Intervall aus, nach der das nächste Auto erstellt wird.
     * Dabei wird die Tageszeit berücksichtigt und morgens sind die Abstände kürzer, als abends.
     * 
     * @param time aktuelle Tageszeit
     * @return die Dauer, nach der das nächste Auto erstellt werdne soll
     */
    private int createRandInt(int time) {
        int chosenValue = (int)Math.round((time/Timer.CLOSETIME-0.5) * (createEnd - createStart) + (rand.nextInt(createEnd-createStart+1)+createStart-1));
        if (chosenValue < createStart) //Grenzen verlassen?
            return createStart;
        if (chosenValue > createEnd) //Grenzen verlassen?
            return createEnd;
        return chosenValue;
    }
    
    @Override
    public Street clone() {
        return new Street(this);
    }
}