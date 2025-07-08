package model;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int drinkId;
    private int quantity;
    private double price;

    public OrderItem(int orderItemId, int orderId, int drinkId, int quantity, double price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.drinkId = drinkId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem(int orderId, int drinkId, int quantity, double price) {
        this.orderId = orderId;
        this.drinkId = drinkId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(int drinkId) {
        this.drinkId = drinkId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
} 