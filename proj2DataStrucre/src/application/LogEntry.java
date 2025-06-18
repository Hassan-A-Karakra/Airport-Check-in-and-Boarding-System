package application;

public class LogEntry {

	private String timestamp;
	private String operation;
	private String passenger;
	private String flightId;
	private String description;

	public LogEntry(String timestamp, String operation, String passenger, String flightId, String description) {

		this.timestamp = timestamp;
		this.operation = operation;
		this.passenger = passenger;
		this.flightId = flightId;
		this.description = description;

	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		if (timestamp == null || timestamp.isEmpty()) {
			throw new IllegalArgumentException("Timestamp cannot be null or empty.");
		}
		this.timestamp = timestamp;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		if (operation == null || operation.isEmpty()) {
			throw new IllegalArgumentException("Operation cannot be null or empty.");
		}
		this.operation = operation;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Timestamp: %s, Operation: %s, Passenger: %s, Flight ID: %s, Description: %s", timestamp,
				operation, passenger, flightId, description);
	}
}