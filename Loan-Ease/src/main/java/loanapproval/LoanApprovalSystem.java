package loanapproval;

import loanapproval.dao.DatabaseConnection;
import loanapproval.ui.MainFrame;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

/**
 * Main application class for the Loan Approval System.
 */
public class LoanApprovalSystem {
    
    /**
     * Main method to start the application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set look and feel to Nimbus
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        // Test database connection
        if (!testDatabaseConnection()) {
            showDatabaseError();
            return;
        }
        
        // Launch the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
    
    /**
     * Test the database connection.
     * @return true if connection is successful, false otherwise
     */
    private static boolean testDatabaseConnection() {
        return DatabaseConnection.testConnection();
    }
    
    /**
     * Show database connection error dialog.
     */
    private static void showDatabaseError() {
        String message = "Failed to connect to the database. Please check:\n\n" +
                "1. MySQL Server is running\n" +
                "2. Database 'loan_approval_system' exists\n" +
                "3. Database credentials in DatabaseConnection.java are correct\n\n" +
                "Please fix the issues and restart the application.";
        
        JOptionPane.showMessageDialog(null,
                message,
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE);
    }
} 