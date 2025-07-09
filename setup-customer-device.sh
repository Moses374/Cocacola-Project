#!/bin/bash
echo "Setting up customer device for Drinks Distribution System demo..."

# Set the admin device IP - change this to your admin device's actual IP
ADMIN_IP=10.55.57.85

# Run the JavaFX application with the admin IP as parameter
echo "Starting application with connection to admin at $ADMIN_IP"
mvn clean javafx:run -Djavafx.args="--dbhost=$ADMIN_IP" 