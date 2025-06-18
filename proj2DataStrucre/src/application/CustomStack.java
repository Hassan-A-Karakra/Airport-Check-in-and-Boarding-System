package application;

public class CustomStack {

	private static class Node {

		Operation operationData;
		Node next;

		public Node(Operation operation) {
			this.operationData = operation;
			this.next = null;
		}

		public Node(String operationDetails) {
			this.operationData = new Operation(operationDetails);
			this.next = null;
		}

	}

	private Node top; // Top of the stack
	private int size; // Size of the stack

	public CustomStack() {
		this.top = null; // Initialize stack as empty
		this.size = 0; // Initialize size as 0
	}

	/**
	 * Pushes an operation onto the stack. Time Complexity O(1)
	 */
	public void push(Operation operationDetails) {
		Node newNode = new Node(operationDetails);
		newNode.next = top;
		top = newNode;
		size++;
	}

	/**
	 * Pops the top operation from the stack. Time Complexity O(1)
	 */
	public Operation pop() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty, cannot pop element.");
		}
		Operation data = top.operationData;
		top = top.next;
		size--;
		return data;
	}

	/**
	 * Peeks at the top operation without removing it. Time Complexity:\ O(1)
	 */
	public Operation peek() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty, cannot peek element.");
		}
		return top.operationData;
	}

	/**
	 * Checks if the stack is empty. Time Complexity:\ O(1)
	 */
	public boolean isEmpty() {
		return top == null;
	}

	/**
	 * Clears the stack by removing all elements. Time Complexity O(1)
	 */
	public void clear() {
		top = null;
		size = 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node current = top;
		while (current != null) {
			if (current.operationData != null) {
				sb.append(current.operationData.toString()).append("\n");
			} else {
				sb.append("Null operationData").append("\n");
			}
			current = current.next;
		}
		return sb.toString();
	}

	public void push(String operationDetails) {
		if (operationDetails == null || operationDetails.isEmpty()) {
			throw new IllegalArgumentException("Operation details cannot be null or empty");
		}
		Operation operation = new Operation(operationDetails);
		push(operation);
	}

}