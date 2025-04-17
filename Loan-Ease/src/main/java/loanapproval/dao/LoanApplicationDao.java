package loanapproval.dao;

import loanapproval.model.LoanApplication;
import loanapproval.model.LoanApplication.LoanStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for Loan Application related database operations.
 */
public class LoanApplicationDao {
    
    /**
     * Get all loan applications.
     * @return List of all loan applications
     */
    public List<LoanApplication> getAllLoanApplications() {
        List<LoanApplication> applications = new ArrayList<>();
        String sql = "SELECT la.*, u.full_name FROM loan_applications la " +
                     "JOIN users u ON la.user_id = u.user_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LoanApplication application = extractLoanApplicationFromResultSet(rs);
                application.setUserName(rs.getString("full_name"));
                applications.add(application);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all loan applications: " + e.getMessage());
        }
        
        return applications;
    }
    
    /**
     * Get loan applications for a specific user.
     * @param userId The ID of the user whose applications to retrieve
     * @return List of user's loan applications
     */
    public List<LoanApplication> getLoanApplicationsByUserId(int userId) {
        List<LoanApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM loan_applications WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(extractLoanApplicationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan applications by user ID: " + e.getMessage());
        }
        
        return applications;
    }
    
    /**
     * Get a specific loan application by ID.
     * @param applicationId The ID of the application to retrieve
     * @return LoanApplication object if found, null otherwise
     */
    public LoanApplication getLoanApplicationById(int applicationId) {
        String sql = "SELECT la.*, u.full_name FROM loan_applications la " +
                     "JOIN users u ON la.user_id = u.user_id " +
                     "WHERE la.application_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LoanApplication application = extractLoanApplicationFromResultSet(rs);
                    application.setUserName(rs.getString("full_name"));
                    return application;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan application by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get loan applications with a specific status.
     * @param status The status to filter by
     * @return List of loan applications with the specified status
     */
    public List<LoanApplication> getLoanApplicationsByStatus(LoanStatus status) {
        List<LoanApplication> applications = new ArrayList<>();
        String sql = "SELECT la.*, u.full_name FROM loan_applications la " +
                     "JOIN users u ON la.user_id = u.user_id " +
                     "WHERE la.status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LoanApplication application = extractLoanApplicationFromResultSet(rs);
                    application.setUserName(rs.getString("full_name"));
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan applications by status: " + e.getMessage());
        }
        
        return applications;
    }
    
    /**
     * Insert a new loan application into the database.
     * @param application The loan application to insert
     * @return true if successful, false otherwise
     */
    public boolean insertLoanApplication(LoanApplication application) {
        String sql = "INSERT INTO loan_applications (user_id, loan_amount, loan_purpose, " +
                     "duration_months, interest_rate, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, application.getUserId());
            pstmt.setDouble(2, application.getLoanAmount());
            pstmt.setString(3, application.getLoanPurpose());
            pstmt.setInt(4, application.getDurationMonths());
            
            if (application.getInterestRate() > 0) {
                pstmt.setDouble(5, application.getInterestRate());
            } else {
                pstmt.setNull(5, java.sql.Types.DECIMAL);
            }
            
            pstmt.setString(6, application.getStatus().toString());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    application.setApplicationId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting loan application: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update the status of a loan application.
     * @param applicationId The ID of the application to update
     * @param newStatus The new status to set
     * @return true if successful, false otherwise
     */
    public boolean updateLoanApplicationStatus(int applicationId, LoanStatus newStatus) {
        String sql = "UPDATE loan_applications SET status = ? WHERE application_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus.toString());
            pstmt.setInt(2, applicationId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating loan application status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing loan application in the database.
     * @param application The loan application to update
     * @return true if successful, false otherwise
     */
    public boolean updateLoanApplication(LoanApplication application) {
        String sql = "UPDATE loan_applications SET user_id = ?, loan_amount = ?, " +
                     "loan_purpose = ?, duration_months = ?, interest_rate = ?, " +
                     "status = ? WHERE application_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, application.getUserId());
            pstmt.setDouble(2, application.getLoanAmount());
            pstmt.setString(3, application.getLoanPurpose());
            pstmt.setInt(4, application.getDurationMonths());
            
            if (application.getInterestRate() > 0) {
                pstmt.setDouble(5, application.getInterestRate());
            } else {
                pstmt.setNull(5, java.sql.Types.DECIMAL);
            }
            
            pstmt.setString(6, application.getStatus().toString());
            pstmt.setInt(7, application.getApplicationId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating loan application: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a loan application from the database.
     * @param applicationId The ID of the application to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteLoanApplication(int applicationId) {
        String sql = "DELETE FROM loan_applications WHERE application_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting loan application: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract a LoanApplication object from a ResultSet.
     * @param rs The ResultSet containing loan application data
     * @return LoanApplication object
     * @throws SQLException if data extraction fails
     */
    private LoanApplication extractLoanApplicationFromResultSet(ResultSet rs) throws SQLException {
        LoanApplication application = new LoanApplication();
        application.setApplicationId(rs.getInt("application_id"));
        application.setUserId(rs.getInt("user_id"));
        application.setLoanAmount(rs.getDouble("loan_amount"));
        application.setLoanPurpose(rs.getString("loan_purpose"));
        
        Timestamp timestamp = rs.getTimestamp("application_date");
        application.setApplicationDate(new Date(timestamp.getTime()));
        
        application.setDurationMonths(rs.getInt("duration_months"));
        
        // Handle null interest_rate
        double interestRate = rs.getDouble("interest_rate");
        if (!rs.wasNull()) {
            application.setInterestRate(interestRate);
        }
        
        application.setStatus(LoanStatus.valueOf(rs.getString("status")));
        
        return application;
    }
} 