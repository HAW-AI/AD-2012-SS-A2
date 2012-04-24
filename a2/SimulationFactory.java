package a2;

public abstract /*final*/ class SimulationFactory {
    
    public static Simulation createSimulation(IOManager io) {
        return new SimulationImpl(io);
    }
    
    static Simulation getSimulation(int a1, int a2, int e1, int e2, int tb, int maxDuration, IOManager io) {
        return new SimulationImpl(a1, a2, e1, e2, tb, maxDuration, io);        
    }
}
