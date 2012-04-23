package a2_v2;

import static a2.States.NONE;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import a2.Car;
import a2.Controlsystem;
import a2.States;
import static a2.States.*;
import a2.TrafficLightController;

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
	
	private void carsPark(int time) {
		int quantity = time/intervalBetweenCars;
		for(int i = 0; i <= quantity; i++) {
			Car car = inQueue.poll();
			parking.parkCar(car,currentTime+tB+car.getParkingDuration()+intervalBetweenCars*i);
		}
	}
	
	private void carsLeave(int time) {
		int quantity = time/intervalBetweenCars;
		for(int i = 0; i <= quantity; i++) {
			outQueue.poll();
		}
	}

	//doppelte aufrufe in var speichern
	//func fuer math.min(bound,x)
	@Override
	public boolean manageTraffic() {
		int inQueueAsTime = (inQueue.size()-1)*intervalBetweenCars+1;					//time needed to dequeue all cars in inQueue
		int outQueueAsTime = (outQueue.size()-1)*intervalBetweenCars+1;					//time needed to dequeue all cars in outQueue
		int stateTime;																	//contains time that the chosen state will last
		if (currentTime >= Timer.CLOSETIME) {											//checks if it's time to close (no one can enter after this point)
			if (outQueue.isEmpty()) {
				if (parking.getParkingCars() == 0) {
					return false;														//parkinglot empty, everyone left -> program terminates
				} else {
					tlc.setGreen(NONE, 1);
				}
			} else {
				tlc.setGreen(OUT, outQueueAsTime);
				carsLeave(outQueueAsTime);
			}
		} else {
			if (outQueue.isEmpty()) {
				if(inQueue.isEmpty() || parking.getParkingCars() == 150) {
					tlc.setGreen(NONE, 1);
				} else {
					stateTime = Math.min(150-parking.getParkingCars(),inQueueAsTime);
					tlc.setGreen(IN, stateTime);
					carsPark(stateTime);
				}
			} else {
				if(inQueue.isEmpty() || parking.getParkingCars() == 150) {
					tlc.setGreen(OUT, outQueueAsTime);
					carsLeave(outQueueAsTime);
				} else {
					int inCount = Math.min(inQueue.size(), 150-(outQueue.size()+parking.getParkingCars()));
					if(tlc.currentState == OUT) {
						stateTime = Math.min(maxDuration,(inCount-1)*intervalBetweenCars+1);
						tlc.setGreen(IN, stateTime);
						carsPark(stateTime);
					} else if(tlc.currentState == IN){
						stateTime = Math.min(maxDuration,outQueueAsTime);
						tlc.setGreen(OUT,stateTime);
						carsLeave(stateTime);
					} else {
						if(inCount > outQueue.size()) {
							stateTime = Math.min(maxDuration,(inCount-1)*intervalBetweenCars+1);
							tlc.setGreen(IN, stateTime);
							carsPark(stateTime);
						} else {
							stateTime = Math.min(maxDuration,outQueueAsTime);
							tlc.setGreen(OUT, stateTime);
							carsLeave(stateTime);
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
		private int askAgain;				//nächste anfrage an leitsystem
		private int waitUntil;				//internes warten bei state wechsel

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
				int timestamp = (int) arg;
				currentTime = timestamp;
				if(timestamp == waitUntil) {
					currentState = nextState;
					if(timestamp == askAgain) {
						if(!ControlsystemImpl.this.manageTraffic()) {
							System.out.println("Simulation beendet nach "+currentTime);
						}
					}	
				}
			}
		}
	}

}
