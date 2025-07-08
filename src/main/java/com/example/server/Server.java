package com.example.server;

import com.example.dao.*;
import model.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 5000;
    private CustomerDAO customerDAO = new CustomerDAO();
    private BranchDAO branchDAO = new BranchDAO();
    private DrinkDAO drinkDAO = new DrinkDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private OrderItemDAO orderItemDAO = new OrderItemDAO();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            String requestType = (String) in.readObject();
            switch (requestType) {
                case "PLACE_ORDER":
                    Order order = (Order) in.readObject();
                    // Save order and order items
                    int orderId = orderDAO.addOrder(order);
                    out.writeObject(orderId);
                    break;
                case "GET_INVENTORY":
                    out.writeObject(inventoryDAO.getAllInventory());
                    break;
                case "GET_DRINKS":
                    out.writeObject(drinkDAO.getAllDrinks());
                    break;
                case "GET_BRANCHES":
                    out.writeObject(branchDAO.getAllBranches());
                    break;
                case "GET_CUSTOMERS":
                    out.writeObject(customerDAO.getAllCustomers());
                    break;
                case "GET_ALL_ORDERS":
                    out.writeObject(orderDAO.getAllOrders());
                    break;
                case "GET_CUSTOMER_BY_ID":
                    int customerId = (int) in.readObject();
                    out.writeObject(customerDAO.getCustomerById(customerId));
                    break;
                case "GET_ALL_ORDER_ITEMS":
                    out.writeObject(orderItemDAO.getAllOrderItems());
                    break;
                case "ADD_INVENTORY":
                    Inventory addInv = (Inventory) in.readObject();
                    inventoryDAO.addInventory(addInv);
                    out.writeObject("OK");
                    break;
                case "UPDATE_INVENTORY":
                    Inventory updInv = (Inventory) in.readObject();
                    inventoryDAO.updateInventory(updInv);
                    out.writeObject("OK");
                    break;
                // Add more request types as needed
                default:
                    out.writeObject("ERROR: Unknown request type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
} 