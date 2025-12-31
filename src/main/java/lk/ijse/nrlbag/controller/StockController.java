package lk.ijse.nrlbag.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.util.Locale;
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
    private TableView<MaterialDTO> tblMaterial;
    @FXML
    private TableColumn<MaterialDTO, Integer> colId;
    @FXML
    private TableColumn<MaterialDTO, String> colName;
    @FXML
    private TableColumn<MaterialDTO, Integer> colQty;
    @FXML
    private TableColumn<MaterialDTO, String> colUnit;
    @FXML
    private TableColumn<MaterialDTO, Integer> colSupId;

    final static MaterialModel materialModel = new MaterialModel();
    private final ObservableList<MaterialDTO> masterMaterialList = FXCollections.observableArrayList();
    private FilteredList<MaterialDTO> filteredMaterialList;

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
        enableLiveSearch();

        tblMaterial.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, selected) -> {

                    if (selected == null) return;
                    int id = selected.getMaterial_id();
                    handleSearchMaterialByRowSelected(String.valueOf(id));
                }
        );

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

            masterMaterialList.clear();
            masterMaterialList.addAll(matDTO);

            // create filtered list once
            filteredMaterialList = new FilteredList<>(masterMaterialList, p -> true);

            // allow sorting also
            SortedList<MaterialDTO> sortedList = new SortedList<>(filteredMaterialList);
            sortedList.comparatorProperty().bind(tblMaterial.comparatorProperty());

            // set to table
            tblMaterial.setItems(sortedList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void highLightStock(int id) {
        //row factory allows us to define how each row look
        tblMaterial.setRowFactory( tv -> new TableRow<>() {
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
                    setStyle("-fx-background-color: #DB804E;");
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
        tblMaterial.setRowFactory( tv -> new TableRow<>() {
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
                if(item.getQtyAvailable() <50) {
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

    @FXML
    private void handleSearchMaterialByRowSelected(String id) {

        try {
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

    private void enableLiveSearch() {
        searchField.textProperty().addListener((obs,oldText, newText) -> {
            String search = newText.toLowerCase().trim();

            filteredMaterialList.setPredicate(materialDTO -> {

                // show all when search field is empty
                if (search.isEmpty()) return true;

                // search by material name
                if (materialDTO.getMaterial_name().toLowerCase().contains(search)) {
                    return true;
                }

                // search by material ID
                if (String.valueOf(materialDTO.getMaterial_id()).contains(search)) {
                    return true;
                }

                // search by supplier ID
                return String.valueOf(materialDTO.getSupplier_id()).contains(search);
            });
        });
    }

}
