package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lk.ijse.nrlbag.App;
import lk.ijse.nrlbag.dto.UserDTO;
import lk.ijse.nrlbag.model.UserModel;

import java.io.IOException;

public class LoginController {

    private final LayoutController layoutController = new LayoutController();

    private final UserModel userModel = new UserModel();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

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
                        e.printStackTrace();
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

}
