package a2;

public interface Simulation {
    boolean simulationFinished();
    String getStateSummary();
    Simulation nextStep();
    Simulation nextStep(int stepcount);
    Simulation nextChangedStep();
}


class SimulationImpl implements Simulation {
    private final IOManager io;
    private final Timer timer;
    private final Controlsystem cs;
    private final Street street;
    
    /**
     * Standartkonstruktor, der von der SimulationFactory nach Außen zur 
     * Verfügung gestellt wird.
     * @param io der IOManager, mit dem die Einstellungen für die Simulation eingelesen werden
     */
    SimulationImpl(IOManager io) {
        this.io = io;
        timer = new Timer();
        int[] data = io.readSimFile();
        cs = new Controlsystem(data[4], data[5]);
        street = new Street(cs, data[0], data[1], data[2], data[3]);
        timer.addObserver(cs.parking);
        timer.addObserver(cs.tlc);
        timer.addObserver(street);
    }
    
    SimulationImpl(int a1, int a2, int e1, int e2, int tb, int maxDuration, IOManager io) {
        this.io = io;
        timer = new Timer();
        cs = new Controlsystem(tb, maxDuration);
        street = new Street(cs, a1, a2, e1, e2);
        timer.addObserver(cs.parking);
        timer.addObserver(cs.tlc);
        timer.addObserver(street);        
    }
    
    /**
     * Privater Copykonstruktor, der von clone() benutzt wird.
     * @param sim die Simulation, die kopiert werden soll
     */
    private SimulationImpl(SimulationImpl sim) {
        this.io = sim.io;
        this.timer = sim.timer.clone();
        this.cs = sim.cs.clone();
        this.street = sim.street.clone();
        this.street.cs = this.cs;
    
        this.timer.addObserver(this.cs.parking);
        this.timer.addObserver(this.cs.tlc);
        this.timer.addObserver(this.street);
    }

    /**
     * Entscheidet, ob die Simulation noch andauert, oder bereits beendet ist
     * @return true falls die Simulation beendet ist, ansonsten false
     */
    @Override
    public boolean simulationFinished() {
        return cs.terminated;
    }

    /**
     * Liefert eine druckreife Zusammenfassung des aktuellen Zustands der Simulation.
     * @return der Zustand der Simulation als String
     */
    @Override
    public String getStateSummary() {
        return timer.toString() + ": " + cs.toString();
    }

    /**
     * Liefert eine neue Simulation nach genau einem Simulationsschrit(1 Sekunde)
     * @return die neue Simulation nach einem Schritt
     */
    @Override
    public Simulation nextStep() {
        return nextStep(1);
    }
    
    /**
     * Liefert eine neue Simulation nach stepcount Schritten(Sekunden)
     * @param stepcount die Anzahl der Schritte, die simuliert werden sollen
     * @return eine neue Simulation nach stepcount Schritten
     */
    @Override
    public Simulation nextStep(int stepcount) {
        return this.clone().nextImpStep(stepcount);
    }

    /**
     * Lässt die Simulation so lange laufen, bis sich etwas in den Queues oder
     * auf der Baustellendurchfahrt ändert oder die Simulation beendet ist.
     * @return liefert eine neue Simulation, welche den neuen Simulationsstand wiederspiegelt
     */
    @Override
    public Simulation nextChangedStep() {
        if (cs.terminated)
            return this;
        SimulationImpl copySimulation = this.clone();
        while(this.equals(copySimulation) && !copySimulation.cs.terminated) {
            copySimulation.nextImpStep(1);
        }
        return copySimulation;
    }
    
    @Override
    public SimulationImpl clone() {
        return new SimulationImpl(this);
    }

    /**
     * Berechnet die nächsten Schritte der Simulation destruktiv.
     * Wird von nextChangedStep() und nextStep() aufgerufen
     * @param stepcount Anzahl der Schritte, die auf einmal simuliert werden sollen.
     * @return es wird lediglich ein this zurückgeben
     */
    private Simulation nextImpStep(int stepcount) {
        for (int i = 0; i < stepcount; ++i) {
            timer.step();
        }
        return this;
    }
    
    /**
     * Die Prüfung auf die Änderung des Zustands einer Simulation wird von
     * nextChangedStep() auf equals() zurückgeführt.
     * @param o die Simulation, mit welcher verglichen werden soll
     * @return true falls gleich, ansonsten false
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null) return false;
        if(getClass() == o.getClass()) {
            SimulationImpl s = (SimulationImpl) o;
            return cs.equals(s.cs);
        }
        return false;
    }
}