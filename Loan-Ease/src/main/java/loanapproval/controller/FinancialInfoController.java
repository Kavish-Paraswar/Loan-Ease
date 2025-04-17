package loanapproval.controller;

import loanapproval.dao.FinancialInfoDao;
import loanapproval.model.FinancialInfo;

/**
 * Controller for handling financial information operations.
 */
public class FinancialInfoController {
    private FinancialInfoDao financialInfoDao;
    
    /**
     * Create a new FinancialInfoController.
     */
    public FinancialInfoController() {
        this.financialInfoDao = new FinancialInfoDao();
    }
    
    /**
     * Get financial information for a specific user.
     * @param userId The ID of the user
     * @return FinancialInfo object if found, null otherwise
     */
    public FinancialInfo getFinancialInfoByUserId(int userId) {
        return financialInfoDao.getFinancialInfoByUserId(userId);
    }
    
    /**
     * Save (insert or update) financial information.
     * @param financialInfo The financial information to save
     * @return true if successful, false otherwise
     */
    public boolean saveFinancialInfo(FinancialInfo financialInfo) {
        return financialInfoDao.saveFinancialInfo(financialInfo);
    }
    
    /**
     * Delete financial information for a specific user.
     * @param userId The ID of the user
     * @return true if successful, false otherwise
     */
    public boolean deleteFinancialInfo(int userId) {
        return financialInfoDao.deleteFinancialInfo(userId);
    }
    
    /**
     * Calculate the credit worthiness score of a user.
     * @param userId The ID of the user
     * @return Credit score between 0-100, or -1 if not enough information
     */
    public int calculateCreditWorthinessScore(int userId) {
        FinancialInfo financialInfo = getFinancialInfoByUserId(userId);
        
        if (financialInfo == null) {
            return -1; // Not enough information
        }
        
        int score = 0;
        
        // Factor 1: Employment Status (max 20 points)
        String employmentStatus = financialInfo.getEmploymentStatus();
        if (employmentStatus != null) {
            if (employmentStatus.equalsIgnoreCase("Full-time")) {
                score += 20;
            } else if (employmentStatus.equalsIgnoreCase("Part-time")) {
                score += 15;
            } else if (employmentStatus.equalsIgnoreCase("Self-employed")) {
                score += 15;
            } else if (employmentStatus.equalsIgnoreCase("Retired")) {
                score += 10;
            } else {
                score += 5;
            }
        }
        
        // Factor 2: Employment Duration (max 15 points)
        int employmentDuration = financialInfo.getEmploymentDuration();
        if (employmentDuration >= 60) { // 5+ years
            score += 15;
        } else if (employmentDuration >= 36) { // 3+ years
            score += 12;
        } else if (employmentDuration >= 24) { // 2+ years
            score += 10;
        } else if (employmentDuration >= 12) { // 1+ year
            score += 8;
        } else if (employmentDuration >= 6) { // 6+ months
            score += 5;
        } else {
            score += 2;
        }
        
        // Factor 3: Credit Score (max 25 points)
        int creditScore = financialInfo.getCreditScore();
        if (creditScore >= 750) {
            score += 25;
        } else if (creditScore >= 700) {
            score += 20;
        } else if (creditScore >= 650) {
            score += 15;
        } else if (creditScore >= 600) {
            score += 10;
        } else if (creditScore >= 550) {
            score += 5;
        } else if (creditScore > 0) {
            score += 2;
        }
        
        // Factor 4: Debt-to-Income Ratio (max 20 points)
        double debtToIncome = financialInfo.calculateDebtToIncomeRatio();
        if (debtToIncome < 15) {
            score += 20;
        } else if (debtToIncome < 25) {
            score += 15;
        } else if (debtToIncome < 35) {
            score += 10;
        } else if (debtToIncome < 45) {
            score += 5;
        } else {
            score += 0;
        }
        
        // Factor 5: Annual Income (max 20 points)
        double annualIncome = financialInfo.getAnnualIncome();
        if (annualIncome >= 100000) {
            score += 20;
        } else if (annualIncome >= 75000) {
            score += 16;
        } else if (annualIncome >= 50000) {
            score += 12;
        } else if (annualIncome >= 35000) {
            score += 8;
        } else if (annualIncome >= 25000) {
            score += 4;
        } else {
            score += 0;
        }
        
        return score;
    }
} 