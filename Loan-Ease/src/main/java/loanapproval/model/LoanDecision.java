package loanapproval.model;

import java.util.Date;

/**
 * Represents a decision made by a loan officer on a loan application.
 */
public class LoanDecision {
    private int decisionId;
    private int applicationId;
    private int officerId;
    private Date decisionDate;
    private DecisionStatus status;
    private String comments;
    
    // Officer's name for display purposes (not stored in DB)
    private String officerName;
    
    // Enum for decision status
    public enum DecisionStatus {
        APPROVED, REJECTED
    }
    
    // Default constructor
    public LoanDecision() {
        this.decisionDate = new Date();
    }
    
    // Constructor with essential fields
    public LoanDecision(int applicationId, int officerId, DecisionStatus status) {
        this.applicationId = applicationId;
        this.officerId = officerId;
        this.status = status;
        this.decisionDate = new Date();
    }
    
    // Full constructor
    public LoanDecision(int decisionId, int applicationId, int officerId, 
                       Date decisionDate, DecisionStatus status, String comments) {
        this.decisionId = decisionId;
        this.applicationId = applicationId;
        this.officerId = officerId;
        this.decisionDate = decisionDate;
        this.status = status;
        this.comments = comments;
    }
    
    // Getters and Setters
    public int getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(int decisionId) {
        this.decisionId = decisionId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public DecisionStatus getStatus() {
        return status;
    }

    public void setStatus(DecisionStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }
    
    @Override
    public String toString() {
        return "LoanDecision{" +
                "decisionId=" + decisionId +
                ", applicationId=" + applicationId +
                ", officerId=" + officerId +
                ", decisionDate=" + decisionDate +
                ", status=" + status +
                '}';
    }
} 