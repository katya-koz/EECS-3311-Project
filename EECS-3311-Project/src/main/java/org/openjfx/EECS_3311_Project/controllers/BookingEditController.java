package org.openjfx.EECS_3311_Project.controllers;

import java.net.URL;
<<<<<<< HEAD
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import org.openjfx.EECS_3311_Project.Mediator;
=======
import java.util.ResourceBundle;

>>>>>>> refs/heads/detached
import org.openjfx.EECS_3311_Project.Session;
import org.openjfx.EECS_3311_Project.model.AccountRole;
import org.openjfx.EECS_3311_Project.model.Booking;
import org.openjfx.EECS_3311_Project.model.User;

<<<<<<< HEAD
public class BookingEditController implements Initializable {
	@FXML
	private TextField bookingName;
	@FXML
	private Text dateTime;
	@FXML
	private Text hostEmail;
	@FXML
	private Text bookingPrice;
	@FXML
	private TextField studentIdField;
	@FXML
	private TextField attendeeIdField;
	@FXML
	private VBox attendeesList;
	
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	Mediator mediator = Mediator.getInstance();
=======
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
>>>>>>> refs/heads/detached

<<<<<<< HEAD
	private Booking currentBooking;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Booking booking= Session.getNewBooking();
		User user = Session.getUser();
		
		this.currentBooking = booking;
		setBookingInfo(booking,user);
		populateAttendees();
	}
	
	private void setBookingInfo(Booking booking, User user) {
		bookingName.getText(); //retrieve name for room
		LocalDateTime start = booking.getStartTime();
		LocalDateTime end = booking.getEndTime();
		String dateTimeText = start.format(timeFormatter) + " - " + end.format(timeFormatter);
		dateTime.setText(dateTimeText);
=======
public class BookingEditController implements Initializable{
	
	@FXML
	private Label text_name;

    private Booking booking; 
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    	booking = Session.getNewBooking();
    	
    	if (booking != null)
    	{
    		text_name.setText("Editing Booking for: " + booking.getName());
    	}
    	else
    	{
    		text_name.setText("Error: No booking selected");
    	}
    }
>>>>>>> refs/heads/detached

		hostEmail.setText(user.getEmail());

		double hoursBetween = Duration.between(start, end).toMinutes() / 60.0;
		AccountRole user_type = user.getAccountRole();

		double price = booking.calculatePrice(user_type, hoursBetween);

		String bookingPriceText = String.format("$ %.2f", price);
		bookingPrice.setText(bookingPriceText);
	
	}
	
	//use this one to display all the attendees. 
	private void populateAttendees() {
		attendeesList.getChildren().clear();
		
		List<String> attendeeIds = currentBooking.getAttendeeIds();
		
		for (String id : attendeeIds) {
			addAttendeeToUI(id);
		}
	}
	
	// i need an adder to add when the user wanna add their friends
	@FXML
	private void onAddAttendee() {
		String id = attendeeIdField.getText();
		if(id.isEmpty()) {
			return; //nothing if empty
		}
		//creates the attendeeids list
		List<String> attendeeIds = currentBooking.getAttendeeIds();
		if(attendeeIds == null) {
			attendeeIds = new ArrayList<>();
			currentBooking.setAttendeeIds(attendeeIds);
		}
		//dupe case, let user know we have it alr, clear it
		if(attendeeIds.contains(id)) {
			System.out.println("Attendee ID has already been added");
			attendeeIdField.clear();
			return;
		}
		//else
		attendeeIds.add(id); //add id to attendees
		mediator.saveBooking(currentBooking.getId());
		addAttendeeToUI(id);
		attendeeIdField.clear();
	}
	
	//ui add to row
	private void addAttendeeToUI(String id) {
		HBox row = new HBox(10);
		Text idText = new Text(id);
		Button XButton = new Button("X");
		
		//on x pressed
		XButton.setOnAction(e -> {
			attendeesList.getChildren().remove(row);
			List<String> attendeeIds = currentBooking.getAttendeeIds();
			//get the list of all the attendees
			
			if (!attendeeIds.isEmpty()) {
				attendeeIds.remove(id);
				mediator.saveBooking(currentBooking.getId());
			}
			
		});
		row.getChildren().addAll(idText,XButton);
		attendeesList.getChildren().add(row);
	}

	
	/*
	 * public void setBookingInfo(Booking booking, User user, Mediator mediator) {
	 * bookingName.setText(booking.getName()); LocalDateTime start =
	 * booking.getStartTime(); LocalDateTime end = booking.getEndTime();
	 * 
	 * String dateTimeText = start.format(timeFormatter) + " - " +
	 * end.format(timeFormatter); dateTime.setText(dateTimeText);
	 * 
	 * hostEmail.setText(user.getEmail());
	 * 
	 * double hoursBetween = Duration.between(start, end).toMinutes() / 60.0;
	 * AccountRole user_type = user.getAccountRole();
	 * 
	 * double price = booking.calculatePrice(user_type, hoursBetween);
	 * 
	 * String bookingPriceText = String.format("$ %.2f", price);
	 * bookingPrice.setText(bookingPriceText);
	 * 
	 * ArrayList<User> allUsers = mediator.getAllUsers();
	 * 
	 * }
	 */
	
   

}