package lk.ijse.nrlbag.util;

import lk.ijse.nrlbag.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {

    public static <T> T execute (String sql, Object...obj) throws SQLException {

        // in here get the connection from dbc and select or update the database
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ptsm = conn.prepareStatement(sql);

        for (int i = 0; i < obj.length; i++) {
            ptsm.setObject(i+1, obj[i]);
        }

        // if there happen selection or get getting details from the database
        // here return a result set.
        if (sql.startsWith("select") || sql.startsWith("SELECT")) {
            ResultSet result = ptsm.executeQuery();
            return (T) result;
        } else {

            // if happen an update of the database
            // here gives the int value, if update happen value>0 and value = 0 not happen
            int result = ptsm.executeUpdate();
            boolean rs = result > 0;
            return (T) (Boolean) rs;
        }
    }

    public static int executeAndReturnGeneratedKey(String sql, Object... obj) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ptsm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        for (int i = 0; i<obj.length; i++) {
            ptsm.setObject(i+1, obj[i]);
        }

        ptsm.executeUpdate();

        //after the update in here get the generated key from the database. (pk)
        ResultSet rs = ptsm.getGeneratedKeys();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;

    }

    public static <T> T execute(Connection conn, String sql, Object... obj) throws SQLException {

        PreparedStatement ptsm = conn.prepareStatement(sql);

        for (int i = 0; i<obj.length; i++) {
            ptsm.setObject(i + 1, obj[i]);
        }

        if (sql.startsWith("select") || sql.startsWith("SELECT")) {
            return (T) ptsm.executeQuery();
        } else {
            return (T) (Boolean) (ptsm.executeUpdate()>0);
        }

    }
}
