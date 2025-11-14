package org.openjfx.EECS_3311_Project;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class RegisterController implements Initializable{
	
	@FXML
	private ComboBox<String> comboBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");
		
	}
	
	@FXML
	public String getComboBoxInput(ActionEvent event) {
		
		return comboBox.getValue();
	}
}
