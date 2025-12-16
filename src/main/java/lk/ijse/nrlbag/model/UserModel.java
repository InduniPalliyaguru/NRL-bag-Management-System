package lk.ijse.nrlbag.model;

import lk.ijse.nrlbag.dto.UserDTO;
import lk.ijse.nrlbag.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {

    public UserDTO validLogin(String name) throws SQLException {

        ResultSet result = CrudUtil.execute("SELECT * FROM User WHERE userName=?", name);

        if(result.next()) {
            String userNAme = result.getString("userName");
            String password = result.getString("user_password");

            return new UserDTO(userNAme,password);
        }
        return null;
    }

}
