package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjfx.EECS_3311_Project.model.AccountRole;
import org.openjfx.EECS_3311_Project.model.Booking;
import org.openjfx.EECS_3311_Project.model.User;

class UserTest {
	String userId;
	String firstName;
	String lastName;
	String email;
	String password;
	String userType;
	AccountRole accountRole;
	User user;
	
	String roomId;
	LocalDateTime start;
	LocalDateTime end;

	@BeforeEach
	void setup() {
		userId = UUID.randomUUID().toString();
		firstName = "John";
		lastName = "Doe";
		email = "john.doe@example.com";
		password = "password123";
		userType = "student";
		accountRole = new AccountRole("Student", "R001", 20.0);
		
		roomId = UUID.randomUUID().toString();
		start = LocalDateTime.of(2024, 1, 1, 10, 0);
		end = LocalDateTime.of(2024, 1, 1, 11, 0);

		user = new User();
		user.setId(userId);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(password);
		user.setUserType(userType);
		user.setAccountRole(accountRole);
	}

	@Test
	void add_booking_to_user() {
		Booking booking = new Booking(roomId, userId, start, end);
		booking.setId("B1");

		Booking result = user.addBooking(booking);

		assertNotNull(result);
		assertEquals(1, user.getBookings().size());
		assertEquals("B1", user.getBookings().get(0).getId());
	}

	@Test
	void add_multiple_bookings_to_user() {
		Booking booking = new Booking("ROOM1", userId, start, end);
		booking.setId("B1");

		Booking duplicate = new Booking("ROOM2", userId, start, end);
		duplicate.setId("B2");
		
		user.addBooking(booking);
		user.addBooking(duplicate);

		assertEquals(2, user.getBookings().size());
		assertEquals("B1", user.getBookings().get(0).getId());
		assertEquals("B2", user.getBookings().get(1).getId());
	}

	@Test
	void adding_null_booking_does_nothing() {
		assertTrue(user.getBookings().isEmpty());

		Booking result = user.addBooking(null);

		assertNull(result);
		assertTrue(user.getBookings().isEmpty());
	}

	@Test
	void user_to_csv_row_with_bookings_and_role() {
		Booking b1 = new Booking("ROOM1", userId, start, end);
		b1.setId("B1");

		Booking b2 = new Booking("ROOM2", userId, start, end);
		b2.setId("B2");

		user.addBooking(b1);
		user.addBooking(b2);

		String expected = String.join(",",
				userId,
				firstName,
				lastName,
				email,
				password,
				userType,
				"[B1;B2]",
				accountRole.getId()
		);

		assertEquals(expected, user.toCSVRow());
	}

	@Test
	void get_visible_bookings_filters_and_sorts_correctly() {
		LocalDateTime now = LocalDateTime.now();

		// visible: future booking
		Booking futureBooking = new Booking("ROOM1", userId, now.plusHours(1), now.plusHours(2));
		futureBooking.setId("B_FUTURE");

		// visible: started within last 30 minutes
		Booking recentPast = new Booking("ROOM2", userId, now.minusMinutes(20), now.plusMinutes(40));
		recentPast.setId("B_RECENT");

		// visible: started long ago but checked in & not finished
		Booking checkedInPast = new Booking("ROOM3", userId, now.minusHours(1), now.plusMinutes(10));
		checkedInPast.setId("B_CHECKED");
		checkedInPast.setIsCheckedIn(true);

		// not visible: fully in the past and not checked in
		Booking oldPast = new Booking("ROOM4", userId, now.minusHours(2), now.minusHours(1));
		oldPast.setId("B_OLD");

		user.addBooking(futureBooking);
		user.addBooking(recentPast);
		user.addBooking(checkedInPast);
		user.addBooking(oldPast);

		ArrayList<Booking> visible = user.getVisibleBookings();

		assertEquals(3, visible.size());
		// sorted by start time ascending
		assertEquals("B_CHECKED", visible.get(0).getId());
		assertEquals("B_RECENT", visible.get(1).getId());
		assertEquals("B_FUTURE", visible.get(2).getId());
	}

	@Test
	void remove_booking_removes_from_list() {
		Booking booking = new Booking("ROOM1", userId, start, end);
		booking.setId("B1");

		user.addBooking(booking);
		assertEquals(1, user.getBookings().size());

		user.removeBooking(booking);

		assertTrue(user.getBookings().isEmpty());
	}

	@Test
	void setters_with_valid_params_update_fields() {
		String newId = UUID.randomUUID().toString();
		String newFirst = "Jane";
		String newLast = "Smith";
		String newEmail = "jane.smith@example.com";
		String newPassword = "newpass";
		String newType = "staff";
		AccountRole newRole = new AccountRole("Staff", "ROLE2", 30.0);
		ArrayList<Booking> newBookings = new ArrayList<>();

		user.setId(newId);
		user.setFirstName(newFirst);
		user.setLastName(newLast);
		user.setEmail(newEmail);
		user.setPassword(newPassword);
		user.setUserType(newType);
		user.setAccountRole(newRole);
		user.setBookings(newBookings);

		assertEquals(newId, user.getId());
		assertEquals(newFirst, user.getFirstName());
		assertEquals(newLast, user.getLastName());
		assertEquals(newEmail, user.getEmail());
		assertEquals(newPassword, user.getPassword());
		assertEquals(newType, user.getUserType());
		assertEquals(newRole, user.getAccountRole());
		assertSame(newBookings, user.getBookings());
	}
}