/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2;

/**
 *
 * @author David
 */
public class Runner {
    public static void main(String[] args) {
        Simulation sim = new SimulationImpl(5,10,20,30,40,120);
        while(!sim.simulationFinished()) {
            sim = sim.nextChangedStep();
            System.out.println(sim.getStateSummary());
        }
    }
}
