/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2;


public class Runner {
    public static void main(String[] args) {
    	IOManager io = new IOManager("Sim1.txt");
        Simulation sim = new SimulationImpl(io);
        while(!sim.simulationFinished()) {
            sim = sim.nextChangedStep();
//            io.logToConsole(sim.getStateSummary());
            io.logToFile(sim.getStateSummary());
        }
    }
}
