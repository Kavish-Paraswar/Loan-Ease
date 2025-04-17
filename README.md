# 💼 Loan Approval System

The Loan Approval System is a desktop application built using **Java Swing** and **MySQL**, following the **MVC architecture**. It allows users to apply for loans and officers to review, approve, or reject applications through dedicated dashboards.

## 🔑 Key Features

This system supports role-based login for both users and officers. Users can apply for new loans, while officers have access to all submitted applications for decision-making. It includes proper form validation, error handling, and ensures a smooth user experience.

## 🧱 Tech Stack

- **Java SE 17** for core development  
- **Java Swing** for GUI design  
- **JDBC** for database connectivity  
- **MySQL** as the relational database  
- Implements **MVC + DAO** design pattern

## 🗂️ Project Structure

- `controller/` – Manages application logic  
- `model/` – Contains data classes (User, LoanApplication, LoanDecision)  
- `dao/` – Handles database operations  
- `ui/` – Builds and controls the user interface (LoginPanel, DashboardPanel, etc.)

## 💡 Architecture

This system uses the **Model-View-Controller (MVC)** pattern. The `Model` holds data structures, the `View` manages user interactions through Java Swing components, and the `Controller` handles logic and data flow. The DAO layer provides abstraction for database queries.

## 🧪 Testing & Validation

Input validation is enforced at both GUI and database levels. The system handles SQL and runtime exceptions gracefully, ensuring robustness. Test cases simulate realistic loan processing workflows.

## 🚀 How to Run

1. Import the project in any IDE (VSCode / IntelliJ / Eclipse)  
2. Configure MySQL database and credentials in `DatabaseConnection.java`  
3. Compile and run `LoanApprovalSystem.java`  
4. Login as a user or officer and explore the system


