package lk.ijse.nrlbag.dto;

public class MaterialUsedDTO {

    private int order_id;
    private int material_id;
    private int qty_used;
    private String material_name;
    private String unit;

    public MaterialUsedDTO() {
    }

    public MaterialUsedDTO(int order_id, int material_id, int qty_used) {
        this.order_id = order_id;
        this.material_id = material_id;
        this.qty_used = qty_used;
    }

    public MaterialUsedDTO(int order_id, int material_id, int qty_used, String material_name, String unit) {
        this.order_id = order_id;
        this.material_id = material_id;
        this.qty_used = qty_used;
        this.material_name = material_name;
        this.unit = unit;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    public int getQty_used() {
        return qty_used;
    }

    public void setQty_used(int qty_used) {
        this.qty_used = qty_used;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "MaterialUsedDTO{" +
                "order_id=" + order_id +
                ", material_id=" + material_id +
                ", qty_used=" + qty_used +
                ", material_name='" + material_name + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
