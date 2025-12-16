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
import lk.ijse.nrlbag.dto.OderDetailsDTO;
import lk.ijse.nrlbag.dto.OrderDTO;
import lk.ijse.nrlbag.model.OrderModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private TableView tblOrder;
    @FXML
    private TableColumn colOrderId;
    @FXML
    private TableColumn colCusId;
    @FXML
    private TableColumn colCusName;
    @FXML
    private TableColumn colContact;
    @FXML
    private TableColumn colOrderDate;
    @FXML
    private TableColumn colDeadline;
    @FXML
    private TableColumn colStatus;
    @FXML
    private TableColumn colTotal;
    @FXML
    private TableColumn colRemain;
    @FXML
    private TableColumn colProductID;
    @FXML
    private TableColumn colQty;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private TextField searchField;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField orderIdField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField orderDateField;
    @FXML
    private TextField deadlineField;
    @FXML
    private TextField costField;
    @FXML
    private TextField remainField;
    @FXML
    private Label lblComplete;
    @FXML
    private Label lblPending;
    @FXML
    private Label lblProcessing;
    @FXML
    private Label lblCancel;

    private final String ORDER_ID_REGEX = "^[0-9]+$";

    private final OrderModel orderModel = new OrderModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("CustomerContact"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total_cost"));
        colRemain.setCellValueFactory(new PropertyValueFactory<>("remaining_payment"));
        colProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadOrderTable();


        // in here set the values for the combo box to status
        comboStatus.getItems().addAll("Pending","Processing","Completed","Cancelled");

        try {
            /* here get the total complete order count from the OrderModel class, it assigns into the
            label total complete order that have in order Management. */
            lblComplete.setText(String.valueOf(orderModel.completeOrderCount()));

        /* here get the total pending order count from the OrderModel class, it assigns into the
            label total pending order that have in order Management. */
            lblPending.setText(String.valueOf(orderModel.pendingOrderCount()));

        /* here get the total processing order count from the OrderModel class, it assigns into the
            label total processing order that have in order Management. */
            lblProcessing.setText(String.valueOf(orderModel.processingOrderCount()));

        /* here get the total cancelled order count from the OrderModel class, it assigns into the
            label total cancelled order that have in order Management. */
            lblCancel.setText(String.valueOf(orderModel.cancelledOrderCount()));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void loadOrderTable() {

        try {

            List<OrderDTO> orderDTO = orderModel.getOrders();

            // TableView always requires and ObservableList it automatically update that details
            ObservableList<OrderDTO> obList = FXCollections.observableArrayList();

            // after that we set one by one from oderList in to the observable list
            for (OrderDTO ordDTO : orderDTO) {
                obList.add(ordDTO);
            }

            // then set that list to the table
            tblOrder.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSearchOrderByOrderID() {

        try {
            // get the id from field
            String id = searchField.getText();

            //check validity
            if(!id.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Order ID").show();
            } else {
                // get the details of the order through orderDTO & OderModel
                OrderDTO orderDto = orderModel.searchOrderByOrderID(Integer.parseInt(id));

                // orderDTO is not null then assign their values into the text fields
                if(orderDto!= null) {
                    orderIdField.setText(String.valueOf(orderDto.getId()));
                    idField.setText(String.valueOf(orderDto.getCustomer_id()));
                    nameField.setText(orderDto.getName());
                    contactField.setText(orderDto.getCustomerContact());
                    orderDateField.setText(orderDto.getOrder_date());
                    deadlineField.setText(orderDto.getDeadline());
                    comboStatus.setValue(orderDto.getStatus());
                    costField.setText(String.valueOf(orderDto.getTotal_cost()));
                    remainField.setText(String.valueOf(orderDto.getRemaining_payment()));
                    highLightOrdersWithAllProducts(orderDto.getId());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Order Not Found!").show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleSearchOrderByCustomerID() {

        try {
            // in this method display the all orders of the customer we want
            String id = searchField.getText();

            if(!id.matches(ORDER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Customer ID").show();
            } else {
                OrderDTO orderDto = orderModel.searchOrderByCustomerID(Integer.parseInt(id));

                // get the details is had or not id not null then call the highlight orders method
                if(orderDto!= null) {
                    // here, highlight all the orders belong to search customer
                    highLightOrders(orderDto.getCustomer_id());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer Not Found!").show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!").show();
        }

    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private void clearFields() {
        searchField.setText("");
        idField.setText("");
        nameField.setText("");
        orderIdField.setText("");
        contactField.setText("");
        orderDateField.setText("");
        deadlineField.setText("");
        comboStatus.setValue("");
        costField.setText("");
        remainField.setText("");
    }

    private void highLightOrders(int id) {
        //rowfactory allows us to define how each row look
        tblOrder.setRowFactory( tv -> new TableRow<OrderDTO>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(OrderDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows id number matches to the searching customer's id
                if(item.getCustomer_id() == id) {
                    // here set the colour for the search order row
                    setStyle("-fx-background-color: #e2baf7;");
                } else {
                    // if doesnt match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblOrder.refresh();
    }

    private void highLightOrdersWithAllProducts(int id) {
        //rowfactory allows us to define how each row look
        tblOrder.setRowFactory( tv -> new TableRow<OrderDTO>() {
            @Override
            // this method is called for every row in the table
            protected  void updateItem(OrderDTO item, boolean empty) {
                super.updateItem(item, empty);

                // check row is empty or item is null
                if(empty || item == null) {
                    setStyle("");
                    return;
                }

                // search rows id number matches to the searching order's id
                if(item.getId() == id) {
                    // here set the colour for the search order row
                    setStyle("-fx-background-color: #e2baf7;");
                } else {
                    // if doesnt match that keep default style
                    setStyle("");
                }
            }
        });
        // after set the colour refresh table for show colour on the table
        tblOrder.refresh();
    }

    @FXML
    private void btnAddOnActionOrder(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/ijse/nrlbag/view/orderPopup.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Orders");
            stage.initModality(Modality.APPLICATION_MODAL); // Block main window
            stage.setResizable(false);
            stage.showAndWait();
            loadOrderTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
