package lk.ijse.nrlbag.dto;

import java.time.LocalDate;

public class OrderDTO {

    private int id;
    private int customer_id;
    private String name;
    private String customerContact;
    private String order_date;
    private String deadline;
    private String status;
    private double total_cost;
    private double remaining_payment;
    private int productId;
    private int quantity;


    public OrderDTO() {
    }

    public OrderDTO(int id, int customer_id, String name, String customerContact, String order_date, String deadline, String status, double total_cost, double remaining_payment) {
        this.id = id;
        this.customer_id = customer_id;
        this.name = name;
        this.customerContact = customerContact;
        this.order_date = order_date;
        this.deadline = deadline;
        this.status = status;
        this.total_cost = total_cost;
        this.remaining_payment = remaining_payment;
    }

    public OrderDTO(int id, int customer_id, String order_date, String deadline, String status, double total_cost) {
        this.id = id;
        this.customer_id = customer_id;
        this.order_date = order_date;
        this.deadline = deadline;
        this.status = status;
        this.total_cost = total_cost;
    }

    public OrderDTO(int customer_id, String order_date, String deadline, String status, double total_cost) {
        this.customer_id = customer_id;
        this.order_date = order_date;
        this.deadline = deadline;
        this.status = status;
        this.total_cost = total_cost;
    }

    public OrderDTO(int id, int customer_id, String name, String customerContact, String order_date, String deadline, String status, double total_cost, double remaining_payment, int productId, int quantity) {
        this.id = id;
        this.customer_id = customer_id;
        this.name = name;
        this.customerContact = customerContact;
        this.order_date = order_date;
        this.deadline = deadline;
        this.status = status;
        this.total_cost = total_cost;
        this.remaining_payment = remaining_payment;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderDTO(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public double getRemaining_payment() {
        return remaining_payment;
    }

    public void setRemaining_payment(double remaining_payment) {
        this.remaining_payment = remaining_payment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", name='" + name + '\'' +
                ", customerContact='" + customerContact + '\'' +
                ", order_date='" + order_date + '\'' +
                ", deadline='" + deadline + '\'' +
                ", status='" + status + '\'' +
                ", total_cost=" + total_cost +
                ", remaining_payment=" + remaining_payment +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
