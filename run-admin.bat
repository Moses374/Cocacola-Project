@echo off
echo Starting Drinks Distribution System - Admin Interface

REM Run the main application
java -cp "target/classes;mysql-connector-j-9.3.0.jar" com.example.App

echo Admin application closed. 