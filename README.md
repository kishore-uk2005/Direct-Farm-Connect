# Direct Farm Connect — Farmer-to-Consumer Web Application

## Overview

Direct Farm Connect is a Java Servlet-based web application that connects farmers directly with consumers for agricultural crop trading. Farmers can manage crop listings, while consumers can browse available crops and purchase products.

## Features

- Role-based registration and login for farmers and consumers
- Farmer dashboard for adding and viewing crop listings
- Consumer dashboard for browsing and purchasing crops
- Inventory updates after successful purchases
- Consumer purchase history and bill summary
- Farmer earnings and crop-wise sales summary
- Database operations using JDBC and Microsoft SQL Server
- Styled web interface using HTML and CSS

## Technologies Used

- **Backend:** Java, Java Servlets
- **Frontend:** HTML5, CSS3
- **Database Connectivity:** JDBC
- **Database:** Microsoft SQL Server
- **Server:** Apache Tomcat 10.1
- **Build Tool:** Apache Maven
- **IDE:** IntelliJ IDEA

## Project Structure

```text
Direct-Farm-Connect-Web/
├── src/
│   ├── com/
│   │   └── directfarmconnect/
│   │       └── servlet/
│   │           ├── LoginServlet.java
│   │           ├── SignupServlet.java
│   │           ├── AddCropServlet.java
│   │           ├── ViewCropsServlet.java
│   │           ├── AvailableCropsServlet.java
│   │           ├── BuyCropServlet.java
│   │           ├── ConsumerBillServlet.java
│   │           └── FarmerEarningsServlet.java
│   ├── util/
│   │   └── DBConnection.java
│   └── main/
│       └── webapp/
│           ├── index.html
│           ├── login.html
│           ├── signup.html
│           ├── add-crop.html
│           ├── buy-crop.html
│           ├── farmer-dashboard.html
│           ├── consumer-dashboard.html
│           ├── my-crops.html
│           └── css/
│               └── style.css
├── pom.xml
└── README.md
```

## Database

The application uses Microsoft SQL Server. The configured database name is supplied through the `DB_NAME` environment variable.

The main tables used by the application are:

- **Users:** User details, login credentials, and roles.
- **Crops:** Farmer crop listings, prices, and available quantities.
- **Orders:** Consumer purchases, quantities, and total prices.

The database must be configured with the tables and columns expected by the application.

## How the System Works

1. Users register or log in as farmers or consumers.
2. Farmers add crops with prices and available quantities.
3. Consumers browse available crops and purchase selected quantities.
4. The application records orders and updates crop inventory.
5. Consumers can view purchase history and bill summaries.
6. Farmers can view crop-wise sales and earnings.

## How to Run

1. Install a compatible JDK, Apache Maven, Apache Tomcat 10.1, and Microsoft SQL Server.
2. Create and configure the required database and tables.
3. Set the `DB_SERVER` and `DB_NAME` environment variables in the environment used by Tomcat.
4. Ensure SQL Server Windows integrated authentication is configured correctly for the Tomcat process.
5. Open the project in IntelliJ IDEA and build it using Maven.
6. Deploy the generated WAR file to Apache Tomcat.
7. Start Tomcat and open the application's local URL.

## Future Improvements

- Secure password hashing and stronger authentication
- Payment status and transaction records
- Online payment gateway integration
- Crop price analytics and notifications
- Further mobile-friendly improvements

## Author

Kishore U K

## Project Summary

Direct Farm Connect is a Java Servlet-based web application for farmer-to-consumer crop trading, supporting user authentication, crop management, purchasing, inventory updates, purchase history, and farmer earnings reports.