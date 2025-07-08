package model;
import java.io.Serializable;

public class NetworkOrder implements Serializable {
    private String customerName;
    private String productName;
    private int quantity;
    private String branchName;

    public NetworkOrder(String customerName, String productName, int quantity, String branchName) {
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.branchName = branchName;
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
} 