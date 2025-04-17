package loanapproval.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles database connections for the loan approval system.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/loan_approval_system";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "123456789"; // Change to your MySQL password
    
    private static Connection connection = null;
    
    /**
     * Get a connection to the database.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }
    
    /**
     * Close the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test the database connection.
     * @return true if successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            boolean isValid = testConn != null && testConn.isValid(2);
            return isValid;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
} 