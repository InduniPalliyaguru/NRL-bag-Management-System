package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lk.ijse.nrlbag.App;
import lk.ijse.nrlbag.util.SetBackground;

import java.io.IOException;
import java.sql.SQLException;

public class SettingController {

    @FXML
    private StackPane mainContent;

    @FXML
    private Pane rootPane;

    public void initialize() throws SQLException {
        SetBackground.setBackground(rootPane);
        clickProfileNav();

    }

    @FXML
    public void clickProfileNav() {

        try {
            mainContent.getChildren().clear();
            mainContent.getChildren().setAll(App.loadFXML("profile"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    @FXML
    public void clickChangePasswordNav() {
        try {

            Parent customerFXML = App.loadFXML("changePassword");
            mainContent.getChildren().clear();
            mainContent.getChildren().setAll(customerFXML);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void clickAboutNav() {
        try {
            mainContent.getChildren().clear();
            mainContent.getChildren().setAll(App.loadFXML("about"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
