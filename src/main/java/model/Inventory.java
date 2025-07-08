package model;

public class Inventory {
    private int inventoryId;
    private int drinkId;
    private int branchId;
    private int quantity;
    private int threshold;

    public Inventory(int inventoryId, int drinkId, int branchId, int quantity, int threshold) {
        this.inventoryId = inventoryId;
        this.drinkId = drinkId;
        this.branchId = branchId;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    public Inventory(int drinkId, int branchId, int quantity, int threshold) {
        this.drinkId = drinkId;
        this.branchId = branchId;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(int drinkId) {
        this.drinkId = drinkId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
} 