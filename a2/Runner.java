package a2;


public class Runner {
    public static void main(String[] args) {
        String localPath = "C:\\Users\\David\\Documents\\Studium\\BAI3 Git repo\\AD\\A2\\a2\\"; //<<<< Muss auf jedem Rechner angepasst werden
    	IOManager io = new IOManager(localPath + "Sim1.txt");
        Simulation sim = SimulationFactory.createSimulation(io);
        while(!sim.simulationFinished()) {
            sim = sim.nextChangedStep();
            io.logToConsole(sim.getStateSummary());
//            io.logToFile(sim.getStateSummary());
        }
    }
}
