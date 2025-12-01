package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjfx.EECS_3311_Project.model.AccountRole;
import org.openjfx.EECS_3311_Project.model.Booking;

class BookingTest {
	
	String bookingId;
	String roomId;
	String meetingName;
	String hostId;
	boolean isCheckedIn;
	LocalDateTime start;
    LocalDateTime end;
    LocalDateTime checkInTime;
    ArrayList<String> attendeeIds;
    String studentOrOrganizationId;
    boolean isCancelled;
    AccountRole userType;
	
	@BeforeEach
	void setup() {
    	bookingId = UUID.randomUUID().toString();
    	roomId = UUID.randomUUID().toString();
    	meetingName = "New Meeting";
    	hostId = UUID.randomUUID().toString();
    	isCheckedIn = true;
    	start = LocalDateTime.of(2024, 1, 1, 10, 0);
    	end = LocalDateTime.of(2024, 1, 1, 11, 0);
        checkInTime = LocalDateTime.of(2024, 1, 1, 10, 30);
        attendeeIds = new ArrayList<>(Arrays.asList("A1", "A2"));
        studentOrOrganizationId = "123456789";
        isCancelled = false;
        
        userType = new AccountRole("Student", "R001", 20.0);
	}

    @Test
    void constructor_with_valid_params() {
        Booking booking = new Booking(roomId, hostId, start, end);

        assertNotNull(booking.getId());
        assertEquals(hostId, booking.getHostId());
        assertEquals(start, booking.getStartTime());
        assertEquals(end, booking.getEndTime());
        
        assertEquals("New Meeting", booking.getName());			// default to New Meeting
        assertFalse(booking.getIsCheckedIn());					// default to false
        assertNull(booking.getCheckInTime());					// default to null
        assertTrue(booking.getAttendeeIds().isEmpty());			// default to empty array list
        assertEquals("", booking.getStudentOrOrganizationId());	// default empty string
        assertFalse(booking.isCancelled());						// default to false
    }

    @Test
    void csv_constructor_with_valid_param() {
        String csv = String.join(",",
        		bookingId,
        		roomId,
        		meetingName,
        		Boolean.toString(isCheckedIn),
        		hostId,
                "[A1;A2]",
                start.toString(),
                end.toString(),
                checkInTime.toString(),
                studentOrOrganizationId,
                Boolean.toString(isCancelled)
        );

        Booking booking = new Booking(csv);

        assertEquals(bookingId, booking.getId());
        assertEquals(roomId, booking.getRoomId());
        assertEquals(meetingName, booking.getName());
        assertEquals(isCheckedIn, booking.getIsCheckedIn());
        assertEquals(hostId, booking.getHostId());
        assertEquals(attendeeIds, booking.getAttendeeIds());
        assertEquals(start, booking.getStartTime());
        assertEquals(end, booking.getEndTime());
        assertEquals(checkInTime, booking.getCheckInTime());
        assertEquals(studentOrOrganizationId, booking.getStudentOrOrganizationId());
        assertFalse(booking.isCancelled());
    }

    @Test
    void csv_constructor_with_empty_strings() {
        String csv = String.join(",",
        		bookingId,
        		roomId,
        		meetingName,
        		"",
        		hostId,
                "[]",
                start.toString(),
                end.toString(),
                "",
                studentOrOrganizationId,
                ""
        );

        Booking booking = new Booking(csv);
        
        assertNull(booking.getCheckInTime());
        assertTrue(booking.getAttendeeIds().isEmpty());
        assertNull(booking.getIsCheckedIn());
        assertFalse(booking.isCancelled());
    }

    @Test
    void booking_to_csv_row() {
        Booking booking = new Booking(roomId, hostId, start, end);
        booking.setId(bookingId);
        booking.setName(meetingName);
        booking.setIsCheckedIn(isCheckedIn);
        booking.setCheckInTime(checkInTime);
        booking.setStudentOrOrganizationId(studentOrOrganizationId);
        booking.setCancelled(isCancelled);
        booking.setAttendeeIds(attendeeIds);

        String expected = String.join(",",
        		bookingId,
        		roomId,
        		meetingName,
        		Boolean.toString(isCheckedIn),
        		hostId,
                "[A1;A2]",
                start.toString(),
                end.toString(),
                checkInTime.toString(),
                studentOrOrganizationId,
                Boolean.toString(isCancelled)
        );

        assertEquals(expected, booking.toCSVRow());
    }

    @Test
    void calculate_deposit_price_thirty_minutes() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 10, 30); // 30 min
    	
        Booking booking = new Booking(roomId, hostId, start, end);

        double result = booking.calculateDepositPrice(userType);
        
        assertEquals(userType.getHourlyRate() / 2.0, result); // half hourly rate as deposit
    }

    @Test
    void calculate_deposit_price_one_hour_or_more() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0); // 2 hours

        Booking booking = new Booking(roomId, hostId, start, end);

        double result = booking.calculateDepositPrice(userType);

        assertEquals(userType.getHourlyRate(), result); // full hourly rate as deposit
    }

    @Test
    void calculate_total_price_with_three_hour_meeting() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 13, 0); // 3 hours

        Booking booking = new Booking(roomId, hostId, start, end);

        double result = booking.calculateTotalPrice(userType);

        assertEquals(userType.getHourlyRate() * (3 - 1), result); // full hourly rate minus deposit (1 hour)
    }

    @Test
    void calculate_extend_price_with_two_hour_extension() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 11, 0); // 1 hours
    	
        Booking booking = new Booking(roomId, hostId, start, end);

        Duration extension = Duration.ofHours(3); // 3 hours
        double result = booking.calculateExtendPrice(userType, extension);

        // hours = 3, (hours - 1) = 2 → 2 * 50 = 100
        assertEquals(userType.getHourlyRate() * (3 - 1), result); // full hourly rate minus deposit (1 hour)
    }

    @Test
    void check_in_sets_is_checked_in_and_check_in_time() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 11, 0); // 1 hours

        Booking booking = new Booking(roomId, hostId, start, end);

        assertFalse(booking.getIsCheckedIn());
        assertNull(booking.getCheckInTime());

        booking.checkIn();

        assertTrue(booking.getIsCheckedIn());
        assertNotNull(booking.getCheckInTime());
    }

    @Test
    void cancelled_flag_getter_and_setter() {
        Booking booking = new Booking(roomId, hostId, start, end);

        assertFalse(booking.isCancelled());

        booking.setCancelled(true);

        assertTrue(booking.isCancelled());
    }
}
