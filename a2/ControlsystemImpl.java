package a2;

import java.util.Queue;
import static a2.States.*;

//Ich habe mal die Implementierung rausgenommen, damit einem keine Fehler im Source angemarkert werden
//und wir eine Syntaktisch korrekte Lösung im Repo haben, mit welcher alle weiterarbeiten können ;)
//David
class ControlsystemImpl /*implements Controlsystem*/ {
	/*
	final int tB;
	final private Parking park;
	final int closeTime;
	final private TrafficLightController as = new TrafficLightControllerImpl();
	private Queue<Car> inQueue;
	private Queue<Car> outQueue;
	

	@Override
	public void addToExitQueue(Car car) {
		outQueue.add(car);
	}

	@Override
	public void addToEntryQueue(Car car) {
		inQueue.add(car);
		
	}
	
	private boolean empty() {
		return outQueue.isEmpty() && park.isEmpty();
	}

	@Override
	public void manageTraffic() {
		if(closeTime > Timer.getTime()) {
			if(outQueue.isEmpty()) 
				if(park.isEmpty()) return;
				else as.setGreen(NONE,5);
			else as.setGreen(OUT, (outQueue.size()-1)*3+1);
			
		}
		
	}
	
	
	private class TrafficLightControllerImpl implements TrafficLightController {
		
		private States currentState = NONE;

		@Override
		public void setGreen(States newState, int duration) {
			if(currentState != newState && currentState != States.NONE) {
				sleep(tB);
				currentState = newState;
			} else if(currentState == States.NONE) {
				currentState = newState;
			}
			sleep(duration);
			ControlsystemImpl.this.manageTraffic();
		}

		public void sleep(int duration) {
			int waitUntil = Timer.getTime()+duration;
			while(Timer.getTime() < waitUntil) {};
		}

	}*/
}