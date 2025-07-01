package model;

import java.io.*;
import java.net.Socket;

public class OrderHandler implements Runnable {
    private Socket socket;

    public OrderHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            Order order = (Order) in.readObject();

            Product product = HeadquartersServer.stock.get(order.getProductName());

            if (product != null) {
                if (product.getStock() >= order.getQuantity()) {
                    product.setStock(product.getStock() - order.getQuantity());

                    String response = "Order from " + order.getBranchName() + " processed for "
                            + order.getQuantity() + " units of " + product.getName();

                    if (product.getStock() < 20) {
                        response += " ⚠️ LOW STOCK ALERT for " + product.getName();
                    }

                    out.writeObject(response);
                } else {
                    out.writeObject("❌ Insufficient stock for " + product.getName());
                }
            } else {
                out.writeObject("❌ Product not found: " + order.getProductName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
