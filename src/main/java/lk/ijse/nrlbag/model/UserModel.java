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

    public UserDTO getUserDetails() throws SQLException{
        ResultSet rs = CrudUtil.execute("SELECT * FROM User");

        if (rs.next()) {
            //userName, user_password, email, name, role
            return new UserDTO(
                    rs.getString("userName"),
                    rs.getString("user_password"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getString("email")
            );
        }
        return null;
    }

    public boolean updateUserDetails(UserDTO userDTO) throws SQLException{

        return CrudUtil.execute("UPDATE User SET email=?, name=?, role=? WHERE userName=?",
                userDTO.getEmail(),
                userDTO.getName(),
                userDTO.getRole(),
                userDTO.getUserName());
    }

    public boolean updateLoginPassword(String password) throws SQLException{
        boolean result = CrudUtil.execute("UPDATE User SET user_password=? WHERE userName=?",
                password,
                "induni");

        return result;
    }

}
