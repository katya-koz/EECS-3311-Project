package org.openjfx.EECS_3311_Project;

import javafx.event.ActionEvent;
import java.util.ArrayList;

public interface ICSVRepository {

    Booking upsertBooking(Booking booking);
    String cancelBooking(String bookingId);
    ArrayList<Booking> getHostBookingsByUserId(String userId);
    ArrayList<Booking> getInvitedBookingsByUserId(String userId);

    User getUserById(String userId);
    ArrayList<User> getAllUsers();

    User createUser(ActionEvent event, String firstName, String lastName, String email, String password, String userType, String accountType);
    void signIn(ActionEvent event, String email, String password);
    boolean validateCredentials(String email, String password);
    String getUserID(String email, String password);
}