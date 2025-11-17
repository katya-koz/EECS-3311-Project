package org.openjfx.EECS_3311_Project;

import javafx.event.ActionEvent;

import java.util.ArrayList;

public class CSVAdapter implements ICSVRepository {

	private final ICSVRepository csvRepository;

    public CSVAdapter(ICSVRepository csvRepository) {
        this.csvRepository = csvRepository;
    }

    // create the append function using the private upsertBooking
    // create the listall function using the function in ICSV Repository -> getAllUsers
    // create getHostBookingsByUserId using the function in ICSV Repository -> getHostBookingsByUserId
    // create getInvitedBookingsByUserId using the function in ICSV Repository -> getInvitedBookingsByUserId

    @Override
	public Booking upsertBooking(Booking booking) {

		return null;
	}

    @Override
	public String cancelBooking(String bookingId) {

		return null;
	}

    @Override
	public ArrayList<Booking> getHostBookingsByUserId(String userId) {

		return null;
	}

    @Override
	public ArrayList<Booking> getInvitedBookingsByUserId(String userId) {

		return null;
	}

	@Override
	public User getUserById(String userId) {
		return csvRepository.getUserById(userId);
	}

	@Override
	public ArrayList<User> getAllUsers() {
		return csvRepository.getAllUsers();
	}

	@Override
	public User createUser(ActionEvent event, String firstName, String lastName, String email, String password, String userType, String accountType) {
		return csvRepository.createUser(event, firstName, lastName, email, password, userType, accountType);
	}

	@Override
	public void signIn(ActionEvent event, String email, String password) {
		csvRepository.signIn(event, email, password);
	}

	@Override
	public boolean validateCredentials(String email, String password) {
		return csvRepository.validateCredentials(email, password);
	}

	@Override
	public String getUserID(String email, String password) {
		return csvRepository.getUserID(email, password);
	}
}
