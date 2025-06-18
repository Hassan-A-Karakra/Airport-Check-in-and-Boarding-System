package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main extends Application {

	private ObservableList<Flight> flights = FXCollections.observableArrayList();
	private ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();
	private ObservableList<Operation> operations = FXCollections.observableArrayList();
	private ObservableList<Passenger> passengers = FXCollections.observableArrayList();
	private ObservableList<Passenger> vipPassengerList = FXCollections.observableArrayList();
	private ObservableList<Passenger> regularPassengerList = FXCollections.observableArrayList();
	private ObservableList<Passenger> canceledPassengerList = FXCollections.observableArrayList();
	private ObservableList<Passenger> boardedPassengers = FXCollections.observableArrayList();

	TableView<Passenger> vipTable = new TableView<>();
	TableView<Passenger> regularTable = new TableView<>();
	TableView<Passenger> boardedTable = new TableView<>();

	private CustomQueue vipQueue = new CustomQueue();
	private CustomQueue regularQueue = new CustomQueue();
	private CustomQueue passengersQueue = new CustomQueue();

	private TableView<Flight> flightTable;
	private TableView<Operation> operationTable;
	private TableView<Passenger> passengerTable = new TableView<>();;

	private FlightDoubleLinkedList flightList = new FlightDoubleLinkedList();
	private PassengerLinkedList boardedPassengersList = new PassengerLinkedList();
	private PassengerLinkedList canceledList = new PassengerLinkedList();

	private PassengerLinkedList undoStack1 = new PassengerLinkedList();
	private PassengerLinkedList redoStack1 = new PassengerLinkedList();

	private CustomStack undoStack = new CustomStack();
	private CustomStack redoStack = new CustomStack();

	private int totalCanceledVIP = 0;
	private int totalCanceledRegular = 0;
	private int totalBoardedVIP = 0;
	private int totalBoardedRegular = 0;
	private int totalCheckedInVIP = 0;
	private int totalCheckedInRegular = 0;
	private int stepCounter = 1;

	private File flightFile = null;
	private File passengerFile = null;
	private ComboBox<String> flightSelection = new ComboBox<>();

	@Override
	public void start(Stage primaryStage) {

		showWelcomeScreen(primaryStage);

	}

	private void showWelcomeScreen(Stage primaryStage) {
		VBox welcomeLayout = new VBox(20);
		welcomeLayout.setAlignment(Pos.CENTER);
		welcomeLayout.setPadding(new Insets(20));

		welcomeLayout.setStyle("-fx-background-color: #BFEFFF;");

		Label welcomeLabel = new Label("Welcome to Airport Check-In System");
		welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

		Button enterButton = new Button("Enter System");
		enterButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");

		enterButton.setOnAction(e -> showMainTabs(primaryStage));

		welcomeLayout.getChildren().addAll(welcomeLabel, enterButton);

		Scene welcomeScene = new Scene(welcomeLayout, 800, 500);
		welcomeScene.setFill(Color.web("#BFEFFF"));
		primaryStage.setScene(welcomeScene);
		primaryStage.setTitle("Airport Check-In System");
		primaryStage.show();
	}

	private void showMainTabs(Stage primaryStage) {

		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		tabPane.setStyle("-fx-background-color: #BFEFFF;");

		Tab passengerTab = createPassengerManagementTab(primaryStage);
		Tab flightTab = createFlightManagementTab(primaryStage);
		Tab operationTab = createOperationTab(primaryStage);
		Tab logTab = createLogFileTab(primaryStage);
		Tab statsTab = createStatisticsTab();

		flightTab.setStyle("-fx-text-fill: #FFFFFF;");
		passengerTab.setStyle("-fx-text-fill: #FFFFFF;");
		operationTab.setStyle("-fx-text-fill: #FFFFFF;");
		logTab.setStyle("-fx-text-fill: #FFFFFF;");
		statsTab.setStyle("-fx-text-fill: #FFFFFF;");

		tabPane.getTabs().addAll(flightTab, passengerTab, logTab, operationTab, statsTab);

		Scene mainScene = new Scene(tabPane, 1450, 700);
		mainScene.setFill(Color.web("#BFEFFF"));
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Airport Check-In and Boarding System");
		primaryStage.show();
	}

	// ======================== Flight Management Tab ==========================

	private Tab createFlightManagementTab(Stage primaryStage) {

		Tab tab = new Tab("Flight Management");

		BorderPane flightPane = new BorderPane();

		// Create TableView
		flightTable = setupFlightTable();

		// Create all buttons
		Button loadFlightsButton = new Button("Load Flights");
		Button saveFlightsButton = new Button("Save Flights");
		Button addFlightButton = new Button("Add Flight");
		Button updateFlightButton = new Button("Update Flight");
		Button removeFlightButton = new Button("Remove Flight");
		Button searchFlightButton = new Button("Search Flight");
		Button printAllFlightsButton = new Button("Print All Flights");
		Button printSpecificFlightButton = new Button("Print Specific Flight Info");
		Button integrateNavigationButton = new Button("Integrate Navigation Options");
		Button displayActiveFlightsButton = new Button("Display Active Flights");
		Button displayInactiveFlightsButton = new Button("Display Inactive Flights");

		butoonEffect(loadFlightsButton);
		butoonEffect(saveFlightsButton);
		butoonEffect(addFlightButton);
		butoonEffect(updateFlightButton);
		butoonEffect(removeFlightButton);
		butoonEffect(searchFlightButton);
		butoonEffect(printAllFlightsButton);
		butoonEffect(printSpecificFlightButton);
		butoonEffect(integrateNavigationButton);
		butoonEffect(displayActiveFlightsButton);
		butoonEffect(displayInactiveFlightsButton);

		// Set actions for buttons
		loadFlightsButton.setOnAction(e -> {

			loadFlightFile(primaryStage);
			syncLinkedListWithTableView();

		});

		saveFlightsButton.setOnAction(e -> saveFlightFile(primaryStage));

		addFlightButton.setOnAction(e -> {

			insertFlight(flightPane);
			syncLinkedListWithTableView();

		});

		updateFlightButton.setOnAction(e -> {

			updateFlight(flightPane);
			syncLinkedListWithTableView();

		});

		removeFlightButton.setOnAction(e -> {

			removeFlight(flightPane);
			syncLinkedListWithTableView();

		});

		searchFlightButton.setOnAction(e -> searchFlight(flightPane));
		printAllFlightsButton.setOnAction(e -> printAllFlights(flightPane));
		printSpecificFlightButton.setOnAction(e -> printSpecificFlight(flightPane));
		integrateNavigationButton.setOnAction(e -> integrateNavigationOptions(flightPane));
		displayActiveFlightsButton.setOnAction(e -> displayActiveFlights(flightPane));
		displayInactiveFlightsButton.setOnAction(e -> displayInactiveFlights(flightPane));

		HBox topBarContainer = new HBox(1, loadFlightsButton, saveFlightsButton, addFlightButton, removeFlightButton,
				searchFlightButton, updateFlightButton, printAllFlightsButton, printSpecificFlightButton,
				integrateNavigationButton, displayActiveFlightsButton, displayInactiveFlightsButton);

		topBarContainer.setAlignment(Pos.CENTER_LEFT);
		topBarContainer.setPadding(new Insets(10));
		topBarContainer.setStyle("-fx-background-color: #e0e7ef; -fx-border-color: #d3d3d3; -fx-border-width: 1;");

		VBox tableContainer = new VBox(flightTable);
		VBox.setVgrow(flightTable, Priority.ALWAYS);
		tableContainer.setPadding(new Insets(10));
		tableContainer.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d3d3d3; -fx-border-width: 1;");

		flightPane.setTop(topBarContainer);
		flightPane.setLeft(tableContainer);

		tab.setContent(flightPane);
		return tab;
	}

	private void insertFlight(BorderPane flightPane) {

		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);
		formGrid.setAlignment(Pos.CENTER);

		Label lblFlightID = new Label("Flight ID:");
		TextField tfFlightID = new TextField();

		Label lblDestination = new Label("Destination:");
		TextField tfDestination = new TextField();

		Label lblStatus = new Label("Status:");
		ComboBox<String> cbStatus = new ComboBox<>();
		cbStatus.getItems().addAll("Active", "Inactive");

		Button btAdd = new Button("Add");
		Button btClear = new Button("Clear");

		formGrid.add(lblFlightID, 0, 0);
		formGrid.add(tfFlightID, 1, 0);
		formGrid.add(lblDestination, 0, 1);
		formGrid.add(tfDestination, 1, 1);
		formGrid.add(lblStatus, 0, 2);
		formGrid.add(cbStatus, 1, 2);
		formGrid.add(btAdd, 0, 3);
		formGrid.add(btClear, 1, 3);

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		butoonEffect(btAdd);
		butoonEffect(btClear);

		IconedTextFieled(tfFlightID, lblFlightID);
		IconedTextFieled(tfDestination, lblDestination);
		IconedTextFieled(cbStatus, lblStatus);

		btClear.setOnAction(e -> {

			tfFlightID.clear();
			tfDestination.clear();
			cbStatus.getSelectionModel().clearSelection();

		});

		btAdd.setOnAction(e -> {

			String flightID = tfFlightID.getText().trim();
			String destination = tfDestination.getText().trim();
			String status = cbStatus.getValue();

			if (flightID.isEmpty() || destination.isEmpty() || status == null) {
				showAlert("Error", "All fields are required.");

				return;

			}

			if (!flightID.matches("[a-zA-Z0-9]*")) {

				showAlert("Error", "Flight ID must contain only letters and numbers.");
				return;

			}

			boolean flightExists = false;

			for (Flight flight : flights) {

				if (flight.getId().equals(flightID)) {
					flightExists = true;
					break;
				}
			}

			if (!flightExists) {

				for (Flight flight : flights) {

					if (flight.getId().equals(flightID)) {
						flightExists = true;
						break;
					}
				}

			}

			if (flightExists) {

				showAlert("Error", "Flight with the given ID already exists.");
				return;
			}

			Flight newFlight = new Flight(flightID, destination, status);
			flightList.insert(newFlight);
			flights.add(newFlight);

			flightSelection.getItems().add(newFlight.getId());
			logAction("Add Flight", "Flight ID: " + newFlight.getId() + ", Destination: " + destination);
			showAlert("Success", "Flight added successfully.");

			flightTable.refresh();
		});

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(flightTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(formGrid);

		mainLayout.getChildren().add(sidePanel);

		flightPane.setCenter(mainLayout);
	}

	private void removeFlight(BorderPane flightPane) {

		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);
		formGrid.setAlignment(Pos.CENTER);

		Label lblFlightID = new Label("Flight ID:");
		TextField tfFlightID = new TextField();

		Button btRemove = new Button("Remove");
		Button btClear = new Button("Clear");

		formGrid.add(lblFlightID, 0, 0);
		formGrid.add(tfFlightID, 1, 0);
		formGrid.add(btRemove, 0, 1);
		formGrid.add(btClear, 1, 1);

		butoonEffect(btRemove);
		butoonEffect(btClear);

		IconedTextFieled(tfFlightID, lblFlightID);

		btClear.setOnAction(e -> tfFlightID.clear());

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		btRemove.setOnAction(e -> {

			String flightID = tfFlightID.getText().trim();

			if (flightID.isEmpty()) {
				showAlert("Error", "Flight ID is required.");
				return;

			}

			if (!flightID.matches("[a-zA-Z0-9]+")) {
				showAlert("Error", "Flight ID must contain only letters and numbers.");
				return;
			}

			Flight flightToRemove = flightList.findById(flightID);

			if (flightToRemove != null) {

				Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
				confirmationAlert.setTitle("Confirm Remove");
				confirmationAlert.setHeaderText(
						"Are you sure you want to remove this flight?\nThis will delete all passengers registered for this flight.");

				confirmationAlert.setContentText("Flight ID: " + flightToRemove.getId());

				confirmationAlert.showAndWait().ifPresent(response -> {

					if (response == ButtonType.OK) {

						// Delete the flight from the flight list
						flightList.remove(flightToRemove);
						flights.remove(flightToRemove);

						// Delete passengers registered for this flight from the VIP Queue
						CustomQueue.Node vipNode = vipQueue.front;

						while (vipNode != null) {

							Passenger passenger = vipNode.data;

							if (passenger.getFlightID().equals(flightID)) {

								vipQueue.remove(passenger);
								vipPassengerList.remove(passenger);

							}

							vipNode = vipNode.next;

						}

						// Delete the passengers registered for this flight from the Regular Queue
						CustomQueue.Node regularNode = regularQueue.front;

						while (regularNode != null) {

							Passenger passenger = regularNode.data;

							if (passenger.getFlightID().equals(flightID)) {
								regularQueue.remove(passenger);
								regularPassengerList.remove(passenger);

							}

							regularNode = regularNode.next;

						}

						// Update the flight list in the ComboBox and in the schedule
						flightSelection.getItems().remove(flightID); // Update the list
						flightTable.refresh();

						logAction("Remove Flight", "Removed Flight ID: " + flightToRemove.getId());
						showAlert("Success", "Flight removed successfully along with all associated passengers.");

					}

				});

			} else {

				showAlert("Error", "No flight found with the given Flight ID.");

			}

		});

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(flightTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(formGrid);

		mainLayout.getChildren().add(sidePanel);

		flightPane.setCenter(mainLayout);

	}

	private void searchFlight(BorderPane flightPane) {
		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);

		Label lblSearchBy = new Label("Search By:");
		ComboBox<String> cbSearchBy = new ComboBox<>();
		cbSearchBy.getItems().addAll("Flight ID", "Destination");
		cbSearchBy.setPromptText("Select criteria");

		Label lblSearchValue = new Label("Enter Value:");
		TextField tfSearchValue = new TextField();

		Button btSearch = new Button("Search");
		Button btClear = new Button("Clear");

		Label lblResult = new Label(); // Label to display the result
		lblResult.setStyle("-fx-font-size: 14px; -fx-text-fill: blue; -fx-font-weight: bold;"); // Styling

		formGrid.add(lblSearchBy, 0, 0);
		formGrid.add(cbSearchBy, 1, 0);
		formGrid.add(lblSearchValue, 0, 1);
		formGrid.add(tfSearchValue, 1, 1);
		formGrid.add(btSearch, 0, 2);
		formGrid.add(btClear, 1, 2);
		formGrid.add(lblResult, 0, 3, 2, 1); // Add the label to the form (spanning 2 columns)

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		butoonEffect(btSearch);
		butoonEffect(btClear);

		IconedTextFieled(tfSearchValue, lblSearchValue);
		IconedTextFieled(cbSearchBy, lblSearchBy);

		btClear.setOnAction(e -> {
			cbSearchBy.getSelectionModel().clearSelection();
			tfSearchValue.clear();
			flightTable.getSelectionModel().clearSelection();
			lblResult.setText("");
		});

		btSearch.setOnAction(e -> {
			syncLinkedListWithTableView();
			String searchBy = cbSearchBy.getValue(); // Get the selected search criteria
			String searchValue = tfSearchValue.getText().trim(); // Get the entered search value

			if (searchBy == null || searchValue.isEmpty()) {
				showAlert("Error", "Please select a search criteria and enter a value.");
				logAction("Search Flight Attempt", "Failed: Missing criteria or search value.");
				lblResult.setText("Error: Missing criteria or value."); // Update the label with error message
				return;
			}
			// Search for the flight in the LinkedList based on the criteria and value
			Flight foundFlight = flightList.searchFlight(searchBy, searchValue);
			if (foundFlight != null) {

				flightTable.getSelectionModel().select(foundFlight);
				flightTable.scrollTo(foundFlight);

				// Log the successful search
				logAction("Search Flight",
						"Found Flight ID: " + foundFlight.getId() + ", Destination: " + foundFlight.getDestination());

				// Display the flight details in the label on separate lines
				lblResult.setText("Found Flight Details:\n" + "Flight ID: " + foundFlight.getId() + "\n"
						+ "Destination: " + foundFlight.getDestination() + "\n" + "Status: " + foundFlight.getStatus());
			} else {
				// If no matching flight is found, show an alert and update the label
				showAlert("Not Found", "No flight found with the given " + searchBy + ": " + searchValue);
				logAction("Search Flight Attempt", "No flight found with " + searchBy + ": " + searchValue);
				lblResult.setText("No results found for " + searchBy + ": " + searchValue);
			}
		});

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(flightTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(formGrid);

		mainLayout.getChildren().add(sidePanel);

		flightPane.setCenter(mainLayout);
	}

	private void updateFlight(BorderPane flightPane) {
		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);
		formGrid.setAlignment(Pos.CENTER);

		// Form fields
		Label lblFlightID = new Label("Flight ID:");
		TextField tfFlightID = new TextField();

		Label lblDestination = new Label("Destination:");
		TextField tfDestination = new TextField();
		tfDestination.setDisable(true);

		Label lblStatus = new Label("Status:");
		ComboBox<String> cbStatus = new ComboBox<>();
		cbStatus.getItems().addAll("Active", "Inactive");
		cbStatus.setDisable(true);

		Button btFind = new Button("Find");
		Button btUpdate = new Button("Update");
		btUpdate.setDisable(true);
		Button btClear = new Button("Clear");

		// Add components to the grid
		formGrid.add(lblFlightID, 0, 0);
		formGrid.add(tfFlightID, 1, 0);
		formGrid.add(btFind, 2, 0);
		formGrid.add(lblDestination, 0, 1);
		formGrid.add(tfDestination, 1, 1);
		formGrid.add(lblStatus, 0, 2);
		formGrid.add(cbStatus, 1, 2);
		formGrid.add(btUpdate, 0, 3);
		formGrid.add(btClear, 1, 3);

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		butoonEffect(btFind);
		butoonEffect(btUpdate);
		butoonEffect(btClear);
		IconedTextFieled(tfFlightID, lblFlightID);
		IconedTextFieled(tfDestination, lblDestination);
		IconedTextFieled(cbStatus, lblStatus);

		btFind.setOnAction(e -> {
			String flightID = tfFlightID.getText().trim();

			if (flightID.isEmpty()) {
				showAlert("Error", "Flight ID is required.");
				return;
			}

			// Search for the flight
			Flight flightToFind = flightList.findFlightByID(flightID);

			if (flightToFind != null) {
				tfDestination.setText(flightToFind.getDestination());
				cbStatus.setValue(flightToFind.getStatus());
				tfDestination.setDisable(false);
				cbStatus.setDisable(false);
				btUpdate.setDisable(false);
				showAlert("Success", "Flight found. You can now update the details.");

			} else {
				showAlert("Error", "No flight found with the given Flight ID.");
				tfDestination.clear();
				cbStatus.getSelectionModel().clearSelection();
				tfDestination.setDisable(true);
				cbStatus.setDisable(true);
				btUpdate.setDisable(true);
			}
		});

		btUpdate.setOnAction(e -> {
			String flightID = tfFlightID.getText().trim();
			String destination = tfDestination.getText().trim();
			String status = cbStatus.getValue();

			if (flightID.isEmpty() || destination.isEmpty() || status == null) {
				showAlert("Error", "All fields are required.");
				return;
			}
			if (!destination.matches("[a-zA-Z0-9 ]*")) {
				showAlert("Error", "Destination must contain only letters, numbers, and spaces.");
				return;
			}
			// Search for the flight
			Flight flightToUpdate = flightList.findFlightByID(flightID);
			if (flightToUpdate == null) {
				showAlert("Error", "No flight found with the given Flight ID.");
				return;
			}

			// Update the flight details
			flightToUpdate.setDestination(destination);
			flightToUpdate.setStatus(status);
			flightTable.refresh();

			logAction("Update Flight", "Updated Flight ID: " + flightToUpdate.getId() + ", Destination: "
					+ flightToUpdate.getDestination() + ", Status: " + flightToUpdate.getStatus());

			syncLinkedListWithTableView();
			showAlert("Success", "Flight details updated successfully.");
		});

		// Clear button action
		btClear.setOnAction(e -> {
			tfFlightID.clear();
			tfDestination.clear();
			cbStatus.getSelectionModel().clearSelection();
			tfDestination.setDisable(true);
			cbStatus.setDisable(true);
			btUpdate.setDisable(true);
		});

		// Layout for the main window
		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(flightTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(formGrid);

		mainLayout.getChildren().add(sidePanel);

		flightPane.setCenter(mainLayout);
	}

	private void printAllFlights(BorderPane flightPane) {
		syncLinkedListWithTableView();
		TableView<Flight> flightTableView = new TableView<>();

		TableColumn<Flight, String> flightIDColumn = new TableColumn<>("Flight ID");
		flightIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		flightIDColumn.setPrefWidth(150);

		TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
		destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
		destinationColumn.setPrefWidth(150);

		TableColumn<Flight, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusColumn.setPrefWidth(100);

		flightTableView.getColumns().addAll(flightIDColumn, destinationColumn, statusColumn);
		ObservableList<Flight> flightListObservable = FXCollections.observableArrayList();

		FlightDoubleLinkedList.Node currentNode = flightList.getHead();

		if (currentNode == null) {
			showAlert("No Flights", "No flights available.");
		} else {
			do {
				Flight flight = currentNode.data;
				flightListObservable.add(flight);
				currentNode = currentNode.next;
			} while (currentNode != flightList.getHead());
		}

		flightTableView.setItems(flightListObservable);

		logAction("Print All Flights", "User requested to print all flights.");

		Button btClear = new Button("Clear");
		btClear.setOnAction(e -> flightTableView.getItems().clear());
		butoonEffect(btClear);

		VBox newFlightBox = new VBox(10, flightTableView, btClear);
		newFlightBox.setAlignment(Pos.CENTER);
		newFlightBox.setPadding(new Insets(15));

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().addAll(flightTable, newFlightBox);

		flightPane.setCenter(mainLayout);

		flightTableView.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14; " + "-fx-border-width: 1; "
				+ "-fx-border-radius: 10; " + "-fx-background-color: #E6E6FA; " + "-fx-background-radius: 10;");

		flightTableView.setRowFactory(tv -> {
			TableRow<Flight> row = new TableRow<>();
			row.setStyle("-fx-background-color: #E6E6FA;");
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #B0C4DE;"));
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #E6E6FA;"));
			return row;
		});
	}

	private void printSpecificFlight(BorderPane flightPane) {
		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);

		Label lblFlightID = new Label("Enter Flight ID:");
		TextField tfFlightID = new TextField();

		Button btSearch = new Button("Search");
		Button btClear = new Button("Clear");

		formGrid.add(lblFlightID, 0, 0);
		formGrid.add(tfFlightID, 1, 0);
		formGrid.add(btSearch, 0, 1);
		formGrid.add(btClear, 1, 1);

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		butoonEffect(btSearch);
		butoonEffect(btClear);
		IconedTextFieled(tfFlightID, lblFlightID);

		// Create a TableView to display the flight details
		TableView<Flight> flightDetailsTable = new TableView<>();

		TableColumn<Flight, String> flightIDColumn = new TableColumn<>("Flight ID");
		flightIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		flightIDColumn.setPrefWidth(150);

		TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
		destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
		destinationColumn.setPrefWidth(150);

		TableColumn<Flight, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusColumn.setPrefWidth(100);

		flightDetailsTable.getColumns().addAll(flightIDColumn, destinationColumn, statusColumn);

		ObservableList<Flight> flightDetailsObservable = FXCollections.observableArrayList();

		btSearch.setOnAction(e -> {
			String flightID = tfFlightID.getText().trim();
			if (flightID.isEmpty()) {
				showAlert("Error", "Flight ID is required.");
				return;
			}

			if (!flightID.matches("[a-zA-Z0-9]*")) {
				showAlert("Error", "Flight ID must contain only letters and numbers.");
				return;
			}

			// Search the flight in the custom linked list
			FlightDoubleLinkedList.Node currentNode = flightList.getHead();
			boolean found = false;
			flightDetailsObservable.clear(); // Clear previous search results

			if (currentNode == null) {
				showAlert("Error", "No flights available.");
				return;
			}

			do {
				Flight flight = currentNode.data;
				if (flight.getId().equalsIgnoreCase(flightID)) {

					flightDetailsObservable.add(flight);
					found = true;
					logAction("Search Specific Flight", "Searched for Flight ID: " + flightID);
					break;
				}
				currentNode = currentNode.next; // Move to the next node
			} while (currentNode != flightList.getHead());

			if (found) {
				flightDetailsTable.setItems(flightDetailsObservable);
			} else {
				showAlert("Error", "No flight found with ID: " + flightID);
			}
		});

		btClear.setOnAction(e -> {
			tfFlightID.clear();
			flightDetailsTable.getItems().clear(); // Clear the table view
		});

		VBox detailsBox = new VBox(10, formGrid, flightDetailsTable);
		detailsBox.setAlignment(Pos.CENTER);
		detailsBox.setPadding(new Insets(15));

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().addAll(flightTable, detailsBox);

		flightPane.setCenter(mainLayout);

		flightDetailsTable.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14; " + "-fx-border-width: 1; "
				+ "-fx-border-radius: 10; " + "-fx-background-color: #E6E6FA; " + "-fx-background-radius: 10;");

		flightDetailsTable.setRowFactory(tv -> {
			TableRow<Flight> row = new TableRow<>();
			row.setStyle("-fx-background-color: #E6E6FA;");
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #B0C4DE;"));
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #E6E6FA;"));
			return row;
		});
	}

	private void displayActiveFlights(BorderPane flightPane) {

		VBox activeFlightsBox = new VBox(10);
		activeFlightsBox.setPadding(new Insets(15));
		activeFlightsBox.setAlignment(Pos.CENTER);

		Label lblTitle = new Label("Active Flights and Their Destinations");
		lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		TableView<Flight> activeFlightsTable = new TableView<>();

		TableColumn<Flight, String> flightIDColumn = new TableColumn<>("Flight ID");
		flightIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		flightIDColumn.setPrefWidth(150);

		TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
		destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
		destinationColumn.setPrefWidth(150);

		activeFlightsTable.getColumns().addAll(flightIDColumn, destinationColumn);

		ObservableList<Flight> activeFlightsObservable = FXCollections.observableArrayList();

		FlightDoubleLinkedList.Node currentNode = flightList.getHead();

		if (currentNode != null) {

			do {
				Flight flight = currentNode.data;

				if ("Active".equalsIgnoreCase(flight.getStatus())) {
					activeFlightsObservable.add(flight); // Add active flight to the list

				}

				currentNode = currentNode.next; // Move to the next node

			} while (currentNode != flightList.getHead()); // Loop until back to head
		}

		activeFlightsTable.setItems(activeFlightsObservable);

		if (activeFlightsObservable.isEmpty()) {

			activeFlightsObservable.add(new Flight("N/A", "N/A", "N/A"));

		}

		activeFlightsBox.getChildren().addAll(lblTitle, activeFlightsTable);

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(flightTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(activeFlightsBox);

		mainLayout.getChildren().add(sidePanel);

		flightPane.setCenter(mainLayout);
		logAction("Display Active Flights", "User requested to display active flights.");

		activeFlightsTable.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14; " + "-fx-border-width: 1; "
				+ "-fx-border-radius: 10; " + "-fx-background-color: #E6E6FA; " + "-fx-background-radius: 10;");

		activeFlightsTable.setRowFactory(tv -> {

			TableRow<Flight> row = new TableRow<>();
			row.setStyle("-fx-background-color: #E6E6FA;");
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #B0C4DE;"));
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #E6E6FA;"));
			return row;

		});
	}

	private void displayInactiveFlights(BorderPane flightPane) {

		VBox inactiveFlightsBox = new VBox(10);
		inactiveFlightsBox.setPadding(new Insets(15));
		inactiveFlightsBox.setAlignment(Pos.CENTER);

		Label lblTitle = new Label("Inactive Flights");
		lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		TableView<Flight> inactiveFlightsTable = new TableView<>();

		TableColumn<Flight, String> flightIDColumn = new TableColumn<>("Flight ID");
		flightIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		flightIDColumn.setPrefWidth(150); // Set a suitable width

		TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
		destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
		destinationColumn.setPrefWidth(150); // Set a suitable width

		inactiveFlightsTable.getColumns().addAll(flightIDColumn, destinationColumn);

		inactiveFlightsTable.setPrefWidth(300); // Set a suitable width
		inactiveFlightsTable.setPrefHeight(400); // Reduced height to make it smaller

		ObservableList<Flight> inactiveFlightsObservable = FXCollections.observableArrayList();

		FlightDoubleLinkedList.Node currentNode = flightList.getHead();

		if (currentNode != null) {

			do {
				Flight flight = currentNode.data;

				if ("Inactive".equalsIgnoreCase(flight.getStatus())) {

					inactiveFlightsObservable.add(flight); // Add inactive flight to the list

				}
				currentNode = currentNode.next; // Move to the next node

			} while (currentNode != flightList.getHead()); // Loop until back to head
		}

		inactiveFlightsTable.setItems(inactiveFlightsObservable);

		if (inactiveFlightsObservable.isEmpty()) {
			inactiveFlightsObservable.add(new Flight("N/A", "N/A", "Inactive"));
		}

		inactiveFlightsBox.getChildren().addAll(lblTitle, inactiveFlightsTable);

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().addAll(flightTable, inactiveFlightsBox);

		flightPane.setCenter(mainLayout); // Set the final layout to the flightPane

		logAction("Display Inactive Flights", "User requested to display Inactive flights.");

		inactiveFlightsTable.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14; " + "-fx-border-width: 1; "
				+ "-fx-border-radius: 10; " + "-fx-background-color: #E6E6FA; " + "-fx-background-radius: 10;");

		inactiveFlightsTable.setRowFactory(tv -> {
			TableRow<Flight> row = new TableRow<>();
			row.setStyle("-fx-background-color: #E6E6FA;");
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #B0C4DE;"));
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #E6E6FA;"));
			return row;
		});
	}

	private void integrateNavigationOptions(BorderPane flightPane) {

		VBox navigationBox = new VBox(15);
		navigationBox.setPadding(new Insets(15));
		navigationBox.setAlignment(Pos.CENTER);

		ComboBox<String> activeFlightComboBox = new ComboBox<>();
		activeFlightComboBox.setPromptText("Select Active Flight");

		ComboBox<String> inactiveFlightComboBox = new ComboBox<>();
		inactiveFlightComboBox.setPromptText("Select Inactive Flight");

		for (Flight flight : flights) {
			if ("Active".equalsIgnoreCase(flight.getStatus())) {
				activeFlightComboBox.getItems().add(flight.getId());
			} else {
				inactiveFlightComboBox.getItems().add(flight.getId());
			}
		}

		TableView<Passenger> activeFlightPassengersTable = new TableView<>();
		activeFlightPassengersTable.setItems(FXCollections.observableArrayList());
		initializePassengerTable(activeFlightPassengersTable);

		TableView<Passenger> inactiveFlightPassengersTable = new TableView<>();
		inactiveFlightPassengersTable.setItems(FXCollections.observableArrayList());
		initializePassengerTable(inactiveFlightPassengersTable);

		// Setup navigation buttons for active flights
		Button btnPreviousActive = new Button("<--- Previous Flight");
		Button btnNextActive = new Button("Next Flight --->");

		// Setup navigation buttons for inactive flights
		Button btnPreviousInactive = new Button("<--- Previous Flight");
		Button btnNextInactive = new Button("Next Flight --->");

		// Event handling for active flights
		activeFlightComboBox
				.setOnAction(e -> updatePassengersForFlight(activeFlightComboBox, activeFlightPassengersTable));

		btnPreviousActive.setOnAction(e -> {
			int currentIndex = activeFlightComboBox.getSelectionModel().getSelectedIndex();
			if (currentIndex > 0) {
				activeFlightComboBox.getSelectionModel().select(currentIndex - 1);
				activeFlightComboBox.fireEvent(new ActionEvent());
			} else {
				showAlert("Navigation Error", "No previous active flight available.");
			}
		});

		btnNextActive.setOnAction(e -> {
			int currentIndex = activeFlightComboBox.getSelectionModel().getSelectedIndex();
			if (currentIndex < activeFlightComboBox.getItems().size() - 1) {
				activeFlightComboBox.getSelectionModel().select(currentIndex + 1);
				activeFlightComboBox.fireEvent(new ActionEvent());
			} else {
				showAlert("Navigation Error", "No next active flight available.");
			}
		});

		// Event handling for inactive flights
		inactiveFlightComboBox
				.setOnAction(e -> updatePassengersForFlight(inactiveFlightComboBox, inactiveFlightPassengersTable));

		btnPreviousInactive.setOnAction(e -> {
			int currentIndex = inactiveFlightComboBox.getSelectionModel().getSelectedIndex();
			if (currentIndex > 0) {
				inactiveFlightComboBox.getSelectionModel().select(currentIndex - 1);
				inactiveFlightComboBox.fireEvent(new ActionEvent());
			} else {
				showAlert("Navigation Error", "No previous inactive flight available.");
			}
		});

		btnNextInactive.setOnAction(e -> {
			int currentIndex = inactiveFlightComboBox.getSelectionModel().getSelectedIndex();
			if (currentIndex < inactiveFlightComboBox.getItems().size() - 1) {
				inactiveFlightComboBox.getSelectionModel().select(currentIndex + 1);
				inactiveFlightComboBox.fireEvent(new ActionEvent());
			} else {
				showAlert("Navigation Error", "No next inactive flight available.");
			}
		});

		VBox activeFlightContainer = new VBox(5, new Label("Active Flights"), activeFlightComboBox,
				activeFlightPassengersTable, btnPreviousActive, btnNextActive);
		VBox inactiveFlightContainer = new VBox(5, new Label("Inactive Flights"), inactiveFlightComboBox,
				inactiveFlightPassengersTable, btnPreviousInactive, btnNextInactive);

		HBox allFlightsLayout = new HBox(10, activeFlightContainer, inactiveFlightContainer);
		navigationBox.getChildren().add(allFlightsLayout);

		flightPane.setCenter(navigationBox);
	}

	private void updatePassengersForFlight(ComboBox<String> flightComboBox, TableView<Passenger> passengerTable) {

		String selectedFlightID = flightComboBox.getValue();

		if (selectedFlightID != null) {

			ObservableList<Passenger> passengersForFlight = FXCollections.observableArrayList();

			for (Passenger passenger : vipPassengerList) {

				if (passenger.getFlightID().equals(selectedFlightID) && !passengersForFlight.contains(passenger)) {
					passengersForFlight.add(passenger);

				}
			}

			for (Passenger passenger : regularPassengerList) {

				if (passenger.getFlightID().equals(selectedFlightID) && !passengersForFlight.contains(passenger)) {
					passengersForFlight.add(passenger);
				}
			}

			passengerTable.setItems(passengersForFlight);
		}
	}

	private void initializePassengerTable(TableView<Passenger> table) {
		TableColumn<Passenger, String> passengerIDCol = new TableColumn<>("Passenger ID");
		passengerIDCol.setCellValueFactory(new PropertyValueFactory<>("passengerID"));
		passengerIDCol.setPrefWidth(100);

		TableColumn<Passenger, String> passengerNameCol = new TableColumn<>("Name");
		passengerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		passengerNameCol.setPrefWidth(100);

		TableColumn<Passenger, String> flightIDCol = new TableColumn<>("Flight ID");
		flightIDCol.setCellValueFactory(new PropertyValueFactory<>("flightID"));
		flightIDCol.setPrefWidth(100);

		TableColumn<Passenger, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setPrefWidth(100);

		table.getColumns().addAll(passengerIDCol, passengerNameCol, flightIDCol, statusCol);
	}

	private void syncLinkedListWithTableView() {

		flightSelection.getItems().clear();

		for (Flight flight : flightTable.getItems()) {

			flightList.insert(flight);
			flightSelection.getItems().add(flight.getId());

		}

	}

	private TableView<Flight> setupFlightTable() {

		TableView<Flight> table = new TableView<>();
		table.setItems(flights);

		TableColumn<Flight, String> idColumn = new TableColumn<>("Flight ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		idColumn.setPrefWidth(100);

		TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
		destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
		destinationColumn.setPrefWidth(150);

		TableColumn<Flight, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusColumn.setPrefWidth(100);

		table.getColumns().setAll(idColumn, destinationColumn, statusColumn);
		table.setPrefWidth(550);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Apply overall table style
		table.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14px; " + "-fx-border-width: 1; "
				+ "-fx-background-color: #F0F8FF; " + "-fx-background-radius: 10px; " + "-fx-font-weight: bold;");

		table.setRowFactory(tv -> {

			TableRow<Flight> row = new TableRow<>();
			row.setStyle("-fx-background-color: #F0F8FF;"); // Default row background color
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #ADD8E6;")); // Light blue on hover
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #F0F8FF;")); // Revert back
			return row;

		});

		return table;

	}

	private void loadFlightFile(Stage primaryStage) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Flight File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		flightFile = fileChooser.showOpenDialog(primaryStage);

		if (flightFile != null) {

			flights.clear();
			flightSelection.getItems().clear();

			try (Scanner scanner = new Scanner(flightFile)) {

				while (scanner.hasNextLine()) {

					String line = scanner.nextLine().trim();

					if (!line.isEmpty() && !line.startsWith("FlightID")) {
						String[] data = line.split(",");

						if (data.length == 3) {
							Flight newFlight = new Flight(data[0].trim(), data[1].trim(), data[2].trim());

							boolean exists = false;
							for (Flight flight : flights) {

								if (flight.getId().equals(newFlight.getId())) {
									exists = true;
									break;

								}
							}

							if (!exists) {

								flights.add(newFlight);
								flightSelection.getItems().add(newFlight.getId());

							}
						}
					}
				}

				flightTable.refresh();

			} catch (IOException e) {

				showAlert("Error", "Failed to load flight data.");

			}
		}

	}

	private void saveFlightFile(Stage primaryStage) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Flight File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		File saveFile = fileChooser.showSaveDialog(primaryStage);

		if (saveFile != null) {

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {

				for (Flight flight : flights) {

					writer.write(
							String.format("%s,%s,%s", flight.getId(), flight.getDestination(), flight.getStatus()));

					writer.newLine();
				}

			} catch (IOException e) {

				showAlert("Error", "Failed to save flight data.");

			}
		}
	}

	// ======================== Passenger Management Tab ==========================

	private Tab createPassengerManagementTab(Stage primaryStage) {
		Tab tab = new Tab("Passenger Management");

		BorderPane passengerPane = new BorderPane();

		// Create TableView
		passengerTable = setupPassengerTable();

		Button loadPassengersButton = new Button("Load Passengers");
		Button savePassengersButton = new Button("Save Passengers");

		Button addPassengerButton = new Button("Add Passenger");
		Button updatePassengerButton = new Button("Update Passenger");
		Button removePassengerButton = new Button("Remove Passenger");
		Button searchPassengerButton = new Button("Search Passenger");
		Button printAllPassengersButton = new Button("Print All Passengers");
		Button printSpecificPassengerButton = new Button("Print Specific Passenger Info");

		butoonEffect(loadPassengersButton);
		butoonEffect(savePassengersButton);
		butoonEffect(addPassengerButton);
		butoonEffect(updatePassengerButton);
		butoonEffect(removePassengerButton);
		butoonEffect(searchPassengerButton);
		butoonEffect(printAllPassengersButton);
		butoonEffect(printSpecificPassengerButton);

		loadPassengersButton.setOnAction(e -> loadPassengerFile(primaryStage));
		savePassengersButton.setOnAction(e -> savePassengerFile(primaryStage));

		addPassengerButton.setOnAction(e -> insertPassenger(passengerPane));
		updatePassengerButton.setOnAction(e -> updatePassenger(passengerPane));
		removePassengerButton.setOnAction(e -> removePassenger(passengerPane));
		searchPassengerButton.setOnAction(e -> searchPassenger(passengerPane));
		printAllPassengersButton.setOnAction(e -> printAllPassengers(passengerPane));
		printSpecificPassengerButton.setOnAction(e -> printSpecificPassenger(passengerPane));

		HBox topBarContainer = new HBox(10, loadPassengersButton, savePassengersButton, addPassengerButton,
				removePassengerButton, updatePassengerButton, searchPassengerButton, printAllPassengersButton,
				printSpecificPassengerButton);
		topBarContainer.setAlignment(Pos.CENTER_LEFT);
		topBarContainer.setPadding(new Insets(10));
		topBarContainer.setStyle("-fx-background-color: #e0e7ef; -fx-border-color: #d3d3d3; -fx-border-width: 1;");

		VBox tableContainer = new VBox(passengerTable);
		VBox.setVgrow(passengerTable, Priority.ALWAYS);
		tableContainer.setPadding(new Insets(10));
		tableContainer.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d3d3d3; -fx-border-width: 1;");

		passengerPane.setTop(topBarContainer);
		passengerPane.setLeft(tableContainer);
		tab.setContent(passengerPane);
		return tab;
	}

	private void insertPassenger(BorderPane passengerPane) {

		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);
		formGrid.setAlignment(Pos.CENTER);

		Label lblID = new Label("Passenger ID:");
		TextField tfID = new TextField();

		Label lblName = new Label("Name:");
		TextField tfName = new TextField();

		Label lblFlightID = new Label("Flight ID:");
		TextField tfFlightID = new TextField();

		Label lblStatus = new Label("Status:");
		ComboBox<String> cbStatus = new ComboBox<>();
		cbStatus.getItems().addAll("Regular", "VIP");

		Button btAdd = new Button("Add");
		Button btClear = new Button("Clear");

		formGrid.add(lblID, 0, 0);
		formGrid.add(tfID, 1, 0);
		formGrid.add(lblName, 0, 1);
		formGrid.add(tfName, 1, 1);
		formGrid.add(lblFlightID, 0, 2);
		formGrid.add(tfFlightID, 1, 2);
		formGrid.add(lblStatus, 0, 3);
		formGrid.add(cbStatus, 1, 3);
		formGrid.add(btAdd, 0, 4);
		formGrid.add(btClear, 1, 4);

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		butoonEffect(btAdd);
		butoonEffect(btClear);
		IconedTextFieled(tfID, lblID);
		IconedTextFieled(tfName, lblName);
		IconedTextFieled(tfFlightID, lblFlightID);
		IconedTextFieled(cbStatus, lblStatus);

		btClear.setOnAction(e -> {

			tfID.clear();
			tfName.clear();
			tfFlightID.clear();
			cbStatus.getSelectionModel().clearSelection();

		});

		// Add button action: Adds a new passenger to the main queue
		btAdd.setOnAction(e -> {

			String id = tfID.getText().trim();
			String name = tfName.getText().trim();
			String flightID = tfFlightID.getText().trim();
			String status = cbStatus.getValue();

			if (id.isEmpty() || name.isEmpty() || flightID.isEmpty() || status == null) {

				showAlert("Error", "All fields are required. Please fill in all the details.");
				return;

			}

			if (!id.matches("[a-zA-Z0-9]*")) {

				showAlert("Error", "Passenger ID must contain only letters and numbers.");
				return;

			}

			if (!name.matches("[a-zA-Z ]*")) {

				showAlert("Error", "Name must contain only letters and spaces.");
				return;

			}

			if (!flightID.matches("[a-zA-Z0-9]*")) {

				showAlert("Error", "Flight ID must contain only letters and numbers.");
				return;

			}

			// Check for flight
			Flight flight = flightList.findFlightByID(flightID);
			if (flight == null) {

				showAlert("Error", "Cannot add passenger. The selected flight does not exist.");
				return;

			}

			boolean passengerExists = false;
			for (Passenger passenger : passengers) {

				if (passenger.getPassengerID().equals(id)) {
					passengerExists = true;
					break;
				}
			}

			if (!passengerExists && vipQueue.contains(new Passenger(id))) {

				passengerExists = true;

			}

			if (!passengerExists && regularQueue.contains(new Passenger(id))) {

				passengerExists = true;

			}

			if (passengerExists) {

				showAlert("Error", "Passenger with ID " + id + " already exists.");
				return;

			}

			Passenger newPassenger = new Passenger(id, name, flightID, status);
			passengersQueue.enqueue(newPassenger);
			passengers.add(newPassenger);

			logAction("Add Passenger", "Passenger ID: " + newPassenger.getPassengerID());
			showAlert("Success", "Passenger added successfully.");

		});

		// Use the existing TableView defined in the main interface
		HBox mainLayout = new HBox(20, passengerTable, formGrid);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		// Set the combined layout to the center of the BorderPane
		passengerPane.setCenter(mainLayout);

	}

	private void removePassenger(BorderPane passengerPane) {
		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);
		formGrid.setAlignment(Pos.CENTER);

		Label lblID = new Label("Passenger ID:");
		TextField tfID = new TextField();

		Button btRemove = new Button("Remove");
		Button btClear = new Button("Clear");

		formGrid.add(lblID, 0, 0);
		formGrid.add(tfID, 1, 0);
		formGrid.add(btRemove, 0, 1);
		formGrid.add(btClear, 1, 1);

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		butoonEffect(btRemove);
		butoonEffect(btClear);
		IconedTextFieled(tfID, lblID);

		btRemove.setOnAction(e -> {
			String id = tfID.getText().trim();

			if (id.isEmpty()) {
				showAlert("Error", "Please enter a Passenger ID.");
				return;
			}
			if (!id.matches("[a-zA-Z0-9]+")) {
				showAlert("Error", "Passenger ID must contain only letters and numbers.");
				return;
			}

			Passenger passengerToRemove = findPassengerByID(id);

			if (passengerToRemove == null) {
				showAlert("Error", "Passenger not found.");
				return;
			}

			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmAlert.setTitle("Confirm Deletion");
			confirmAlert.setHeaderText("Are you sure you want to remove this passenger?");
			confirmAlert.setContentText(
					"Passenger ID: " + passengerToRemove.getPassengerID() + "\nName: " + passengerToRemove.getName());

			confirmAlert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					removePassengerFromQueues(passengerToRemove);
					passengers.remove(passengerToRemove);

					logAction("Remove Passenger", "Removed Passenger ID: " + passengerToRemove.getPassengerID());
					showAlert("Success", "Passenger removed successfully.");
				} else {
					showAlert("Info", "Passenger removal canceled.");
				}
			});
		});
		btClear.setOnAction(e -> {
			tfID.clear();
		});

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(passengerTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(formGrid);

		mainLayout.getChildren().add(sidePanel);
		passengerPane.setCenter(mainLayout);
	}

	private void updatePassenger(BorderPane passengerPane) {
		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);
		formGrid.setAlignment(Pos.CENTER);

		Label lblID = new Label("Passenger ID:");
		TextField tfID = new TextField();

		Label lblName = new Label("Name:");
		TextField tfName = new TextField();
		tfName.setDisable(true);

		Label lblFlightID = new Label("Flight ID:");
		ComboBox<String> cbFlightID = new ComboBox<>();
		cbFlightID.setDisable(true);

		Label lblStatus = new Label("Status:");
		ComboBox<String> cbStatus = new ComboBox<>();
		cbStatus.getItems().addAll("Regular", "VIP");
		cbStatus.setDisable(true);

		Button btFind = new Button("Find");
		Button btUpdate = new Button("Update");
		Button btClear = new Button("Clear");

		formGrid.add(lblID, 0, 0);
		formGrid.add(tfID, 1, 0);
		formGrid.add(btFind, 2, 0);
		formGrid.add(lblName, 0, 1);
		formGrid.add(tfName, 1, 1);
		formGrid.add(lblFlightID, 0, 2);
		formGrid.add(cbFlightID, 1, 2);
		formGrid.add(lblStatus, 0, 3);
		formGrid.add(cbStatus, 1, 3);
		formGrid.add(btUpdate, 0, 4);
		formGrid.add(btClear, 1, 4);

		butoonEffect(btFind);
		butoonEffect(btUpdate);
		butoonEffect(btClear);
		IconedTextFieled(tfID, lblID);
		IconedTextFieled(tfName, lblName);
		IconedTextFieled(cbFlightID, lblFlightID);
		IconedTextFieled(cbStatus, lblStatus);

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		// Find Button Action
		btFind.setOnAction(e -> {
			String id = tfID.getText().trim();

			if (id.isEmpty()) {
				showAlert("Error", "Please enter a Passenger ID.");
				return;
			}
			// Search in both VIP and Regular queues
			Passenger foundPassenger = vipQueue.findById(id);
			if (foundPassenger == null) {
				foundPassenger = regularQueue.findById(id);
			}
			if (foundPassenger != null) {
				// Populate fields with passenger data
				tfName.setText(foundPassenger.getName());
				cbStatus.setValue(foundPassenger.getStatus());
				tfName.setDisable(false);
				cbStatus.setDisable(false);

				// Add flight IDs to ComboBox
				cbFlightID.getItems().clear(); // Clear any previous entries
				for (Flight flight : flights) { // Assuming 'flights' is a list of flight objects
					cbFlightID.getItems().add(flight.getId());
				}
				cbFlightID.setDisable(false);
				cbFlightID.setValue(foundPassenger.getFlightID()); // Set the current flight ID
			} else {
				showAlert("Not Found", "No passenger found with ID: " + id);
				clearForm(tfID, tfName, cbFlightID, cbStatus);
			}
		});

		btUpdate.setOnAction(e -> {
			String id = tfID.getText().trim();
			String name = tfName.getText().trim();
			String flightID = cbFlightID.getValue(); // Now getting the selected flight ID from ComboBox
			String status = cbStatus.getValue();

			if (id.isEmpty() || name.isEmpty() || flightID == null || status == null) {
				showAlert("Error", "All fields are required.");
				return;
			}
			if (!name.matches("[a-zA-Z ]+")) {
				showAlert("Error", "Name must contain only letters and spaces.");
				return;
			}

			Passenger foundPassenger = passengersQueue.findById(id);
			if (foundPassenger != null) {
				foundPassenger.setName(name);
				foundPassenger.setFlightID(flightID); // Update the flight ID
				foundPassenger.setStatus(status);

				passengerTable.refresh();

				logAction("Update Passenger", "Updated Passenger ID: " + foundPassenger.getPassengerID()
						+ ", New Name: " + name + ", New Flight ID: " + flightID + ", New Status: " + status);

				showAlert("Success", "Passenger updated successfully.");
			} else {
				showAlert("Error", "Passenger not found.");
			}
		});

		btClear.setOnAction(e -> clearForm(tfID, tfName, cbFlightID, cbStatus));

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(passengerTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(formGrid);

		mainLayout.getChildren().add(sidePanel);
		passengerPane.setCenter(mainLayout);
	}

	private void searchPassenger(BorderPane passengerPane) {

		GridPane formGrid = new GridPane();
		formGrid.setPadding(new Insets(15));
		formGrid.setHgap(10);
		formGrid.setVgap(12);
		formGrid.setAlignment(Pos.CENTER);

		Label lblID = new Label("Passenger ID:");
		TextField tfID = new TextField();

		Button btSearch = new Button("Search");
		Button btClear = new Button("Clear");

		formGrid.add(lblID, 0, 0);
		formGrid.add(tfID, 1, 0);
		formGrid.add(btSearch, 0, 1);
		formGrid.add(btClear, 1, 1);

		IconedTextFieled(tfID, lblID);
		butoonEffect(btSearch);
		butoonEffect(btClear);

		formGrid.setStyle("-fx-border-color: #6A5ACD;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;"
				+ "-fx-background-radius: 50 0 0 50");

		btClear.setOnAction(e -> {

			tfID.clear(); // This will clear the Passenger ID field.

		});

		btSearch.setOnAction(e -> {

			String id = tfID.getText().trim();

			if (id.isEmpty()) {

				showAlert("Error", "Please enter a Passenger ID.");
				return;

			}

			Passenger foundPassenger = null;

			// Search only in passengersQueue
			CustomQueue.Node currentPassenger = passengersQueue.front;

			while (currentPassenger != null) {

				if (currentPassenger.data.getPassengerID().equals(id)) {
					foundPassenger = currentPassenger.data;
					break;

				}
				currentPassenger = currentPassenger.next;
			}

			// If not found in the queue, search in the passengerTable
			if (foundPassenger == null) {

				ObservableList<Passenger> passengersInTable = passengerTable.getItems();
				for (Passenger p : passengersInTable) {

					if (p.getPassengerID().equals(id)) {
						foundPassenger = p;
						break;

					}
				}
			}

			// Show the result
			if (foundPassenger != null) {
				logAction("Search Passenger",
						"Passenger ID: " + foundPassenger.getPassengerID() + ", Name: " + foundPassenger.getName());

				Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
				infoAlert.setTitle("Passenger Found");
				infoAlert.setHeaderText("Passenger Details");
				infoAlert.setContentText("ID: " + foundPassenger.getPassengerID() + "\n" + "Name: "
						+ foundPassenger.getName() + "\n" + "Flight ID: " + foundPassenger.getFlightID() + "\n"
						+ "Status: " + foundPassenger.getStatus());
				infoAlert.showAndWait();
			} else {
				logAction("Search Passenger", "Passenger ID: " + id + " not found.");
				showAlert("Error", "Passenger not found.");
			}
		});
		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(15));
		mainLayout.setAlignment(Pos.CENTER_LEFT);

		mainLayout.getChildren().add(passengerTable);

		VBox sidePanel = new VBox();
		sidePanel.setPadding(new Insets(15));
		sidePanel.setAlignment(Pos.CENTER);
		sidePanel.getChildren().add(formGrid);

		mainLayout.getChildren().add(sidePanel);
		passengerPane.setCenter(mainLayout);
	}

	private void printAllPassengers(BorderPane passengerPane) {

		VBox mainLayout = new VBox(15);
		mainLayout.setPadding(new Insets(20));
		mainLayout.setAlignment(Pos.CENTER);

		Label lblTitle = new Label("All Passengers");
		lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		TableView<Passenger> passengersTableView = new TableView<>();

		TableColumn<Passenger, String> idColumn = new TableColumn<>("Passenger ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("passengerID"));
		idColumn.setPrefWidth(100);
		idColumn.setMaxWidth(150);

		TableColumn<Passenger, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(150);

		TableColumn<Passenger, String> flightIDColumn = new TableColumn<>("Flight ID");
		flightIDColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));
		flightIDColumn.setPrefWidth(100);

		TableColumn<Passenger, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusColumn.setPrefWidth(120);

		passengersTableView.getColumns().addAll(idColumn, nameColumn, flightIDColumn, statusColumn);

		// Add passengers from the passengersQueue to the table
		CustomQueue.Node currentPassenger = passengersQueue.front;
		while (currentPassenger != null) {
			passengersTableView.getItems().add(currentPassenger.data);
			currentPassenger = currentPassenger.next;
		}

		// Add passengers from the passengerTable to the table
		ObservableList<Passenger> passengersInTable = passengerTable.getItems();
		passengersTableView.getItems().addAll(passengersInTable);

		logAction("Print All Passengers", "Displayed all passengers in the system.");

		mainLayout.getChildren().addAll(lblTitle, passengersTableView);

		HBox mainContent = new HBox(20, passengerTable, mainLayout);
		mainContent.setPadding(new Insets(10));
		mainContent.setAlignment(Pos.CENTER_LEFT);

		passengerPane.setCenter(mainContent);

		passengersTableView.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14; " + "-fx-border-width: 1; "
				+ "-fx-border-radius: 10; " + "-fx-background-color: #E6E6FA; " + "-fx-background-radius: 10;");

		passengersTableView.setRowFactory(tv -> {
			TableRow<Passenger> row = new TableRow<>();
			row.setStyle("-fx-background-color: #E6E6FA;");
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #B0C4DE;"));
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #E6E6FA;"));
			return row;
		});

		passengersTableView.setPrefWidth(470);
	}

	private void printSpecificPassenger(BorderPane passengerPane) {
		VBox mainLayout = new VBox(15);
		mainLayout.setPadding(new Insets(20));
		mainLayout.setAlignment(Pos.CENTER);

		Label lblTitle = new Label("Search Specific Passenger");
		lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		Label lblID = new Label("Select Passenger ID:");
		ComboBox<String> cbID = new ComboBox<>();
		cbID.setPromptText("Select Passenger ID");

		// Populate ComboBox with Passenger IDs from passengersQueue only
		CustomQueue.Node currentPassenger = passengersQueue.front;
		while (currentPassenger != null) {
			cbID.getItems().add(currentPassenger.data.getPassengerID());
			currentPassenger = currentPassenger.next;
		}

		TableView<Passenger> passengerDetailsTable = new TableView<>();

		passengerDetailsTable.setPrefWidth(600);
		passengerDetailsTable.setPrefHeight(300);

		TableColumn<Passenger, String> idColumn = new TableColumn<>("Passenger ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("passengerID"));
		idColumn.setPrefWidth(150);

		TableColumn<Passenger, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(170);

		TableColumn<Passenger, String> flightIDColumn = new TableColumn<>("Flight ID");
		flightIDColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));
		flightIDColumn.setPrefWidth(150);

		TableColumn<Passenger, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusColumn.setPrefWidth(130);

		// Add columns to the TableView
		passengerDetailsTable.getColumns().addAll(idColumn, nameColumn, flightIDColumn, statusColumn);

		Button btSearch = new Button("Search");
		Button btClear = new Button("Clear");

		butoonEffect(btSearch);
		butoonEffect(btClear);

		btSearch.setOnAction(e -> {
			String id = cbID.getValue(); // Get selected ID from ComboBox

			if (id == null || id.isEmpty()) {
				showAlert("Error", "Please select a Passenger ID.");
				passengerDetailsTable.getItems().clear(); // Clear previous results
				return;
			}

			Passenger foundPassenger = null;

			// Search only in passengersQueue
			CustomQueue.Node current = passengersQueue.front;
			while (current != null) {
				if (current.data.getPassengerID().equals(id)) {
					foundPassenger = current.data;
					break;
				}
				current = current.next;
			}

			if (foundPassenger != null) {
				// Clear previous items and add the found passenger to the table
				passengerDetailsTable.getItems().clear();
				passengerDetailsTable.getItems().add(foundPassenger);
				logAction("Search Specific Passenger", "Found passenger with ID: " + id);
			} else {
				// Clear the table if not found
				passengerDetailsTable.getItems().clear();
				showAlert("Error", "Passenger not found.");
			}
		});

		btClear.setOnAction(e -> {
			cbID.setValue(null); // Clear ComboBox selection
			passengerDetailsTable.getItems().clear();
		});

		HBox buttons = new HBox(10, btSearch, btClear);
		buttons.setAlignment(Pos.CENTER);

		mainLayout.getChildren().addAll(lblTitle, lblID, cbID, passengerDetailsTable, buttons);

		HBox mainContent = new HBox(20, passengerTable, mainLayout);
		mainContent.setPadding(new Insets(10));
		mainContent.setAlignment(Pos.CENTER_LEFT);

		passengerPane.setCenter(mainContent);

		passengerDetailsTable.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14; " + "-fx-border-width: 1; "
				+ "-fx-border-radius: 10; " + "-fx-background-color: #E6E6FA; " + "-fx-background-radius: 10;");

		passengerDetailsTable.setRowFactory(tv -> {
			TableRow<Passenger> row = new TableRow<>();
			row.setStyle("-fx-background-color: #E6E6FA;");
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #B0C4DE;"));
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #E6E6FA;"));
			return row;
		});
	}

	private void clearForm(TextField tfID, TextField tfName, ComboBox<String> cbFlightID, ComboBox<String> cbStatus) {
		tfID.clear();
		tfName.clear();
//		cbFlightID.clear();
		cbStatus.getSelectionModel().clearSelection();
		tfName.setDisable(true);
		cbFlightID.setDisable(true);
		cbStatus.setDisable(true);
	}

	private Passenger findPassengerByID(String id) {
		for (Passenger p : passengers) {
			if (p.getPassengerID().equals(id)) {
				return p;
			}
		}
		CustomQueue.Node current = vipQueue.getHead();
		while (current != null) {
			if (current.data.getPassengerID().equals(id)) {
				return current.data;
			}
			current = current.next;
		}
		current = regularQueue.getHead();
		while (current != null) {
			if (current.data.getPassengerID().equals(id)) {
				return current.data;
			}
			current = current.next;
		}
		return null;
	}

	private void removePassengerFromQueues(Passenger passenger) {
		if (vipQueue.contains(passenger)) {
			vipQueue.remove(passenger);
		} else if (regularQueue.contains(passenger)) {
			regularQueue.remove(passenger);
		}
		if (passengersQueue.contains(passenger)) {
			passengersQueue.remove(passenger);
		}
	}

	private TableView<Passenger> setupPassengerTable() {
		TableView<Passenger> table = new TableView<>();
		table.setItems(passengers);

		TableColumn<Passenger, String> idColumn = new TableColumn<>("Passenger ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("passengerID"));
		idColumn.setPrefWidth(100);

		TableColumn<Passenger, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(100);

		TableColumn<Passenger, String> flightColumn = new TableColumn<>("Flight ID");
		flightColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));
		flightColumn.setPrefWidth(100);

		TableColumn<Passenger, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusColumn.setPrefWidth(100);

		table.getColumns().addAll(idColumn, nameColumn, flightColumn, statusColumn);

		table.setPrefWidth(550);

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14px; " + "-fx-border-width: 1; "
				+ "-fx-background-color: #F0F8FF; " + "-fx-background-radius: 10px; " + "-fx-font-weight: bold;");

		table.setRowFactory(tv -> {
			TableRow<Passenger> row = new TableRow<>();
			row.setStyle("-fx-background-color: #F0F8FF;");
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #ADD8E6;"));
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #F0F8FF;"));
			return row;
		});
		return table;
	}

	private void loadPassengerFile(Stage primaryStage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Passenger File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		passengerFile = fileChooser.showOpenDialog(primaryStage);

		if (passengerFile != null) {
			passengers.clear();
			vipQueue.clear();
			regularQueue.clear();
			passengersQueue.clear();

			boolean hasError = false;

			try (Scanner scanner = new Scanner(passengerFile)) {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine().trim();
					if (!line.isEmpty() && !line.startsWith("PassengerID")) {
						String[] data = line.split(",");
						if (data.length == 4) {
							String id = data[0].trim();
							String name = data[1].trim();
							String flightID = data[2].trim();
							String status = data[3].trim();

							if (!id.matches("[a-zA-Z0-9]*") || !name.matches("[a-zA-Z ]*")
									|| !flightID.matches("[a-zA-Z0-9]*")) {
								continue;
							}
							Passenger newPassenger = new Passenger(id, name, flightID, status);

							if ("VIP".equalsIgnoreCase(status)) {
								vipQueue.enqueue(newPassenger);
							} else {
								regularQueue.enqueue(newPassenger);
							}
							passengersQueue.enqueue(newPassenger);
							passengers.add(newPassenger);
						} else {
							hasError = true;
						}
					}
				}
				if (!hasError) {
					showAlert("Success", "Data loaded successfully!");
				}
			} catch (IOException e) {
				hasError = true;
				showAlert("Error", "Failed to load passenger data.");
			} catch (Exception e) {
				hasError = true;
				showAlert("Error", "An unexpected error occurred while processing the file.");
			}
			if (hasError) {
				showAlert("Error", "Some errors were found while processing the file.");
			}
			passengerTable.refresh();
		}
	}

	private void savePassengerFile(Stage primaryStage) {
		if (passengerFile == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Passenger File");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
			passengerFile = fileChooser.showSaveDialog(primaryStage);
		}
		if (passengerFile != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(passengerFile))) {
				for (Passenger passenger : passengers) {
					writer.write(String.format("%s,%s,%s,%s", passenger.getId(), passenger.getName(),
							passenger.getFlightId(), passenger.getStatus()));
					writer.newLine();
				}
			} catch (IOException e) {
				showAlert("Error", "Failed to save passenger data.");
			}
		}
	}

	// ======================== Operation Management Tab ==========================

	public Tab createOperationTab(Stage primaryStage) {

		Tab tab = new Tab("Operations");
		BorderPane operationPane = new BorderPane();

		operationTable = setupOperationTable();
		operationTable.setItems(operations);

		HBox buttonsBox = new HBox(10);
		buttonsBox.setAlignment(Pos.CENTER);
		buttonsBox.setPadding(new Insets(10));

		Button checkInButton = new Button("Check-In Passenger");
		Button boardButton = new Button("Board Passenger");
		Button cancelButton = new Button("Cancel Passenger");
		Button undoButton = new Button("Undo Last Operation");
		Button redoButton = new Button("Redo Last Operation");

		butoonEffect(checkInButton);
		butoonEffect(boardButton);
		butoonEffect(cancelButton);
		butoonEffect(undoButton);
		butoonEffect(redoButton);

		buttonsBox.getChildren().addAll(checkInButton, boardButton, cancelButton, undoButton, redoButton);

		checkInButton.setOnAction(e -> {

			checkInPassenger(primaryStage);

		});

		boardButton.setOnAction(e -> boardPassenger(primaryStage));
		cancelButton.setOnAction(e -> cancelPassenger(primaryStage));
		undoButton.setOnAction(e -> undoOperation(null));
		redoButton.setOnAction(e -> redoOperation(null));

		HBox inputBox = new HBox(10);
		inputBox.setPadding(new Insets(10));
		inputBox.setVisible(false);

		TextField tfPassengerID = new TextField();
		TextField tfName = new TextField();
		TextField tfFlightID = new TextField();
		ComboBox<String> cbStatus = new ComboBox<>();
		cbStatus.getItems().addAll("Regular", "VIP");

		inputBox.getChildren().addAll(new Label("Passenger ID:"), tfPassengerID, new Label("Name:"), tfName,
				new Label("Flight ID:"), tfFlightID, new Label("Status:"), cbStatus);

		operationPane.setTop(buttonsBox);
		operationPane.setCenter(operationTable);
		operationPane.setBottom(inputBox);

		tab.setContent(operationPane);
		tab.setClosable(false);

		return tab;

	}

	private void checkInPassenger(Stage checkInStage) {

		checkInStage.setTitle("Check In Passengers");

		VBox mainLayout = new VBox(10);
		HBox tableLayout = new HBox(10);
		GridPane inputLayout = new GridPane();
		inputLayout.setVgap(10);
		inputLayout.setHgap(10);
		inputLayout.setPadding(new Insets(10));

		Label vipLabel = new Label("VIP Passengers in the Queue");
		Label regularLabel = new Label("Regular Passengers in the Queue");

		setupPassengerTables();

		vipTable.setPrefWidth(400);
		regularTable.setPrefWidth(400);
		vipTable.setPrefHeight(400);
		regularTable.setPrefHeight(400);

		VBox vipContainer = new VBox(5, vipLabel, vipTable);
		VBox regularContainer = new VBox(5, regularLabel, regularTable);
		tableLayout.getChildren().addAll(vipContainer, regularContainer);

		Label idLabel = new Label("Passenger ID");
		TextField idField = new TextField();
		Label nameLabel = new Label("Name");
		TextField nameField = new TextField();
		Label flightLabel = new Label("Flight ID");
		TextField flightField = new TextField();
		Label statusLabel = new Label("Status");

		ComboBox<String> statusComboBox = new ComboBox<>();

		statusComboBox.getItems().addAll("VIP", "Regular");
		statusComboBox.setValue("");

		inputLayout.add(idLabel, 0, 0);
		inputLayout.add(idField, 1, 0);
		inputLayout.add(nameLabel, 0, 1);
		inputLayout.add(nameField, 1, 1);
		inputLayout.add(flightLabel, 0, 2);
		inputLayout.add(flightField, 1, 2);
		inputLayout.add(statusLabel, 0, 3);
		inputLayout.add(statusComboBox, 1, 3);

		Button checkInButton = new Button("Check In");
		inputLayout.add(checkInButton, 0, 4, 2, 1);

		Button backButton = new Button("<--- Back");
		backButton.setOnAction(backEvent -> showMainTabs(checkInStage));

		Button checkInAllButton = new Button("Check-In All Passengers");

		butoonEffect(checkInButton);
		butoonEffect(backButton);
		butoonEffect(checkInButton);
		butoonEffect(checkInAllButton);

		HBox buttonLayout = new HBox(10, backButton);
		buttonLayout.setAlignment(Pos.CENTER);

		checkInButton.setOnAction(e -> {

			String passengerID = idField.getText().trim();
			String passengerName = nameField.getText().trim();
			String flightID = flightField.getText().trim();
			String status = statusComboBox.getValue();

			Passenger passengerToCheckIn = findPassenger(passengerID, passengerName, flightID, status);

			if (passengerToCheckIn != null) {

				// If the passenger is a VIP
				if (status.equals("VIP")) {

					vipQueue.enqueue(passengerToCheckIn);
					vipPassengerList.add(passengerToCheckIn);
					totalCheckedInVIP++;

				} else {
					// If the passenger is Regular

					regularQueue.enqueue(passengerToCheckIn);
					regularPassengerList.add(passengerToCheckIn);
					totalCheckedInRegular++;

				}

				// **Remove passenger from primary Queue
				passengersQueue.remove(passengerToCheckIn); // Remove the passenger from the underlying Queue

				// Remove the passenger from the TableView
				passengerTable.getItems().remove(passengerToCheckIn);
				passengerTable.refresh();

				String operationDetails = "Check-in " + passengerName + " (Flight " + flightID + ")";
				updateOperationTable(operationDetails, flightID, status, passengerName);

				// Clean up fields
				idField.clear();
				nameField.clear();
				flightField.clear();
				statusComboBox.setValue("");

				logAction("Check-In", operationDetails);
				showAlert("Success", "Passenger checked in successfully!");

			} else {

				showAlert("Error", "Passenger not found or invalid data.");

			}

		});

		checkInAllButton.setOnAction(e -> {

			while (!passengersQueue.isEmpty()) {

				// Extract the first passenger from the Queue
				Passenger passenger = passengersQueue.dequeue();

				if (passenger.getStatus().equalsIgnoreCase("VIP")) {

					vipQueue.enqueue(passenger);
					vipPassengerList.add(passenger);
					totalCheckedInVIP++;

				} else {

					regularQueue.enqueue(passenger);
					regularPassengerList.add(passenger);
					totalCheckedInRegular++;

				}

				// Remove the occupant from the TableView
				passengerTable.getItems().remove(passenger);

				String operationDetails = "Check-In (Flight " + passenger.getFlightID() + ")";
				undoStack.push(operationDetails);

				logAction("Check-In", operationDetails);
				updateOperationTable("Check-In Passenger", passenger.getFlightID(), passenger.getStatus(),
						passenger.getName());

			}

			// Update the table after completion

			passengerTable.refresh();
			syncVIPTable();
			syncRegularTable();
			updateOperationTable("Check-In All Passengers", "---", "---", "---");

//			updateOperationTable("Check-In All Passengers");
			showAlert("Success", "All passengers have been checked in successfully!");

		});

		inputLayout.add(checkInAllButton, 0, 5, 2, 1);
		mainLayout.getChildren().addAll(tableLayout, inputLayout, buttonLayout);

		Scene scene = new Scene(mainLayout, 850, 700);

		checkInStage.setScene(scene);
		checkInStage.show();

	}

	private void updateOperationTable(String actionDescription, String flightID, String passengerType,
			String passengerName) {

		String undoStackMessage = "Undo Stack (" + flightID + "): " + actionDescription;

		// Update queues
		String regularQueueMessage = passengerType.equalsIgnoreCase("Regular") ? passengerName : "---";
		String vipQueueMessage = passengerType.equalsIgnoreCase("VIP") ? passengerName : "---";

		Operation newOperation = new Operation(stepCounter++, actionDescription, regularQueueMessage, vipQueueMessage,
				undoStackMessage, "---", // Redo Stack
				"---", // Boarded Passengers
				"---" // Canceled Passengers

		);

		// Add the new process to the list
		operations.add(newOperation);

		// Update the table automatically
		operationTable.refresh();

	}

	public void boardPassenger(Stage boardStage) {

		boardStage.setTitle("Move Passenger to Boarded List");

		VBox mainLayout = new VBox(10);
		GridPane inputLayout = new GridPane();
		inputLayout.setVgap(10);
		inputLayout.setHgap(10);
		inputLayout.setPadding(new Insets(10));

		Label tableLabel = new Label("Move a passenger from the queue to the LinkedList");

		preparePassengerTable(vipTable);
		preparePassengerTable(regularTable);
		preparePassengerTable(boardedTable);

		vipTable.setPrefWidth(400);
		regularTable.setPrefWidth(400);
		boardedTable.setPrefWidth(400);

		VBox vipContainer = new VBox(5, new Label("VIP Passengers"), vipTable);
		VBox regularContainer = new VBox(5, new Label("Regular Passengers"), regularTable);
		VBox boardedContainer = new VBox(5, new Label("Boarded Passengers (LinkedList)"), boardedTable);

		HBox allPassengersLayout = new HBox(10, vipContainer, regularContainer, boardedContainer);

		Label idLabel = new Label("Passenger ID");
		TextField idField = new TextField();

		inputLayout.add(idLabel, 0, 0);
		inputLayout.add(idField, 1, 0);

		Button boardButton = new Button("Board");
		Button backButton = new Button("<--- Back");

		butoonEffect(boardButton);
		butoonEffect(backButton);

		inputLayout.add(boardButton, 0, 1);
		inputLayout.add(backButton, 1, 1);

		// Load passengers who have been checked-in
		vipTable.setItems(vipPassengerList);
		regularTable.setItems(regularPassengerList);
		boardedTable.setItems(boardedPassengers);

		boardButton.setOnAction(e -> {
			String passengerID = idField.getText().trim();
			Passenger passengerToBoard = findPassengerByID(passengerID, vipPassengerList, regularPassengerList);

			if (passengerToBoard == null) {
				showAlert("Error", "Passenger not found or not checked-in.");
				return;
			}

			boolean isAlreadyBoarded = boardedPassengers.contains(passengerToBoard);
			if (isAlreadyBoarded) {
				showAlert("Error", "Passenger is already boarded.");
				return;
			}

			if (vipPassengerList.contains(passengerToBoard)) {
				// Removing from VIP list
				vipPassengerList.remove(passengerToBoard);
				boardedPassengersList.addFirst(passengerToBoard); // Add to boarded list
				updateBoardOperationTable(passengerToBoard.getName(), passengerToBoard.getFlightID(), "VIP");
				totalBoardedVIP++;
				totalCheckedInVIP--;
			} else {
				// Removing from Regular list
				regularPassengerList.remove(passengerToBoard);
				boardedPassengersList.addLast(passengerToBoard); // Add to boarded list
				updateBoardOperationTable(passengerToBoard.getName(), passengerToBoard.getFlightID(), "Regular");
				totalBoardedRegular++;
				totalCheckedInRegular--;
			}

			// Refreshing logic for TableViews
			vipTable.setItems(vipPassengerList); // Refresh the VIP TableView
			regularTable.setItems(regularPassengerList); // Refresh the Regular TableView
			boardedTable.getItems().add(passengerToBoard); // Add boarded passenger to boarded TableView

			// Clear the input field and show success alert
			idField.clear();
			showAlert("Success", "Passenger boarded successfully!");

			// Refresh tables
			vipTable.refresh();
			regularTable.refresh();
			boardedTable.refresh();
		});

		backButton.setOnAction(backEvent -> {

			showMainTabs(boardStage);
			boardedTable.refresh();

		});

		Button boardAllButton = new Button("Board All Passengers");
		butoonEffect(boardAllButton);

		boardAllButton.setOnAction(e -> {
			// Moving all checked-in VIP passengers to boarded list
			while (!vipPassengerList.isEmpty()) {
				Passenger vipPassenger = vipPassengerList.remove(0); // Remove from VIP list

				if (!boardedPassengersList.contains(vipPassenger)) { // Check if already boarded
					boardedPassengersList.addFirst(vipPassenger); // Add to LinkedList
					boardedPassengers.add(vipPassenger); // Update boarded table

					updateBoardOperationTable(vipPassenger.getName(), vipPassenger.getFlightID(), "VIP");

					totalBoardedVIP++;
					totalCheckedInVIP--;

					logAction("Board All - VIP",
							"Passenger ID: " + vipPassenger.getPassengerID() + ", Name: " + vipPassenger.getName()
									+ ", Flight ID: " + vipPassenger.getFlightID() + ", Status: "
									+ vipPassenger.getStatus());
				}
			}

			// Moving all checked-in Regular passengers to boarded list
			while (!regularPassengerList.isEmpty()) {

				Passenger regularPassenger = regularPassengerList.remove(0); // Remove from Regular list

				if (!boardedPassengersList.contains(regularPassenger)) { // Check if already boarded

					boardedPassengersList.addLast(regularPassenger); // Add to LinkedList
					boardedPassengers.add(regularPassenger); // Update boarded table

					updateBoardOperationTable(regularPassenger.getName(), regularPassenger.getFlightID(), "Regular");

					totalBoardedRegular++;
					totalCheckedInRegular--;

					logAction("Board All - Regular",
							"Passenger ID: " + regularPassenger.getPassengerID() + ", Name: "
									+ regularPassenger.getName() + ", Flight ID: " + regularPassenger.getFlightID()
									+ ", Status: " + regularPassenger.getStatus());
				}
			}

			// Refresh tables
			vipTable.refresh();
			regularTable.refresh();
			boardedTable.refresh();

//			updateOperationTable("Board All Passengers");

			showAlert("Success", "All passengers have been boarded successfully!");

		});

		inputLayout.add(boardAllButton, 0, 2, 2, 1);

		mainLayout.getChildren().addAll(tableLabel, allPassengersLayout, inputLayout);
		Scene scene = new Scene(mainLayout, 1400, 700);
		boardStage.setScene(scene);
		boardStage.show();

	}

	private void updateBoardOperationTable(String passengerName, String flightID, String passengerType) {

		String actionDescription = "Board Passenger: " + passengerName;
		String undoStackMessage = "Undo Stack (" + flightID + "): " + actionDescription;

		String regularQueueMessage = passengerType.equalsIgnoreCase("Regular") ? "---" : "---";
		String vipQueueMessage = passengerType.equalsIgnoreCase("VIP") ? "---" : "---";
		String boardedPassengersMessage = passengerName + " (Boarded)";

		Operation newOperation = new Operation(stepCounter++, actionDescription, regularQueueMessage, vipQueueMessage,
				undoStackMessage, "---", boardedPassengersMessage, "---");

		operations.add(newOperation);

		operationTable.refresh();

	}

	public void cancelPassenger(Stage cancelStage) {

		cancelStage.setTitle("Cancel Passenger");

		VBox mainLayout = new VBox(10);
		GridPane inputLayout = new GridPane();
		inputLayout.setVgap(10);
		inputLayout.setHgap(10);
		inputLayout.setPadding(new Insets(10));

		vipTable = createPassengerTable(vipPassengerList);
		regularTable = createPassengerTable(regularPassengerList);

		TableView<Passenger> canceledTable = createPassengerTable(canceledPassengerList);
		canceledTable.setPrefWidth(400);

		vipTable.setPrefWidth(300);
		regularTable.setPrefWidth(300);

		VBox vipContainer = new VBox(5, new Label("VIP Passengers"), vipTable);
		VBox regularContainer = new VBox(5, new Label("Regular Passengers"), regularTable);
		VBox canceledContainer = new VBox(5, new Label("Canceled Passengers"), canceledTable);

		HBox tableLayout = new HBox(10, vipContainer, regularContainer, canceledContainer);

		Label idLabel = new Label("Passenger ID:");
		TextField idField = new TextField();

		Button cancelButton = new Button("Cancel Passenger");
		Button backButton = new Button("<--- Back");

		butoonEffect(cancelButton);
		butoonEffect(backButton);

		inputLayout.add(idLabel, 0, 0);
		inputLayout.add(idField, 1, 0);
		inputLayout.add(cancelButton, 0, 1);
		inputLayout.add(backButton, 1, 1);

		cancelButton.setOnAction(e -> {

			String passengerID = idField.getText().trim();

			Passenger passengerToCancel = findPassengerByID(passengerID, vipPassengerList, regularPassengerList);

			if (passengerToCancel == null) {
				showAlert("Error", "Passenger not found in the queue.");
				return;
			}

			if (vipPassengerList.contains(passengerToCancel)) {
				vipPassengerList.remove(passengerToCancel);
				vipQueue.remove(passengerToCancel);
				totalCanceledVIP++;
				totalCheckedInVIP--;

			} else if (regularPassengerList.contains(passengerToCancel)) {
				regularPassengerList.remove(passengerToCancel);
				regularQueue.remove(passengerToCancel);
				totalCanceledRegular++;
				totalCheckedInRegular--;

			}

			canceledList.add(passengerToCancel);
			canceledPassengerList.add(passengerToCancel);

			updateCancelOperationTable(passengerToCancel);

			logAction("Cancel Passenger",
					"Passenger ID: " + passengerToCancel.getPassengerID() + ", Name: " + passengerToCancel.getName()
							+ ", Flight ID: " + passengerToCancel.getFlightID() + ", Status: "
							+ passengerToCancel.getStatus());

			vipTable.refresh();
			regularTable.refresh();
			canceledTable.refresh();

			idField.clear();

			showAlert("Success", "Passenger canceled successfully!");

		});

		backButton.setOnAction(e -> {

			showMainTabs(cancelStage);
			vipTable.refresh();
			regularTable.refresh();
			canceledTable.refresh();

		});

		mainLayout.getChildren().addAll(tableLayout, inputLayout);
		Scene scene = new Scene(mainLayout, 1050, 700);
		cancelStage.setScene(scene);
		cancelStage.show();

	}

	private void updateCancelOperationTable(Passenger passengerToCancel) {

		String actionDescription = "Cancel " + passengerToCancel.getName();
		String flightInfo = "(Flight " + passengerToCancel.getFlightID() + ")";
		String cancelMessage = actionDescription + " " + flightInfo;

		String undoStackMessage = "Undo Stack: " + cancelMessage;

		String regularQueueMessage = passengerToCancel.getStatus().equalsIgnoreCase("Regular") ? "---" : "---";
		String vipQueueMessage = passengerToCancel.getStatus().equalsIgnoreCase("VIP") ? "---" : "---";

		String canceledPassengersMessage = cancelMessage;

		Operation newOperation = new Operation(stepCounter++, cancelMessage, regularQueueMessage, vipQueueMessage,
				undoStackMessage, "---", "---", canceledPassengersMessage);

		operations.add(newOperation);

		operationTable.refresh();

	}

	private Passenger findPassenger(String passengerID, String passengerName, String flightID, String status) {

		for (Passenger passenger : passengers) {
			if (passenger.getPassengerID().equals(passengerID) && passenger.getName().equals(passengerName)
					&& passenger.getFlightID().equals(flightID) && passenger.getStatus().equals(status)) {
				return passenger;
			}
		}

		return null;

	}

	private void syncVIPTable() {

		vipTable.getItems().clear();
		CustomQueue.Node currentVIP = vipQueue.front;
		while (currentVIP != null) {

			vipTable.getItems().add(currentVIP.data);
			currentVIP = currentVIP.next;

		}

		vipTable.refresh();
	}

	private void syncRegularTable() {

		regularTable.getItems().clear();
		CustomQueue.Node currentRegular = regularQueue.front;

		while (currentRegular != null) {

			regularTable.getItems().add(currentRegular.data);
			currentRegular = currentRegular.next;

		}

		regularTable.refresh();

	}

	private void updateOperationTable(String actionDescription) {

		String regularQueueNames = regularQueue.toString(); // Display the options available in the regular queue
		String vipQueueNames = vipQueue.toString(); // Display the options available in the vip queue
		String boardedPassengersString = boardedPassengersList.isEmpty() ? "---" : boardedPassengersList.toString();
		String canceledPassengersString = canceledPassengerList.isEmpty() ? "---" : canceledPassengerList.toString();

		Operation newOperation = new Operation(stepCounter++, actionDescription, regularQueueNames, vipQueueNames,
				undoStack != null ? undoStack.toString() : "---", redoStack != null ? redoStack.toString() : "---",
				boardedPassengersString, canceledPassengersString);

		operations.add(newOperation);
		operationTable.refresh(); // Update the table

	}

	private void preparePassengerTable(TableView<Passenger> table) {

		TableColumn<Passenger, String> idColumn = new TableColumn<>("Passenger ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("passengerID"));

		TableColumn<Passenger, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Passenger, String> flightColumn = new TableColumn<>("Flight ID");
		flightColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));

		TableColumn<Passenger, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

		table.getColumns().addAll(idColumn, nameColumn, flightColumn, statusColumn);

	}

	private Passenger findPassengerByID(String passengerID, ObservableList<Passenger> vipPassengers,

			ObservableList<Passenger> regularPassengers) {
		for (Passenger passenger : vipPassengers) {

			if (passenger.getPassengerID().equals(passengerID)) {
				return passenger;

			}
		}

		for (Passenger passenger : regularPassengers) {

			if (passenger.getPassengerID().equals(passengerID)) {
				return passenger;

			}
		}

		return null; // If the passenger is not found

	}

	public void undoOperation(String flightID) {

		if (undoStack.isEmpty()) {

			showAlert("Error", "No operations to undo!");
			return;

		}

		Operation lastOperation = undoStack.pop();
		redoStack.push(lastOperation);

		switch (lastOperation.getAction()) {
		case "Check-in":

			if (lastOperation.getRegularQueue() != null) {

				regularQueue.enqueue(lastOperation.getRegularQueue());
				regularPassengerList.add(
						findPassengerByID(lastOperation.getRegularQueue(), vipPassengerList, regularPassengerList));

			} else if (lastOperation.getVipQueue() != null) {

				vipQueue.enqueue(lastOperation.getVipQueue());
				vipPassengerList
						.add(findPassengerByID(lastOperation.getVipQueue(), vipPassengerList, regularPassengerList));

			}

			break;

		case "Board Passenger":

			Passenger boardedPassenger = findPassengerByID(lastOperation.getBoardedPassengers(), vipPassengerList,
					regularPassengerList);
			boardedPassengers.remove(boardedPassenger);
			boardedPassengersList.remove(boardedPassenger);

			break;

		case "Cancel":

			Passenger canceledPassenger = findPassengerByID(lastOperation.getCanceledPassengers(), vipPassengerList,
					regularPassengerList);
			canceledPassengerList.remove(canceledPassenger);

			if (canceledPassenger.getStatus().equalsIgnoreCase("VIP")) {

				vipPassengerList.add(canceledPassenger);
				vipQueue.enqueue(canceledPassenger);

			} else {

				regularPassengerList.add(canceledPassenger);
				regularQueue.enqueue(canceledPassenger);

			}

			break;

		}

		operationTable.refresh();

	}

	public void redoOperation(String flightID) {

		if (redoStack.isEmpty()) {

			showAlert("Error", "No operations to redo!");
			return;

		}

		Operation lastOperation = redoStack.pop();
		undoStack.push(lastOperation);

		switch (lastOperation.getAction()) {

		case "Check-in":

			if (lastOperation.getRegularQueue() != null) {

				regularQueue.enqueue(lastOperation.getRegularQueue());
				regularPassengerList.add(
						findPassengerByID(lastOperation.getRegularQueue(), vipPassengerList, regularPassengerList));

			} else if (lastOperation.getVipQueue() != null) {

				vipQueue.enqueue(lastOperation.getVipQueue());
				vipPassengerList
						.add(findPassengerByID(lastOperation.getVipQueue(), vipPassengerList, regularPassengerList));

			}

			break;

		case "Board Passenger":

			Passenger boardedPassenger = findPassengerByID(lastOperation.getBoardedPassengers(), vipPassengerList,
					regularPassengerList);
			boardedPassengers.add(boardedPassenger);
			boardedPassengersList.add(boardedPassenger);

			break;

		case "Cancel":

			Passenger canceledPassenger = findPassengerByID(lastOperation.getCanceledPassengers(), vipPassengerList,
					regularPassengerList);
			canceledPassengerList.add(canceledPassenger);

			if (canceledPassenger.getStatus().equalsIgnoreCase("VIP")) {

				vipPassengerList.remove(canceledPassenger);
				vipQueue.dequeue();

			} else {

				regularPassengerList.remove(canceledPassenger);
				regularQueue.dequeue();

			}

			break;

		}

		operationTable.refresh();

	}

	private void showAlert(String title, String message) {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();

	}

	private TableView<Operation> setupOperationTable() {

		TableView<Operation> table = new TableView<>();

		TableColumn<Operation, Integer> stepColumn = new TableColumn<>("Step");
		stepColumn.setCellValueFactory(new PropertyValueFactory<>("step"));
		stepColumn.setPrefWidth(50);

		TableColumn<Operation, String> actionColumn = new TableColumn<>("Action");
		actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
		actionColumn.setPrefWidth(150);

		TableColumn<Operation, String> regularQueueColumn = new TableColumn<>("Regular Queue");
		regularQueueColumn.setCellValueFactory(new PropertyValueFactory<>("regularQueue"));
		regularQueueColumn.setPrefWidth(150);

		TableColumn<Operation, String> vipQueueColumn = new TableColumn<>("VIP Queue");
		vipQueueColumn.setCellValueFactory(new PropertyValueFactory<>("vipQueue"));
		vipQueueColumn.setPrefWidth(150);

		TableColumn<Operation, String> undoStackColumn = new TableColumn<>("Undo Stack");
		undoStackColumn.setCellValueFactory(new PropertyValueFactory<>("undoStack"));
		undoStackColumn.setPrefWidth(150);

		TableColumn<Operation, String> redoStackColumn = new TableColumn<>("Redo Stack");
		redoStackColumn.setCellValueFactory(new PropertyValueFactory<>("redoStack"));
		redoStackColumn.setPrefWidth(150);

		TableColumn<Operation, String> boardedPassengersColumn = new TableColumn<>("Boarded Passengers (LinkedList)");
		boardedPassengersColumn.setCellValueFactory(new PropertyValueFactory<>("boardedPassengers"));
		boardedPassengersColumn.setPrefWidth(200);

		TableColumn<Operation, String> canceledPassengersColumn = new TableColumn<>("Canceled Passengers");
		canceledPassengersColumn.setCellValueFactory(new PropertyValueFactory<>("canceledPassengers"));
		canceledPassengersColumn.setPrefWidth(150);

		table.getColumns().addAll(stepColumn, actionColumn, regularQueueColumn, vipQueueColumn, undoStackColumn,
				redoStackColumn, boardedPassengersColumn, canceledPassengersColumn);

		table.setPrefWidth(1000);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.setStyle("-fx-border-color: #6A5ACD; " + "-fx-font-size: 14; " + "-fx-border-width: 1; "
				+ "-fx-border-radius: 10; " + "-fx-background-color: #E6E6FA; " + "-fx-background-radius: 10;");

		table.setRowFactory(tv -> {

			TableRow<Operation> row = new TableRow<>();
			row.setStyle("-fx-background-color: #F0F8FF;"); // Default row background color
			row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #ADD8E6;")); // Light blue on hover
			row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #F0F8FF;")); // Revert back

			return row;

		});

		return table;

	}

	private void setupPassengerTables() {

		vipTable = createPassengerTable(vipPassengerList);
		regularTable = createPassengerTable(regularPassengerList);

	}

	private TableView<Passenger> createPassengerTable(ObservableList<Passenger> passengerList) {

		TableView<Passenger> table = new TableView<>();

		TableColumn<Passenger, String> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Passenger, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Passenger, String> flightColumn = new TableColumn<>("Flight ID");
		flightColumn.setCellValueFactory(new PropertyValueFactory<>("flightID"));

		TableColumn<Passenger, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

		table.setItems(passengerList);
		table.getColumns().addAll(idColumn, nameColumn, flightColumn, statusColumn);

		return table;

	}

	// ======================== Log Management Tab ==========================

	private Tab createLogFileTab(Stage primaryStage) {

		Tab tab = new Tab("Log File");

		BorderPane logPane = new BorderPane();

		TableView<LogEntry> logTable = setupLogTableView();

		Button loadLogButton = new Button("Load Log File");
		Button exportLogButton = new Button("Export Log File");
		Button clearLogButton = new Button("Clear Logs");

		butoonEffect(loadLogButton);
		butoonEffect(exportLogButton);
		butoonEffect(clearLogButton);

		// Load the log file
		loadLogButton.setOnAction(e -> loadLogFile(primaryStage, logTable));

		// Export the log to a file
		exportLogButton.setOnAction(e -> exportLogFile(primaryStage));

		// Clear the current history
		clearLogButton.setOnAction(e -> {
			logEntries.clear();
			logTable.refresh();
		});

		HBox topBar = new HBox(10, loadLogButton, exportLogButton, clearLogButton);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new Insets(10));

		VBox tableContainer = new VBox(logTable);
		VBox.setVgrow(logTable, Priority.ALWAYS);
		tableContainer.setPadding(new Insets(10));

		logPane.setTop(topBar);
		logPane.setCenter(tableContainer);

		tab.setContent(logPane);

		return tab;

	}

	private TableView<LogEntry> setupLogTableView() {

		TableView<LogEntry> logTable = new TableView<>();
		logTable.setItems(logEntries);

		TableColumn<LogEntry, String> timestampColumn = new TableColumn<>("Timestamp");
		timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		timestampColumn.setPrefWidth(150);

		TableColumn<LogEntry, String> operationColumn = new TableColumn<>("Operation");
		operationColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
		operationColumn.setPrefWidth(100);

		TableColumn<LogEntry, String> passengerColumn = new TableColumn<>("Passenger");
		passengerColumn.setCellValueFactory(new PropertyValueFactory<>("passenger"));
		passengerColumn.setPrefWidth(120);

		TableColumn<LogEntry, String> flightIdColumn = new TableColumn<>("Flight ID");
		flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
		flightIdColumn.setPrefWidth(100);

		TableColumn<LogEntry, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		descriptionColumn.setPrefWidth(250);

		logTable.getColumns().addAll(timestampColumn, operationColumn, passengerColumn, flightIdColumn,
				descriptionColumn);

		logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		logTable.setPrefHeight(400);
		logTable.setPrefWidth(700);

		descriptionColumn.setCellFactory(column -> new TableCell<LogEntry, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null && !empty) {
					setText(item);
					setStyle("-fx-background-color: #FFF3E0; -fx-text-fill: #BF360C;");
				} else {
					setText(null);
					setStyle("-fx-background-color: transparent;");
				}
			}
		});

		return logTable;
	}

	private void loadLogFile(Stage primaryStage, TableView<LogEntry> logTable) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load Log File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		File logFile = fileChooser.showOpenDialog(primaryStage);
		if (logFile != null) {
			logEntries.clear();
			try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split("\\|");
					if (parts.length == 5) {
						LogEntry entry = new LogEntry(parts[0].trim(), parts[1].trim(), parts[2].trim(),
								parts[3].trim(), parts[4].trim());
						logEntries.add(entry);
					} else {
						System.out.println("Invalid log entry: " + line);
					}
				}
				logTable.setItems(logEntries);
				logTable.refresh();
				showAlert("Success", "Log file loaded successfully!");
			} catch (IOException ex) {
				showAlert("Error", "Failed to load the log file.");
			}
		}
	}

	private void exportLogFile(Stage primaryStage) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Log File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		File logFile = fileChooser.showSaveDialog(primaryStage);
		if (logFile != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {

				for (LogEntry entry : logEntries) {
					String line = String.format("%s | %s | %s | %s | %s", entry.getTimestamp(), entry.getOperation(),
							entry.getPassenger(), entry.getFlightId(), entry.getDescription());
					writer.write(line);
					writer.newLine();
				}
				showAlert("Success", "Log file exported successfully!");
			} catch (IOException ex) {
				showAlert("Error", "Failed to export the log file.");
			}
		}
	}

	// ======================== Statistics Menu ==========================

	public Tab createStatisticsTab() {
		Tab tab = new Tab("Statistics");

		VBox layout = new VBox(15);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: #f0f0f0;");

		Label titleLabel = new Label("Passenger Statistics");
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		// Labels to display overall statistics
		Label totalCanceledVIPLabel = new Label("Total Canceled VIP Passengers: " + totalCanceledVIP);
		Label totalCanceledRegularLabel = new Label("Total Canceled Regular Passengers: " + totalCanceledRegular);
		Label totalCheckedInVIPLabel = new Label("Total Checked In VIP Passengers: " + totalCheckedInVIP);
		Label totalCheckedInRegularLabel = new Label("Total Checked In Regular Passengers: " + totalCheckedInRegular);
		Label totalBoardedVIPLabel = new Label("Total VIP Passengers Who Have Boarded: " + totalBoardedVIP);
		Label totalBoardedRegularLabel = new Label("Total Regular Passengers Who Have Boarded: " + totalBoardedRegular);

		// Set styles for the labels
		setLabelStyle(totalCanceledVIPLabel);
		setLabelStyle(totalCanceledRegularLabel);
		setLabelStyle(totalCheckedInVIPLabel);
		setLabelStyle(totalCheckedInRegularLabel);
		setLabelStyle(totalBoardedVIPLabel);
		setLabelStyle(totalBoardedRegularLabel);

		// Create a ComboBox for selecting flights
		ComboBox<Flight> flightComboBox = new ComboBox<>(flights);
		flightComboBox.setPromptText("Select a Flight");

		// Button to display statistics for the selected flight
		Button showStatisticsButton = new Button("Show Statistics");

		// TableView for displaying flight statistics
		TableView<PassengerStatistics> statisticsTable = new TableView<>();
		statisticsTable.setPrefWidth(600);

		// Define columns for PassengerStatistics
		TableColumn<PassengerStatistics, String> flightNumberColumn = new TableColumn<>("Flight Number");
		flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));

		TableColumn<PassengerStatistics, Integer> canceledVIPColumn = new TableColumn<>("Canceled VIP");
		canceledVIPColumn.setCellValueFactory(new PropertyValueFactory<>("totalCanceledVIP"));

		TableColumn<PassengerStatistics, Integer> canceledRegularColumn = new TableColumn<>("Canceled Regular");
		canceledRegularColumn.setCellValueFactory(new PropertyValueFactory<>("totalCanceledRegular"));

		TableColumn<PassengerStatistics, Integer> checkedInVIPColumn = new TableColumn<>("Checked In VIP");
		checkedInVIPColumn.setCellValueFactory(new PropertyValueFactory<>("totalCheckedInVIP"));

		TableColumn<PassengerStatistics, Integer> checkedInRegularColumn = new TableColumn<>("Checked In Regular");
		checkedInRegularColumn.setCellValueFactory(new PropertyValueFactory<>("totalCheckedInRegular"));

		TableColumn<PassengerStatistics, Integer> boardedVIPColumn = new TableColumn<>("Boarded VIP");
		boardedVIPColumn.setCellValueFactory(new PropertyValueFactory<>("totalBoardedVIP"));

		TableColumn<PassengerStatistics, Integer> boardedRegularColumn = new TableColumn<>("Boarded Regular");
		boardedRegularColumn.setCellValueFactory(new PropertyValueFactory<>("totalBoardedRegular"));

		statisticsTable.getColumns().addAll(flightNumberColumn, canceledVIPColumn, canceledRegularColumn,
				checkedInVIPColumn, checkedInRegularColumn, boardedVIPColumn, boardedRegularColumn);

		// Action for show statistics button
		showStatisticsButton.setOnAction(e -> {
			Flight selectedFlight = flightComboBox.getValue();
			if (selectedFlight != null) {
				PassengerStatistics stats = calculateStatistics(selectedFlight);
				statisticsTable.setItems(FXCollections.observableArrayList(stats));
			} else {
				showAlert("Error", "Please select a flight.");
			}
		});

		layout.getChildren().addAll(titleLabel, totalCanceledVIPLabel, totalCanceledRegularLabel,
				totalCheckedInVIPLabel, totalCheckedInRegularLabel, totalBoardedVIPLabel, totalBoardedRegularLabel,
				flightComboBox, showStatisticsButton, statisticsTable);

		tab.setContent(layout);

		return tab;
	}

// Helper method to calculate statistics for a specific flight
	private PassengerStatistics calculateStatistics(Flight flight) {
		int totalCanceledVIP = 0;
		int totalCanceledRegular = 0;
		int totalCheckedInVIP = 0;
		int totalCheckedInRegular = 0;
		int totalBoardedVIP = 0;
		int totalBoardedRegular = 0;

		for (Passenger passenger : passengers) {
			if (passenger.getFlightID().equals(flight.getId())) {
				switch (passenger.getStatus()) {
				case "Canceled":
					if (passenger.isVIP()) {
						totalCanceledVIP++;
					} else {
						totalCanceledRegular++;
					}
					break;
				case "Checked In":
					if (passenger.isVIP()) {
						totalCheckedInVIP++;
					} else {
						totalCheckedInRegular++;
					}
					break;
				case "Boarded":
					if (passenger.isVIP()) {
						totalBoardedVIP++;
					} else {
						totalBoardedRegular++;
					}
					break;
				}
			}
		}

		return new PassengerStatistics(flight.getId(), totalCanceledVIP, totalCanceledRegular, totalCheckedInVIP,
				totalCheckedInRegular, totalBoardedVIP, totalBoardedRegular);
	}

// Set style for labels
	private void setLabelStyle(Label label) {
		label.setStyle("-fx-font-size: 23px; -fx-padding: 7;");
	}

	// ======================== Main Method ==========================

	public void logAction(String action, String details) {

		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String logEntry = String.format("%s | %s | %s%n", timestamp, action, details);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {

			writer.write(logEntry);
		} catch (IOException e) {

			e.printStackTrace();
		}

		logEntries.add(new LogEntry(timestamp, action, details, logEntry, logEntry));

	}

	private void butoonEffect(Node b) {

		b.setOnMouseMoved(e -> {
			b.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 15;\n"
					+ "-fx-font-family: Times New Roman;\n" + "-fx-font-weight: Bold;\n" + "-fx-text-fill: #FFFFFF;\n"
					+ "-fx-background-color: #3498db;\n" + "-fx-border-color: #2980b9;\n" + "-fx-border-width: 3.5;\n"
					+ "-fx-background-radius: 25 25 25 25;");
		});

		b.setOnMouseExited(e -> {
			b.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 15;\n"
					+ "-fx-font-family: Times New Roman;\n" + "-fx-font-weight: Bold;\n" + "-fx-text-fill: #FFFFFF;\n"
					+ "-fx-background-color: linear-gradient(to right, #e74c3c, #2ecc71);\n"
					+ "-fx-border-color: #2980b9;\n" + "-fx-border-width: 3.5;\n"
					+ "-fx-background-radius: 25 25 25 25;");
		});
	}

	private void IconedTextFieled(Node l, Node t) {
		l.setStyle("-fx-border-color: #6A5ACD;" // SlateBlue border color
				+ "-fx-font-size: 14;\n" + "-fx-border-width: 1;" + "-fx-border-radius: 50;"
				+ "-fx-font-weight: Bold;\n" + "-fx-background-color: #E6E6FA;" // Lavender background
				+ "-fx-background-radius: 50 0 0 50");

		t.setStyle("-fx-border-radius: 0 50 50 0;\n" + "-fx-font-size: 14;\n" + "-fx-font-family: Times New Roman;\n"
				+ "-fx-font-weight: Bold;\n" + "-fx-background-color: #FFFFFF;\n" // White background for text field
				+ "-fx-border-color: #6A5ACD;\n" // SlateBlue border color
				+ "-fx-border-width: 3.5;" + "-fx-background-radius: 0 50 50 0");

	}

	public static void main(String[] args) {

		launch(args);

	}
}