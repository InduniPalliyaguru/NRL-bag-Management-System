package lk.ijse.nrlbag.dto;

public class UserDTO {

    private int id;
    private String userName;
    private String userPassword;
    private String name;
    private String role;

    public UserDTO() {
    }

    public UserDTO(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public UserDTO(String userName, String userPassword, String name, String role) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.name = name;
        this.role = role;
    }

    public UserDTO(String userName, String name, String role) {
        this.userName = userName;
        this.name = name;
        this.role = role;
    }



    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
