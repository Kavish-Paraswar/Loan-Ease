package loanapproval.dao;

import loanapproval.model.FinancialInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for Financial Information related database operations.
 */
public class FinancialInfoDao {
    
    /**
     * Get financial information for a specific user.
     * @param userId The ID of the user
     * @return FinancialInfo object if found, null otherwise
     */
    public FinancialInfo getFinancialInfoByUserId(int userId) {
        String sql = "SELECT * FROM user_financial_info WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractFinancialInfoFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting financial info by user ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Insert new financial information into the database.
     * @param financialInfo The financial information to insert
     * @return true if successful, false otherwise
     */
    public boolean insertFinancialInfo(FinancialInfo financialInfo) {
        String sql = "INSERT INTO user_financial_info (user_id, annual_income, employment_status, " +
                     "employer_name, job_title, employment_duration, credit_score, existing_debts) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, financialInfo.getUserId());
            pstmt.setDouble(2, financialInfo.getAnnualIncome());
            pstmt.setString(3, financialInfo.getEmploymentStatus());
            pstmt.setString(4, financialInfo.getEmployerName());
            pstmt.setString(5, financialInfo.getJobTitle());
            pstmt.setInt(6, financialInfo.getEmploymentDuration());
            
            if (financialInfo.getCreditScore() > 0) {
                pstmt.setInt(7, financialInfo.getCreditScore());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }
            
            pstmt.setDouble(8, financialInfo.getExistingDebts());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    financialInfo.setFinancialInfoId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting financial info: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update existing financial information in the database.
     * @param financialInfo The financial information to update
     * @return true if successful, false otherwise
     */
    public boolean updateFinancialInfo(FinancialInfo financialInfo) {
        String sql = "UPDATE user_financial_info SET annual_income = ?, employment_status = ?, " +
                     "employer_name = ?, job_title = ?, employment_duration = ?, " +
                     "credit_score = ?, existing_debts = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, financialInfo.getAnnualIncome());
            pstmt.setString(2, financialInfo.getEmploymentStatus());
            pstmt.setString(3, financialInfo.getEmployerName());
            pstmt.setString(4, financialInfo.getJobTitle());
            pstmt.setInt(5, financialInfo.getEmploymentDuration());
            
            if (financialInfo.getCreditScore() > 0) {
                pstmt.setInt(6, financialInfo.getCreditScore());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            pstmt.setDouble(7, financialInfo.getExistingDebts());
            pstmt.setInt(8, financialInfo.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating financial info: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Insert or update financial information in the database.
     * @param financialInfo The financial information to save
     * @return true if successful, false otherwise
     */
    public boolean saveFinancialInfo(FinancialInfo financialInfo) {
        // Check if user already has financial info
        FinancialInfo existingInfo = getFinancialInfoByUserId(financialInfo.getUserId());
        
        if (existingInfo == null) {
            // Insert new financial info
            return insertFinancialInfo(financialInfo);
        } else {
            // Update existing financial info
            financialInfo.setFinancialInfoId(existingInfo.getFinancialInfoId());
            return updateFinancialInfo(financialInfo);
        }
    }
    
    /**
     * Delete financial information from the database.
     * @param userId The ID of the user whose financial info to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteFinancialInfo(int userId) {
        String sql = "DELETE FROM user_financial_info WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting financial info: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract a FinancialInfo object from a ResultSet.
     * @param rs The ResultSet containing financial info data
     * @return FinancialInfo object
     * @throws SQLException if data extraction fails
     */
    private FinancialInfo extractFinancialInfoFromResultSet(ResultSet rs) throws SQLException {
        FinancialInfo financialInfo = new FinancialInfo();
        financialInfo.setFinancialInfoId(rs.getInt("financial_info_id"));
        financialInfo.setUserId(rs.getInt("user_id"));
        financialInfo.setAnnualIncome(rs.getDouble("annual_income"));
        financialInfo.setEmploymentStatus(rs.getString("employment_status"));
        financialInfo.setEmployerName(rs.getString("employer_name"));
        financialInfo.setJobTitle(rs.getString("job_title"));
        financialInfo.setEmploymentDuration(rs.getInt("employment_duration"));
        
        // Handle null credit_score
        int creditScore = rs.getInt("credit_score");
        if (!rs.wasNull()) {
            financialInfo.setCreditScore(creditScore);
        }
        
        financialInfo.setExistingDebts(rs.getDouble("existing_debts"));
        
        return financialInfo;
    }
} 