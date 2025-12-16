package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.dto.MaterialUsedDTO;
import lk.ijse.nrlbag.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialUsedModel {

    public List<MaterialUsedDTO> getMaterialUsage() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT mu.orders_id, mu.material_id, mu.used_qty, m.name, m.unit " +
                        "FROM Material m JOIN Material_Used mu ON m.material_id = mu.material_id WHERE m.material_id=?;"
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

}
