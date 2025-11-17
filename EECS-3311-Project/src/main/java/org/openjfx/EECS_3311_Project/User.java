package org.openjfx.EECS_3311_Project;

import java.util.ArrayList;
import java.util.UUID;

public class User {
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String userType;
	private ArrayList<Booking> bookings;
	private String accountType;


	public User(String id, String firstName, String lastName, String email, String password) {
		this.id = UUID.randomUUID().toString();
	    this.firstName = firstName;
	    this.lastName = lastName;
	    this.email = email;
	    this.password= password;
    }
	
	public User(String firstName, String lastName, String email, String password) {
		this.id = UUID.randomUUID().toString();
	    this.firstName = firstName;
	    this.lastName = lastName;
	    this.email = email;
	    this.password= password;
    }

	//constructor with added parameters not including bookings
	public User(String id, String firstName, String lastName, String email, String password, String userType, String accountType)
	{
		this.id = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.accountType = accountType;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public ArrayList<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(ArrayList<Booking> bookings) {
		this.bookings = bookings;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
}


