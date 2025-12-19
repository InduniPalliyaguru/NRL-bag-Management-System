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
import lk.ijse.nrlbag.dto.MaterialDTO;
import lk.ijse.nrlbag.model.MaterialModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField qtyField;
    @FXML
    private ComboBox<String> comboUnit;
    @FXML
    private TextField supplierIdField;
    @FXML
    private TextField searchField;
    @FXML
    private TableView tblMaterial;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colQty;
    @FXML
    private TableColumn colUnit;
    @FXML
    private TableColumn colSupId;

    final static MaterialModel materialModel = new MaterialModel();

    private final String MATERIAL_ID_REGEX = "^[0-9]+$";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colId.setCellValueFactory(new PropertyValueFactory<>("material_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("material_name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyAvailable"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));

        loadMaterialTable();
        highLightLowStockMaterials();
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
                    idField.setText(String.valueOf(materialDTO.getMaterial_id()));
                    nameField.setText(materialDTO.getMaterial_name());
                    comboUnit.setValue(materialDTO.getUnit());
                    qtyField.setText(String.valueOf(materialDTO.getQtyAvailable()));
                    supplierIdField.setText(String.valueOf(materialDTO.getSupplier_id()));

                    highLightStock(materialDTO.getMaterial_id());
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
    private void handleResetFields() {
        clearFields();
    }

    private void clearFields() {

        searchField.setText("");
        idField.setText("");
        nameField.setText("");
        qtyField.setText("");
        comboUnit.setValue("");
        supplierIdField.setText("");

    }

    private void loadMaterialTable() {
        try {

            List<MaterialDTO> matDTO  =  materialModel.getMaterial();

            ObservableList<MaterialDTO> obList = FXCollections.observableArrayList();

            // get one by material from the material list and they are add into the obList
            for (MaterialDTO materialDTO : matDTO) {
                obList.add(materialDTO);
            }
            // then add that list to the table
            tblMaterial.setItems(obList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void highLightStock(int id) {
        //row factory allows us to define how each row look
        tblMaterial.setRowFactory( tv -> new TableRow<MaterialDTO>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(MaterialDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows contact number matches to the searching material id
                if(item.getMaterial_id() == id) {
                    // here set the colour for the search material row
                    setStyle("-fx-background-color: #e2baf7;");
                } else {
                    // if it does not match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblMaterial.refresh();
    }

    @FXML
    private void btnAddOnActionStock() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/ijse/nrlbag/view/stockPopup.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Material Stock");
            stage.initModality(Modality.APPLICATION_MODAL); // Block main window
            stage.setResizable(false);
            stage.showAndWait();
            loadMaterialTable();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void btnAddOnActionProduct() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/ijse/nrlbag/view/product.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Products");
            stage.initModality(Modality.APPLICATION_MODAL); // Block main window
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void highLightLowStockMaterials() {
        //row factory allows us to define how each row look
        tblMaterial.setRowFactory( tv -> new TableRow<MaterialDTO>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(MaterialDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows contact number matches to the searching material id
                if(item.getQtyAvailable() <10) {
                    // here set the colour for the search material row
                    setStyle("-fx-background-color: #f1060a; -fx-control-inner-background: #f1060a; -fx-text-fill: white;");
                } else {
                    // if it does not match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblMaterial.refresh();
    }

}
