package lk.ijse.nrlbag.dto;

public class SupplierDTO {

    private int id;
    private String name;
    private String address;
    private String contact;
    private int materialId;
    private String materialName;

    public SupplierDTO() {
    }

    public SupplierDTO(int id, String name, String address, String contact) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public SupplierDTO(int id, String name, String address, String contact, int materialId, String materialName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.materialId = materialId;
        this.materialName = materialName;
    }

    public SupplierDTO(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Override
    public String toString() {
        return "SupplierDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                '}';
    }
}
