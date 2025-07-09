@echo off
echo Setting up customer device for Drinks Distribution System demo...

REM Set the admin device IP - change this to your admin device's actual IP
set ADMIN_IP=10.55.57.85

REM Run the JavaFX application with the admin IP as parameter
echo Starting application with connection to admin at %ADMIN_IP%
mvn clean javafx:run -Djavafx.args="--dbhost=%ADMIN_IP%" 