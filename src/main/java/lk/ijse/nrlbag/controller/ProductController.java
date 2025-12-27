package lk.ijse.nrlbag.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.nrlbag.dto.ProductDTO;
import lk.ijse.nrlbag.model.ProductModel;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private TableView<ProductDTO> tblProduct;
    @FXML
    private TableColumn<ProductDTO, Integer> colId;
    @FXML
    private TableColumn<ProductDTO, String> colName;
    @FXML
    private TableColumn<ProductDTO, String> colSize;
    @FXML
    private TableColumn<ProductDTO, Double> colPrice;
    @FXML
    private TextField searchField;
    @FXML
    private TextField productIdField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> comboSize;
    @FXML
    private TextField priceField;

    private final ProductModel productModel = new ProductModel();

    private final String PRODUCT_ID_REGEX = "^[0-9]+$";
    private final String PRODUCT_NAME_REGEX = "^(?!\\s*$).{3,}$";
    private final String BASIC_COST_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        loadProductTable();
        comboSize.getItems().addAll("SMALL","MEDIUM","LARGE");

        tblProduct.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, selected) -> {

                    if (selected == null) return;
                    int id = selected.getProductId();
                    handleSearchProductBtRowSelected(String.valueOf(id));
                }
        );
    }

    @FXML
    private void handleSearchProduct() {

        try {
            // here, we get the data in search field where the input by user
            String id = searchField.getText();

            // after that we check it is valid input or not
            if (!id.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Material ID").show();
            } else {
                ProductDTO proDTO = productModel.searchProduct(Integer.parseInt(id));

                // id are set other information to the text fields
                if (proDTO != null) {

                    productIdField.setText(String.valueOf(proDTO.getProductId()));
                    nameField.setText(proDTO.getName());
                    comboSize.setValue(proDTO.getSize());
                    priceField.setText(String.valueOf(proDTO.getBasePrice()));

                    highLightProduct(proDTO.getProductId());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Material not found!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    @FXML
    private void handleSaveProduct() {

        // in here get the user inputs to the fields in product management
        String name = nameField.getText().trim();
        String size = comboSize.getValue().trim();
        String price = priceField.getText().trim();

        // after that here, check the validity about that user inputs.
        // if they are not valid then gives the error msg to the user.

        if (!name.matches(PRODUCT_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Product Name").show();
        } else if (!price.matches(BASIC_COST_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Product Price").show();
        } else {

            // in here, if all details are valid
            // pass the data to the model class for save to the database
            try {
                ProductDTO productDTO = new ProductDTO(name,size,Double.parseDouble(price));
                boolean rs = productModel.saveProduct(productDTO);

                if (rs) {
                    new Alert(Alert.AlertType.INFORMATION, "Product save successfully!").show();
                    clearFields();
                    loadProductTable();
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
    private void handleUpdateProduct() {

        try {

            // in here get the user inputs to the fields in Product management
            String id = productIdField.getText().trim();
            String name = nameField.getText().trim();
            String size = comboSize.getValue().trim();
            String price = priceField.getText();

            // after that here, check the validity about that user inputs.
            // if they are not valid then gives the error msg to the user.

            if (!id.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product ID").show();
            } else if (!name.matches(PRODUCT_NAME_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product Name").show();
            } else if (!price.matches(BASIC_COST_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product Price").show();
            } else {

                // in here, if all details are valid
                // pass the data to the model class for update the database

                ProductDTO productDTO = new ProductDTO(Integer.parseInt(id),name,size,Double.parseDouble(price));
                boolean result = productModel.updateProduct(productDTO);

                // if result is true that mean there had an update.
                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Product Updated Successfully!").show();
                    clearFields();
                    loadProductTable();
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
    private void handleDeleteProduct() {

        try {

            // get the id from the id field and check it valid or not
            String id = productIdField.getText();
            if(!id.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product ID").show();
                return;
            }

            // here show confirm alert before delete
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Are you sure to delete this Product?");
            confirmAlert.setContentText("Product ID: "+id);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                // after the validation pass the id to product model for delete
                boolean result1 = productModel.deleteProduct(Integer.parseInt(id));

                if (result1) {
                    new Alert(Alert.AlertType.INFORMATION, "Product Deleted Successfully").show();
                    clearFields();
                    loadProductTable();
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

    private void loadProductTable() {
        try {

            List<ProductDTO> proDTO =  productModel.getProductTable();

            ObservableList<ProductDTO> obList = FXCollections.observableArrayList();

            // get one by product from the product list and they are add into the obList
            for (ProductDTO productDTO : proDTO) {
                obList.add(productDTO);
            }
            // then add that list to the table
            tblProduct.setItems(obList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void highLightProduct(int id) {
        //row factory allows us to define how each row look
        tblProduct.setRowFactory( tv -> new TableRow<>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(ProductDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows id number matches to the searching material id
                if(item.getProductId() == id) {
                    // here set the colour for the search material row
                    setStyle("-fx-background-color: #DB804E;");
                } else {
                    // if it does not match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblProduct.refresh();
    }

    private void clearFields() {
        productIdField.setText("");
        searchField.setText("");
        nameField.setText("");
        comboSize.setValue("");
        priceField.setText("");
    }

    @FXML
    private void handleSearchProductBtRowSelected(String id) {

        try {
            // after that we check it is valid input or not
            if (!id.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Material ID").show();
            } else {
                ProductDTO proDTO = productModel.searchProduct(Integer.parseInt(id));

                // id are set other information to the text fields
                if (proDTO != null) {

                    productIdField.setText(String.valueOf(proDTO.getProductId()));
                    nameField.setText(proDTO.getName());
                    comboSize.setValue(proDTO.getSize());
                    priceField.setText(String.valueOf(proDTO.getBasePrice()));

                    highLightProduct(proDTO.getProductId());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Material not found!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }
}
