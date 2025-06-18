package application;

public class FlightDoubleLinkedList {

	static class Node {

		Flight data;
		Node next;
		Node prev;
		public Passenger passenger;

		Node(Flight data) {
			this.data = data;
		}

		public Node(Passenger passengerToBoard) {
			this.data = null;
		}

		public Flight getFlight() {
			return data;
		}

		public Node getNext() {
			return next;
		}

		public Node getPrev() {
			return prev;
		}
	}

	private Node head;
	private Node tail;
	private int size;

	public void insert(Flight flight) { // O(1)

		Node newNode = new Node(flight);

		if (head == null) {
			head = tail = newNode;

			/// The last node between the head node and the last node
			head.next = head.prev = head;
		} else {
			tail.next = newNode;
			newNode.prev = tail;
			newNode.next = head;
			head.prev = newNode;
			tail = newNode;
		}

		size++;
	}

	public boolean remove(Flight flight) {///// O(n)

		if (flight == null || isEmpty()) {
			return false;
		}

		if (head == tail) {
			if (head.data.equals(flight)) {
				head = tail = null;
				size--;
				return true;
			}
			return false;
		}

		Node current = head;
		do {
			if (current.data.equals(flight)) {
				if (current == head) {
					head = head.next;
					head.prev = tail;
					tail.next = head;
				} else if (current == tail) {
					tail = tail.prev;
					tail.next = head;
					head.prev = tail;
				} else {
					current.prev.next = current.next;
					current.next.prev = current.prev;
				}
				size--;
				return true;
			}
			current = current.next;
		} while (current != head);

		return false;
	}

	public Flight findById(String flightID) {/// O(n)

		if (isEmpty()) {
		}
		Node current = head;
		do {
			if (current.data.getId().equals(flightID)) {
				return current.data;
			}
			current = current.next;
		} while (current != head);
		return null;
	}

	public Flight searchFlight(String searchBy, String searchValue) {/// O(n)

		if (isEmpty() || searchBy == null || searchValue == null || searchValue.isEmpty()) {
			return null;
		}

		Node current = head;
		do {
			Flight flight = current.data;
			if ((searchBy.equalsIgnoreCase("Flight ID") && flight.getId().equalsIgnoreCase(searchValue))
					|| (searchBy.equalsIgnoreCase("Destination")
							&& flight.getDestination().equalsIgnoreCase(searchValue))) {
				return flight; // Return the matched flight
			}
			current = current.next;
		} while (current != head);

		return null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Node getHead() {
		return head;
	}

	@Override
	public String toString() {
		if (isEmpty()) {
			return "---";
		}
		StringBuilder sb = new StringBuilder("(");
		Node current = head;
		do {
			if (current.data != null) {
				sb.append(current.data.toString());
			} else {
				sb.append("---");
			}
			current = current.next;
			if (current != head) {
				sb.append(", ");
			}
		} while (current != head);
		sb.append(")");
		return sb.toString();
	}

	public Flight findFlightByID(String flightID) {/// O(n)

		if (isEmpty()) {
			return null;
		}
		Node current = head;
		do {
			if (current.data.getId().equals(flightID)) {
				return current.data;
			}
			current = current.next;
		} while (current != head);
		return null;
	}

}