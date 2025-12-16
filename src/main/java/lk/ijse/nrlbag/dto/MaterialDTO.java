package lk.ijse.nrlbag.dto;

public class MaterialDTO {

    private int material_id;
    private int supplier_id;
    private String  material_name;
    private String unit;
    private double qtyAvailable;

    public MaterialDTO() {
    }

    public MaterialDTO(int material_id, int supplier_id, String material_name, String unit, double qtyAvailable) {
        this.material_id = material_id;
        this.supplier_id = supplier_id;
        this.material_name = material_name;
        this.unit = unit;
        this.qtyAvailable = qtyAvailable;
    }

    public MaterialDTO(int supplier_id, String material_name, String unit, double qtyAvailable) {
        this.supplier_id = supplier_id;
        this.material_name = material_name;
        this.unit = unit;
        this.qtyAvailable = qtyAvailable;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
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

    public double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    @Override
    public String toString() {
        return "MaterialDTO{" +
                "material_id=" + material_id +
                ", supplier_id=" + supplier_id +
                ", material_name=" + material_name +
                ", unit='" + unit + '\'' +
                ", qtyAvailable=" + qtyAvailable +
                '}';
    }
}
