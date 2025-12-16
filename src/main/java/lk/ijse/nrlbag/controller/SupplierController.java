package lk.ijse.nrlbag.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.nrlbag.dto.SupplierDTO;
import lk.ijse.nrlbag.model.SupplierModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private TableView tblSupplier;
    @FXML
    private TableColumn colSupId;
    @FXML
    private TableColumn colSupName;
    @FXML
    private TableColumn colAddress;
    @FXML
    private TableColumn colContact;
    @FXML
    private TableColumn colMaterialId;
    @FXML
    private TableColumn colMaterialName;
    @FXML
    private TextField searchField;
    @FXML
    private TextField supIdField;
    @FXML
    private TextField supNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField contactField;

    private final SupplierModel supplierModel = new SupplierModel();

    private final String SUPPLIER_ID_REGEX = "^[0-9]+$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colSupId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("materialName"));

        loadSupplierTable();

    }

    @FXML
    private void loadSupplierTable() {

        try {

            List<SupplierDTO> supplierList = supplierModel.getSuppliers();

            // TableView always requires and ObservableList it automatically update that details
            ObservableList<SupplierDTO> obList = FXCollections.observableArrayList();

            // after that we set one by one from supplierList in to the observable list
            for (SupplierDTO supplierDTO : supplierList) {
                obList.add(supplierDTO);
            }
            // then set that list to the table
            tblSupplier.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSearchSupplier() {

        try {

            // get the supplier id from the search field
            String id = searchField.getText();

            // check the validity of the id
            if (!id.matches(SUPPLIER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID!").show();
            } else {

                SupplierDTO supDTO = supplierModel.searchSupplier(Integer.parseInt(id));

                if (supDTO != null) {
                    supIdField.setText(String.valueOf(supDTO.getId()));
                    supNameField.setText(supDTO.getName());
                    addressField.setText(supDTO.getAddress());
                    contactField.setText(supDTO.getContact());

                    highlightSearchSupplier(supDTO.getId());
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
    private void handleClearField() {

        clearFields();

    }

    private void clearFields() {

        supIdField.setText("");
        supNameField.setText("");
        addressField.setText("");
        contactField.setText("");
        searchField.setText("");

    }

    private void highlightSearchSupplier(int id) {

        //rowfactory allows us to define how each row look
        tblSupplier.setRowFactory( tv -> new TableRow<SupplierDTO>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(SupplierDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows id number matches to the searching supplier's id
                if(item.getId() == id) {
                    // here set the colour for the search supplier row
                    setStyle("-fx-background-color: #e2baf7;");
                } else {
                    // if doesnt match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblSupplier.refresh();

    }

    @FXML
    private void btnAddOnActionSupplier(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/ijse/nrlbag/view/supplierPopup.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Suppliers");
            stage.initModality(Modality.APPLICATION_MODAL); // Block main window
            stage.setResizable(false);
            stage.showAndWait();
            loadSupplierTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
