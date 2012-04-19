package a2;

import java.util.Observer;

public interface Leitsystem extends Observer{
    public void addToExitQueue(Auto auto);
    public void addToEntryQueue(Auto auto);
    public void callAlgorithm(); //Kümmert sich um die Schaltung der Ampeln
}
