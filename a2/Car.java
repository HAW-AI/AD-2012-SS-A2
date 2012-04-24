package a2;

/**
 * Diese Klasse modelliert die Autos. Die Autos wissen, wie lange sie 
 * auf dem Parkplatz bleiben.
 */
class Car {
	private final int parkingDuration;

	Car(int parkingDuration) {
		this.parkingDuration = parkingDuration;
	}

	public int getParkingDuration() {
		return parkingDuration;
	}

}