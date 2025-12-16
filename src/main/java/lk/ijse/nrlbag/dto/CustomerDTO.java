package lk.ijse.nrlbag.dto;

public class CustomerDTO {

    private int id;
    private String name;
    private String address;
    private String contact;
    private String date;

    public CustomerDTO() {
    }

    public CustomerDTO(int id, String name, String address, String contact, String date) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.date = date;
    }

    public CustomerDTO(int id, String name, String address, String contact) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public CustomerDTO(String name, String address, String contact) {
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
