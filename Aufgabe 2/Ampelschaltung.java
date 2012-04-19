package A2;

import java.util.Observer;

public interface Ampelschaltung extends Observer{
     public void setGreen(Richtung richtung, int dauer);
}
