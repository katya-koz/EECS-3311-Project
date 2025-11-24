package org.openjfx.EECS_3311_Project.controllers;

import org.openjfx.EECS_3311_Project.Mediator;
import org.openjfx.EECS_3311_Project.model.Booking;
import org.openjfx.EECS_3311_Project.model.Payment;
import org.openjfx.EECS_3311_Project.model.User;

public class PaymentDecorator {

    public interface PaymentAction {
        String execute();  
    }


    public static class BasePaymentAction implements PaymentAction {
        private final double price;
        private final String maskedNumber;
        private final Booking booking;
        private final User user;
        private final Mediator mediator;
        private final BookingController bookingController;

        public BasePaymentAction(double price,
                                 String maskedNumber,
                                 Booking booking,
                                 User user,
                                 Mediator mediator,
                                 BookingController bookingController) {
            this.price = price;
            this.maskedNumber = maskedNumber;
            this.booking = booking;
            this.user = user;
            this.mediator = mediator;
            this.bookingController = bookingController;
        }

        @Override
        public String execute() {
            bookingController.saveBooking(booking, user);

            Payment payment = new Payment(
                    price,
                    maskedNumber,
                    user.getId(),
                    booking.getId()
            );

            mediator.createPaymentRecord(payment);
            return null;
        }
    }


    public abstract static class PaymentActionDecorator implements PaymentAction {
        protected final PaymentAction next;
        public PaymentActionDecorator(PaymentAction next) { this.next = next; }
    }


    public static class CardValidationDecorator extends PaymentActionDecorator {
        private final String card, csv, expiry;
        private final ValidateFn validateFn;

        public interface ValidateFn {
            String validate(String card, String csv, String expiry);
        }

        public CardValidationDecorator(PaymentAction next,
                                       String card,
                                       String csv,
                                       String expiry,
                                       ValidateFn validateFn) {
            super(next);
            this.card = card;
            this.csv = csv;
            this.expiry = expiry;
            this.validateFn = validateFn;
        }

        @Override
        public String execute() {
            String error = validateFn.validate(card, csv, expiry);
            return (error != null) ? error : next.execute();
        }
    }

    public static class InstitutionalValidationDecorator extends PaymentActionDecorator {
        private final String number, name;
        private final ValidateFn validateFn;

        public interface ValidateFn {
            String validate(String number, String name);
        }

        public InstitutionalValidationDecorator(PaymentAction next,
                                                String number,
                                                String name,
                                                ValidateFn validateFn) {
            super(next);
            this.number = number;
            this.name = name;
            this.validateFn = validateFn;
        }

        @Override
        public String execute() {
            String error = validateFn.validate(number, name);
            return (error != null) ? error : next.execute();
        }
    }
}
