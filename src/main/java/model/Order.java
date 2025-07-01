package model;


import java.io.Serializable;

public class Order implements Serializable {
    private String customerName;
    private String productName;
    private int quantity;
    private String branchName;

    public Order(String customerName, String productName, int quantity, String branchName) {
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.branchName = branchName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBranchName() {
        return branchName;
    }
}
