
# Drinks Distribution System

A comprehensive beverage distribution management system that handles inventory, ordering, and administrative functions across multiple branches.

## System Overview

This application provides an integrated solution for managing a drinks distribution business with multiple branches. It features:

- **Role-based access control** (Admin, Regular User, Guest)
- **Centralized login system**
- **Inventory management** across branches
- **Order processing** and tracking
- **Customer management**
- **Administrative dashboard** for business oversight

## Architecture

The system is built with a hybrid architecture:

1. **Swing-based Main Application** - Provides the central navigation hub and administrative interfaces
2. **JavaFX-based Ordering Interface** - Offers a modern UI for the ordering system
3. **MySQL Database** - Stores all system data including inventory, orders, users, and customers

## Multi-Device Presentation Setup

The system is designed to be demonstrated across multiple devices:

1. **Administrator Device** - Runs the admin interface to monitor and manage the system
2. **Customer Devices (3)** - Each representing a different branch, running the ordering interface

This setup allows for a realistic demonstration where:
- Customers can place orders simultaneously from different branches
- The administrator can view real-time updates and generate reports
- Inventory changes are reflected across the system

### Setting Up the Multi-Device Demo

1. Ensure all devices are connected to the same network
2. Set up the MySQL database on a host machine that's accessible to all devices
3. Update the database connection settings in `BaseDAO.java` on each device:
   ```java
   String url = "jdbc:mysql://HOST_IP:3306/drinks_distributor";
   ```
4. On the admin device:
   ```
   java -cp "target/classes;mysql-connector-j-9.3.0.jar" com.example.App
   ```
   Then log in with admin credentials

5. On each customer device:
   ```
   mvn clean javafx:run
   ```
   Or use the guest login option from the main launcher

## Key Components

### Main Launcher (Entry Point)

`MainLauncher.java` serves as the entry point to the application, providing:
- Login functionality for admin users
- Guest access option
- User authentication and validation

### Main GUI (Navigation Hub)

`MainGUI.java` provides a central navigation hub with access to different parts of the system based on user role:

- **Common Features** (All Users):
  - Ordering Interface
  - Branch Information

- **Admin Features**:
  - Inventory Management
  - Headquarters Dashboard
  - User Management
  - Order History

### Ordering Interface

The ordering interface (`HelloApplication.java` and related components in `com.example.demo1`) is a JavaFX application that allows:
- Browsing available products
- Adding items to cart
- Checking out and placing orders

### Headquarters Dashboard

`HeadquartersDashboard.java` provides administrators with:
- Real-time inventory monitoring
- Low stock alerts
- Sales reports
- Branch performance metrics

### Admin Dashboard

`AdminDashboard.java` extends the Headquarters Dashboard with additional features:
- User management (add/edit/delete users)
- System configuration

### Data Access Objects (DAOs)

The system uses the DAO pattern to abstract database operations:
- `BaseDAO.java` - Provides common database connection functionality
- `UserDAO.java` - Handles user authentication and management
- `BranchDAO.java` - Manages branch information
- `DrinkDAO.java` - Handles product data
- `InventoryDAO.java` - Manages inventory across branches
- `OrderDAO.java` and `OrderItemDAO.java` - Handle order processing

## Database Structure

The system uses a MySQL database with the following key tables:
- `users` - Stores user credentials and roles
- `branches` - Stores branch information
- `drinks` - Contains product information
- `inventory` - Tracks stock levels across branches
- `customers` - Stores customer information
- `orders` - Tracks order headers
- `order_items` - Stores order line items

## Integration Points

1. **Swing to JavaFX Integration**:
   - The MainGUI launches the JavaFX ordering interface using Maven's JavaFX plugin
   - Command: `mvn clean javafx:run`

2. **Database Integration**:
   - All components connect to the same MySQL database
   - Connection details in `BaseDAO.java`

3. **User Session Management**:
   - User credentials and permissions flow from MainLauncher to appropriate interfaces
   - Role-based access controls are enforced throughout the application

## Getting Started

### Prerequisites

- Java JDK 21 or higher
- JavaFX SDK 21 or higher
- Maven 3.8+
- MySQL Server 8.0+

### Setup Instructions

1. **Database Setup**:
   ```
   mysql -u root -p < drinks_distributer.sql
   ```

2. **Compile the Application**:
   ```
   mvn clean compile
   ```

3. **Run the Application**:
   ```
   java -cp "target/classes;mysql-connector-j-9.3.0.jar" com.example.App
   ```

4. **Default Admin Credentials**:
   - Username: ADMIN1
   - Password: passadmin

### Running the Ordering Interface Directly

To run just the ordering interface:
```
mvn clean javafx:run
```

## System Workflow

1. User starts the application via `com.example.App`
2. The MainLauncher displays login options
3. User can:
   - Login as admin to access all features
   - Continue as guest to access limited features
4. The MainGUI provides navigation to different system components
5. Admin users can manage inventory, users, and view reports
6. All users can access the ordering interface to place orders

## Technical Implementation Notes

- The system uses a modular design with clear separation of concerns
- Swing components handle administrative functions
- JavaFX provides a modern UI for customer-facing features
- Database operations are abstracted through the DAO pattern
- Maven handles dependencies and builds

## Future Enhancements

- Integration with payment processing systems
- Mobile application for field sales
- Advanced analytics and reporting
- Automated inventory replenishment
- Customer loyalty program 