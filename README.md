# Direct-Farm-Connect

## Overview
Direct-Farm-Connect is a Java-based desktop application developed to enable direct trading between farmers and consumers without the involvement of middlemen. The system allows farmers to add and manage crops, while consumers can browse and purchase products transparently. The application also supports billing, inventory updates, and role-based login functionality.

## Features
- Farmer and Consumer role-based login system
- User registration and authentication using phone number
- Farmer crop management
- Real-time crop quantity updates
- Consumer crop purchasing system
- Payment mode selection (Cash / GPay)
- Farmer earnings bill generation
- Consumer purchase bill generation
- Inventory management system
- Database connectivity using JDBC and SQL Server
- Full-screen Java Swing graphical user interface

## Technologies Used
- Java
- Java Swing
- JDBC
- Microsoft SQL Server
- Object-Oriented Programming (OOP)
- File Handling

## Project Structure
```text
src/
 ├── ui/
 │    ├── StartPage.java
 │    ├── Login.java
 │    ├── SignUp.java
 │    ├── FarmerMenuDashboard.java
 │    ├── AddCropPage.java
 │    ├── ViewMyCropsPage.java
 │    ├── ConsumerMenuDashboard.java
 │    ├── ConsumerDashboard.java
 │    └── ConsumerBillPage.java
 │
 ├── util/
 │    └── DBConnection.java
 │
 └── Main.java
```

## How the System Works
1. Users can register as Farmer or Consumer
2. Login is performed using phone number and password
3. Farmers can add crops with quantity and price details
4. Consumers can browse and purchase available crops
5. Crop quantities are automatically updated after purchase
6. Bills are generated and stored as text files
7. The system maintains transparent farmer-to-consumer trade

## How to Run
1. Open the project in IntelliJ IDEA
2. Configure SQL Server database
3. Create required tables (users, crops, orders)
4. Update database credentials in DBConnection.java
5. Add SQL Server JDBC Driver
6. Run StartPage.java or Main.java

## Future Improvements
- Online payment integration
- Real-time crop price prediction
- Mobile application support
- Admin dashboard
- Notification system
- Cloud database integration

## Author
Kishore U K

## Project Summary
A Java desktop application that enables direct crop trading between farmers and consumers with inventory management, billing generation, and secure database connectivity.
