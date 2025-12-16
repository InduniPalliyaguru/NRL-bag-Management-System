package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import lk.ijse.nrlbag.dto.SupplierDTO;
import lk.ijse.nrlbag.model.SupplierModel;

import java.util.Optional;

public class SupplierPopup {

    @FXML
    private TextField supNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField searchField1;
    @FXML
    private TextField supIdField1;
    @FXML
    private TextField supNameField1;
    @FXML
    private TextField addressField1;
    @FXML
    private TextField contactField1;

    private final SupplierModel supplierModel = new SupplierModel();

    private final String SUPPLIER_ID_REGEX = "^[0-9]+$";
    private final String SUPPLIER_NAME_REGEX = "^[A-Za-z]{3,}\\s[A-Za-z]{3,}$";
    private final String SUPPLIER_ADDRESS_REGEX = "^[A-Za-z0-9]{5,}$";
    private final String SUPPLIER_CONTACT_REGEX = "^[0-9]{10}$";

    @FXML
    private void handleSaveSupplier() {

        try {

            //get the user input from the fields
            String name = supNameField.getText();
            String address = addressField.getText();
            String contact = contactField.getText();

            // after that check the validity of the user inputs
            if (!name.matches(SUPPLIER_NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier Name!").show();
            } else if (!address.matches(SUPPLIER_ADDRESS_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier Address!").show();
            } else if (!contact.matches(SUPPLIER_CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier Contact!").show();
            } else {

                // then create the supplierDTO object and assign these values into it
                SupplierDTO supplierDTO = new SupplierDTO(name, address, contact);

                // after that pass it to Supplier model to save in database
                String rs = supplierModel.saveSupplier(supplierDTO);

                if (rs.isEmpty()) {
                    // rs == null mean it is saved and there has no errors
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Added Successfully").show();
                    clearFieldsSaved();

                } else {
                    new Alert(Alert.AlertType.ERROR, rs+"\nTry Again!").show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleSearchSupplier() {

        try {

            // get the supplier id from the search field
            String id = searchField1.getText();

            // check the validity of the id
            if (!id.matches(SUPPLIER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID!").show();
            } else {

                SupplierDTO supDTO = supplierModel.searchSupplier(Integer.parseInt(id));

                if (supDTO != null) {
                    supIdField1.setText(String.valueOf(supDTO.getId()));
                    supNameField1.setText(supDTO.getName());
                    addressField1.setText(supDTO.getAddress());
                    contactField1.setText(supDTO.getContact());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Cannot Find Supplier!").show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleUpdateSupplier() {

        try {

            // in here get the user inputs to the fields in supplier management
            String id = supIdField1.getText().trim();
            String name = supNameField1.getText().trim();
            String address = addressField1.getText();
            String contact = contactField1.getText();

            // after that here, check the validity about that user inputs.
            // if they are not valid then gives an error msg to the user.

            if (!id.matches(SUPPLIER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID").show();
            } else if (!name.matches(SUPPLIER_NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier Name").show();
            } else if (!address.matches(SUPPLIER_ADDRESS_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier Address").show();
            } else if (!contact.matches(SUPPLIER_CONTACT_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier Contact Number").show();
            } else {

                // in here, if all details are valid
                // pass the data to the model class for update the database

                SupplierDTO supplierDTO = new SupplierDTO(Integer.parseInt(id), name, address, contact);
                boolean result = supplierModel.updateSupplier(supplierDTO);

                // if result is true that mean there had an update.
                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteSupplier() {

        try {

            // get the id from the id field and check it valid or not
            String id = supIdField1.getText();
            if(!id.matches(SUPPLIER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID").show();
                return;
            }

            // here show confirm alert before delete
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Are you sure to delete this Supplier?");
            confirmAlert.setContentText("Supplier ID: "+id);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                // after the validation pass the id to supplier model for delete
                boolean result1 = supplierModel.deleteSupplier(Integer.parseInt(id));

                if(result1) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong").show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong").show();
        }

    }

    @FXML
    private void handleClearField() {

        clearFields();

    }

    private void clearFields() {

        supIdField1.setText("");
        supNameField1.setText("");
        addressField1.setText("");
        contactField1.setText("");
        searchField1.setText("");

    }

    private void clearFieldsSaved() {

        supNameField.setText("");
        addressField.setText("");
        contactField.setText("");

    }

}
