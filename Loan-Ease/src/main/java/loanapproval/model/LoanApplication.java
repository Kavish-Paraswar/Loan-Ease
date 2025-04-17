package loanapproval.model;

import java.util.Date;

/**
 * Represents a loan application in the system.
 */
public class LoanApplication {
    private int applicationId;
    private int userId;
    private double loanAmount;
    private String loanPurpose;
    private Date applicationDate;
    private int durationMonths;
    private double interestRate;
    private LoanStatus status;
    
    // User's name for display purposes (not stored in DB)
    private String userName;
    
    // Enum for loan status
    public enum LoanStatus {
        PENDING, APPROVED, REJECTED
    }
    
    // Default constructor
    public LoanApplication() {
        this.applicationDate = new Date();
        this.status = LoanStatus.PENDING;
    }
    
    // Constructor with essential fields
    public LoanApplication(int userId, double loanAmount, String loanPurpose, int durationMonths) {
        this.userId = userId;
        this.loanAmount = loanAmount;
        this.loanPurpose = loanPurpose;
        this.durationMonths = durationMonths;
        this.applicationDate = new Date();
        this.status = LoanStatus.PENDING;
    }
    
    // Full constructor
    public LoanApplication(int applicationId, int userId, double loanAmount, String loanPurpose, 
                          Date applicationDate, int durationMonths, double interestRate, LoanStatus status) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.loanAmount = loanAmount;
        this.loanPurpose = loanPurpose;
        this.applicationDate = applicationDate;
        this.durationMonths = durationMonths;
        this.interestRate = interestRate;
        this.status = status;
    }
    
    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    // Calculate monthly payment
    public double calculateMonthlyPayment() {
        double monthlyRate = interestRate / 100.0 / 12.0;
        double payment = loanAmount * monthlyRate * Math.pow(1 + monthlyRate, durationMonths) / 
                        (Math.pow(1 + monthlyRate, durationMonths) - 1);
        return payment;
    }
    
    // Calculate total payment
    public double calculateTotalPayment() {
        return calculateMonthlyPayment() * durationMonths;
    }
    
    @Override
    public String toString() {
        return "LoanApplication{" +
                "applicationId=" + applicationId +
                ", userId=" + userId +
                ", loanAmount=" + loanAmount +
                ", loanPurpose='" + loanPurpose + '\'' +
                ", applicationDate=" + applicationDate +
                ", status=" + status +
                '}';
    }
} 