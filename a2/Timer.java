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
    
    @Override 
    public String toString() {
        int hours, minutes, seconds;
        seconds = stepcount + (3600*10);
        hours = seconds / 3600;
        seconds -= hours * 3600;
        minutes = seconds / 60;
        seconds -= minutes * 60;
        return "" + hours + ":" + minutes + ":" + seconds;       
    }
}
