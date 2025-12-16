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
                    App.setRoot("dashBoard");
                } else {
                    System.out.println("Invalid Password");
                    new Alert(Alert.AlertType.ERROR , "Invalid Password Try Again!").show();
                }
            } else {
                System.out.println("Invalid Username & Password!");
                new Alert(Alert.AlertType.ERROR , "Invalid Username & Password Try Again!").show();
            }

        } catch(Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR , "Something Went Wrong!").show();
        }
    }

}
