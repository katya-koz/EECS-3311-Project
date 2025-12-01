package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjfx.EECS_3311_Project.model.Payment;

class PaymentTest {

	String paymentId;
	Double amount;
	String cardNumber;
	String userId;
	String bookingId;

	@BeforeEach
	void setup() {
		paymentId = UUID.randomUUID().toString();
		amount = 50.0;
		cardNumber = "1234567890123456";
		userId = UUID.randomUUID().toString();
		bookingId = UUID.randomUUID().toString();
	}

	@Test
	void constructor_with_valid_params() {
		Payment payment = new Payment(amount, cardNumber, userId, bookingId);

		assertNotNull(payment.getId());
		assertEquals(amount, payment.getAmount());
		assertEquals(cardNumber, payment.getCardNumber());
		assertEquals(userId, payment.getUserId());
		assertEquals(bookingId, payment.getBookingId());
	}

	@Test
	void csv_constructor_with_valid_param() {
		String csv = String.join(",",
				paymentId,
				amount.toString(),
				cardNumber,
				userId,
				bookingId
		);

		Payment payment = new Payment(csv);

		assertEquals(paymentId, payment.getId());
		assertEquals(amount, payment.getAmount());
		assertEquals(cardNumber, payment.getCardNumber());
		assertEquals(userId, payment.getUserId());
		assertEquals(bookingId, payment.getBookingId());
	}

	@Test
	void csv_constructor_invalid_amount() {
		String csv = String.join(",",
				paymentId,
				"notANumber",
				cardNumber,
				userId,
				bookingId
		);

		assertThrows(NumberFormatException.class,
				() -> new Payment(csv));
	}

	@Test
	void payment_to_csv_row() {
		Payment payment = new Payment(amount, cardNumber, userId, bookingId);
		payment.setId(paymentId); // override random UUID so we can compare

		String expected = String.join(",",
				paymentId,
				amount.toString(),
				cardNumber,
				userId,
				bookingId
		);

		assertEquals(expected, payment.toCSVRow());
	}

	@Test
	void setters_with_valid_params() {
		Payment payment = new Payment(amount, cardNumber, userId, bookingId);

		String newId = UUID.randomUUID().toString();
		String newUserId = "USER_002";
		String newBookingId = "BOOKING_002";
		Double newAmount = 75.5;

		payment.setId(newId);
		payment.setAmount(newAmount);
		payment.setUserId(newUserId);
		payment.setBookingId(newBookingId);

		assertEquals(newId, payment.getId());
		assertEquals(newAmount, payment.getAmount());
		assertEquals(newUserId, payment.getUserId());
		assertEquals(newBookingId, payment.getBookingId());
	}
}
