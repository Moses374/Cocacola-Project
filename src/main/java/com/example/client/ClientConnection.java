package com.example.client;

import model.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientConnection {
    private String host;
    private int port;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Object sendRequest(String requestType, Object data) throws Exception {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.writeObject(requestType);
            if (data != null) out.writeObject(data);
            return in.readObject();
        }
    }

    public int placeOrder(Order order) throws Exception {
        Object response = sendRequest("PLACE_ORDER", order);
        return (int) response;
    }

    public List<Inventory> getInventory() throws Exception {
        Object response = sendRequest("GET_INVENTORY", null);
        return (List<Inventory>) response;
    }

    public List<Drinks> getDrinks() throws Exception {
        Object response = sendRequest("GET_DRINKS", null);
        return (List<Drinks>) response;
    }

    public List<Branch> getBranches() throws Exception {
        Object response = sendRequest("GET_BRANCHES", null);
        return (List<Branch>) response;
    }

    public List<Customer> getCustomers() throws Exception {
        Object response = sendRequest("GET_CUSTOMERS", null);
        return (List<Customer>) response;
    }

    public List<Order> getAllOrders() throws Exception {
        Object response = sendRequest("GET_ALL_ORDERS", null);
        return (List<Order>) response;
    }

    public Customer getCustomerById(int customerId) throws Exception {
        Object response = sendRequest("GET_CUSTOMER_BY_ID", customerId);
        return (Customer) response;
    }

    public List<OrderItem> getAllOrderItems() throws Exception {
        Object response = sendRequest("GET_ALL_ORDER_ITEMS", null);
        return (List<OrderItem>) response;
    }

    public List<Branch> getAllBranches() throws Exception {
        return getBranches();
    }

    public void addInventory(Inventory inventory) throws Exception {
        sendRequest("ADD_INVENTORY", inventory);
    }

    public void updateInventory(Inventory inventory) throws Exception {
        sendRequest("UPDATE_INVENTORY", inventory);
    }
} 