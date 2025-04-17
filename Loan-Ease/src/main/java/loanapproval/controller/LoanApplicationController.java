package loanapproval.controller;

import loanapproval.dao.LoanApplicationDao;
import loanapproval.model.LoanApplication;
import loanapproval.model.LoanApplication.LoanStatus;

import java.util.List;

/**
 * Controller for handling loan application operations.
 */
public class LoanApplicationController {
    private LoanApplicationDao loanApplicationDao;
    
    /**
     * Create a new LoanApplicationController.
     */
    public LoanApplicationController() {
        this.loanApplicationDao = new LoanApplicationDao();
    }
    
    /**
     * Get all loan applications.
     * @return List of all loan applications
     */
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationDao.getAllLoanApplications();
    }
    
    /**
     * Get loan applications for a specific user.
     * @param userId The ID of the user
     * @return List of user's loan applications
     */
    public List<LoanApplication> getLoanApplicationsByUserId(int userId) {
        return loanApplicationDao.getLoanApplicationsByUserId(userId);
    }
    
    /**
     * Get a specific loan application by ID.
     * @param applicationId The ID of the application
     * @return LoanApplication object if found, null otherwise
     */
    public LoanApplication getLoanApplicationById(int applicationId) {
        return loanApplicationDao.getLoanApplicationById(applicationId);
    }
    
    /**
     * Get loan applications with a specific status.
     * @param status The status to filter by
     * @return List of loan applications with the specified status
     */
    public List<LoanApplication> getLoanApplicationsByStatus(LoanStatus status) {
        return loanApplicationDao.getLoanApplicationsByStatus(status);
    }
    
    /**
     * Create a new loan application.
     * @param application The loan application to create
     * @return true if successful, false otherwise
     */
    public boolean createLoanApplication(LoanApplication application) {
        return loanApplicationDao.insertLoanApplication(application);
    }
    
    /**
     * Update the status of a loan application.
     * @param applicationId The ID of the application to update
     * @param newStatus The new status to set
     * @return true if successful, false otherwise
     */
    public boolean updateLoanApplicationStatus(int applicationId, LoanStatus newStatus) {
        return loanApplicationDao.updateLoanApplicationStatus(applicationId, newStatus);
    }
    
    /**
     * Update a loan application.
     * @param application The loan application to update
     * @return true if successful, false otherwise
     */
    public boolean updateLoanApplication(LoanApplication application) {
        return loanApplicationDao.updateLoanApplication(application);
    }
    
    /**
     * Delete a loan application.
     * @param applicationId The ID of the application to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteLoanApplication(int applicationId) {
        return loanApplicationDao.deleteLoanApplication(applicationId);
    }
} 