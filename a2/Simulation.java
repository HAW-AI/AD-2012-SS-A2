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
    
    SimulationImpl(int a1, int a2, int e1, int e2, int tb, int maxDuration) {
        io = new IOManager();
        timer = new Timer();
        cs = new Controlsystem(tb, timer, maxDuration);
        street = new Street(cs, a1, a2, e1, e2);
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
        return cs.terminated;
    }

    @Override
    public String getStateSummary() {
        return timer.toString() + ": " + cs.toString();
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
        SimulationImpl sim = new SimulationImpl();
        sim.io = io;
        sim.timer = timer.clone();
        sim.cs = cs.clone();
        sim.street = street.clone();
        sim.street.cs = sim.cs;
    
        sim.timer.addObserver(sim.cs.parking);
        sim.timer.addObserver(sim.cs.tlc);
        sim.timer.addObserver(sim.street);
        return sim;
    }

    private Simulation nextImpStep(int stepcount) {
        for (int i = 0; i < stepcount; ++i) {
            timer.step();
        }
        return this;
    }
    
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