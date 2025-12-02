package org.openjfx.EECS_3311_Project.managers;
import org.openjfx.EECS_3311_Project.csv.*;
import org.openjfx.EECS_3311_Project.model.Payment;

public class PaymentManager{
	private final PaymentCSVOperations paymentCSV = new PaymentCSVOperations();
	
	public boolean isValidLuhn(String cardNumber) {
	    int sum = 0;
	    boolean alternate = false;

	    for (int i = cardNumber.length() - 1; i >= 0; i--) {
	        int n = cardNumber.charAt(i) - '0';

	        if (alternate) {
	            n *= 2;
	            if (n > 9) {
	                n = (n % 10) + 1;
	            }
	        }

	        sum += n;
	        alternate = !alternate;
	    }

	    return (sum % 10 == 0);
	}
	
	public Payment createRecord(Payment payment) {
		return paymentCSV.create(payment);
	
	}

	public Payment upsertRecord(Payment payment) {
		// TODO Auto-generated method stub
		return paymentCSV.upsert(payment);
	}
}
