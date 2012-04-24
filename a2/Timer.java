package a2;

import java.util.Observable;

public class Timer extends Observable {
    private int stepcount;
    public static final int CLOSETIME = 36000;

    public Timer() {
        stepcount = 0;
    }
    
    private Timer(int stepcount) {
        this.stepcount = stepcount;
    }

    public void step() {
        stepcount += 1;
        setChanged();
        notifyObservers(stepcount);
    }
    
    @Override
    public Timer clone() {
        return new Timer(stepcount);
    }
}
