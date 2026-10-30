@echo off
cd /d "C:\Users\ANEX\OneDrive\Documents\ProjectSample\ClassroomMaintenance"
title Classroom Maintenance Request System
echo Starting Classroom Maintenance Request System...
java -cp "target/classes;target/lib/*" com.iesce.maintenance.Main
if errorlevel 1 (
    echo.
    echo ERROR: Failed to launch the application.
    echo Make sure Java is installed and MySQL is running.
    pause
)
