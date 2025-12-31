package lk.ijse.nrlbag.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.nrlbag.dto.PaymentDTO;
import lk.ijse.nrlbag.model.PaymentModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    private TableView<PaymentDTO> tblPayment;
    @FXML
    private TableColumn<PaymentDTO, Integer> colPayId;
    @FXML
    private TableColumn<PaymentDTO, Integer> colOrderId;
    @FXML
    private TableColumn<PaymentDTO, String> colDate;
    @FXML
    private TableColumn<PaymentDTO, Double> colAmount;
    @FXML
    private TableColumn<PaymentDTO, String> colType;
    @FXML
    private TableColumn<PaymentDTO, String> colStatus;
    @FXML
    private TextField searchField;
    @FXML
    private TextField payIdField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField orderIdField;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private ComboBox<String> comboStatus;

    private final PaymentModel paymentModel = new PaymentModel();

    private final String PAYMENT_ID_REGEX = "^[0-9]+$";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colPayId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("payment_date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadPaymentTable();

        tblPayment.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, selected) -> {

                    if (selected == null) return;
                    int id = selected.getId();
                    handleSearchPaymentByRowSelected(String.valueOf(id));
                }
        );

    }

    @FXML
    private void handleSearchPayment() {

        try {

            // get the supplier id from the search field
            String id = searchField.getText();

            // check the validity of the id
            if (!id.matches(PAYMENT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Payment ID!").show();
            } else {

                PaymentDTO payDTO = paymentModel.searchPayment(Integer.parseInt(id));

                if (payDTO != null) {
                    payIdField.setText(String.valueOf(payDTO.getId()));
                    orderIdField.setText(String.valueOf(payDTO.getOrder_id()));
                    dateField.setText(payDTO.getPayment_date());
                    comboType.setValue(payDTO.getType());
                    comboStatus.setValue(payDTO.getStatus());
                    amountField.setText(String.valueOf(payDTO.getAmount()));

                    highlightSearchPayment(payDTO.getId());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Cannot Find Payment!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private void loadPaymentTable() {

        try {

            List<PaymentDTO> paymentDTO = paymentModel.getPayments();

            // TableView always requires and ObservableList it automatically update that details
            ObservableList<PaymentDTO> obList = FXCollections.observableArrayList();

            // after that we set one by one from paymentList in to the observable list
            for (PaymentDTO payDTO : paymentDTO) {
                obList.add(payDTO);
            }

            // then set that list to the table
            tblPayment.setItems(obList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void highlightSearchPayment(int id) {

        //row factory allows us to define how each row look
        tblPayment.setRowFactory( tv -> new TableRow<>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(PaymentDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows id number matches to the searching payment id
                if(item.getId() == id) {
                    // here set the colour for the search supplier row
                    setStyle("-fx-background-color: #DB804E;");
                } else {
                    // if it does not match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblPayment.refresh();

    }

    private void clearFields() {
        searchField.clear();
        payIdField.clear();
        orderIdField.clear();
        dateField.clear();
        amountField.clear();
        comboType.setValue("");
        comboStatus.setValue("");
    }

    @FXML
    private void btnAddOnActionPayment() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/ijse/nrlbag/view/paymentPopup.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Payments");
            stage.initModality(Modality.APPLICATION_MODAL); // Block main window
            stage.setResizable(false);
            stage.showAndWait();
            loadPaymentTable();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleSearchPaymentByRowSelected(String id) {

        try {

            // check the validity of the id
            if (!id.matches(PAYMENT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Payment ID!").show();
            } else {

                PaymentDTO payDTO = paymentModel.searchPayment(Integer.parseInt(id));

                if (payDTO != null) {
                    payIdField.setText(String.valueOf(payDTO.getId()));
                    orderIdField.setText(String.valueOf(payDTO.getOrder_id()));
                    dateField.setText(payDTO.getPayment_date());
                    comboType.setValue(payDTO.getType());
                    comboStatus.setValue(payDTO.getStatus());
                    amountField.setText(String.valueOf(payDTO.getAmount()));

                    highlightSearchPayment(payDTO.getId());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Cannot Find Payment!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

}
