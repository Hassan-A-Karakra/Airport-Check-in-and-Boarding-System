package application;

public class Flight {

	private String id;
	private String destination;
	private String status;
	private CustomQueue vipQueue;
	private CustomQueue regularQueue;
	private PassengerLinkedList boardedList;
	private PassengerLinkedList canceledList;
	private CustomStack undoStack;
	private CustomStack redoStack;

	public Flight(String id, String destination, String status) {

		this.id = id;
		this.destination = destination;
		this.status = status;
		this.vipQueue = new CustomQueue();
		this.regularQueue = new CustomQueue();
		this.boardedList = new PassengerLinkedList();
		this.canceledList = new PassengerLinkedList();
		this.undoStack = new CustomStack();
		this.redoStack = new CustomStack();

	}

	public String getId() {
		return this.id;
	}

	public String getFlightID() {
		return this.id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CustomStack getUndoStack() {
		return this.undoStack;
	}

	public CustomStack getRedoStack() {
		return this.redoStack;
	}

	public CustomQueue getVipQueue() {
		return this.vipQueue;
	}

	public CustomQueue getRegularQueue() {
		return this.regularQueue;
	}

	public PassengerLinkedList getBoardedList() {
		return this.boardedList;
	}

	public PassengerLinkedList getCanceledList() {
		return canceledList;
	}

	@Override
	public String toString() {
		return this.id; // Assuming 'id' is the identifier for the flight
	}
}