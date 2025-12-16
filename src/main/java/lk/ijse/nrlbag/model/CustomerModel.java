package lk.ijse.nrlbag.model;

import javafx.scene.control.Alert;
import lk.ijse.nrlbag.dto.CustomerDTO;
import lk.ijse.nrlbag.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public String saveCustomer(CustomerDTO customerDTO) throws SQLException {

        // in here before saving customer checking the already save or not
        CustomerDTO cus = searchCustomer(customerDTO.getContact());

        // if not save before insert data to the database
        if(cus == null) {
            boolean result = CrudUtil.execute("INSERT INTO Customer (name,address,contact) VALUES (?,?,?)",
                    customerDTO.getName(),
                    customerDTO.getAddress(),
                    customerDTO.getContact());

            return result ? "" : "Failed to save customer!";

        } else {
            return "Customer contact already exist.";
        }
    }

    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException {

        boolean result = CrudUtil.execute("UPDATE Customer SET name=? , address=? , contact=? WHERE customer_id=?",
                customerDTO.getName(),
                customerDTO.getAddress(),
                customerDTO.getContact(),
                customerDTO.getId());

        return result;
    }

    public boolean deleteCustomer(String id) throws SQLException {

        boolean result = CrudUtil.execute("DELETE FROM Customer WHERE customer_id=?", Integer.parseInt(id));
        return result;

    }

    public CustomerDTO searchCustomer(String contact) throws SQLException {
        //get the details according to the contact number
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE contact = ?",contact);

        if(result.next()) {
            return new CustomerDTO(
                    result.getInt("customer_id"),
                    result.getString("name"),
                    result.getString("address"),
                    result.getString("contact"),
                    result.getString("create_date")
            );
        }
        return null;
    }

    public List<CustomerDTO> getCustomer() throws SQLException {

        // here, get the all the customer details to the list using customerDTO
        ResultSet rs = CrudUtil.execute("SELECT * FROM Customer ORDER BY customer_id DESC");

        List<CustomerDTO> customerList = new ArrayList<>();

        while(rs.next()) {
            CustomerDTO cusDTO = new CustomerDTO(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("contact"),
                    rs.getString("create_date")
            );
            customerList.add(cusDTO);
        }
        return customerList;
    }

    public static int totalCustomerCount() throws SQLException {

        // in here get the number of the customers from customer table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_customer FROM Customer");
        int customerCount = 0;

        // get the int value from the execution
        if (result.next()) {
            customerCount = result.getInt("Total_customer");
        }

        return customerCount;

    }



}
