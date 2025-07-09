# Multi-Device Presentation Setup Guide

This guide explains how to set up the Drinks Distribution System for a presentation across 4 devices:
- 1 Administrator device
- 3 Customer devices (each representing a different branch)

## Prerequisites

- All devices must be connected to the same network
- MySQL installed on the administrator device
- Java JDK 21 or higher installed on all devices
- Maven installed on all devices

## Step 1: Administrator Device Setup

1. **Database Setup**:
   ```
   mysql -u root -p < drinks_distributer.sql
   ```

2. **Configure MySQL for Remote Access**:
   - Edit MySQL configuration file (my.ini or my.cnf)
   - Set `bind-address = 0.0.0.0`
   - Restart MySQL service
   - Grant remote access:
     ```sql
     GRANT ALL PRIVILEGES ON drinks_distributor.* TO 'root'@'%' IDENTIFIED BY 'your_password';
     FLUSH PRIVILEGES;
     ```

3. **Find Your IP Address**:
   - Windows: `ipconfig` in Command Prompt
   - Mac/Linux: `ifconfig` or `ip addr` in Terminal
   - Note down the IP address (e.g., 192.168.1.100)

4. **Run the Admin Application**:
   ```
   java -cp "target/classes;mysql-connector-j-9.3.0.jar" com.example.App
   ```
   - Log in with admin credentials (Username: ADMIN1, Password: passadmin)

## Step 2: Customer Device Setup

For each of the three customer devices:

1. **Clone/Copy the Project**:
   - Ensure the project is available on each device

2. **Update Database Connection**:
   - Use the provided setup script:
     - Windows: `setup-customer-device.bat`
     - Mac/Linux: `chmod +x setup-customer-device.sh && ./setup-customer-device.sh`
   - Or manually update the IP in `src/main/java/com/example/dao/BaseDAO.java`

3. **Run the Customer Application**:
   ```
   mvn clean javafx:run -Djavafx.args="--dbhost=ADMIN_IP_ADDRESS"
   ```
   Replace `ADMIN_IP_ADDRESS` with the actual IP address of the admin device

4. **Select Different Branches**:
   - When prompted, select a different branch for each customer device:
     - Device 1: Branch 1 - Headquarters
     - Device 2: Branch 2 - Downtown
     - Device 3: Branch 3 - Westside

## Presentation Flow

1. **Administrator Demo**:
   - Show the admin dashboard
   - Explain the inventory management system
   - Display initial inventory levels across branches
   - Show reporting capabilities

2. **Customer Ordering Process**:
   - Have each "customer" place orders from their respective branches
   - Order different products and quantities

3. **Real-time Updates**:
   - Return to the admin device to show how orders are reflected in real-time
   - Demonstrate inventory updates
   - Show sales reports and branch performance metrics

4. **System Features Demonstration**:
   - Show how low stock alerts work
   - Demonstrate order fulfillment process
   - Display branch performance comparisons

## Troubleshooting

- **Connection Issues**: Ensure all devices are on the same network and can ping the admin device
- **Database Access**: Verify MySQL is configured to accept remote connections
- **Application Errors**: Check logs for specific error messages

## Additional Notes

- For a more realistic demo, prepare some pre-planned ordering scenarios
- Consider having printed materials showing the system architecture
- Prepare screenshots as backup in case of technical difficulties 