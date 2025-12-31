package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.db.DBConnection;
import lk.ijse.nrlbag.dto.MaterialDTO;
import lk.ijse.nrlbag.util.CrudUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialModel {

    public MaterialDTO searchMaterial(int id) throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Material WHERE material_id=?", id);

        if (rs.next()) {
            int materialId = rs.getInt("material_id");
            int supId = rs.getInt("supplier_id");
            String materialName = rs.getString("name");
            String unit = rs.getString("unit");
            double qty = rs.getDouble("qty_available");

            return new MaterialDTO(materialId,supId,materialName,unit,qty);
        }
        return null;
    }

    // pass values, to insert in the database
    public boolean saveMaterial(MaterialDTO materialDTO) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO Material (name, unit, qty_available, supplier_id) VALUES (?,?,?,?)",
                materialDTO.getMaterial_name(),
                materialDTO.getUnit(),
                materialDTO.getQtyAvailable(),
                materialDTO.getSupplier_id()
        );
        return result;
    }

    public boolean updateMaterial(MaterialDTO materialDTO) throws SQLException {
        boolean result = CrudUtil.execute("UPDATE Material SET name=?, unit=?, qty_available=?, supplier_id=? WHERE material_id=? ",
                materialDTO.getMaterial_name(),
                materialDTO.getUnit(),
                materialDTO.getQtyAvailable(),
                materialDTO.getSupplier_id(),
                materialDTO.getMaterial_id()
        );
        return result;
    }

    public boolean deleteMaterial(int id) throws SQLException {
        boolean result = CrudUtil.execute("DELETE FROM Material WHERE material_id=?",id);

        return result;
    }

    public static int totalLowMaterialCount() throws SQLException {

        // in here get the number of material below 10 from Material table
        ResultSet result = CrudUtil.execute("SELECT COUNT(*) AS Total_Low_material FROM Material WHERE qty_available<50;");
        int materialCount = 0;

        // get the int value from the execution
        if (result.next()) {
            materialCount = result.getInt("Total_Low_material");
        }

        return materialCount;

    }

    public List<MaterialDTO> getMaterial() throws SQLException {

        // here, get the all the material details to the list using MaterialDTO
        ResultSet rs = CrudUtil.execute("SELECT * FROM Material ORDER BY material_id DESC");

        List<MaterialDTO> materialList = new ArrayList<>();

        while(rs.next()) {
            MaterialDTO matDTO = new MaterialDTO(
                    rs.getInt("material_id"),
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("unit"),
                    rs.getDouble("qty_available")

            );
            materialList.add(matDTO);
        }
        return materialList;
    }

    public boolean updateMaterialQtyAvailable(Connection conn, double newQty, int materialID) throws SQLException {
        boolean result = CrudUtil.execute(
                conn,
                "UPDATE Material SET qty_available=? WHERE material_id=? ",
                newQty,
                materialID
        );
        return result;
    }

    public void printMaterialStockReport() throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        InputStream reportObj = getClass().getResourceAsStream("/lk/ijse/nrlbag/reports/materialStockReport.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObj);

        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);

        JasperViewer.viewReport(jp, false);

    }

    public void printLowMaterialStockReport() throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        InputStream reportObj = getClass().getResourceAsStream("/lk/ijse/nrlbag/reports/lowStockMaterial.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObj);

        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);

        JasperViewer.viewReport(jp, false);

    }

    public List<MaterialDTO> searchMaterialByKeyword(String keyword) throws SQLException {

        // here, get the all the material details to the list using MaterialDTO
        String sql = "SELECT * FROM Material WHERE material_id LIKE ? OR name LIKE ? LIMIT 10";

        List<MaterialDTO> materialList = new ArrayList<>();

        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql);

        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            MaterialDTO matDTO = new MaterialDTO(
                    rs.getInt("material_id"),
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("unit"),
                    rs.getDouble("qty_available")

            );
            materialList.add(matDTO);
        }
        return materialList;
    }

}
