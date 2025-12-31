package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import lk.ijse.nrlbag.dto.CustomerDTO;
import lk.ijse.nrlbag.model.CustomerModel;

import java.util.Optional;

public class CustomerPopUpController {

    @FXML
    private TextField idField1;
    @FXML
    private TextField nameField1;
    @FXML
    private TextField addressField1;
    @FXML
    private TextField contactField1;
    @FXML
    private TextField createDateField1;
    @FXML
    private TextField searchField1;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField contactField;

    private final CustomerModel customerModel = new CustomerModel();

    private final String CUSTOMER_ID_REGEX = "^[0-9]+$";
    private final String CUSTOMER_NAME_REGEX = "^[A-Za-z]{3,}\\s[A-Za-z]{3,}$";
    private final String CUSTOMER_ADDRESS_REGEX = "^[A-Za-z0-9]{5,}$";
    private final String CUSTOMER_CONTACT_REGEX = "^[0-9]{10}$";


    @FXML
    private void handleSaveCustomer() {

        // in here get the user inputs to the fields in customer management
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String contact = contactField.getText().trim();

        // after that here, check the validity about that user inputs.
        // if they are not valid then gives the error msg to the user.

        if (!name.matches(CUSTOMER_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Name").show();
        } else if (!address.matches(CUSTOMER_ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Address").show();
        } else if (!contact.matches(CUSTOMER_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Contact Number").show();
        } else {

            // in here, if all details are valid
            // pass the data to the model class for save to the database
            try {
                CustomerDTO customerDTO = new CustomerDTO(name, address, contact);
                String result = customerModel.saveCustomer(customerDTO);

                // if result is not empty that mean there has an error
                if (!result.isEmpty()) {

                    new Alert(Alert.AlertType.ERROR, result + "\nTry again!").show();
                    return;
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully!").show();
                    clearFieldsSaved();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
            }
        }

    }

    @FXML
    private void handleSearchCustomer() {

        try {
            // here, we get the data in search field where the input by user
            String contact = searchField1.getText();

            // after that we check it is valid input or not
            if (!contact.matches(CUSTOMER_CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer contact number").show();
            } else {
                CustomerDTO customerDTO = customerModel.searchCustomer(contact);

                // when contact are set other information to the text fields
                if (customerDTO != null) {
                    idField1.setText(String.valueOf(customerDTO.getId()));
                    nameField1.setText(customerDTO.getName());
                    addressField1.setText(customerDTO.getAddress());
                    contactField1.setText(customerDTO.getContact());
                    createDateField1.setText(customerDTO.getDate());

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
    private void handleUpdateCustomer() {

        try {

            // in here get the user inputs to the fields in customer management
            String id = idField1.getText().trim();
            String name = nameField1.getText().trim();
            String address = addressField1.getText();
            String contact = contactField1.getText();

            // after that here, check the validity about that user inputs.
            // if they are not valid then gives the error msg to the user.

            if (!id.matches(CUSTOMER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Customer ID").show();
            } else if (!name.matches(CUSTOMER_NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Customer Name").show();
            } else if (!address.matches(CUSTOMER_ADDRESS_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Customer Address").show();
            } else if (!contact.matches(CUSTOMER_CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Customer Contact Number").show();
            } else {

                // in here, if all details are valid
                // pass the data to the model class for update the database

                CustomerDTO customerDTO = new CustomerDTO(Integer.parseInt(id), name, address, contact);
                boolean result = customerModel.updateCustomer(customerDTO);

                // if result is true that mean there had an update.
                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();

                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleDeleteCustomer() {

        try {

            // get the id from the id field and check it valid or not
            String id = idField1.getText();
            if(!id.matches(CUSTOMER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Customer ID").show();
                return;
            }
            // here show confirm alert before delete
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Are you sure to delete this Customer?");
            confirmAlert.setContentText("Customer ID: "+id);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                // after the validation pass the id to customer model for delete
                boolean result1 = customerModel.deleteCustomer(id);

                if (result1) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully").show();
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
    private void handleResetFields() {
        clearFields();
    }

    private void clearFields() {

        searchField1.clear();
        idField1.clear();
        nameField1.clear();
        addressField1.clear();
        contactField1.clear();
        createDateField1.clear();

    }

    private void clearFieldsSaved() {
        nameField.clear();
        addressField.clear();
        contactField.clear();

    }

}

