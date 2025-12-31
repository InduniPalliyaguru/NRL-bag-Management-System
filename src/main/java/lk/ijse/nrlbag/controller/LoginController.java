package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import lk.ijse.nrlbag.App;
import lk.ijse.nrlbag.dto.UserDTO;
import lk.ijse.nrlbag.model.UserModel;
import lk.ijse.nrlbag.util.EmailService;

import java.io.IOException;
import java.util.Optional;

public class LoginController {


    private final UserModel userModel = new UserModel();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;


    @FXML
    public void login() throws IOException {

        try {
            String name = usernameField.getText();
            String password = passwordField.getText();

            UserDTO user = userModel.validLogin(name);

            if(user != null) {
                if(user.getUserPassword().equals(password)) {
                    System.out.println("Successfully login!");
                    //App.setRoot("dashBoard");

                    try {

                        App.setRoot("loadingPage"); // Load the main layout scene after successful login using the method from App class

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Error loading main layout: " + e.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Loading Error");
                        alert.setHeaderText(null);
                        alert.setContentText("An error occurred while loading the main layout. Please try again later.");
                        alert.showAndWait();
                    }

                } else {
                    System.out.println("Invalid Password");
                    new Alert(Alert.AlertType.ERROR , "Invalid Password Try Again!").show();
                }
            } else {
                System.out.println("Invalid Username & Password!");
                new Alert(Alert.AlertType.ERROR , "Invalid Username & Password Try Again!").show();
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR , "Something Went Wrong!").show();
        }
    }

    @FXML
    private void handleForgotPassword() {

        // create dialog box to ask user for their email
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reset Password");
        dialog.setHeaderText("Forgot Your Password");
        dialog.setContentText("Please enter your registered email:");

        // show and wait for the user input
        Optional<String> result = dialog.showAndWait();

        //if user closes the dialog without entering anything, stop here
        if (!result.isPresent()) {
            return;
        }
        // get the entered email and remove extra spaces.
        String email = result.get().trim();

        // check if the email field is empty
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required",
                    "Email cannot be empty");
            return;
        }
        // create UserModel obj for to access the database methods
        UserModel userModel1 = new UserModel();

        try {
            // get the user password using the enter email
            String actualPassword = userModel1.getPasswordByEmail(email);
            // if email have in the system
            if (actualPassword != null) {
                // send the recovery email
                boolean sent = EmailService.sendPasswordRecovery(email, actualPassword);

                // send msg if email was send
                if (sent) {
                    showAlert(Alert.AlertType.INFORMATION, "Email Sent",
                            "Your password has been sent to your email address.");
                } else {
                    // show error msg if email is not send
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not send email");
                }
            } else {
                // print error msg if entered email is not found in the database
                showAlert(Alert.AlertType.ERROR, "Not Found", "This mail is not registered");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
