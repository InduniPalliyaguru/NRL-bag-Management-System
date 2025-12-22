package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.nrlbag.dto.UserDTO;
import lk.ijse.nrlbag.model.UserModel;

public class ChangePasswordController {

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    private final UserModel userModel = new UserModel();

    private final String normal = "-fx-background-color: transparent; -fx-border-width: 0 0 1 0; -fx-border-color: linear-gradient(to right, #003D99, #001433);";
    private final String error  = "-fx-background-color: transparent; -fx-border-width: 0 0 1 0; -fx-border-color: linear-gradient(to right, #CC1F1F, #9e0404);";

    @FXML
    private void handleChangePasswordButton() {

        String PASSWORD_REGEX = "^.{4,5}$";

        try {

            String newPassword = newPasswordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            // get the new password and check the matching of the confirmation and new one.
            if (!newPassword.matches(PASSWORD_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Password! Enter between 4 to 5 characters").show();
                newPasswordField.setStyle(error);
                newPasswordField.clear();
                confirmPasswordField.clear();
            } else if (!newPassword.equals(confirmPassword)){
                new Alert(Alert.AlertType.ERROR, "Confirm Password is not equal to new Password").show();
                newPasswordField.setStyle(normal);
                confirmPasswordField.setStyle(error);
                confirmPasswordField.clear();
            } else {
                boolean isChanged = userModel.updateLoginPassword(newPassword);

                if (isChanged) {
                    new Alert(Alert.AlertType.INFORMATION, "Password Change Successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    @FXML
    private void handleNewPasswordEnter(KeyEvent event) {

        try {
            if (event.getCode() == KeyCode.ENTER) {
                String currentPassword = currentPasswordField.getText();
                UserDTO userDTO = userModel.getUserDetails();

                // get the input password and compare the validity
                if (currentPassword.equals(userDTO.getUserPassword())) {
                    currentPasswordField.setStyle(normal);
                    newPasswordField.setEditable(true);
                    newPasswordField.requestFocus();
                    confirmPasswordField.setEditable(true);
                } else {
                    // show error when password is wrong
                    currentPasswordField.setStyle(error);
                }
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    private void handleResetButton() {

        clearFields();

    }

    private void clearFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        newPasswordField.setEditable(false);
        confirmPasswordField.setEditable(false);
        currentPasswordField.setStyle(normal);
        newPasswordField.setStyle(normal);
        confirmPasswordField.setStyle(normal);
    }

}
