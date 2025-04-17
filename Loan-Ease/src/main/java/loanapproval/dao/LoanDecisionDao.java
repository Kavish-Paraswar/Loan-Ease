package loanapproval.dao;

import loanapproval.model.LoanDecision;
import loanapproval.model.LoanDecision.DecisionStatus;

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
 * Data Access Object for Loan Decision related database operations.
 */
public class LoanDecisionDao {
    
    /**
     * Get all loan decisions.
     * @return List of all loan decisions
     */
    public List<LoanDecision> getAllLoanDecisions() {
        List<LoanDecision> decisions = new ArrayList<>();
        String sql = "SELECT ld.*, u.full_name FROM loan_decisions ld " +
                     "JOIN users u ON ld.officer_id = u.user_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LoanDecision decision = extractLoanDecisionFromResultSet(rs);
                decision.setOfficerName(rs.getString("full_name"));
                decisions.add(decision);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all loan decisions: " + e.getMessage());
        }
        
        return decisions;
    }
    
    /**
     * Get loan decisions for a specific application.
     * @param applicationId The ID of the application
     * @return LoanDecision object if found, null otherwise
     */
    public LoanDecision getLoanDecisionByApplicationId(int applicationId) {
        String sql = "SELECT ld.*, u.full_name FROM loan_decisions ld " +
                     "JOIN users u ON ld.officer_id = u.user_id " +
                     "WHERE ld.application_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LoanDecision decision = extractLoanDecisionFromResultSet(rs);
                    decision.setOfficerName(rs.getString("full_name"));
                    return decision;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan decision by application ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get loan decisions made by a specific officer.
     * @param officerId The ID of the officer
     * @return List of loan decisions made by the officer
     */
    public List<LoanDecision> getLoanDecisionsByOfficerId(int officerId) {
        List<LoanDecision> decisions = new ArrayList<>();
        String sql = "SELECT * FROM loan_decisions WHERE officer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, officerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    decisions.add(extractLoanDecisionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan decisions by officer ID: " + e.getMessage());
        }
        
        return decisions;
    }
    
    /**
     * Insert a new loan decision into the database.
     * @param decision The loan decision to insert
     * @return true if successful, false otherwise
     */
    public boolean insertLoanDecision(LoanDecision decision) {
        String sql = "INSERT INTO loan_decisions (application_id, officer_id, status, comments) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, decision.getApplicationId());
            pstmt.setInt(2, decision.getOfficerId());
            pstmt.setString(3, decision.getStatus().toString());
            pstmt.setString(4, decision.getComments());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    decision.setDecisionId(generatedKeys.getInt(1));
                    
                    // Also update the loan application status
                    LoanApplicationDao loanAppDao = new LoanApplicationDao();
                    
                    switch (decision.getStatus()) {
                        case APPROVED:
                            loanAppDao.updateLoanApplicationStatus(decision.getApplicationId(), 
                                    loanapproval.model.LoanApplication.LoanStatus.APPROVED);
                            break;
                        case REJECTED:
                            loanAppDao.updateLoanApplicationStatus(decision.getApplicationId(), 
                                    loanapproval.model.LoanApplication.LoanStatus.REJECTED);
                            break;
                    }
                    
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting loan decision: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing loan decision in the database.
     * @param decision The loan decision to update
     * @return true if successful, false otherwise
     */
    public boolean updateLoanDecision(LoanDecision decision) {
        String sql = "UPDATE loan_decisions SET status = ?, comments = ? WHERE decision_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, decision.getStatus().toString());
            pstmt.setString(2, decision.getComments());
            pstmt.setInt(3, decision.getDecisionId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Also update the loan application status
                LoanApplicationDao loanAppDao = new LoanApplicationDao();
                
                switch (decision.getStatus()) {
                    case APPROVED:
                        loanAppDao.updateLoanApplicationStatus(decision.getApplicationId(), 
                                loanapproval.model.LoanApplication.LoanStatus.APPROVED);
                        break;
                    case REJECTED:
                        loanAppDao.updateLoanApplicationStatus(decision.getApplicationId(), 
                                loanapproval.model.LoanApplication.LoanStatus.REJECTED);
                        break;
                }
                
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error updating loan decision: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a loan decision from the database.
     * @param decisionId The ID of the decision to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteLoanDecision(int decisionId) {
        // First get the loan application ID to revert its status
        LoanDecision decision = getLoanDecisionById(decisionId);
        if (decision == null) {
            return false;
        }
        
        String sql = "DELETE FROM loan_decisions WHERE decision_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, decisionId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Revert the loan application status to PENDING
                LoanApplicationDao loanAppDao = new LoanApplicationDao();
                loanAppDao.updateLoanApplicationStatus(decision.getApplicationId(), 
                        loanapproval.model.LoanApplication.LoanStatus.PENDING);
                
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error deleting loan decision: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a loan decision by ID.
     * @param decisionId The ID of the decision to retrieve
     * @return LoanDecision object if found, null otherwise
     */
    public LoanDecision getLoanDecisionById(int decisionId) {
        String sql = "SELECT * FROM loan_decisions WHERE decision_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, decisionId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractLoanDecisionFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan decision by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Extract a LoanDecision object from a ResultSet.
     * @param rs The ResultSet containing loan decision data
     * @return LoanDecision object
     * @throws SQLException if data extraction fails
     */
    private LoanDecision extractLoanDecisionFromResultSet(ResultSet rs) throws SQLException {
        LoanDecision decision = new LoanDecision();
        decision.setDecisionId(rs.getInt("decision_id"));
        decision.setApplicationId(rs.getInt("application_id"));
        decision.setOfficerId(rs.getInt("officer_id"));
        
        Timestamp timestamp = rs.getTimestamp("decision_date");
        decision.setDecisionDate(new Date(timestamp.getTime()));
        
        decision.setStatus(DecisionStatus.valueOf(rs.getString("status")));
        decision.setComments(rs.getString("comments"));
        
        return decision;
    }
} 