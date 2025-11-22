package org.openjfx.EECS_3311_Project.managers;

import java.util.ArrayList;

import org.openjfx.EECS_3311_Project.CSVAdapter;
import org.openjfx.EECS_3311_Project.ICSVRepository;
import org.openjfx.EECS_3311_Project.model.AccountRole;
import org.openjfx.EECS_3311_Project.model.Room;
import org.openjfx.EECS_3311_Project.model.User;

public class UserManager {
	
	ICSVRepository csvRepository = CSVAdapter.getInstance();

	public User createAccount(User user) {
		// TODO Auto-generated method stub
		return csvRepository.createAccount(user);
	}

	public boolean isEmailTaken(String text) {
		return csvRepository.isEmailTaken(text);
	}

	public User signIn(String email, String password) {
		return csvRepository.signIn(email, password);
	}

	public ArrayList<AccountRole> getAccountRoles() {
		return csvRepository.getAccountRoles();
	}
	
	public ArrayList<User> getAllUsers() {
		return csvRepository.getAllUsers();
	}
	
	   public User getUserById(String userId) {
	        return csvRepository.getUserById(userId);
	    }
	   
		 public void toggleAdmin(String userId, boolean isAdmin) {
	         csvRepository.toggleAdmin(userId, isAdmin);
	    }
	 
	 public void upsertAccountRole(AccountRole role) {
	        csvRepository.upsertAccountRole(role);
	    }
	 
	   public void removeAccountRole(String roleName) {
	        csvRepository.removeAccountRole(roleName);
	    }

	   public double getHourlyRate(User user) {
		    for (AccountRole role : csvRepository.getAccountRoles()) {
		        if (role.getId().equals(user.getAccountRole().getId())) {
		            return role.getHourlyRate();
		        }
		    }
		    return 0.0;
		}

}


