package lk.ijse.nrlbag.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.nrlbag.dto.MaterialDTO;
import lk.ijse.nrlbag.model.MaterialModel;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockPopupController implements Initializable {

    @FXML
    private TextField materialNameField;
    @FXML
    private TextField qtyField;
    @FXML
    private ComboBox<String> comboUnit;
    @FXML
    private TextField supplierIdField;
    @FXML
    private TextField materialIdField1;
    @FXML
    private TextField materialNameField1;
    @FXML
    private ComboBox<String> comboUnit1;
    @FXML
    private TextField supIdField1;
    @FXML
    private TextField searchField;
    @FXML
    private TextField qtyField1;

    private final MaterialModel materialModel = new MaterialModel();

    private final String MATERIAL_ID_REGEX = "^[0-9]+$";
    private final String SUPPLIER_ID_REGEX = "^[0-9]+$";
    private final String MATERIAL_NAME_REGEX = "^[A-Za-z]{3,}\\s[A-Za-z0-9]{2,}$";
    private final String QTY_AVAILABLE_REGEX = "^[0-9]+(\\\\.[0-9]+)?$";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // in here set the values for the combo box to unit
        comboUnit.getItems().addAll("YARDS","METERS","LITERS","KG","ROLL","PIECES");
        comboUnit1.getItems().addAll("YARDS","METERS","LITERS","KG","ROLL","PIECES");

    }

    @FXML
    private void handleSearchMaterial() {

        try {
            // get the id from field
            String id = searchField.getText();

            //check validity
            if(!id.matches(MATERIAL_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Material ID").show();
            } else {
                // get the details of the material through MaterialDTO & MaterialModal
                MaterialDTO materialDTO = materialModel.searchMaterial(Integer.parseInt(id));

                // materialDTO is not null then assign their values into the text fields
                if(materialDTO!= null) {
                    materialIdField1.setText(String.valueOf(materialDTO.getMaterial_id()));
                    materialNameField1.setText(materialDTO.getMaterial_name());
                    comboUnit1.setValue(materialDTO.getUnit());
                    qtyField1.setText(String.valueOf(materialDTO.getQtyAvailable()));
                    supIdField1.setText(String.valueOf(materialDTO.getSupplier_id()));
                } else {
                    new Alert(Alert.AlertType.ERROR, "Material Not Found!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleSaveMaterials() {

        // in here get the user inputs to the fields in material management
        String name = materialNameField.getText().trim();
        String qty = qtyField.getText().trim();
        String supID = supplierIdField.getText().trim();
        String unit = comboUnit.getValue().trim();

        // after that here, check the validity about that user inputs.
        // if they are not valid then gives the error msg to the user.

        if (!name.matches(MATERIAL_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Material Name").show();
        } else if (!qty.matches(QTY_AVAILABLE_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity").show();
        } else if (!supID.matches(SUPPLIER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID").show();
        } else {

            // in here, if all details are valid
            // pass the data to the model class for save to the database
            try {
                MaterialDTO materialDTO = new MaterialDTO(Integer.parseInt(supID),name,unit,Double.parseDouble(qty));
                boolean result = materialModel.saveMaterial(materialDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Material added successfully!").show();
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
    private void handleUpdateMaterial() {

        try {

            // get the user input details from the fields
            String materialId = materialIdField1.getText().trim();
            String supId = supIdField1.getText().trim();
            String name = materialNameField1.getText().trim();
            String unit = comboUnit1.getValue().trim();
            String qty = qtyField1.getText().trim();

            // check they are valid or not
            if(!materialId.matches(MATERIAL_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Material ID").show();
            } else if(!supId.matches(SUPPLIER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID").show();
            } else if(!name.matches(MATERIAL_NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Material Name").show();
            } else if(!qty.matches(QTY_AVAILABLE_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Quantity").show();
            } else {

                // if valid then create a materialDTO object including that details
                MaterialDTO mtDTO = new MaterialDTO(Integer.parseInt(materialId),Integer.parseInt(supId),name,unit,Double.parseDouble(qty));

                // after that pass that to the orderModel class for connect with database
                boolean result = materialModel.updateMaterial(mtDTO);
                if(result) {
                    new Alert(Alert.AlertType.INFORMATION, "Material Details Updated Successfully!").show();
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
    private void handleDeleteMaterial() {

        try {
            // in this method delete the material details according to the material id
            String id = searchField.getText();

            if(!id.matches(MATERIAL_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Material ID").show();
                return;
            }

            // here show confirm alert before delete
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Are you sure to delete this Material?");
            confirmAlert.setContentText("Material ID: "+id);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                boolean result1 = materialModel.deleteMaterial(Integer.parseInt(id));

                // result is true that mean it is deleted
                if(result1) {
                    new Alert(Alert.AlertType.INFORMATION, "Material Deleted Successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Material Not Found!").show();
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

    private void clearSavedField() {
        materialNameField.setText("");
        qtyField.setText("");
        comboUnit.setValue("");
        supplierIdField.setText("");

    }

    private void clearFields() {
        materialNameField1.setText("");
        materialIdField1.setText("");
        comboUnit1.setValue("");
        supIdField1.setText("");
        searchField.setText("");
        qtyField1.setText("");
    }

}
