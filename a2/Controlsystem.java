package a2;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import static a2.States.*;

class Controlsystem  {

    final private int tB;
    final Parking parking;
    private Queue<Car> inQueue;
    private Queue<Car> outQueue;
    final TrafficLightController tlc;
    private int currentTime;
    final private int maxDuration;
    final private int intervalBetweenCars = 3;
    boolean terminated;

    Controlsystem(int tB, Timer t, int maxDuration) {
        inQueue = new LinkedList<Car>();
        outQueue = new LinkedList<Car>();
        tlc = new TrafficLightController();
        this.tB = tB;
        this.parking = new Parking(this);
        this.maxDuration = maxDuration;
        terminated = false;
    }

    private Controlsystem(Controlsystem cs) {
        tB = cs.tB;
        parking = new Parking(cs.parking, this);
        maxDuration = cs.maxDuration;
        inQueue = new LinkedList<Car>(cs.inQueue);
        outQueue = new LinkedList<Car>(cs.outQueue);
        tlc = new TrafficLightController(cs.tlc);
        terminated = cs.terminated;
    }
    
    @Override
    public String toString() {
        return "InQueue: " + inQueue.size() +  " " + tlc.toString() + " OutQueue:" + outQueue.size() + " Parkplatz:" + parking.getParkingCars();
    }
    
    
    @Override
    public Controlsystem clone() {
        return new Controlsystem(this);
    }
    
    void addToEntryQueue(Car car) {
            inQueue.add(car);
    }

    void addToExitQueue(Car car) {
            outQueue.add(car);
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null) return false;
        if(getClass() == o.getClass()) {
            Controlsystem s = (Controlsystem) o;
            return (inQueue.equals(s.inQueue) &&
                    outQueue.equals(s.outQueue) &&
                    tlc.constructionRoad.equals(s.tlc.constructionRoad));
            
        }
        return false;
    }

    boolean manageTraffic() {
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
        private States currentState;	//momentaner State
        private States nextState;	//vom Leitsystem vorgegebener State
        private int askAgain;				//n�chste anfrage an leitsystem
        private int waitUntil;				//internes warten bei state wechsel
        private Map<Integer, Pair<States,Car>> constructionRoad = new HashMap<Integer, Pair<States,Car>>(); //Baustellendurchfahrt Abbildung Ausfahrtzeit => Auto

        TrafficLightController() {
            currentState = NONE;
            nextState = NONE;
        }
        
        TrafficLightController(TrafficLightController tlc) {
            currentState = tlc.currentState;
            nextState = tlc.nextState;
            askAgain = tlc.askAgain;
            waitUntil = tlc.waitUntil;
            for(Entry<Integer, Pair<States,Car>> entry : tlc.constructionRoad.entrySet()) {
                constructionRoad.put(entry.getKey(), entry.getValue());
            }
        }
        
        @Override
        public String toString() {
            return "Ampel: " + this.currentState + " Straße:" + this.constructionRoad.entrySet().size();
        }
        
        void driveIn(Car car) {
                constructionRoad.put(currentTime+tB, new Pair<States, Car>(currentState, car));
        }

        void setGreen(States newState, int duration) {
                 if (currentState == NONE) {
                        currentState = newState;
                        nextState = newState;
                        waitUntil = currentTime;
                        askAgain = currentTime+duration;
                } else if (newState == NONE ){
                        currentState = newState;
                        nextState = currentState;
                        waitUntil = currentTime;
                        askAgain = waitUntil+3;
                } else if (currentState != newState) {
                        currentState = NONE;
                        nextState = newState;
                        waitUntil = currentTime+tB;
                        askAgain = waitUntil+duration;
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
                if(timestamp >= waitUntil) {
                    currentState = nextState;
                    if(timestamp >= askAgain) {
                        if(!Controlsystem.this.manageTraffic()) {
                                terminated = true;
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
                    Pair<States,Car> pair = constructionRoad.get(currentTime);
                    constructionRoad.remove(currentTime);
                    if (pair.getKey() == IN)
                            parking.parkCar(pair.getValue(), pair.getValue().getParkingDuration() + currentTime);
                }
            }
        }
    }
}
