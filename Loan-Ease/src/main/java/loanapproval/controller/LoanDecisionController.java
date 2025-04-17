package loanapproval.controller;

import loanapproval.dao.LoanDecisionDao;
import loanapproval.model.LoanDecision;
import loanapproval.model.LoanDecision.DecisionStatus;

import java.util.List;

/**
 * Controller for handling loan decision operations.
 */
public class LoanDecisionController {
    private LoanDecisionDao loanDecisionDao;
    
    /**
     * Create a new LoanDecisionController.
     */
    public LoanDecisionController() {
        this.loanDecisionDao = new LoanDecisionDao();
    }
    
    /**
     * Get all loan decisions.
     * @return List of all loan decisions
     */
    public List<LoanDecision> getAllLoanDecisions() {
        return loanDecisionDao.getAllLoanDecisions();
    }
    
    /**
     * Get a loan decision for a specific application.
     * @param applicationId The ID of the application
     * @return LoanDecision object if found, null otherwise
     */
    public LoanDecision getLoanDecisionByApplicationId(int applicationId) {
        return loanDecisionDao.getLoanDecisionByApplicationId(applicationId);
    }
    
    /**
     * Get loan decisions made by a specific officer.
     * @param officerId The ID of the officer
     * @return List of loan decisions made by the officer
     */
    public List<LoanDecision> getLoanDecisionsByOfficerId(int officerId) {
        return loanDecisionDao.getLoanDecisionsByOfficerId(officerId);
    }
    
    /**
     * Process a loan application (approve or reject).
     * @param applicationId The ID of the application to process
     * @param officerId The ID of the officer making the decision
     * @param approve true to approve, false to reject
     * @param comments Any comments related to the decision
     * @return true if successful, false otherwise
     */
    public boolean processLoanApplication(int applicationId, int officerId, boolean approve, String comments) {
        LoanDecision decision = new LoanDecision();
        decision.setApplicationId(applicationId);
        decision.setOfficerId(officerId);
        decision.setStatus(approve ? DecisionStatus.APPROVED : DecisionStatus.REJECTED);
        decision.setComments(comments);
        
        // Check if there's an existing decision
        LoanDecision existingDecision = loanDecisionDao.getLoanDecisionByApplicationId(applicationId);
        
        if (existingDecision != null) {
            // Update existing decision
            decision.setDecisionId(existingDecision.getDecisionId());
            return loanDecisionDao.updateLoanDecision(decision);
        } else {
            // Create new decision
            return loanDecisionDao.insertLoanDecision(decision);
        }
    }
    
    /**
     * Get a loan decision by ID.
     * @param decisionId The ID of the decision
     * @return LoanDecision object if found, null otherwise
     */
    public LoanDecision getLoanDecisionById(int decisionId) {
        return loanDecisionDao.getLoanDecisionById(decisionId);
    }
    
    /**
     * Delete a loan decision.
     * @param decisionId The ID of the decision to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteLoanDecision(int decisionId) {
        return loanDecisionDao.deleteLoanDecision(decisionId);
    }
} 