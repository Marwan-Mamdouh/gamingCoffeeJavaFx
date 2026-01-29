# ☕ Gaming Coffee - Café Management System

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-19%2B-blue)
![SQLite](https://img.shields.io/badge/SQLite-Integrated-lightgrey)
![Build](https://img.shields.io/badge/Build-Maven-C71A36)

A robust, JavaFX-based desktop application designed to streamline operations for gaming cafés. This system seamlessly integrates Point of Sale (POS) features with gaming session management, offering a comprehensive solution for business owners.

## 🚀 Key Features

### 🎮 Gaming Session Management

- **Real-time Tracking**: Monitor active gaming sessions on multiple stations.
- **Automated Billing**: specific cost calculation based on usage time.
- **Session Control**: Start, stop, and manage customer sessions effortlessly.

### 🍔 Point of Sale (Order Management)

- **Menu System**: Dynamic menu with categorized items (Food, Drinks, etc.).
- **Order Processing**: sophisticated cart system with tax calculation and total computation.
- **Receipts**: Generation of detailed transaction receipts.

### 🛡️ Security & Administration

- **Secure Authentication**: Implementation of **BCrypt** hashing for robust password security.
- **Role-Based Access**: Distinct interfaces and permissions for Admins and Employees.
- **Administrative Dashboard**:
  - **User Management**: CRUD operations for managing staff (Admin/Employee).
  - **Analytics**: Sales reporting and performance metrics.
  - **Database**: Direct management of the underlying SQLite database.

## 🏗️ Technical Architecture

This project is built with a focus on clean architecture and maintainability:

- **MVC Pattern**: Strict separation of concerns using Model-View-Controller architecture.
- **DAO Pattern**: Data Access Object layer for abstracting and encapsulating all access to the data source.
- **Singleton Pattern**: Utilized for the Database Connection to ensure efficient resource management.
- **JavaFX Modern UI**: Responsive and intuitive user interface designed with FXML and CSS3.
- **Embedded Database**: Zero-configuration deployment using SQLite.

## 🛠️ Technology Stack

- **Core Language**: Java 17
- **UI Framework**: JavaFX (OpenJFX)
- **Build Tool**: Maven
- **Database**: SQLite (JDBC)
- **Security**: BCrypt (Password Hashing)
- **Libraries**:
  - `at.favre.lib:bcrypt`: For secure password hashing.
  - `org.xerial:sqlite-jdbc`: SQLite driver.
  - `de.jensd:fontawesomefx`: For modern UI icons.

## 📦 Installation & Setup

### Prerequisites

- Java JDK 17 or higher
- Maven 3.x

### Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/Marwan-Mamdouh/gamingCoffeeJavaFx.git
   cd gamingCoffeeJavaFx
   ```

2. **Build the Project**

   ```bash
   ./mvnw clean install
   ```

3. **Run the Application**

   **Windows:**

   ```powershell
   ./mvnw.cmd javafx:run
   ```

   **Linux / macOS:**

   ```bash
   ./mvnw javafx:run
   ```

---

_Built as a comprehensive learning project to demonstrate advanced Java application development patterns and practices._
