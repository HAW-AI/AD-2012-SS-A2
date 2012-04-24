package a2;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

class Street implements Observer {
    
    Controlsystem cs;
    private final int createStart, createEnd, minParkduration, maxParkduration;
    private int nextCar = 1;
    private Random rand = new Random();
    
    Street(Controlsystem cs, int createStart, int createEnd, int minParkduration, int maxParkduration){
        this.cs = cs;
        this.createStart = createStart;
        this.createEnd = createEnd;
        this.minParkduration = minParkduration;
        this.maxParkduration = maxParkduration;
    }
    
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