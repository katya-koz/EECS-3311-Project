package org.openjfx.EECS_3311_Project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import org.openjfx.EECS_3311_Project.Session;
import org.openjfx.EECS_3311_Project.model.Booking;
import org.openjfx.EECS_3311_Project.model.User;
import org.openjfx.EECS_3311_Project.views.BookingView;

public class HomePageController implements Initializable {

    @FXML
    private AnchorPane anchorPane_homePage;

    @FXML
    private Button button_Logout;

    @FXML
    private Text text_email;
    @FXML
    private Text text_accountType;
    @FXML
    private Text text_name;         
    @FXML
    private Text text_accountRole;   
    @FXML
    private Text text_hourlyRate;
    @FXML
    private ListView<Booking> lv_bookingsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUserInfo(Session.getUser());

        lv_bookingsListView.setCellFactory(param -> new BookingView());

        lv_bookingsListView.setPlaceholder(new Label("No bookings yet"));


        // 2. CREATE DUMMY DATA (The test)
        // In the real app, this list comes from your User object
        ObservableList<Booking> testBookings = FXCollections.observableArrayList();

        testBookings.add(new Booking("Test Name 1", "Room 101", LocalDateTime.now(), LocalDateTime.now(), false));
        testBookings.add(new Booking("Test Name 2", "Room 101", LocalDateTime.now(), LocalDateTime.now(), false));

        testBookings.add(new Booking("Test Name 3", "Room 101", LocalDateTime.now(), LocalDateTime.now(), false));


        // 3. LOAD THE DATA INTO THE VIEW
        lv_bookingsListView.setItems(testBookings);
    }

    public void setUserInfo(User user) {
        text_name.setText(user.getFirstName() + " " + user.getLastName());
        text_email.setText(user.getEmail());
        text_accountType.setText(user.getUserType());

        if (user.getAccountRole() != null) {
            text_accountRole.setText(user.getAccountRole().getRoleName());
            text_hourlyRate.setText("$" + user.getAccountRole().getHourlyRate());
        } else {
            text_accountRole.setText("N/A");
            text_hourlyRate.setText("N/A");
        }
    }
}

