package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.db.DBConnection;
import lk.ijse.nrlbag.dto.PaymentDTO;
import lk.ijse.nrlbag.util.CrudUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentModel {

    private final OrderModel orderModel = new OrderModel();

    public static int totalPendingPaymentsCount() throws SQLException {

        // in here get the number of orders from customer table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_Pending_Payment FROM Payment WHERE status='Pending';");
        int paymentCount = 0;

        // get the int value from the execution
        if (result.next()) {
            paymentCount = result.getInt("Total_Pending_Payment");
        }

        return paymentCount;

    }

    // get the all details in payment table
    public List<PaymentDTO> getPayments() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Payment");

        List<PaymentDTO> paymentList = new ArrayList<>();

        // get rows one by one and add into payment list
        while (rs.next()) {
            PaymentDTO paymentDTO = new PaymentDTO(
                    rs.getInt("payment_id"),
                    rs.getInt("orders_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_date"),
                    rs.getString("type"),
                    rs.getString("status")
            );
            paymentList.add(paymentDTO);
        }
        return paymentList;

    }

    public PaymentDTO searchPayment(int id) throws SQLException {

        // get the payments details from the database
        ResultSet rs = CrudUtil.execute("SELECT * FROM Payment WHERE payment_id=?",id);

        if (rs.next()) {
            return new PaymentDTO(
                    rs.getInt("payment_id"),
                    rs.getInt("orders_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_date"),
                    rs.getString("type"),
                    rs.getString("status")
            );
        }
        return null;

    }


    public boolean savePaymentWithOrderUpdate(PaymentDTO paymentDTO) throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        try {
            // in here start the transaction and give a msg to stop the auto commit.
            conn.setAutoCommit(false);

            // next save the payment in database in temporary
            boolean paymentSaved = CrudUtil.execute(
                    conn,
                    "INSERT INTO Payment (amount, payment_date, type, status, orders_id) VALUES (?,?,?,?,?)",
                    paymentDTO.getAmount(),
                    paymentDTO.getPayment_date(),
                    paymentDTO.getType(),
                    paymentDTO.getStatus(),
                    paymentDTO.getOrder_id()
            );

            if (!paymentSaved) {
                conn.rollback();
                return false;
            }

            // here get the total order cost from order table through orderModel
            ResultSet orderTotal = orderModel.getOrderCost(conn, paymentDTO.getOrder_id());

            if (!orderTotal.next()) {
                conn.rollback();
                return false;
            }

            double totalCost = orderTotal.getDouble("total_cost");

            // here get the total paid amount
            ResultSet rsPaid = CrudUtil.execute(
                    conn,
                    "SELECT COALESCE(SUM(amount),0) AS paid FROM Payment WHERE orders_id = ?",
                    paymentDTO.getOrder_id()
            );

            rsPaid.next();
            double totalPaid = rsPaid.getDouble("paid");

            // next calculate the remaining payment
            double remaining = totalCost - totalPaid;

            if (remaining<0) {
                conn.rollback();
                throw new SQLException("Over Payment is not allowed");
            }

            // update the order table
            boolean orderUpdate = orderModel.updateOrderRemainingPayment(conn, remaining, paymentDTO.getOrder_id());

            if (!orderUpdate) {
                conn.rollback();
                return false;
            }

            // after adding to the database then print the payment receipt
            printOrderPaymentReceipt(paymentDTO.getOrder_id());

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

    }

    public boolean updatePaymentWithOrderUpdate(PaymentDTO paymentDTO) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();

        try {
            // in here start the transaction
            conn.setAutoCommit(false);

            // next update the payment in database
            boolean paymentUpdate = CrudUtil.execute(
                    conn,
                    "UPDATE Payment SET amount=?, payment_date=?, type=?, status=?, orders_id=? WHERE payment_id=?",
                    paymentDTO.getAmount(),
                    paymentDTO.getPayment_date(),
                    paymentDTO.getType(),
                    paymentDTO.getStatus(),
                    paymentDTO.getOrder_id(),
                    paymentDTO.getId()
            );
            if (!paymentUpdate) {
                conn.rollback();
                return false;
            }

            // here get the total order cost
            ResultSet orderTotal = orderModel.getOrderCost(conn, paymentDTO.getOrder_id());
            if (!orderTotal.next()) {
                conn.rollback();
                return false;
            }

            double totalCost = orderTotal.getDouble("total_cost");

            // here get the total paid amount
            ResultSet rsPaid = CrudUtil.execute(
                    conn,
                    "SELECT COALESCE(SUM(amount),0) AS paid FROM Payment WHERE orders_id = ?",
                    paymentDTO.getOrder_id()
            );

            rsPaid.next();
            double totalPaid = rsPaid.getDouble("paid");

            // next calculate the remaining payment
            double remaining = totalCost - totalPaid;

            if (remaining<0) {
                conn.rollback();
                throw new SQLException("Over Payment is not allowed");
            }

            // update the order table
            boolean orderUpdate = orderModel.updateOrderRemainingPayment(conn, remaining, paymentDTO.getOrder_id());

            if (!orderUpdate) {
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            conn.commit();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

    }

    public boolean deletePaymentWithOrderUpdate(int payID, int orderID) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();

        try {
            // in here start the transaction
            conn.setAutoCommit(false);

            // next delete the payment in database
            boolean paymentUpdate = CrudUtil.execute(
                    conn,
                    "DELETE FROM Payment WHERE payment_id=?",
                    payID
            );
            if (!paymentUpdate) {
                conn.rollback();
                return false;
            }

            // here get the total order cost
            ResultSet orderTotal = orderModel.getOrderCost(conn, orderID);
            if (!orderTotal.next()) {
                conn.rollback();
                return false;
            }

            double totalCost = orderTotal.getDouble("total_cost");

            // when payment is deleted , the remaining is equal to totalCost
            double remaining = totalCost;

            // update the order table
            boolean orderUpdate = orderModel.updateOrderRemainingPayment(conn, remaining, orderID);

            if (!orderUpdate) {
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            conn.commit();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

    }

    public void printOrderPaymentReceipt(int orderID) throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        InputStream reportObj = getClass().getResourceAsStream("/lk/ijse/nrlbag/reports/orderPaymentReciept.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObj);

        Map<String, Object> params = new HashMap<>();
        params.put("ORDER_ID", orderID);

        JasperPrint jp = JasperFillManager.fillReport(jr, params, conn);

        JasperViewer.viewReport(jp, false);

    }

}
