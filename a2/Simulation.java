package a2;

public interface Simulation {
    boolean simulationFinished();
    String getStateSummary();
    Simulation nextStep();
    Simulation nextStep(int stepcount);
    Simulation nextChangedStep();
}


class SimulationImpl implements Simulation {
    private IOManager io;
    private Timer timer;
    private Controlsystem cs;
    private Street street;
    
    public SimulationImpl(String filepath) {
        io = new IOManager();
        timer = new Timer();
        int[] data = io.readSimFile(filepath);
        cs = new Controlsystem(data[4], timer, data[5]);
        street = new Street(cs, data[0], data[1], data[2], data[3]);
        timer.addObserver(cs.parking);
        timer.addObserver(cs.tlc);
        timer.addObserver(street);
    }
    
    private SimulationImpl() {
        timer = null;
        io = null;
        cs = null;
        street = null;
    }

    @Override
    public boolean simulationFinished() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getStateSummary() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Simulation nextStep() {
        return nextStep(1);
    }
    
    @Override
    public Simulation nextStep(int stepcount) {
        return this.clone().nextImpStep(stepcount);
    }

    @Override
    public Simulation nextChangedStep() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public SimulationImpl clone() {
        SimulationImpl sim = new SimulationImpl();
        sim.io = io;
        sim.timer = timer.clone();
        sim.cs = cs.clone();
        sim.street = street.clone();
        sim.street.cs = cs;
    
        timer.addObserver(cs.parking);
        timer.addObserver(cs.tlc);
        timer.addObserver(street);
        return null;
    }

    private Simulation nextImpStep(int stepcount) {
        for (int i = 0; i < stepcount; ++i) {
            timer.step();
        }
        return this;
    }
    
    @Override
    public boolean equals(Object o) {
        return false;
    }
}