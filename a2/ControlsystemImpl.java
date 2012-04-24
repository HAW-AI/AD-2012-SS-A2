package a2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import static a2.States.*;

public class ControlsystemImpl implements Controlsystem {

	final private int tB;
	final private Parking parking;
	private Queue<Car> inQueue = new LinkedList<Car>();
	private Queue<Car> outQueue = new LinkedList<Car>();
	final private TrafficLightController tlc = new TrafficLightController();
	private int currentTime;
	final private int maxDuration;
	final private int intervalBetweenCars = 3;

	ControlsystemImpl(int tB, Parking parking, Timer t, int maxDuration) {
		this.tB = tB;
		this.parking = parking;
		this.maxDuration = maxDuration;
		t.addObserver(tlc);
	}

	@Override
	public void addToEntryQueue(Car car) {
		inQueue.add(car);
	}

	@Override
	public void addToExitQueue(Car car) {
		outQueue.add(car);
	}

	@Override
	public boolean manageTraffic() {
		int inCount = Math.min(inQueue.size(), Parking.space-(outQueue.size()+parking.getParkingCars())); //zahl der Autos aus der Inqueue, die noch platz aufm dem parkplatz haben
		int inCountAsTime = (inCount-1)*intervalBetweenCars+1;											//zeit um alle autos auf den Parkplatz fahren zu lassen, die noch Platz haben
		int outQueueAsTime = (outQueue.size()-1)*intervalBetweenCars+1;					//zeit um alle autos vom Parkplatz fahren zu lassen (aus der Outqueue)												
		if (currentTime >= Timer.CLOSETIME) {											//checks if it's time to close (no one can enter after this point)
			if (outQueue.isEmpty()) {
				if (parking.getParkingCars() == 0) {
					return false;														//parkinglot empty, everyone left -> program terminates
				} else {
					tlc.setGreen(NONE, 1);
				}
			} else {
				tlc.setGreen(OUT, outQueueAsTime);
			}
		} else {
			if (outQueue.isEmpty()) {
				if(inQueue.isEmpty() || parking.getParkingCars() == Parking.space) {
					tlc.setGreen(NONE, 1);
				} else {
					tlc.setGreen(IN, Math.min(maxDuration,inCountAsTime));
				}
			} else {
				if(inQueue.isEmpty() || parking.getParkingCars()+outQueue.size() == Parking.space) {
					tlc.setGreen(OUT, Math.min(maxDuration,outQueueAsTime));
				} else {																//autos wollen (und koennen) rein + autos wollen raus
					if(tlc.currentState == OUT) {
						tlc.setGreen(IN, Math.min(maxDuration,inCountAsTime));
					} else if(tlc.currentState == IN){		
						tlc.setGreen(OUT,Math.min(maxDuration,outQueueAsTime));
					} else {
						if(inCount > outQueue.size()) {
							tlc.setGreen(IN, Math.min(maxDuration,inCountAsTime));
						} else {
							tlc.setGreen(OUT, Math.min(maxDuration,outQueueAsTime));
						}
					}
				}
			}
		}
		return true;

	}

	private class TrafficLightController implements Observer {

		private States currentState = NONE;	//momentaner State
		private States nextState = NONE;	//vom Leitsystem vorgegebener State
		private int askAgain;				//n�chste anfrage an leitsystem
		private int waitUntil;				//internes warten bei state wechsel
		private Map<Integer, Car> constructionRoad = new HashMap<Integer, Car>(); //Baustellendurchfahrt Abbildung Ausfahrtzeit => Auto
		
		public void driveIn(Car car) {
			constructionRoad.put(currentTime+tB, car);
		}
		
		
		
		public void setGreen(States newState, int duration) {
			if (currentState != newState && currentState != States.NONE) {
				currentState = NONE;
				nextState = newState;
				waitUntil = currentTime+tB;
				askAgain = waitUntil+duration;
			} else if (currentState == States.NONE) {
				currentState = newState;
				nextState = newState;
				waitUntil = currentTime;
				askAgain = currentTime+duration;
			} else {
				waitUntil = currentTime;
				askAgain = currentTime+duration;
			}
		}

		@Override
		public void update(Observable o, Object arg) {
			if(o instanceof Timer && arg != null && arg instanceof Integer) {
				int timestamp = (Integer) arg;
				currentTime = timestamp;
				if(timestamp == waitUntil) {
					currentState = nextState;
					if(timestamp == askAgain) {
						if(!ControlsystemImpl.this.manageTraffic()) {
							System.out.println("Simulation beendet nach "+currentTime);
						}
					}	
				}
				if((askAgain-currentTime)%3 == 1 ) {
					if(currentState == IN) {
						driveIn(inQueue.poll());
					} else if(currentState == OUT) {
						driveIn(outQueue.poll());
					}
				}
				if (constructionRoad.containsKey(currentTime)) {
					Car auto = constructionRoad.get(currentTime);
					constructionRoad.remove(currentTime);
					if (currentState == IN)
						parking.parkCar(auto, auto.getParkingDuration() + currentTime);
				}
			}
		}
		
		
	}
	
	
	
	

}
