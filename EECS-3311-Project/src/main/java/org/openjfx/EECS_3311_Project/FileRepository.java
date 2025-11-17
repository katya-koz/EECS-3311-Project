package org.openjfx.EECS_3311_Project;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class FileRepository implements ICSVRepository{
	private final Path bookingFilePath;
	private final Path userFilePath;
//    private final String userDB_CSV = "UserDB.csv";
//    private final String bookingDB = "BookingDB.csv";

	
	public FileRepository(String bookingFileName, String userFileName) {
        this.bookingFilePath = Paths.get(bookingFileName);
        this.userFilePath = Paths.get(userFileName);
    }



    public Booking upsertBooking(Booking booking) {
        ArrayList<Booking> all = getAllBookingsFromFile();

        all.removeIf(b -> b.getRoomId().equals(booking.getRoomId()));
        all.add(booking);

        writeAllBookingsToFile(all);
        return booking;
    }

    public String cancelBooking(String bookingId) {
        ArrayList<Booking> all = getAllBookingsFromFile();

        for (Booking b : all) {
            if (b.getRoomId().equals(bookingId)) {
                b.setInactive(); 
            }
        }

        writeAllBookingsToFile(all);
        return bookingId;
    }

    public ArrayList<Booking> getHostBookingsByUserId(String userId) {
        ArrayList<Booking> result = new ArrayList<>();
        for (Booking b : getAllBookingsFromFile()) {
            if (b.getHostUserId().equals(userId)) {
                result.add(b);
            }
        }
        return result;
    }

    public ArrayList<Booking> getInvitedBookingsByUserId(String userId) {
        ArrayList<Booking> result = new ArrayList<>();
        for (Booking b : getAllBookingsFromFile()) {
            if (b.getInvitedUserIds().contains(userId)) {
                result.add(b);
            }
        }
        return result;
    }
    
    private ArrayList<Booking> getAllBookingsFromFile() {
        ArrayList<Booking> bookings = new ArrayList<>();

        if (!Files.exists(bookingFilePath)) {
            return bookings;
        }

        try (BufferedReader reader = Files.newBufferedReader(bookingFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Booking b = parseBooking(line);
                bookings.add(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    private void writeAllBookingsToFile(ArrayList<Booking> bookings) {
        try (BufferedWriter writer = Files.newBufferedWriter(bookingFilePath)) {
            for (Booking b : bookings) {
                writer.write(bookingToCsvLine(b));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Booking parseBooking(String line) {
        String[] parts = line.split(",", -1);

        if (parts.length < 10) {
            throw new IllegalArgumentException("Invalid booking CSV line: " + line);
        }

        String id = parts[0];
        String roomId = parts[1];
        String name = parts[2];
        Boolean isCheckedIn = Boolean.parseBoolean(parts[3]);        
        String hostUserId = parts[4];
        String[] attendeeIds = parts[5].split(";");
        LocalDateTime startTime  = LocalDateTime.parse(parts[6]);
        LocalDateTime endTime = LocalDateTime.parse(parts[7]);
        LocalDateTime checkInTime = LocalDateTime.parse(parts[8]);
        Status status = Status.valueOf(parts[9]);
        
        User host = getUserById(hostUserId);
        ArrayList<User> attendees = getUsersByIds(attendeeIds);
        
        return new Booking(id, roomId, name, isCheckedIn, host, attendees, startTime, endTime, checkInTime, status);
    }
    
    public User getUserById(String userId) {
        ArrayList<User> allUsers = getAllUsers();  // read from CSV
        for (User u : allUsers) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }
    
    private ArrayList<User> getUsersByIds(String[] userIds) {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<User> allUsers = getAllUsers();

        for (String userId : userIds) {
            for (User u : allUsers) {
                if (u.getId().equals(userId)) {
                    users.add(u);
                    break;
                }
            }
        }
        return users;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(userFilePath)) {

            String line;
            // Skip the header
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                User user = parseUser(line);
                if (user != null) {
                    users.add(user);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    private User parseUser(String line) {

        String[] parts = line.split(";", -1);

        if (parts.length < 8) {
            System.err.println("Skipping invalid user CSV line: " + line);
            return null;
        }

        String id = parts[0];
        String firstName = parts[1];
        String lastName = parts[2];
        String email = parts[3];
        String password = parts[4];
        String userType = parts[5];
        // parts[6] is bookings
        String accountType = parts[7];


        return new User(id, firstName, lastName, email, password, userType, accountType);
    }
    
    private String bookingToCsvLine(Booking b) {
        // convert Booking fields → CSV line string
        return "";
    }

    //function for creating a user into the userDB
    public User createUser(ActionEvent event, String firstName, String lastName, String email, String password, String userType, String accountType)
    {
        //checking whether the email address is taken
        if (isEmailTaken(email))
        {
            //display an alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Email address is already in use!");
            alert.show();

            return null;

        }
        else {
            //generate the random id
            String id = UUID.randomUUID().toString();

            String newUserInfo = String.join(";", id, firstName, lastName, email, password, userType, "", accountType);

            System.out.println("Writing new user to: " + userFilePath.toString());
            //append to the end of the csv
            try (
                FileWriter filewriter = new FileWriter(userFilePath.toString(), true);
                PrintWriter printwriter = new PrintWriter(filewriter))
                {
                    //adding the new line to the file
                    printwriter.println(newUserInfo);

                    System.out.println("file written");
                }
                 catch (IOException e) {
                e.printStackTrace();
                return null;
            }

                return new User(id, firstName, lastName, email, password, userType, accountType);

        }
    }

    //login
    public void signIn(ActionEvent event, String email, String password)
    {
        //checking whether the user is a valid one
        User user = validateAndGetUser(email, password);

        if (user != null) {

            String accountType = user.getAccountType();
            UsersDatabaseUtils.changeScene(event, "HomePage.fxml", "Home", user.getEmail(), accountType);
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid credentials entered.");
            alert.show();
        }
    }

    //helper function for validating credentials
    public boolean validateCredentials(String email, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath.toString()))) {
            String line;

            //skip line 1
            br.readLine();

            //check all lines of the db
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";", -1);


                if (values.length > 4) {
                    String emailInFile = values[3];
                    String passwordInFile = values[4];


                    if (emailInFile.equalsIgnoreCase(email) && passwordInFile.equals(password)) {
                        //validate
                        return true;
                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }


        return false;
    }

    //helper function for checking whether the email address is already taken
    public boolean isEmailTaken(String email) {

        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath.toString()))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] values = line.split(";", -1);


                if (values.length > 3) {
                    String emailInFile = values[3];

                    if (emailInFile.equalsIgnoreCase(email)) {

                        return true;
                    }
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }

    //helper function to get userID
    public String getUserID(String email, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath.toString()))) {
            String line;

            //skip line 1
            br.readLine();

            //check all lines of the db
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";", -1);


                if (values.length > 4) {
                    String emailInFile = values[3];
                    String passwordInFile = values[4];


                    if (emailInFile.equalsIgnoreCase(email) && passwordInFile.equals(password)) {
                        //validate
                        return values[0]; //the user ID
                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }


        return null;
    }

    public User validateAndGetUser(String email, String password) {

        //if whitespace was entered in the password, remove it
        String cleanEmail = email.trim();
        String cleanPassword = password.trim();

        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath.toString()))) {
            String line;
            br.readLine(); // skip line 1

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;


                String[] values = line.split(";", -1);

                if (values.length > 4) {
                    String emailInFile = values[3].trim();
                    String passwordInFile = values[4].trim();


                    if (emailInFile.equalsIgnoreCase(cleanEmail) && passwordInFile.equals(cleanPassword)) {


                        return parseUser(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}