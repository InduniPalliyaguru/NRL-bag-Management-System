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
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.nrlbag.dto.CustomerDTO;
import lk.ijse.nrlbag.model.CustomerModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class CustomerController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField createDateField;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<CustomerDTO> tblCustomer;
    @FXML
    private TableColumn<CustomerDTO, Integer> colId;
    @FXML
    private TableColumn<CustomerDTO, String> colName;
    @FXML
    private TableColumn<CustomerDTO, String> colAddress;
    @FXML
    private TableColumn<CustomerDTO, String> colContact;
    @FXML
    private TableColumn<CustomerDTO, String> colCreateDate;

    private final String CUSTOMER_CONTACT_REGEX = "^[0-9]{10}$";

    private final CustomerModel customerModel = new CustomerModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {



        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colCreateDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadCustomerTable();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, selected) -> {

                    if (selected == null) return;
                    String contact = selected.getContact();
                    handleSearchCustomerByRowSelected(contact);
                }
        );

    }

    @FXML
    private void handleSearchCustomer() {

        try {
            // here, we get the data in search field where the input by user
            String contact = searchField.getText();

            // after that we check it is valid input or not
            if (!contact.matches(CUSTOMER_CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer contact number").show();
            } else {
                CustomerDTO customerDTO = customerModel.searchCustomer(contact);

                // when contact are set other information to the text fields
                if (customerDTO != null) {
                    idField.setText(String.valueOf(customerDTO.getId()));
                    nameField.setText(customerDTO.getName());
                    addressField.setText(customerDTO.getAddress());
                    contactField.setText(customerDTO.getContact());
                    createDateField.setText(customerDTO.getDate());

                    highLightCustomer(customerDTO.getContact());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer not found!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    @FXML
    private void handleResetFields() {
        clearFields();
    }

    private void loadCustomerTable() {
        try {

            List<CustomerDTO> cusDTO =  customerModel.getCustomer();

            ObservableList<CustomerDTO> obList = FXCollections.observableArrayList();

            // get one by customer from the customer list and they are add into the obList
            for (CustomerDTO customerDTO : cusDTO) {
                obList.add(customerDTO);
            }
            // then add that list to the table
            tblCustomer.setItems(obList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void clearFields() {

        searchField.setText("");
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        contactField.setText("");
        createDateField.setText("");

    }

    private void highLightCustomer(String contact) {
        //row factory allows us to define how each row look
        tblCustomer.setRowFactory( tv -> new TableRow<>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(CustomerDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows contact number matches to the searching customer's contact
                if(item.getContact().equals(contact)) {
                    // here set the colour for the search customer row
                    setStyle("-fx-background-color: #DB804E;");
                } else {
                    // if it does not match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblCustomer.refresh();
    }

    @FXML
    private void btnAddOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/ijse/nrlbag/view/customerPopUp.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Customers");
            stage.initModality(Modality.APPLICATION_MODAL); // Block main window
            stage.setResizable(false);
            stage.showAndWait();
            loadCustomerTable();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleSearchCustomerByRowSelected(String contact) {

        try {

            // after that we check it is valid input or not
            if (!contact.matches(CUSTOMER_CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer contact number").show();
            } else {
                CustomerDTO customerDTO = customerModel.searchCustomer(contact);

                // when contact are set other information to the text fields
                if (customerDTO != null) {
                    idField.setText(String.valueOf(customerDTO.getId()));
                    nameField.setText(customerDTO.getName());
                    addressField.setText(customerDTO.getAddress());
                    contactField.setText(customerDTO.getContact());
                    createDateField.setText(customerDTO.getDate());

                    highLightCustomer(customerDTO.getContact());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer not found!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

}
