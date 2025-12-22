package lk.ijse.nrlbag.dto;

public class UserDTO {

    private int id;
    private String userName;
    private String userPassword;
    private String name;
    private String role;
    private String email;

    public UserDTO() {
    }

    public UserDTO(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public UserDTO(String userName, String name, String role, String email) {
        this.userName = userName;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public UserDTO(int id, String userName, String userPassword, String name, String role, String email) {
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public UserDTO(String userName, String userPassword, String name, String role, String email) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.name = name;
        this.role = role;
        this.email = email;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
