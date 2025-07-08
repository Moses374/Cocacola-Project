-- Create and select the database
CREATE DATABASE IF NOT EXISTS drinks_distributor;
USE drinks_distributor;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 01, 2025 at 04:32 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

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
(8, '', '', 0.00),
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
  `drink_id` int(11) DEFAULT NULL,
  `branch_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `threshold` int(11) NOT NULL DEFAULT 10
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table `inventory`
INSERT INTO `inventory` (`inventory_id`, `drink_id`, `branch_id`, `quantity`, `threshold`) VALUES
(1, NULL, NULL, 0, 10),
(132, NULL, NULL, 0, 10);

-- Table structure for table `orders`
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `branch_id` int(11) DEFAULT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table `orders`
INSERT INTO `orders` (`order_id`, `customer_id`, `branch_id`, `order_date`) VALUES
(1, NULL, NULL, '2025-06-28 21:38:47');

-- Table structure for table `order_items`
CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `drink_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table `order_items`
INSERT INTO `order_items` (`order_item_id`, `order_id`, `drink_id`, `quantity`, `price`) VALUES
(1, NULL, NULL, 0, 0.00),
(2, NULL, NULL, 0, 0.00),
(5, NULL, NULL, 0, 0.00),
(7, NULL, NULL, 0, 0.00),
(10, NULL, NULL, 0, 0.00);

-- Indexes
ALTER TABLE `branches` ADD PRIMARY KEY (`branch_id`), ADD UNIQUE KEY `branch_name` (`branch_name`);
ALTER TABLE `customers` ADD PRIMARY KEY (`customer_id`);
ALTER TABLE `drinks` ADD PRIMARY KEY (`drink_id`);
ALTER TABLE `inventory` ADD PRIMARY KEY (`inventory_id`), ADD KEY `drink_id` (`drink_id`), ADD KEY `branch_id` (`branch_id`);
ALTER TABLE `orders` ADD PRIMARY KEY (`order_id`), ADD KEY `customer_id` (`customer_id`), ADD KEY `branch_id` (`branch_id`);
ALTER TABLE `order_items` ADD PRIMARY KEY (`order_item_id`), ADD KEY `order_id` (`order_id`), ADD KEY `drink_id` (`drink_id`);

-- Auto-increment values
ALTER TABLE `branches` MODIFY `branch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
ALTER TABLE `customers` MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
ALTER TABLE `drinks` MODIFY `drink_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
ALTER TABLE `inventory` MODIFY `inventory_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=172;
ALTER TABLE `orders` MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
ALTER TABLE `order_items` MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

-- Foreign key constraints
ALTER TABLE `inventory`
  ADD CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`drink_id`) REFERENCES `drinks` (`drink_id`),
  ADD CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`);

ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`);

ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`drink_id`) REFERENCES `drinks` (`drink_id`);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
