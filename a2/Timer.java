package a2;

import java.util.Observable;

/** 
 * Die Timerklasse ist der Zeitgeber der gesammten Simulation. 
 * Ein Step wird in der Simulation auf eine Sekunde abgebildet
 */
class Timer extends Observable {
    private int stepcount;
    static final int CLOSETIME = 36000;

    Timer() {
        stepcount = 0;
    }
    
    /**
     * Privater Copy-Konstruktor, der intern für die Implementierung von
     * clone benutzt wird.
     * @param other Der zu kopierende Timer 
     */
    private Timer(Timer other) {
        this.stepcount = other.stepcount;
    }

    /**
     * Destruktive Methode, die den internen Stepcount erhöht und alle
     * Observer(Street,Parking,TrafficLightController) benachrichtigt.
     */
    void step() {
        stepcount += 1;
        setChanged();
        notifyObservers(stepcount);
    }
    
    @Override
    public Timer clone() {
        return new Timer(this);
    }
    
    @Override 
    public String toString() {
        int hours, minutes, seconds;
        seconds = stepcount + (3600*10);
        hours = seconds / 3600;
        seconds -= hours * 3600;
        minutes = seconds / 60;
        seconds -= minutes * 60;
        return "" + String.format("%02d", hours) + ":" + String.format("%02d",minutes) + ":" + String.format("%02d",seconds);       
    }
}
