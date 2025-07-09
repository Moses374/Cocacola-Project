# Drinks Distribution System - Presentation Quick Start

## Before Presentation Day
1. Test the setup on all devices
2. Ensure MySQL is properly configured on admin device
3. Verify all devices can connect to the admin device's network

## Presentation Day Setup

### Admin Device (1 device)
1. Connect to the presentation network
2. Start MySQL server
3. Check your IP address: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
4. Run the admin application:
   - Windows: Double-click `run-admin.bat`
   - Mac/Linux: `./run-admin.sh`
5. Log in with admin credentials:
   - Username: ADMIN1
   - Password: passadmin

### Customer Devices (3 devices)
1. Connect all devices to the same network as the admin device
2. Update the IP address in the setup scripts to match the admin device's IP
3. Run the customer application:
   - Windows: Double-click `setup-customer-device.bat`
   - Mac/Linux: `./setup-customer-device.sh`
4. When prompted, select a different branch for each device:
   - Device 1: Branch 1 - Headquarters
   - Device 2: Branch 2 - Downtown
   - Device 3: Branch 3 - Westside

## Presentation Flow Checklist

### Introduction (5 minutes)
- [ ] Introduce the system purpose and architecture
- [ ] Explain the multi-device setup
- [ ] Show the admin dashboard

### Customer Ordering Demo (10 minutes)
- [ ] Customer 1: Place order from Headquarters branch
- [ ] Customer 2: Place order from Downtown branch
- [ ] Customer 3: Place order from Westside branch

### Admin Features Demo (10 minutes)
- [ ] Show real-time inventory updates
- [ ] Generate sales reports
- [ ] Demonstrate branch performance metrics
- [ ] Show low stock alerts

### Q&A (5 minutes)
- [ ] Address questions about the system
- [ ] Explain technical implementation details if asked

## Emergency Backup Plan
If network issues occur:
1. Use screenshots of the multi-device setup
2. Run all demonstrations on the admin device
3. Explain how the distributed system would work in a real environment 