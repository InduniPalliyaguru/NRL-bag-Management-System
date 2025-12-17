package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.nrlbag.dto.PaymentDTO;
import lk.ijse.nrlbag.model.PaymentModel;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentPopupController implements Initializable {

    @FXML
    private TextField orderIdField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField dateField;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private TextField searchField;
    @FXML
    private TextField payIdField;
    @FXML
    private TextField orderIdField1;
    @FXML
    private TextField amountField1;
    @FXML
    private TextField dateField1;
    @FXML
    private ComboBox<String> comboType1;
    @FXML
    private ComboBox<String> comboStatus1;

    private final PaymentModel paymentModel = new PaymentModel();

    private final String ORDER_ID_REGEX = "^[0-9]+$";
    private final String PAYMENT_ID_REGEX = "^[0-9]+$";
    private final String PAY_AMOUNT_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // in here set the values for the combo box to status
        comboStatus.getItems().addAll("Pending","Partial","Completed");
        comboStatus1.getItems().addAll("Pending","Partial","Completed");
        // in here set the values for the combo box to type
        comboType.getItems().addAll("Advance","Balance");
        comboType1.getItems().addAll("Advance","Balance");


    }

    @FXML
    private void handleSearchPayment() {

        try {
            // here, we get the data in search field where the input by user
            String id = searchField.getText();

            // after that we check it is valid input or not
            if (!id.matches(PAYMENT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Payment ID").show();
            } else {
                PaymentDTO paymentDTO = paymentModel.searchPayment(Integer.parseInt(id));

                // when id are set, other information to the text fields
                if (paymentDTO != null) {

                    payIdField.setText(String.valueOf(paymentDTO.getId()));
                    orderIdField1.setText(String.valueOf(paymentDTO.getOrder_id()));
                    amountField1.setText(String.valueOf(paymentDTO.getAmount()));
                    dateField1.setText(String.valueOf(paymentDTO.getPayment_date()));
                    comboType1.setValue(paymentDTO.getType());
                    comboStatus1.setValue(paymentDTO.getStatus());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Payment not found!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    @FXML
    private void handleSavePayment() {

        // in here get the user inputs to the fields in payment management
        String ordersID = orderIdField.getText().trim();
        String amount = amountField.getText().trim();
        String date = dateField.getText().trim();
        String type = comboType.getValue().trim();
        String status = comboStatus.getValue().trim();

        // after that here, check the validity about that user inputs.
        // if they are not valid then gives the error msg to the user.

        if (!ordersID.matches(ORDER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
        } else if (!amount.matches(PAY_AMOUNT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Payment Amount").show();
        } else if (isValidDate(date)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Date Input").show();
        } else if (type.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Payment type is empty").show();
        } else if (status.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Payment status is empty").show();
        }  else {

            // in here, if all details are valid
            // pass the data to the model class for save to the database
            try {
                PaymentDTO payDTO = new PaymentDTO(Integer.parseInt(ordersID),Double.parseDouble(amount),date,type,status);
                boolean result = paymentModel.savePaymentWithOrderUpdate(payDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Payment added successfully!").show();
                    clearSavedField();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
            }
        }

    }

    @FXML
    private void handleUpdatePayment() {

        // in here get the user inputs to the fields in payment management
        String ordersID = orderIdField1.getText().trim();
        String paymentID = payIdField.getText().trim();
        String amount = amountField1.getText().trim();
        String date = dateField1.getText().trim();
        String type = comboType1.getValue().trim();
        String status = comboStatus1.getValue().trim();

        // after that here, check the validity about that user inputs.
        // if they are not valid then gives the error msg to the user.

        if (!ordersID.matches(ORDER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
        } else if (!amount.matches(PAY_AMOUNT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Payment Amount").show();
        } else if (isValidDate(date)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Date Input").show();
        } else if (type.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Payment type is empty").show();
        } else if (status.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Payment status is empty").show();
        } else {

            // in here, if all details are valid
            // pass the data to the model class for save to the database
            try {
                PaymentDTO payDTO = new PaymentDTO(Integer.parseInt(paymentID),Integer.parseInt(ordersID),Double.parseDouble(amount),date,type,status);
                boolean result = paymentModel.updatePaymentWithOrderUpdate(payDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Payment update successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
            }
        }

    }

    @FXML
    private void handleDeletePayment() {

        try {

            // get the id from the id field and check it valid or not
            String id = payIdField.getText().trim();
            String orderId = orderIdField1.getText().trim();
            if(!id.matches(PAYMENT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Payment ID").show();
                return;
            }
            // here show confirm alert before delete
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Are you sure to delete this Payment?");
            confirmAlert.setContentText("Payment ID: "+id + "\nOrder ID: "+orderId);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                // after the validation pass the id to customer model for delete
                boolean result1 = paymentModel.deletePaymentWithOrderUpdate(Integer.parseInt(id), Integer.parseInt(orderId));

                if (result1) {
                    new Alert(Alert.AlertType.INFORMATION, "Payment Deleted Successfully").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong").show();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong").show();
        }

    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private boolean isValidDate(String input) {
        try {
            LocalDate.parse(input);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private void clearSavedField() {
        orderIdField.setText("");
        amountField.setText("");
        dateField.setText("");
        comboType.setValue("");
        comboStatus.setValue("");
    }

    private void clearFields() {
        searchField.setText("");
        payIdField.setText("");
        orderIdField1.setText("");
        amountField.setText("");
        dateField.setText("");
        comboType.setValue("");
        comboStatus.setValue("");
    }

}
