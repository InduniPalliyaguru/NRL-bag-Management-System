package lk.ijse.nrlbag.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lk.ijse.nrlbag.dto.*;
import lk.ijse.nrlbag.dto.tm.MaterialUsedTM;
import lk.ijse.nrlbag.model.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class OrderPopupController implements Initializable {

    @FXML
    private ComboBox<String> comboStatus1;
    @FXML
    private TextField searchField1;
    @FXML
    private TextField productSearchField;
    @FXML
    private TextField idField1;
    @FXML
    private TextField qtyField1;
    @FXML
    private TextField orderIdField1;
    @FXML
    private TextField unitPriceField1;
    @FXML
    private TextField orderDateField1;
    @FXML
    private TextField deadlineField1;
    @FXML
    private TextField costField1;
    @FXML
    private TextField productIdField1;
    @FXML
    private TextField productNameField1;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private TextField idField;
    @FXML
    private TextField orderDateField;
    @FXML
    private TextField deadlineField;
    @FXML
    private TextField costField;
    @FXML
    private TextField qtyField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TreeTableColumn<MaterialUsedTM, Integer> colMaterialID;
    @FXML
    private TreeTableColumn<MaterialUsedTM, String> colName;
    @FXML
    private TreeTableColumn<MaterialUsedTM, Integer> colOrderID;
    @FXML
    private TreeTableColumn<MaterialUsedTM, Integer> colQty;
    @FXML
    private TreeTableColumn<MaterialUsedTM, String> colUnit;
    @FXML
    private TreeTableView<MaterialUsedTM> tblMaterialUsage;
    @FXML
    private TextField searchOrderIdField;
    @FXML
    private TextField orderIdField;
    @FXML
    private TextField materialIdField;
    @FXML
    private TextField orderQtyField;
    @FXML
    private TextField materialNameField;
    @FXML
    private TextField availableQtyField;

    private final String ORDER_ID_REGEX = "^[0-9]+$";
    private final String CUSTOMER_ID_REGEX = "^[0-9]+$";
    private final String ORDER_COST_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";
    private final String PRODUCT_ID_REGEX = "^[0-9]+$";
    private final String QTY_REGEX = "^[0-9]+(\\.[0-9]+)?$";
    private final String MATERIAL_ID_REGEX = "^[0-9]+$";

    private final OrderModel orderModel = new OrderModel();
    private final ProductModel productModel = new ProductModel();
    private final OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
    private final MaterialUsedModel materialUsedModel = new MaterialUsedModel();
    private final MaterialModel materialModel = new MaterialModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // in here set the values for the combo box to status
        comboStatus.getItems().addAll("Pending", "Processing", "Completed", "Cancelled");
        comboStatus1.getItems().addAll("Pending", "Processing", "Completed", "Cancelled");

        //Autoload product details when enter the ID
        productIdField.textProperty().addListener((a, b, c) -> loadProductDetails());

        //Auto calculate the total cost when enter the qty
        qtyField.textProperty().addListener((a, b, c) -> calculateTotalCost());

        //Autoload product details when enter the ID in updates side
        productIdField1.textProperty().addListener((a, b, c) -> loadProductDetailsForUpdates());

        //Auto calculate the total cost when enter the qty in updates side
        qtyField1.textProperty().addListener((a, b, c) -> calculateTotalCostForUpdates());

        //Autoload material details when enter the ID in material usage side
        materialIdField.textProperty().addListener((a, b, c) -> loadMaterialDetails());

        //Autoload qty decreasing when enter the need qty in material usage side
        orderQtyField.textProperty().addListener((a, b, c) -> loadDecreasingQty());

        //in here set up how each column in the tree table view get the data
        colOrderID.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getOrder_id()));
        colMaterialID.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getMaterial_id()));
        colName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getMaterial_name()));
        colQty.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getQty_used()));
        colUnit.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getUnit()));

        tblMaterialUsage.setShowRoot(false);
        loadMaterialUsageTreeTable();

    }

    @FXML
    private void handleSearchOrderByOrderID() {

        try {
            // get the id from field
            String id = searchField1.getText();
            String productId = productSearchField.getText();

            //check validity
            if (!id.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
            } else if (!productId.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product ID").show();
            } else {
                // get the details of the order through orderDTO & OderModel
                OrderDTO orderDto = orderModel.searchOrderByOrderID(Integer.parseInt(id));
                // get the more details about order and product through the orderDetailsModel
                OderDetailsDTO details = orderDetailsModel.searchProduct(Integer.parseInt(productId));

                // orderDTO is not null then assign their values into the text fields
                if (orderDto != null) {
                    orderIdField1.setText(String.valueOf(orderDto.getId()));
                    idField1.setText(String.valueOf(orderDto.getCustomer_id()));
                    orderDateField1.setText(orderDto.getOrder_date());
                    deadlineField1.setText(orderDto.getDeadline());
                    costField1.setText(String.valueOf(orderDto.getTotal_cost()));
                    comboStatus1.setValue(orderDto.getStatus());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Order Not Found!").show();
                }

                if (details != null) {
                    productIdField1.setText(String.valueOf(details.getProduct_id()));
                    qtyField1.setText(String.valueOf(details.getQuantity()));
                    productNameField1.setText(details.getName());
                    unitPriceField1.setText(String.valueOf(details.getUnit_price()));
                } else {
                    new Alert(Alert.AlertType.ERROR, "Product Not Found!").show();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleSaveOrder() {

        try {

            // get the user input details from the fields
            String id = idField.getText().trim();
            String orderDate = orderDateField.getText().trim();
            String deadline = deadlineField.getText().trim();
            String status = comboStatus.getValue().trim();
            String cost = costField.getText().trim();

            String qty = qtyField.getText().trim();
            String productId = productIdField.getText().trim();
            String unitPrice = unitPriceField.getText().trim();

            // check they are valid or not
            if (!id.matches(CUSTOMER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer ID").show();
            } else if (isValidDate(orderDate)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order Date").show();
            } else if (isValidDate(deadline)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Deadline Date").show();
            } else if (!cost.matches(ORDER_COST_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Cost Input").show();
            } else if (!productId.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product ID").show();
            } else if (!qty.matches(QTY_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Quantity Input").show();
            } else {

                // if valid then create a orderDTO object including that details
                OrderDTO orderDto = new OrderDTO(Integer.parseInt(id), orderDate, deadline, status, Double.parseDouble(cost));

                // after that pass that to the orderModel class for connect with database and get order id
                int orderID = orderModel.saveOrderAndOrderID(orderDto);
                // and here order details DTO object create and insert data
                OderDetailsDTO orderDetailsDTO = new OderDetailsDTO(orderID, Integer.parseInt(productId), Integer.parseInt(qty), Double.parseDouble(unitPrice));
                boolean result = orderDetailsModel.saveOrderDetails(orderDetailsDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Order Added Successfully!").show();
                    clearFieldSaved();
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
    private void handleUpdateOrder() {

        try {

            // get the user input details from the fields
            String orderId = orderIdField1.getText().trim();
            String id = idField1.getText().trim();
            String orderDate = orderDateField1.getText().trim();
            String deadline = deadlineField1.getText().trim();
            String status = comboStatus1.getValue().trim();
            String cost = costField1.getText().trim();

            String productID = productIdField1.getText().trim();
            String qty = qtyField1.getText().trim();
            String price = unitPriceField1.getText().trim();


            // check they are valid or not
            if (!orderId.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
            } else if (!id.matches(CUSTOMER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid customer ID").show();
            } else if (isValidDate(orderDate)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order Date").show();
            } else if (isValidDate(deadline)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Deadline Date").show();
            } else if (!productID.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product ID").show();
            } else if (!qty.matches(QTY_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product Quantity").show();
            } else {

                // if valid then create a orderDTO object including that details
                OrderDTO orderDto = new OrderDTO(Integer.parseInt(orderId), Integer.parseInt(id), orderDate, deadline, status, Double.parseDouble(cost));

                OderDetailsDTO details = new OderDetailsDTO(Integer.parseInt(orderId), Integer.parseInt(productID), Integer.parseInt(qty), Double.parseDouble(price));
                // after that pass that to the orderModel class for connect with database
                boolean result = orderModel.updateOrder(orderDto);
                boolean result1 = orderDetailsModel.updateOrderDetails(details);
                if (result && result1) {
                    new Alert(Alert.AlertType.INFORMATION, "Order Details Updated Successfully!").show();
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
    private void handleDeleteOrder() {

        try {
            // get the id from field
            String id = searchField1.getText();
            String productId = productSearchField.getText();

            //check validity
            if (!id.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
            } else if (!productId.matches(PRODUCT_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Product ID").show();
                return;
            }

            // here show confirm alert before delete
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Are you sure to delete this Order?");
            confirmAlert.setContentText("Order ID: " + id);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                boolean result1 = orderDetailsModel.deleteOrderDetails(Integer.parseInt(id), Integer.parseInt(productId));
                boolean result2 = orderModel.deleteOrder(Integer.parseInt(id));

                // result is true that mean it is deleted
                if (result2 && result1) {
                    new Alert(Alert.AlertType.INFORMATION, "Order Deleted Successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Order Not Found!").show();
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

    @FXML
    private void handleSaveMaterialUsage() {
        try {
            String orderId = orderIdField.getText().trim();
            String materialId = materialIdField.getText().trim();
            String orderQty = orderQtyField.getText().trim();
            String availableQty = availableQtyField.getText().trim();

            if (!orderId.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
            } else if (!materialId.matches(MATERIAL_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Material ID").show();
            } else if (!orderQty.matches(QTY_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order Quantity").show();
            } else if (!availableQty.matches(QTY_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order Quantity").show();
            } else {

                // in here set the details into the materialUsedDTO
                MaterialUsedDTO materialUsedDTO = new MaterialUsedDTO(Integer.parseInt(orderId), Integer.parseInt(materialId), Integer.parseInt(orderQty));

                // pass to the model class to insert into the database
                boolean isSaved = materialUsedModel.saveMaterialUsage(materialUsedDTO, Double.parseDouble(availableQty));

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Material Usage Added Successfully!").show();
                    loadMaterialUsageTreeTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }
    }

    private boolean isValidDate(String input) {
        try {
            LocalDate.parse(input);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private void clearFields() {
        searchField1.setText("");
        productSearchField.setText("");
        idField1.setText("");
        orderIdField1.setText("");
        orderDateField1.setText("");
        deadlineField1.setText("");
        comboStatus1.setValue("");
        costField1.setText("");
        productNameField1.setText("");
        productIdField1.setText("");
        qtyField1.setText("");
        unitPriceField1.setText("");
    }

    private void clearFieldSaved() {

        idField.setText("");
        orderDateField.setText("");
        deadlineField.setText("");
        comboStatus.setValue("");
        costField.setText("");
        productIdField.setText("");
        productNameField.setText("");
        unitPriceField.setText("");
        qtyField.setText("");
    }

    private void loadProductDetails() {
        try {

            //in here get the product id and check validity
            String productId = productIdField.getText().trim();

            if (!productId.matches(PRODUCT_ID_REGEX)) {
                productNameField.setText("");
                unitPriceField.setText("");
                return;
            }

            ProductDTO proDTO = productModel.searchProduct(Integer.parseInt(productId));

            // after that get name and price according to the product id
            // set to the name and price fields
            if (proDTO != null) {
                productNameField.setText(proDTO.getName());
                unitPriceField.setText(String.valueOf(proDTO.getBasePrice()));
            } else {
                productNameField.setText("Not Found");
                unitPriceField.setText("");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void calculateTotalCost() {

        try {

            // in here get the qty from the qty field and get the unit Price from the unit price field
            String qty = qtyField.getText().trim();
            String unitPrice = unitPriceField.getText().trim();

            // then check the validity
            if (!qty.matches(QTY_REGEX) || unitPrice.isEmpty()) {
                costField.setText("");
                return;
            }

            // after that convert them in to int and double value and get the total value
            int qtyValue = Integer.parseInt(qty);
            double unitPriceValue = Double.parseDouble(unitPrice);

            double total = qtyValue * unitPriceValue;

            // then set to the totalCost field
            costField.setText(String.valueOf(total));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void loadProductDetailsForUpdates() {
        try {

            //in here get the product id and check validity
            String productId = productIdField1.getText().trim();

            if (!productId.matches(PRODUCT_ID_REGEX)) {
                productNameField1.setText("");
                unitPriceField1.setText("");
                return;
            }

            ProductDTO proDTO = productModel.searchProduct(Integer.parseInt(productId));

            // after that get name and price according to the product id
            // set to the name and price fields
            if (proDTO != null) {
                productNameField1.setText(proDTO.getName());
                unitPriceField1.setText(String.valueOf(proDTO.getBasePrice()));
            } else {
                productNameField1.setText("Not Found");
                unitPriceField1.setText("");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void calculateTotalCostForUpdates() {

        try {

            // in here get the qty from the qty field and get the unit Price from the unit price field
            String qty = qtyField1.getText().trim();
            String unitPrice = unitPriceField1.getText().trim();

            // then check the validity
            if (!qty.matches(QTY_REGEX) || unitPrice.isEmpty()) {
                costField1.setText("");
                return;
            }

            // after that convert them in to int and double value and get the total value
            int qtyValue = Integer.parseInt(qty);
            double unitPriceValue = Double.parseDouble(unitPrice);

            double total = qtyValue * unitPriceValue;

            // then set to the totalCost field
            costField1.setText(String.valueOf(total));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    // in here load data in to the material usage table
    private void loadMaterialUsageTreeTable() {

        try {

            // here get data from the material usage table
            List<MaterialUsedDTO> materialDTO = materialUsedModel.getMaterialUsage();

            // next crete root node of the tree table
            TreeItem<MaterialUsedTM> root = new TreeItem<>();
            root.setExpanded(true);

            // group by order id, map to the group child
            Map<Integer, TreeItem<MaterialUsedTM>> orderMap = new LinkedHashMap<>();

            // loop through all material usage records
            for (MaterialUsedDTO usedDTO : materialDTO) {
                int orderId = usedDTO.getOrder_id();

                // chack if this order id already has a parent node
                TreeItem<MaterialUsedTM> orderNode = orderMap.get(orderId);

                if (orderNode == null) {
                    // create parent node for this order id
                    MaterialUsedTM parentTM = new MaterialUsedTM(usedDTO.getOrder_id(), null, null, "", "");

                    orderNode = new TreeItem<>(parentTM);

                    // save ot to the map for the later
                    orderMap.put(orderId, orderNode);
                    // add the parent node to the root
                    root.getChildren().add(orderNode);
                }

                // create a child node for each material under this order
                MaterialUsedTM childTm = new MaterialUsedTM(
                        usedDTO.getOrder_id(),
                        usedDTO.getMaterial_id(),
                        usedDTO.getQty_used(),
                        usedDTO.getMaterial_name(),
                        usedDTO.getUnit()
                );
                TreeItem<MaterialUsedTM> materialNode = new TreeItem<>(childTm);

                orderNode.getChildren().add(materialNode);
            }

            tblMaterialUsage.setRoot(root);
            tblMaterialUsage.setShowRoot(false);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void loadMaterialDetails() {
        try {

            //in here get the material id and check validity
            String materialId = materialIdField.getText().trim();

            if (!materialId.matches(MATERIAL_ID_REGEX)) {
                materialNameField.setText("");
                availableQtyField.setText("");
                return;
            }

            MaterialDTO materialDTO = materialModel.searchMaterial(Integer.parseInt(materialId));

            // after that get name and available qty according to the material id
            // set to the name and qty fields
            if (materialDTO != null) {
                materialNameField.setText(materialDTO.getMaterial_name());
                availableQtyField.setText(String.valueOf(materialDTO.getQtyAvailable()));
            } else {
                materialNameField.setText("Not Found");
                availableQtyField.setText("");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadDecreasingQty() {
        try {

            //in here get the material id and qty input by user and check validity
            String materialId = materialIdField.getText().trim();
            String useQty = orderQtyField.getText().trim();

            if (!materialId.matches(MATERIAL_ID_REGEX)) {
                materialNameField.setText("");
                availableQtyField.setText("");
                return;
            }
            if (!useQty.matches(QTY_REGEX)) {
                availableQtyField.setText("Invalid qty entered");
            } else {
                MaterialDTO materialDTO = materialModel.searchMaterial(Integer.parseInt(materialId));

                // after that get available qty according to the material id
                if (materialDTO != null) {
                    if (Double.parseDouble(useQty) > materialDTO.getQtyAvailable()) {
                        availableQtyField.setText("Material Qty insufficient");
                    } else {
                        double newQTY = materialDTO.getQtyAvailable() - Double.parseDouble(useQty);
                        availableQtyField.setText(String.valueOf(newQTY));
                    }
                } else {
                    availableQtyField.setText("");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
