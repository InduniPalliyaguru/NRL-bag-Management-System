package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.dto.SupplierDTO;
import lk.ijse.nrlbag.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {

    // get the all details in supplier table join with material details also
    public List<SupplierDTO> getSuppliers() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT m.material_id, m.name,m.supplier_id, s.supplier_name, s.address, s.contact FROM " +
                "Material m JOIN Supplier s on m.supplier_id = s.supplier_id");

        List<SupplierDTO> supplierList = new ArrayList<>();

        // get rows one by one and add into supplier list
        while (rs.next()) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("address"),
                    rs.getString("contact"),
                    rs.getInt("material_id"),
                    rs.getString("name")
            );
            supplierList.add(supplierDTO);
        }
        return supplierList;

    }

    public String saveSupplier(SupplierDTO supplierDTO) throws SQLException {

        // here check the there has a supplier contact before
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier WHERE contact=?", supplierDTO.getContact());

        if (!result.next()) {

            // if not have, save to the database
            boolean rs = CrudUtil.execute("INSERT INTO Supplier(supplier_name, address, contact) VALUES (?,?,?)",
                    supplierDTO.getName(),
                    supplierDTO.getAddress(),
                    supplierDTO.getContact());

            return rs ? "" : "Failed to save Supplier";

        } else {
            return "Supplier Contact Already Exist!";
        }

    }

    public SupplierDTO searchSupplier(int id) throws SQLException {

        // get the supplier details from the database
        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier WHERE supplier_id=?",id);

        if (rs.next()) {
            return new SupplierDTO(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("address"),
                    rs.getString("contact")
            );
        }
        return null;

    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {

        boolean result = CrudUtil.execute("UPDATE Supplier SET supplier_name=?, address=?, contact=? WHERE supplier_id=?",
                supplierDTO.getName(),
                supplierDTO.getAddress(),
                supplierDTO.getContact(),
                supplierDTO.getId()
                );
        return result;
    }

    public boolean deleteSupplier(int id) throws SQLException {

        boolean result = CrudUtil.execute("DELETE FROM Supplier WHERE Supplier_id=?",id);
        return result;

    }

}
