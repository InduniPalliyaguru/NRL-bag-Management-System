package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.db.DBConnection;
import lk.ijse.nrlbag.dto.MaterialUsedDTO;
import lk.ijse.nrlbag.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialUsedModel {

    private final MaterialModel materialModel = new MaterialModel();

    public List<MaterialUsedDTO> getMaterialUsage() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT mu.orders_id, mu.material_id, mu.used_qty, m.name, m.unit " +
                        "FROM Material m JOIN Material_Used mu ON m.material_id = mu.material_id;"
        );

        List<MaterialUsedDTO> materialUsedList = new ArrayList<>();

        // get rows one by one and add into order list
        while (rs.next()) {
            MaterialUsedDTO usedList = new MaterialUsedDTO(
                    rs.getInt("orders_id"),
                    rs.getInt("material_id"),
                    rs.getInt("used_qty"),
                    rs.getString("name"),
                    rs.getString("unit")
            );
            materialUsedList.add(usedList);
        }
        return materialUsedList;

    }

    public boolean saveMaterialUsage(MaterialUsedDTO materialUsedDTO, double newAvailableQty) throws SQLException{

        Connection conn = DBConnection.getInstance().getConnection();

        try {
            // in here start the transaction and give a msg to stop the auto commit.
            conn.setAutoCommit(false);
            boolean isSaved = CrudUtil.execute(
                    conn,
                    "INSERT INTO Material_Used (orders_id, material_id, used_qty) VALUES (?,?,?)",
                    materialUsedDTO.getOrder_id(),
                    materialUsedDTO.getMaterial_id(),
                    materialUsedDTO.getQty_used()
            );
            if (!isSaved) {
                conn.rollback();
                return false;
            }
            // in here send qty available for database update
            boolean isUpdated = materialModel.updateMaterialQtyAvailable(newAvailableQty, materialUsedDTO.getMaterial_id());

            if (!isUpdated) {
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

    }

    public MaterialUsedDTO searchMaterialUsage(int materialID) throws SQLException {

        ResultSet rs = CrudUtil.execute("SELECT * FROM Material_Used WHERE material_id=?", materialID);

        if (rs.next()) {
            return new MaterialUsedDTO(
                    rs.getInt("orders_id"),
                    rs.getInt("material_id"),
                    rs.getDouble("used_qty")
            );
        }
        return null;
    }

    public boolean searchMaterialUsageByOrderID(int orderID) throws SQLException {

        ResultSet rs = CrudUtil.execute("SELECT * FROM Material_Used WHERE orders_id=?", orderID);

        return rs.next();
    }

}
