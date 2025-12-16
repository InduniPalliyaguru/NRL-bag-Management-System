package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lk.ijse.nrlbag.App;
import lk.ijse.nrlbag.model.CustomerModel;
import lk.ijse.nrlbag.model.MaterialModel;
import lk.ijse.nrlbag.model.OrderModel;
import lk.ijse.nrlbag.model.PaymentModel;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.time.LocalDate;

public class DashBoardController {

    @FXML
    private AnchorPane mainContent;
    @FXML
    private BorderPane dashBoardContent;
    @FXML
    private Label lblTotalCustomer;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTotalOrders;
    @FXML
    private Label lblLowStockCount;
    @FXML
    private Label lblPendingCount;
    @FXML
    private BarChart<String,Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private AreaChart<String, Number> monthlyIncomeChart;
    @FXML
    private CategoryAxis incomeXAxis;
    @FXML
    private NumberAxis incomeYAxis;

    public void initialize() throws SQLException {
        lblDate.setText(LocalDate.now().toString());

        /* here get the total customer count from the CustomerModel class, it assigns into the
            label total customers that have in dashboard. */
        lblTotalCustomer.setText(String.valueOf(CustomerModel.totalCustomerCount()));

        /* here get the total order count from the OrderModel class, it assigns into the
            label total orders that have in dashboard. */
        lblTotalOrders.setText(String.valueOf(OrderModel.totalOrderCount()));

        /* here get the total low stock count from the MaterialModel class, it assigns into the
            label total low stock count that have in dashboard. */
        lblLowStockCount.setText(String.valueOf(MaterialModel.totalLowMaterialCount()));

        /* here get the total pending payments count from the PaymentModel class, it assigns into the
            label total pending payments that have in dashboard. */
        lblPendingCount.setText(String.valueOf(PaymentModel.totalPendingPaymentsCount()));

        // this is load at everytime dashboard barchart
        loadMonthlyOrders();
        // this is load at everytime dashboard area chart
        loadMonthlyIncome();
    }

    @FXML
    public void clickDashboardNav() throws IOException {
        Parent customerFXML = App.loadFXML("dashBoard");
        dashBoardContent.getChildren().setAll(customerFXML);
    }

    @FXML
    public void clickCustomerNav() throws IOException {
        Parent customerFXML = App.loadFXML("customer");
        mainContent.getChildren().setAll(customerFXML);
    }

    @FXML
    public void clickOrderNav() throws IOException {
        Parent customerFXML = App.loadFXML("order");
        mainContent.getChildren().setAll(customerFXML);
    }

    @FXML
    public void clickSupplierNav() throws IOException {
        Parent customerFXML = App.loadFXML("supplier");
        mainContent.getChildren().setAll(customerFXML);
    }

    @FXML
    public void clickStockNav() throws IOException {
        Parent customerFXML = App.loadFXML("stock");
        mainContent.getChildren().setAll(customerFXML);
    }

    @FXML
    public void clickPaymentNav() throws IOException {
        Parent customerFXML = App.loadFXML("payment");
        mainContent.getChildren().setAll(customerFXML);
    }

    @FXML
    public void clickLogOutNav() throws IOException {

        // here show confirm alert before delete
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Log Out");
        confirmAlert.setHeaderText("Are you sure to Log Out?");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            Parent customerFXML = App.loadFXML("login");
            dashBoardContent.getChildren().setAll(customerFXML);

        }

    }

    private void loadMonthlyOrders() {

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Orders");

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        try {

            for (int i = 1; i<=12; i++) {
                int count = OrderModel.getOrderByMonths(i);
                series.getData().add(new XYChart.Data<>(months[i-1],count));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        barChart.getData().clear();
        barChart.getData().add(series);

    }

    private void loadMonthlyIncome() {

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        double[] monthlyIncome = new double[12];

        try {
            ResultSet rs = OrderModel.getMonthlyIncome();

            while(rs.next()) {
                int monthIndex = rs.getInt("month")-1;
                double income = rs.getDouble("income");
                monthlyIncome[monthIndex] = income;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i<12; i++) {
            incomeSeries.getData().add(
                    new XYChart.Data<>(months[i], monthlyIncome[i] )
            );
        }

        monthlyIncomeChart.getData().clear();
        monthlyIncomeChart.getData().add(incomeSeries);
    }

}
