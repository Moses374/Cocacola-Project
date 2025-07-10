-- Complete SQL Script for Drinks Distributor Database
-- This script creates the database, tables, and fixes the checkout issues

-- Create and select the database
CREATE DATABASE IF NOT EXISTS drinks_distributor;
USE drinks_distributor;

-- Set SQL mode and character set
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- Drop existing tables if they exist (to start fresh)
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `inventory`;
DROP TABLE IF EXISTS `drinks`;
DROP TABLE IF EXISTS `customers`;
DROP TABLE IF EXISTS `branches`;
DROP TABLE IF EXISTS `users`;

-- Table structure for table `users`
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Add default admin user
INSERT INTO `users` (`username`, `password`, `role`) VALUES
('ADMIN1', 'passadmin', 'admin');

-- Table structure for table `branches`
CREATE TABLE `branches` (
  `branch_id` int(11) NOT NULL,
  `branch_name` varchar(50) NOT NULL,
  `location` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table `branches`
INSERT INTO `branches` (`branch_id`, `branch_name`, `location`) VALUES
(2, 'NAKURU', 'Nakuru'),
(3, 'MOMBASA', 'Mombasa'),
(4, 'KISUMU', 'Kisumu'),
(5, 'NAIROBI', 'Nairobi HQ');

-- Table structure for table `customers`
CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `contact` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table `customers`
INSERT INTO `customers` (`customer_id`, `name`, `contact`) VALUES
(3, 'Ayman Khubran', '0700000001'),
(4, 'Halima Musa', '0700000002'),
(5, 'Brian Otieno', '0700000003'),
(6, 'Grace Wanjiru', '0700000004');

-- Table structure for table `drinks`
CREATE TABLE `drinks` (
  `drink_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `brand` varchar(100) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table `drinks`
INSERT INTO `drinks` (`drink_id`, `name`, `brand`, `unit_price`) VALUES
(9, 'Fanta Orange 500ml', 'Coca-Cola Company', 80.00),
(10, 'Fanta Orange 300ml', 'Coca-Cola Company', 50.00),
(11, 'Fanta Orange 1L', 'Coca-Cola Company', 120.00),
(12, 'Coca-Cola 500ml', 'Coca-Cola Company', 85.00),
(13, 'Coca-Cola 300ml', 'Coca-Cola Company', 55.00),
(14, 'Coca-Cola 1L', 'Coca-Cola Company', 130.00),
(15, 'Minute Maid 500ml', 'Coca-Cola Company', 100.00),
(16, 'Minute Maid 330ml', 'Coca-Cola Company', 70.00),
(17, 'Minute Maid 1L', 'Coca-Cola Company', 150.00),
(18, 'Pepsi 500ml', 'PepsiCo', 80.00),
(19, 'Pepsi 300ml', 'PepsiCo', 50.00),
(20, 'Pepsi 1L', 'PepsiCo', 125.00),
(21, 'Red Bull 250ml', 'Red Bull GmbH', 150.00),
(22, 'Red Bull 350ml', 'Red Bull GmbH', 200.00),
(23, 'Schweppes Tonic 500ml', 'Coca-Cola Company', 80.00),
(24, 'Schweppes Tonic 300ml', 'Coca-Cola Company', 60.00),
(25, 'Coca-Cola Diet 500ml', 'Coca-Cola Company', 85.00),
(26, 'Coca-Cola Diet 300ml', 'Coca-Cola Company', 55.00),
(27, 'Coca-Cola Diet 1L', 'Coca-Cola Company', 130.00),
(28, 'Sprite 500ml', 'Coca-Cola Company', 80.00),
(29, 'Sprite 300ml', 'Coca-Cola Company', 50.00),
(30, 'Sprite 1L', 'Coca-Cola Company', 120.00),
(31, 'Water 500ml', 'Generic', 30.00),
(32, 'Water 1.5L', 'Generic', 70.00),
(33, 'Water 1L', 'Generic', 50.00),
(34, '7UP 500ml', 'PepsiCo', 80.00),
(35, '7UP 300ml', 'PepsiCo', 50.00),
(36, '7UP 1L', 'PepsiCo', 120.00),
(37, 'Fanta Pineapple 500ml', 'Coca-Cola Company', 80.00),
(38, 'Fanta Pineapple 300ml', 'Coca-Cola Company', 50.00),
(39, 'Fanta Pineapple 1L', 'Coca-Cola Company', 120.00);

-- Table structure for table `inventory`
CREATE TABLE `inventory` (
  `inventory_id` int(11) NOT NULL,
  `drink_id` int(11) NOT NULL,
  `branch_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `threshold` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table `inventory`
INSERT INTO `inventory` (`inventory_id`, `drink_id`, `branch_id`, `quantity`, `threshold`) VALUES
-- NAIROBI HQ BRANCH (ID: 5)
(1, 9, 5, 100, 20),  -- Fanta Orange 500ml
(2, 10, 5, 80, 15),  -- Fanta Orange 300ml
(3, 11, 5, 60, 10),  -- Fanta Orange 1L
(4, 12, 5, 100, 20), -- Coca-Cola 500ml
(5, 13, 5, 80, 15),  -- Coca-Cola 300ml
(6, 14, 5, 60, 10),  -- Coca-Cola 1L
(7, 15, 5, 50, 10),  -- Minute Maid 500ml
(8, 16, 5, 40, 8),   -- Minute Maid 330ml
(9, 17, 5, 30, 5),   -- Minute Maid 1L
(10, 18, 5, 100, 20), -- Pepsi 500ml
(11, 19, 5, 80, 15),  -- Pepsi 300ml
(12, 20, 5, 60, 10),  -- Pepsi 1L
(13, 21, 5, 30, 5),   -- Red Bull 250ml
(14, 22, 5, 20, 4),   -- Red Bull 350ml
(15, 23, 5, 40, 8),   -- Schweppes Tonic 500ml
(16, 24, 5, 30, 6),   -- Schweppes Tonic 300ml
(17, 25, 5, 50, 10),  -- Coca-Cola Diet 500ml
(18, 26, 5, 40, 8),   -- Coca-Cola Diet 300ml
(19, 27, 5, 30, 6),   -- Coca-Cola Diet 1L
(20, 28, 5, 100, 20), -- Sprite 500ml
(21, 29, 5, 80, 15),  -- Sprite 300ml
(22, 30, 5, 60, 10),  -- Sprite 1L
(23, 31, 5, 200, 40), -- Water 500ml
(24, 32, 5, 150, 30), -- Water 1.5L
(25, 33, 5, 180, 35), -- Water 1L
(26, 34, 5, 70, 12),  -- 7UP 500ml
(27, 35, 5, 50, 10),  -- 7UP 300ml
(28, 36, 5, 40, 8),   -- 7UP 1L
(29, 37, 5, 80, 15),  -- Fanta Pineapple 500ml
(30, 38, 5, 60, 12),  -- Fanta Pineapple 300ml
(31, 39, 5, 50, 10),  -- Fanta Pineapple 1L

-- NAKURU BRANCH (ID: 2)
(32, 9, 2, 80, 15),   -- Fanta Orange 500ml
(33, 10, 2, 60, 12),  -- Fanta Orange 300ml
(34, 11, 2, 40, 8),   -- Fanta Orange 1L
(35, 12, 2, 70, 14),  -- Coca-Cola 500ml
(36, 13, 2, 55, 10),  -- Coca-Cola 300ml
(37, 14, 2, 35, 7),   -- Coca-Cola 1L
(38, 15, 2, 30, 6),   -- Minute Maid 500ml
(39, 16, 2, 25, 5),   -- Minute Maid 330ml
(40, 17, 2, 20, 4),   -- Minute Maid 1L
(41, 18, 2, 65, 12),  -- Pepsi 500ml
(42, 19, 2, 45, 8),   -- Pepsi 300ml
(43, 20, 2, 30, 6),   -- Pepsi 1L
(44, 21, 2, 15, 3),   -- Red Bull 250ml
(45, 22, 2, 10, 2),   -- Red Bull 350ml
(46, 23, 2, 25, 5),   -- Schweppes Tonic 500ml
(47, 24, 2, 35, 6),   -- Schweppes Tonic 300ml
(48, 25, 2, 30, 5),   -- Coca-Cola Diet 500ml
(49, 26, 2, 25, 5),   -- Coca-Cola Diet 300ml
(50, 27, 2, 20, 4),   -- Coca-Cola Diet 1L
(51, 28, 2, 75, 12),  -- Sprite 500ml
(52, 29, 2, 50, 8),   -- Sprite 300ml
(53, 30, 2, 40, 6),   -- Sprite 1L
(54, 31, 2, 120, 20), -- Water 500ml
(55, 32, 2, 90, 15),  -- Water 1.5L
(56, 33, 2, 100, 18), -- Water 1L
(57, 34, 2, 45, 8),   -- 7UP 500ml
(58, 35, 2, 30, 6),   -- 7UP 300ml
(59, 36, 2, 25, 5),   -- 7UP 1L
(60, 37, 2, 55, 10),  -- Fanta Pineapple 500ml
(61, 38, 2, 40, 8),   -- Fanta Pineapple 300ml
(62, 39, 2, 30, 6),   -- Fanta Pineapple 1L

-- MOMBASA BRANCH (ID: 3)
(63, 9, 3, 95, 18),   -- Fanta Orange 500ml
(64, 10, 3, 70, 12),  -- Fanta Orange 300ml
(65, 11, 3, 50, 8),   -- Fanta Orange 1L
(66, 12, 3, 85, 15),  -- Coca-Cola 500ml
(67, 13, 3, 65, 12),  -- Coca-Cola 300ml
(68, 14, 3, 45, 8),   -- Coca-Cola 1L
(69, 15, 3, 35, 6),   -- Minute Maid 500ml
(70, 16, 3, 50, 10),  -- Minute Maid 330ml
(71, 17, 3, 25, 5),   -- Minute Maid 1L
(72, 18, 3, 75, 12),  -- Pepsi 500ml
(73, 19, 3, 55, 10),  -- Pepsi 300ml
(74, 20, 3, 35, 6),   -- Pepsi 1L
(75, 21, 3, 20, 4),   -- Red Bull 250ml
(76, 22, 3, 15, 3),   -- Red Bull 350ml
(77, 23, 3, 30, 6),   -- Schweppes Tonic 500ml
(78, 24, 3, 45, 8),   -- Schweppes Tonic 300ml
(79, 25, 3, 40, 8),   -- Coca-Cola Diet 500ml
(80, 26, 3, 30, 6),   -- Coca-Cola Diet 300ml
(81, 27, 3, 25, 5),   -- Coca-Cola Diet 1L
(82, 28, 3, 90, 15),  -- Sprite 500ml
(83, 29, 3, 65, 12),  -- Sprite 300ml
(84, 30, 3, 50, 8),   -- Sprite 1L
(85, 31, 3, 150, 25), -- Water 500ml
(86, 32, 3, 110, 20), -- Water 1.5L
(87, 33, 3, 130, 22), -- Water 1L
(88, 34, 3, 55, 10),  -- 7UP 500ml
(89, 35, 3, 40, 8),   -- 7UP 300ml
(90, 36, 3, 30, 6),   -- 7UP 1L
(91, 37, 3, 70, 12),  -- Fanta Pineapple 500ml
(92, 38, 3, 50, 10),  -- Fanta Pineapple 300ml
(93, 39, 3, 40, 8),   -- Fanta Pineapple 1L

-- KISUMU BRANCH (ID: 4)
(94, 9, 4, 60, 12),   -- Fanta Orange 500ml
(95, 10, 4, 45, 8),   -- Fanta Orange 300ml
(96, 11, 4, 30, 6),   -- Fanta Orange 1L
(97, 12, 4, 55, 10),  -- Coca-Cola 500ml
(98, 13, 4, 40, 8),   -- Coca-Cola 300ml
(99, 14, 4, 25, 5),   -- Coca-Cola 1L
(100, 15, 4, 20, 4),  -- Minute Maid 500ml
(101, 16, 4, 30, 6),  -- Minute Maid 330ml
(102, 17, 4, 15, 3),  -- Minute Maid 1L
(103, 18, 4, 50, 10), -- Pepsi 500ml
(104, 19, 4, 35, 6),  -- Pepsi 300ml
(105, 20, 4, 25, 5),  -- Pepsi 1L
(106, 21, 4, 12, 3),  -- Red Bull 250ml
(107, 22, 4, 8, 2),   -- Red Bull 350ml
(108, 23, 4, 20, 4),  -- Schweppes Tonic 500ml
(109, 24, 4, 25, 5),  -- Schweppes Tonic 300ml
(110, 25, 4, 25, 5),  -- Coca-Cola Diet 500ml
(111, 26, 4, 20, 4),  -- Coca-Cola Diet 300ml
(112, 27, 4, 15, 3),  -- Coca-Cola Diet 1L
(113, 28, 4, 60, 12), -- Sprite 500ml
(114, 29, 4, 40, 8),  -- Sprite 300ml
(115, 30, 4, 30, 6),  -- Sprite 1L
(116, 31, 4, 100, 18), -- Water 500ml
(117, 32, 4, 75, 12),  -- Water 1.5L
(118, 33, 4, 85, 15),  -- Water 1L
(119, 34, 4, 35, 6),   -- 7UP 500ml
(120, 35, 4, 25, 5),   -- 7UP 300ml
(121, 36, 4, 20, 4),   -- 7UP 1L
(122, 37, 4, 45, 8),   -- Fanta Pineapple 500ml
(123, 38, 4, 30, 6),   -- Fanta Pineapple 300ml
(124, 39, 4, 25, 5);   -- Fanta Pineapple 1L

-- Table structure for table `orders`
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `branch_id` int(11) NOT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `order_items`
CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `drink_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Add primary keys and indexes
ALTER TABLE `branches` ADD PRIMARY KEY (`branch_id`), ADD UNIQUE KEY `branch_name` (`branch_name`);
ALTER TABLE `customers` ADD PRIMARY KEY (`customer_id`);
ALTER TABLE `drinks` ADD PRIMARY KEY (`drink_id`);
ALTER TABLE `inventory` ADD PRIMARY KEY (`inventory_id`), ADD KEY `drink_id` (`drink_id`), ADD KEY `branch_id` (`branch_id`);
ALTER TABLE `orders` ADD PRIMARY KEY (`order_id`), ADD KEY `customer_id` (`customer_id`), ADD KEY `branch_id` (`branch_id`);
ALTER TABLE `order_items` ADD PRIMARY KEY (`order_item_id`), ADD KEY `order_id` (`order_id`), ADD KEY `drink_id` (`drink_id`);

-- Set auto-increment values
ALTER TABLE `branches` MODIFY `branch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
ALTER TABLE `customers` MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
ALTER TABLE `drinks` MODIFY `drink_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
ALTER TABLE `inventory` MODIFY `inventory_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=125;
ALTER TABLE `orders` MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
ALTER TABLE `order_items` MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

-- Add foreign key constraints but with relaxed settings
ALTER TABLE `inventory`
  ADD CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`drink_id`) REFERENCES `drinks` (`drink_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE CASCADE;

-- Relaxed constraint for orders - no customer_id constraint
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE CASCADE;

-- Relaxed constraint for order_items - no drink_id constraint
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE;

-- Create a stored procedure for future checkouts
DELIMITER //
CREATE PROCEDURE ProcessCheckout(
    IN p_customer_id INT,
    IN p_branch_id INT,
    IN p_drink_id INT,
    IN p_quantity INT,
    IN p_price DECIMAL(10,2)
)
BEGIN
    DECLARE v_order_id INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Create the order
    INSERT INTO orders (customer_id, branch_id, order_date) 
    VALUES (p_customer_id, p_branch_id, NOW());
    
    -- Get the order ID
    SET v_order_id = LAST_INSERT_ID();
    
    -- Add the order item
    INSERT INTO order_items (order_id, drink_id, quantity, price) 
    VALUES (v_order_id, p_drink_id, p_quantity, p_price);
    
    -- Update inventory
    UPDATE inventory 
    SET quantity = quantity - p_quantity 
    WHERE drink_id = p_drink_id AND branch_id = p_branch_id;
    
    COMMIT;
    
    -- Return the order ID
    SELECT v_order_id as order_id;
END//
DELIMITER ;

-- Example usage of the stored procedure:
-- CALL ProcessCheckout(4, 5, 9, 1, 80.00);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */; 