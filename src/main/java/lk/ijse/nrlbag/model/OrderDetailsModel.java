package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.dto.OderDetailsDTO;
import lk.ijse.nrlbag.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailsModel {

    public boolean saveOrderDetails(OderDetailsDTO orderDTO, int orderID) throws SQLException {

        // pass the query for save to the database
        return CrudUtil.execute("INSERT INTO Order_Details (orders_id, product_id, quantity, unit_price) VALUES (?,?,?,?)",
                orderID,
                orderDTO.getProduct_id(),
                orderDTO.getQuantity(),
                orderDTO.getUnit_price()
        );

    }

    public OderDetailsDTO searchProduct(int id) throws SQLException {

        // here, get details of the product
        ResultSet rs = CrudUtil.execute("SELECT o.product_id, o.quantity, o.unit_price, p.name FROM Order_Details o " +
                "JOIN Product p on o.product_id = p.product_id WHERE o.product_id=?;",id);

        if(rs.next()) {
            int productId = rs.getInt("product_id");
            String name = rs.getString("name");
            int qty = rs.getInt("quantity");
            double price = rs.getDouble("unit_price");

            return new OderDetailsDTO(productId, qty, price, name);
        }
        return null;
    }

    public boolean updateOrderDetails(OderDetailsDTO orderDTO) throws SQLException {

        // pass the query for update the database
        boolean result = CrudUtil.execute("UPDATE Order_Details SET orders_id=?, product_id=?, quantity=?, unit_price=? WHERE orders_id=? AND product_id=?",
                orderDTO.getOrder_id(),
                orderDTO.getProduct_id(),
                orderDTO.getQuantity(),
                orderDTO.getUnit_price(),
                orderDTO.getOrder_id(),
                orderDTO.getProduct_id()
        );
        return result;

    }

    public boolean deleteOrderDetails(int oderID, int proID) throws SQLException {

        boolean result = CrudUtil.execute("DELETE FROM Order_Details WHERE orders_id=? AND product_id=?", oderID, proID);
        return result;

    }


}
