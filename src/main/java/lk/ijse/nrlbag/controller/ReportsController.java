package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lk.ijse.nrlbag.model.*;

public class ReportsController {

    @FXML
    private TextField ocOrderIdField;

    @FXML
    private TextField payReportIdField;

    private final String ORDER_ID_REGEX = "^[0-9]+$";
    private final OrderModel orderModel = new OrderModel();
    private final PaymentModel paymentModel = new PaymentModel();
    private final MaterialModel materialModel = new MaterialModel();
    private final ProductModel productModel = new ProductModel();
    private final CustomerModel customerModel = new CustomerModel();

    @FXML
    private void handleCustomerList() {
        try {
            customerModel.printCustomerList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleLowStockReport() {
        try {
            materialModel.printLowMaterialStockReport();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleMaterialStockReport() {
        try {
            materialModel.printMaterialStockReport();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleOrderConfirmationReport() {

        try {
            String orderId = ocOrderIdField.getText().trim();

            if (!orderId.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
            } else {
                orderModel.printOrderConfirmation(Integer.parseInt(orderId));
                ocOrderIdField.clear();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleOrderPaymentReport() {
        try {
            String orderId = payReportIdField.getText().trim();

            if (!orderId.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
            } else {
                paymentModel.printOrderPaymentReceipt(Integer.parseInt(orderId));
                payReportIdField.clear();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleProductList() {
        try {
            productModel.printProductList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
