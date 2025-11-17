package org.openjfx.EECS_3311_Project;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Alert; // <-- IMPORT THIS

public class RegisterController implements Initializable {

	FileRepository icsv = new FileRepository(UsersDatabaseUtils.bookingDB, UsersDatabaseUtils.userDB_CSV);

	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private AnchorPane anchorPane_registerPage;

	@FXML
	private TextField tf_FirstName;
	@FXML
	private TextField tf_LastName;
	@FXML
	private TextField tf_email;
	@FXML
	private PasswordField pf_password;
	@FXML
	private PasswordField pf_confirmPassword;
	@FXML
	private Button button_Register;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//fixing fullscreen scaling issues
		anchorPane_registerPage.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene != null) {

				newScene.widthProperty().addListener((observable, oldWidth, newWidth) -> {
					double width = newWidth.doubleValue();

					// if the width of the window is greater than 1200px, zoom by 25%
					if (width > 1200) {
						anchorPane_registerPage.setScaleX(1.25); // 125% size
						anchorPane_registerPage.setScaleY(1.25);
					} else {
						// Otherwise, go back to normal size
						anchorPane_registerPage.setScaleX(1.0);
						anchorPane_registerPage.setScaleY(1.0);
					}
				});
			}
		});

		//scaling the comboBox
		comboBox.setOnShowing(event -> {
			ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin<?>) comboBox.getSkin();

			if (skin != null) {
				Node popupContent = skin.getPopupContent();

				double currentScale = anchorPane_registerPage.getScaleX();
				double newFontSize = 12 * currentScale;

				popupContent.setStyle("-fx-font-size: " + newFontSize + "px;");
			}
		});

		//add options into the comboBox
		comboBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");


		//logic for registering user
		button_Register.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				//get entered text
				String password = pf_password.getText();
				String confirmPassword = pf_confirmPassword.getText();

				boolean emailInUse = icsv.isEmailTaken(tf_email.getText());

				//verify whether any fields are blank

				if (tf_FirstName.getText().isBlank() || tf_LastName.getText().isBlank() || tf_email.getText().isBlank() ||
						password.isBlank() || confirmPassword.isBlank() || comboBox.getValue() == null) {
					//fields are not full
					System.err.println("Registration Error");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Registration Error");
					alert.setHeaderText("All fields are mandatory");
					alert.setContentText("Please ensure all fields are filled in");
					alert.showAndWait();
				}
				else if(emailInUse)
				{
					// email is already registered
					System.err.println("Registration Error");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Registration Error");
					alert.setHeaderText("Email is already in use");
					alert.setContentText("Please register using a new email or sign in instead");
					alert.showAndWait();
				}
				else if (!password.equals(confirmPassword)){
					//show invalid password error
					System.err.println("Registration Error");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Registration Error");
					alert.setHeaderText("Passwords do not match");
					alert.setContentText("Passwords must match");
					alert.showAndWait();
				}
				else {


					System.out.println("Registering User");

					icsv.createUser(event,
							tf_FirstName.getText(),
							tf_LastName.getText(),
							tf_email.getText(),
							password,
							"User", //users are registered as users by default, pre-defined admins are created in advance and users can be promoted
							comboBox.getValue());

					//change to logged in scene
					UsersDatabaseUtils.changeScene(event, "HomePage.fxml", "Home", tf_email.getText(), comboBox.getValue());

				}
			}
		});
	}
}