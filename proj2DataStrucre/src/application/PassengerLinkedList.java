package application;

public class PassengerLinkedList {

	static class Node {

		Passenger data;
		Node next;

		Node(Passenger data) {
			this.data = data;
		}
	}

	private Node head;
	private Node tail;
	private int size;

	/// this is for the border linked list to add the vip passenger in the first
	/// linked list
	public void addFirst(Passenger passengerToBoard) {

		if (passengerToBoard == null) {
			throw new IllegalArgumentException("Cannot add null passenger to the list");
		}

		Node newNode = new Node(passengerToBoard);

		if (head == null) {
			head = newNode;
			head.next = head;
		} else {
			newNode.next = head;
			Node tail = head;

			while (tail.next != head) {
				tail = tail.next;
			}

			tail.next = newNode;
			head = newNode;
		}

		size++;
	}

	/// this is for the border linked list to add the regular passenger in the last
	/// linked list
	public void addLast(Passenger passengerToBoard) {

		if (passengerToBoard == null) {
			throw new IllegalArgumentException("Cannot add null passenger to the list");
		}

		Node newNode = new Node(passengerToBoard);

		if (head == null) {
			head = newNode;
			head.next = head;
		} else {
			newNode.next = head;
			Node tail = head;

			while (tail.next != head) {
				tail = tail.next;
			}

			tail.next = newNode;
		}

		size++;
	}

	/**
	 * Inserts a new passenger at the end of the list. Time Complexity O(1)
	 */
	public void insert(Passenger passenger) {
		Node newNode = new Node(passenger);

		if (head == null) {
			head = newNode;
			head.next = head; // Circular link
		} else {
			newNode.next = head;
			Node tail = head;

			while (tail.next != head) {
				tail = tail.next;
			}

			tail.next = newNode; // Adding the new node at the end
		}

		size++;
	}

	/**
	 * Removes a passenger from the list if it exists. Time Complexity O(n)
	 */
	public boolean remove(Passenger passenger) {
		if (isEmpty() || passenger == null) {
			return false;
		}

		Node current = head;
		do {
			if (current.data.equals(passenger)) {
				if (current == head && current.next == head) {
					// Single-node case
					head = null; // The list will be empty
				} else if (current == head) {
					// If the node to remove is the head
					Node tail = head;
					while (tail.next != head) {
						tail = tail.next; // Find the tail
					}
					head = head.next; // Move head to next
					tail.next = head; // Update tail to point to new head
				} else {
					// If it's not the head node
					Node tail = head;
					while (tail.next != head) {
						tail = tail.next; // Find the tail
					}

					if (current.next == head) {
						// If current is the last node
						tail.next = head; // Update tail to point to head
					} else {
						// Bypass the current node
						Node previous = head;
						while (previous.next != current) {
							previous = previous.next; // Find the previous node
						}
						previous.next = current.next; // Bypass current
					}
				}
				size--;
				return true;
			}
			current = current.next;
		} while (current != head);

		return false;
	}

	/**
	 * Finds a passenger by ID. Time Complexity O(n)
	 */
	public Passenger findByID(String passengerID) {
		if (isEmpty()) {
			return null;
		}

		Node current = head;
		do {
			if (current.data.getPassengerID().equals(passengerID)) {
				return current.data;
			}
			current = current.next;
		} while (current != head);

		return null;
	}

	/**
	 * Checks if a passenger exists in the list. Time Complexity O(n)
	 */
	public boolean contains(Passenger passenger) {
		if (isEmpty()) {
			return false;
		}

		Node current = head;
		do {
			if (current.data.equals(passenger)) {
				return true;
			}
			current = current.next;
		} while (current != head);

		return false;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void add(Passenger passenger) {
		if (passenger == null) {
			throw new IllegalArgumentException("Passenger cannot be null");
		}
		Node newNode = new Node(passenger);
		if (head == null) {
			head = newNode;
			head.next = head;
		} else {

			Node tail = head;
			while (tail.next != head) {
				tail = tail.next;
			}
			tail.next = newNode;
			newNode.next = head;
		}
		size++;
	}
}