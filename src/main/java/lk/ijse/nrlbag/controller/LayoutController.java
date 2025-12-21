package lk.ijse.nrlbag.controller;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import lk.ijse.nrlbag.App;
import lk.ijse.nrlbag.model.CustomerModel;
import lk.ijse.nrlbag.model.MaterialModel;
import lk.ijse.nrlbag.model.OrderModel;
import lk.ijse.nrlbag.model.PaymentModel;
import lk.ijse.nrlbag.util.SetBackground;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.time.LocalDate;

public class LayoutController {

    @FXML
    private BorderPane dashBoardContent;

    @FXML
    private StackPane mainContent;

    @FXML
    private Pane rootPane;

    public void initialize() throws SQLException {
        SetBackground.setBackground(rootPane);
        clickDashboardNav();

    }

    public void loadingPage() {
        try {
            mainContent.getChildren().clear();
            mainContent.getChildren().setAll(App.loadFXML("loadingPage"));
        } catch (Exception e) {

        }
    }

    @FXML
    public void clickDashboardNav() {

        try {
            //Parent customerFXML = App.loadFXML("dashBoard");
            mainContent.getChildren().clear();
            mainContent.getChildren().setAll(App.loadFXML("dashBoard"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void clickCustomerNav() {
        try {

        Parent customerFXML = App.loadFXML("customer");
        mainContent.getChildren().clear();
        mainContent.getChildren().setAll(customerFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickOrderNav() {
        try {
//        Parent customerFXML = App.loadFXML("order");
        mainContent.getChildren().clear();
        mainContent.getChildren().setAll(App.loadFXML("order"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickSupplierNav() {
        try {
//        Parent customerFXML = App.loadFXML("supplier");
        mainContent.getChildren().clear();
        mainContent.getChildren().setAll(App.loadFXML("supplier"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickStockNav(){
        try {
//        Parent customerFXML = App.loadFXML("stock");
        mainContent.getChildren().clear();
        mainContent.getChildren().setAll(App.loadFXML("stock"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickPaymentNav(){
        try {
//        Parent customerFXML = App.loadFXML("payment");
        mainContent.getChildren().clear();
        mainContent.getChildren().setAll(App.loadFXML("payment"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickLogOutNav(){

        try {

        // show confirmation alert before logging out
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Log Out");
        confirmAlert.setHeaderText("Are you sure to Log Out?");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            // use login scene from App class to load login fxml
            App.setRoot("login");

        }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while trying to log out. Please try again.");
            alert.showAndWait();

            e.printStackTrace();
        }

    }

}



