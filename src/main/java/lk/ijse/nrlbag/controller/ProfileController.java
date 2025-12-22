package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lk.ijse.nrlbag.dto.UserDTO;
import lk.ijse.nrlbag.model.UserModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField roleField;
    @FXML
    private TextField userNameField;

    private final UserModel userModel = new UserModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTextFields();
    }

    @FXML
    private void handleEditButton() {

            fullNameField.setEditable(true);
            fullNameField.requestFocus();
            emailField.setEditable(true);
            roleField.setEditable(true);

    }

    @FXML
    private void handleResetButton() {

        // reset fields to the first data
        loadTextFields();

    }

    @FXML
    private void handleUpdateButton() {

        String NAME_REGEX = "^[A-Za-z]+\\s+[A-Za-z]+$";
        String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        String ROLE_REGEX = "^[A-Za-z]+(\\s+[A-Za-z]+)*$";

        try {

            String userName = userNameField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleField.getText().trim();

            if (!fullName.matches(NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Name Input!").show();
            } else if (!email.matches(EMAIL_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Email Input!").show();
            } else if (!role.matches(ROLE_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Role Input!").show();
            } else {

                UserDTO userDTO = new UserDTO(userName, fullName, role, email);
                boolean isUpdated = userModel.updateUserDetails(userDTO);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "User Details Updates Successfully!").show();
                    loadTextFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    private void loadTextFields() {
        try {

            // get the details from database through model class and set to the text fields
            UserDTO userDetails = userModel.getUserDetails();

            userNameField.setText(userDetails.getUserName());
            fullNameField.setText(userDetails.getName());
            emailField.setText(userDetails.getEmail());
            roleField.setText(userDetails.getRole());
            userNameField.setEditable(false);
            fullNameField.setEditable(false);
            roleField.setEditable(false);
            emailField.setEditable(false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
