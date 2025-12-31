package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.db.DBConnection;
import lk.ijse.nrlbag.dto.OrderDTO;
import lk.ijse.nrlbag.util.CrudUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderModel {

    private final OrderDetailsModel orderDetailsModel = new OrderDetailsModel();

    // get the all details in orders table join with customer details also
    public List< OrderDTO> getOrders() throws SQLException {

        ResultSet rs = CrudUtil.execute("SELECT " +
                " o.orders_id," +
                " o.customer_id," +
                " c.name," +
                " c.contact," +
                " o.order_date," +
                " o.deadline," +
                " o.status," +
                " o.total_cost," +
                " o.remaining_payment," +
                " od.product_id," +
                " od.quantity" +
                " FROM Orders o" +
                " JOIN Customer c ON o.customer_id = c.customer_id" +
                " LEFT JOIN Order_Details od ON o.orders_id = od.orders_id;");

        List< OrderDTO> orderList = new ArrayList<>();

        // get rows one by one and add into order list
        while (rs.next()) {
            OrderDTO orderDTO = new OrderDTO(
                    rs.getInt("orders_id"),
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("order_date"),
                    rs.getString("deadline"),
                    rs.getString("status"),
                    rs.getDouble("total_cost"),
                    rs.getDouble(("remaining_payment")),
                    rs.getInt("product_id"),
                    rs.getInt("quantity")
            );
            orderList.add(orderDTO);
        }
        return orderList;

    }

    public static int totalOrderCount() throws SQLException {

        // in here get the number of orders from customer table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_orders FROM Orders");
        int orderCount = 0;

        // get the int value from the execution
        if (result.next()) {
            orderCount = result.getInt("Total_orders");
        }

        return orderCount;

    }

    public boolean saveOrderAndOrderID(OrderDTO orderDto) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        try {

            conn.setAutoCommit(false);

            // pass the query for save to the database
            int lastOrderId = CrudUtil.executeAndReturnGeneratedKey("INSERT INTO Orders (customer_id, order_date, deadline, status, total_cost, remaining_payment) VALUES (?,?,?,?,?,?)",
                    orderDto.getCustomer_id(),
                    orderDto.getOrder_date(),
                    orderDto.getDeadline(),
                    orderDto.getStatus(),
                    orderDto.getTotal_cost(),
                    orderDto.getTotal_cost()
            );

            if (lastOrderId != 0) {
                boolean result = orderDetailsModel.saveOrderDetails(orderDto.getOrderDetails(), lastOrderId);

                if (result) {
                    // print invoice
                    printOrderConfirmation(lastOrderId);
                } else {
                    throw new Exception("Something went wrong when print document");
                }
            } else {
                throw new Exception("Something went wrong when get order item id");
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }


    }

    public OrderDTO searchOrderByOrderID(int id) throws SQLException {

        // here, get details of the order and customer who place that order using a join query
        ResultSet rs = CrudUtil.execute("SELECT c.name, c.contact,o.customer_id, o.orders_id, o.order_date, " +
                "o.deadline, o.status, o.total_cost, o.remaining_payment FROM Orders o JOIN Customer c ON " +
                "o.customer_id = c.customer_id WHERE orders_id=?;",id);

        if(rs.next()) {
            int orderId = rs.getInt("orders_id");
            int cus_id = rs.getInt("customer_id");
            String order_date = rs.getString("order_date");
            String deadline = rs.getString("deadline");
            String status = rs.getString("status");
            double cost = rs.getDouble("total_cost");
            double remain = rs.getDouble("remaining_payment");
            String cusName = rs.getString("name");
            String contact = rs.getString("contact");

            return new OrderDTO(orderId,cus_id,cusName,contact,order_date,deadline,status,cost,remain);
        }
        return null;
    }

    public OrderDTO searchOrderByCustomerID(int id) throws SQLException {

        // here, get details of the all orders and customer who place that orders using a join query
        ResultSet rs = CrudUtil.execute("SELECT c.name, c.contact,o.customer_id, o.orders_id, o.order_date, " +
                "o.deadline, o.status, o.total_cost, o.remaining_payment FROM Orders o JOIN Customer c ON " +
                "o.customer_id = c.customer_id WHERE o.customer_id=?;",id);

        if(rs.next()) {
            int orderId = rs.getInt("orders_id");
            int cus_id = rs.getInt("customer_id");
            String order_date = rs.getString("order_date");
            String deadline = rs.getString("deadline");
            String status = rs.getString("status");
            double cost = rs.getDouble("total_cost");
            double remain = rs.getDouble("remaining_payment");
            String cusName = rs.getString("name");
            String contact = rs.getString("contact");

            return new OrderDTO(orderId,cus_id,cusName,contact,order_date,deadline,status,cost,remain);
        }
        return null;
    }

    public boolean updateOrder(OrderDTO orderDto) throws SQLException {

        // pass the query for update the database
        boolean result = CrudUtil.execute("UPDATE Orders SET customer_id=?, order_date=?, deadline=?, status=?, total_cost=?, remaining_payment=? WHERE orders_id=?;",
                orderDto.getCustomer_id(),
                orderDto.getOrder_date(),
                orderDto.getDeadline(),
                orderDto.getStatus(),
                orderDto.getTotal_cost(),
                orderDto.getTotal_cost(),
                orderDto.getId()
        );
        return result;

    }

    public boolean deleteOrder(int id) throws SQLException {

        boolean result = CrudUtil.execute("DELETE FROM Orders WHERE orders_id=?",id);
        return result;

    }

    public int completeOrderCount() throws SQLException{

        // in here get the number of orders from customer table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_Complete_Orders FROM Orders WHERE status='Completed';");
        int orderCount = 0;

        // get the int value from the execution
        if (result.next()) {
            orderCount = result.getInt("Total_Complete_Orders");
        }

        return orderCount;

    }

    public int pendingOrderCount() throws SQLException{

        // in here get the number of orders from customer table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_Pending_Orders FROM Orders WHERE status='Pending';");
        int orderCount = 0;

        // get the int value from the execution
        if (result.next()) {
            orderCount = result.getInt("Total_Pending_Orders");
        }

        return orderCount;

    }

    public int processingOrderCount() throws SQLException{

        // in here get the number of orders from customer table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_Processing_Orders FROM Orders WHERE status='Processing';");
        int orderCount = 0;

        // get the int value from the execution
        if (result.next()) {
            orderCount = result.getInt("Total_Processing_Orders");
        }

        return orderCount;

    }

    public int cancelledOrderCount() throws SQLException{

        // in here get the number of orders from customer table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_Cancel_Orders FROM Orders WHERE status='Cancelled';");
        int orderCount = 0;

        // get the int value from the execution
        if (result.next()) {
            orderCount = result.getInt("Total_Cancel_Orders");
        }

        return orderCount;

    }

    public static int getOrderByMonths(int month) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM Orders WHERE MONTH(order_date)=?",month);

        if(rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public static ResultSet getMonthlyIncome() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("SELECT MONTH(payment_date) AS month, SUM(amount) AS income " +
                "FROM Payment WHERE status IN ('Partial','Completed') " +
                "GROUP BY MONTH(payment_date)");

        return resultSet;
    }

    public ResultSet getOrderCost(Connection conn, int id) throws SQLException {

        // here get the total order cost
        ResultSet orderTotal = CrudUtil.execute(
                conn,
                "SELECT total_cost FROM Orders WHERE orders_id = ?",
                id
        );

        return orderTotal;

    }

    public boolean updateOrderRemainingPayment(Connection conn, double remaining, int id) throws SQLException {

        // here get the total order cost
        boolean orderUpdate = CrudUtil.execute(
                conn,
                "UPDATE Orders SET remaining_payment = ? WHERE orders_id = ?",
                remaining,
                id
        );

        return orderUpdate;

    }

    public void printOrderConfirmation(int orderID) throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        InputStream reportObj = getClass().getResourceAsStream("/lk/ijse/nrlbag/reports/orderConfirmation.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObj);

        Map<String, Object> params = new HashMap<>();
        params.put("ORDER_ID", orderID);

        JasperPrint jp = JasperFillManager.fillReport(jr, params, conn);

        JasperViewer.viewReport(jp, false);

    }

    public List<Integer> getAllOrdersID() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT orders_id FROM Orders");
        List<Integer> orderIdList = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("orders_id");
            orderIdList.add(id);
        }
        return orderIdList;
    }

}
