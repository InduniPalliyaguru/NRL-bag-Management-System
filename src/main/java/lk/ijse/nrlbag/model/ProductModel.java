package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.dto.ProductDTO;
import lk.ijse.nrlbag.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {

    public List< ProductDTO> getProductTable() throws SQLException {

        ResultSet rs = CrudUtil.execute("SELECT * FROM Product ORDER BY product_id DESC");

        List< ProductDTO> productList = new ArrayList<>();

        while (rs.next()) {
            ProductDTO proDTO = new ProductDTO(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("size"),
                    rs.getDouble("basic_price")
            );
            productList.add(proDTO);
        }

        return productList;
    }

    public ProductDTO searchProduct(int id) throws SQLException {

        ResultSet rs = CrudUtil.execute("SELECT * FROM Product WHERE product_id=? " , id);

        if (rs.next()) {
            return new ProductDTO(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("size"),
                    rs.getDouble("basic_price")
            );
        }
        return null;
    }

    public boolean saveProduct(ProductDTO productDTO) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO Product (name, size, basic_price) VALUES (?,?,?)",
                productDTO.getName(),
                productDTO.getSize(),
                productDTO.getBasePrice()
                );

        return result;
    }

    public boolean updateProduct(ProductDTO productDTO) throws SQLException {
        boolean result = CrudUtil.execute("UPDATE Product SET name=?, size=?, basic_price=? WHERE product_id=?",
                productDTO.getName(),
                productDTO.getSize(),
                productDTO.getBasePrice(),
                productDTO.getProductId()
        );
        return result;
    }

    public boolean deleteProduct(int id) throws SQLException {
        boolean result = CrudUtil.execute("DELETE FROM Product WHERE product_id=?",id);

        return result;
    }



}
