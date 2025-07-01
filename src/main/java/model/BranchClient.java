package model;
import java.io.*;
import java.net.Socket;

public class BranchClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Order order = new Order("John Doe", "Coca-Cola Classic", 10, "Mombasa");
            out.writeObject(order);
            out.flush();

            String response = (String) in.readObject();
            System.out.println("Server says: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

