package a2;

class CarImpl implements Car {
	private final int parkingDuration;

	CarImpl(int parkingDuration) {
		this.parkingDuration = parkingDuration;
	}

	@Override
	public int getParkingDuration() {
		return parkingDuration;
	}

}
