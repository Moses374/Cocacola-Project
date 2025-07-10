@echo off
echo Running Drinks Distribution System...

REM Use Maven to run the JavaFX application
mvn clean javafx:run -Djavafx.mainClass=model.MainLauncher

pause 