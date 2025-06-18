package application;

public class Operation {

	private int step;
	private String action;
	private String regularQueue;
	private String vipQueue;
	private String undoStack;
	private String redoStack;
	private String boardedPassengers;
	private String canceledPassengers;
	private Passenger passenger;

	// Constructor
	public Operation(int step, String action, String regularQueue, String vipQueue, String undoStack, String redoStack,
			String boardedPassengers, String canceledPassengers) {

		this.step = step;
		this.action = action;
		this.regularQueue = regularQueue;
		this.vipQueue = vipQueue;
		this.undoStack = undoStack;
		this.redoStack = redoStack;
		this.boardedPassengers = boardedPassengers;
		this.canceledPassengers = canceledPassengers;

	}

	public Operation(String operationDetails) {
		this.action = operationDetails;
	}

	// Getters and setters for all fields
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRegularQueue() {
		return regularQueue;
	}

	public void setRegularQueue(String regularQueue) {
		this.regularQueue = regularQueue;
	}

	public String getVipQueue() {
		return vipQueue;
	}

	public void setVipQueue(String vipQueue) {
		this.vipQueue = vipQueue;
	}

	public String getUndoStack() {
		return undoStack;
	}

	public void setUndoStack(String undoStack) {
		this.undoStack = undoStack;
	}

	public String getRedoStack() {
		return redoStack;
	}

	public void setRedoStack(String redoStack) {
		this.redoStack = redoStack;
	}

	public String getBoardedPassengers() {
		return boardedPassengers;
	}

	public void setBoardedPassengers(String boardedPassengers) {
		this.boardedPassengers = boardedPassengers;
	}

	public String getCanceledPassengers() {
		return canceledPassengers;
	}

	public void setCanceledPassengers(String canceledPassengers) {
		this.canceledPassengers = canceledPassengers;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	@Override
	public String toString() {
		if (action != null && passenger != null && passenger.getFlightID() != null) {
			return action + " (Flight " + passenger.getFlightID() + ")";
		}
		return action != null ? action : "No Action";
	}

}