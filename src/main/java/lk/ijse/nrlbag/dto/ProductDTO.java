package lk.ijse.nrlbag.dto;

public class ProductDTO {

    private int productId;
    private String name;
    private String size;
    private double basePrice;

    public ProductDTO() {
    }

    public ProductDTO(int productId, String name, String size, double basePrice) {
        this.productId = productId;
        this.name = name;
        this.size = size;
        this.basePrice = basePrice;
    }

    public ProductDTO(String name, String size, double basePrice) {
        this.name = name;
        this.size = size;
        this.basePrice = basePrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }
}
