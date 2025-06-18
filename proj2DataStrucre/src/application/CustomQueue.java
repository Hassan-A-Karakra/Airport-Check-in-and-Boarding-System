package application;

public class CustomQueue {

	static class Node {
		Passenger data;
		Node next;

		Node(Passenger data) {
			this.data = data;
		}
	}

	Node front;
	private Node rear;
	private int size;

	// Enqueue Adds a passenger to the end of the queue
	// Time Complexity O(1)
	public void enqueue(Passenger passenger) {
		Node newNode = new Node(passenger);
		if (rear != null) {
			rear.next = newNode;
		}
		rear = newNode;
		if (front == null) {
			front = newNode;
		}
		size++;
	}

	// Dequeue Removes a passenger from the front of the queue
	// Time Complexity O(1)
	public Passenger dequeue() {
		if (isEmpty()) {
			throw new IllegalStateException("Queue is empty.");
		}
		Passenger passenger = front.data;
		front = front.next;
		if (front == null) {
			rear = null;
		}
		size--;
		return passenger;
	}

	// Peek Returns the passenger at the front without removing it
	// Time Complexity O(1)
	public Passenger peek() {
		if (isEmpty()) {
			throw new IllegalStateException("Queue is empty.");
		}
		return front.data;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	// Check if the queue contains a specific passenger
	// Time Complexity O(n)
	public boolean contains(Passenger passengerToCheck) {
		Node current = front;
		while (current != null) {
			if (current.data.equals(passengerToCheck)) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	// Remove a specific passenger from the queue
	// Time Complexity O(n)
	public boolean remove(Passenger passengerToRemove) {

		if (isEmpty()) {
			return false;
		}

		Node current = front;
		Node previous = null;

		while (current != null) {
			if (current.data.equals(passengerToRemove)) {
				if (previous == null) {
					front = current.next;
				} else {
					previous.next = current.next;
				}
				if (current == rear) {
					rear = previous;
				}
				size--;
				return true;
			}
			previous = current;
			current = current.next;
		}
		return false;
	}

	public void clear() {
		front = null;
		rear = null;
		size = 0;
	}

	// Find a passenger by ID
	// Time Complexity O(n)
	public Passenger findByID(String passengerID) {
		Node current = front;

		while (current != null) {
			if (current.data.getPassengerID().equals(passengerID)) {
				return current.data;
			}
			current = current.next;
		}

		return null;
	}

	// Convert the queue to a string representation
	// Time Complexity O(n)
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node current = front;
		while (current != null) {
			sb.append(current.data.toString());
			current = current.next;
			if (current != null) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public Passenger findById(String id) {
		Node current = front;

		while (current != null) {
			if (current.data.getPassengerID().equals(id)) {
				return current.data; // Return the passenger if found
			}
			current = current.next; // Move to the next node
		}

		return null; // Return null if the passenger is not found
	}

	public Node getHead() {
		return getHead();
	}

	public void enqueue(String passengerID) {

		Passenger passenger = findByID(passengerID);
		if (passenger == null) {
			throw new IllegalArgumentException("Passenger with ID " + passengerID + " not found.");
		}

		Node newNode = new Node(passenger);
		if (rear != null) {
			rear.next = newNode;
		}
		rear = newNode;

		if (front == null) {
			front = newNode;
		}

		size++;
	}

}