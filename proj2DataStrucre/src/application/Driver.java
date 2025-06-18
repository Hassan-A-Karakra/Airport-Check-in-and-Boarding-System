//package application;
//
//import java.util.Scanner;
//
//public class Driver {
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		CheckInSystem checkInSystem = new CheckInSystem();
//
//		while (true) {
//			System.out.println("\n=== Airport Check-In System ===");
//			System.out.println("1. Add Flight");
//			System.out.println("2. Add Passenger to Flight");
//			System.out.println("3. Board Passenger");
//			System.out.println("4. Cancel Passenger");
//			System.out.println("5. Display Flight Info");
//			System.out.println("6. Undo Last Operation");
//			System.out.println("7. Redo Last Operation");
//			System.out.println("8. Display All Flights");
//			System.out.println("9. Exit");
//			System.out.print("Enter your choice: ");
//
//			int choice = scanner.nextInt();
//			scanner.nextLine(); // Consume the newline character
//
//			switch (choice) {
//			case 1:
//				System.out.print("Enter Flight ID: ");
//				String flightID = scanner.nextLine();
//
//				System.out.print("Enter Destination: ");
//				String destination = scanner.nextLine();
//
//				System.out.print("Enter Status (Active/Inactive): ");
//				String status = scanner.nextLine();
//
//				checkInSystem.addFlight(flightID, destination, status);
//				System.out.println("Flight added successfully!");
//				break;
//
//			case 2:
//				System.out.print("Enter Passenger ID: ");
//				String passengerID = scanner.nextLine();
//
//				System.out.print("Enter Passenger Name: ");
//				String name = scanner.nextLine();
//
//				System.out.print("Enter Flight ID: ");
//				String passengerFlightID = scanner.nextLine();
//
//				System.out.print("Enter Status (VIP/Regular): ");
//				String passengerStatus = scanner.nextLine();
//
//				Passenger passenger = new Passenger(passengerID, name, passengerFlightID, passengerStatus);
//				checkInSystem.addPassengerToFlight(passenger, passengerFlightID);
//				System.out.println("Passenger added to the flight successfully!");
//				break;
//
//			case 3:
//				System.out.print("Enter Flight ID to board passenger: ");
//				String boardFlightID = scanner.nextLine();
//				Flight boardFlight = checkInSystem.findFlight(boardFlightID);
//
//				if (boardFlight != null) {
//					boardFlight.boardPassenger();
//					System.out.println("Passenger boarded successfully!");
//				} else {
//					System.out.println("Flight not found!");
//				}
//				break;
//
//			case 4:
//				System.out.print("Enter Passenger ID to cancel: ");
//				String cancelPassengerID = scanner.nextLine();
//
//				System.out.print("Enter Flight ID: ");
//				String cancelFlightID = scanner.nextLine();
//
//				Flight flightToCancel = checkInSystem.findFlight(cancelFlightID);
//				if (flightToCancel != null) {
//					Passenger passengerToCancel = new Passenger(cancelPassengerID, "", cancelFlightID, "");
//					flightToCancel.cancelPassenger(passengerToCancel);
//					System.out.println("Passenger canceled successfully!");
//				} else {
//					System.out.println("Flight not found!");
//				}
//				break;
//
//			case 5:
//				System.out.print("Enter Flight ID to display info: ");
//				String displayFlightID = scanner.nextLine();
//				Flight flight = checkInSystem.findFlight(displayFlightID);
//
//				if (flight != null) {
//					System.out.println("Flight Info:");
//					System.out.println("ID: " + flight.getId());
//					System.out.println("Destination: " + flight.getDestination());
//					System.out.println("Status: " + flight.getStatus());
//					System.out.println("VIP Queue: \n" + flight.getVipQueue().displayQueue());
//					System.out.println("Regular Queue: \n" + flight.getRegularQueue().displayQueue());
//					System.out.println("Boarded Passengers: " + flight.getBoardedList().size());
//					System.out.println("Canceled Passengers: " + flight.getCanceledList().size());
//				} else {
//					System.out.println("Flight not found!");
//				}
//				break;
//
//			case 6:
//				System.out.print("Enter Flight ID to undo last operation: ");
//				String undoFlightID = scanner.nextLine();
//				Flight undoFlight = checkInSystem.findFlight(undoFlightID);
//
//				if (undoFlight != null) {
//					undoFlight.undoOperation();
//					System.out.println("Undo operation completed!");
//				} else {
//					System.out.println("Flight not found!");
//				}
//				break;
//
//			case 7:
//				System.out.print("Enter Flight ID to redo last operation: ");
//				String redoFlightID = scanner.nextLine();
//				Flight redoFlight = checkInSystem.findFlight(redoFlightID);
//
//				if (redoFlight != null) {
//					redoFlight.redoOperation();
//					System.out.println("Redo operation completed!");
//				} else {
//					System.out.println("Flight not found!");
//				}
//				break;
//
////			case 8:
////				System.out.println("All Flights:");
////				for (Flight f : checkInSystem.getFlights()) {
////					System.out.println(
////							"ID: " + f.getId() + ", Destination: " + f.getDestination() + ", Status: " + f.getStatus());
////				}
////				break;
//
//			case 9:
//				System.out.println("Exiting the system. Goodbye!");
//				scanner.close();
//				System.exit(0);
//				break;
//
//			default:
//				System.out.println("Invalid choice. Please try again.");
//			}
//		}
//	}
//}
