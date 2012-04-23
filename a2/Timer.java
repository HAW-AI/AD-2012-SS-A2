package a2;

import java.util.Observable;

public final class Timer extends Observable {
	private int stepcount;
	
	public static final int CLOSETIME = 36000;

	public Timer() {
		stepcount = 0;
	}

	public void step() {
		stepcount += 1;
		setChanged();
		notifyObservers(stepcount);
	}
}
