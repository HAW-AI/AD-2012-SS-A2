package a2;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public interface Street extends Observer {
    //Keine Public-Methoden.
    //Im update() wird geprÃ¼ft, ob in diesem Step ein neues Auto erstellt werden muss und in die
    //Queue des Leitsystems eingereiht werden soll.
}

class StreetImpl implements Street{
    
    private final Controlsystem ls;
    private final int createStart, createEnd, minParkduration, maxParkduration;
    private int nextCar = 1;
    private Random rand = new Random();
    
    StreetImpl(Controlsystem ls, int createStart, int createEnd, int minParkduration,
            int maxParkduration){
        this.ls = ls;
        this.createStart = createStart;
        this.createEnd = createEnd;
        this.minParkduration = minParkduration;
        this.maxParkduration = maxParkduration;
    }

    @Override
    public void update(Observable o, Object arg) {
       
        if(o instanceof Timer && !(arg == null) && arg instanceof Integer && (Integer)arg <= 36000 && (Integer)arg >= 0){
           int timestamp = (Integer) arg;
           if (timestamp == nextCar) {
               if (timestamp != 1){
                ls.addToEntryQueue(new CarImpl(rand.nextInt(maxParkduration-minParkduration) + minParkduration));
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
        int chosenValue = (int)Math.round((time/Timer.CLOSETIME-0.5) * (createEnd - createStart) + (rand.nextInt(createEnd-createStart)+createStart));
        if (chosenValue < createStart) //Grenzen verlassen?
            return createStart;
        if (chosenValue > createEnd) //Grenzen verlassen?
            return createEnd;
        return chosenValue;
    }
}