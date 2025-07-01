package model;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class HeadquartersServer {
    public static HashMap<String, Product> stock = new HashMap<>();

    public static void main(String[] args) {
        loadStock();

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Coca-Cola HQ Server started...");

            while (true) {
                Socket client = serverSocket.accept();
                new Thread(new OrderHandler(client)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadStock() {
        stock.put("Coca-Cola Classic", new Product("Coca-Cola Classic", 50.0, 100));
        stock.put("Sprite", new Product("Sprite", 45.0, 80));
        stock.put("Fanta", new Product("Fanta", 45.0, 60));
        stock.put("Dasani Water", new Product("Dasani Water", 40.0, 90));
    }
}
