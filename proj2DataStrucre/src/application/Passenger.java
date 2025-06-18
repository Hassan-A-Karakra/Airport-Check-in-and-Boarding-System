package application;

public class Passenger {

	public static final Passenger data = null;
	// Variables
	private String passengerID;
	private String name;
	private String flightID;
	private String status;

	// Constructor with all arguments
	public Passenger(String passengerID, String name, String flightID, String status) {
		this.passengerID = passengerID;
		this.name = name;
		this.flightID = flightID;
		this.status = status;
	}

	public Passenger(String id) {
		// TODO Auto-generated constructor stub
	}

	// Standard Getters and Setters
	public String getPassengerID() {
		return passengerID;
	}

	public void setPassengerID(String passengerID) {
		this.passengerID = passengerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlightID() {
		return flightID;
	}

	public void setFlightID(String flightID) {
		this.flightID = flightID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return name + " (" + status + ", Flight " + flightID + ")";
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		Passenger passenger = (Passenger) obj;
		return passengerID.equals(passenger.passengerID);

	}

	public String getId() {
		return passengerID; // Now returns the passengerID
	}

	public String getFlightId() {
		return flightID; // Now returns the flightID
	}

	public void setFlightId(String flightID) {
		this.flightID = flightID; // Now sets the flightID
	}

	public boolean isVIP() {
		// TODO Auto-generated method stub
		return false;
	}
}