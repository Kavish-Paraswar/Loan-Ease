package loanapproval.ui;

import loanapproval.controller.AuthenticationController;
import loanapproval.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for viewing and editing user profile information.
 */
public class UserProfileDialog extends JDialog {
    private JFrame parent;
    private User user;
    private AuthenticationController authController;
    
    // UI Components
    private JTextField usernameField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private boolean profileUpdated = false;
    
    /**
     * Create a new UserProfileDialog.
     * @param parent The parent frame
     * @param user The user whose profile to view/edit
     */
    public UserProfileDialog(JFrame parent, User user) {
        super(parent, "User Profile", true);
        this.parent = parent;
        this.user = user;
        this.authController = new AuthenticationController();
        
        initComponents();
        layoutComponents();
        populateFields();
        setupListeners();
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        usernameField = new JTextField(20);
        usernameField.setEditable(false); // Username cannot be changed
        
        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        
        fullNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        
        saveButton = new JButton("Save Changes");
        cancelButton = new JButton("Cancel");
    }
    
    /**
     * Layout the UI components.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Account Information section
        JLabel accountLabel = new JLabel("Account Information");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(accountLabel, gbc);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);
        
        // Current Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Current Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(currentPasswordField, gbc);
        
        // New Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("New Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(newPasswordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(confirmPasswordField, gbc);
        
        // Personal Information section
        JLabel personalLabel = new JLabel("Personal Information");
        personalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(personalLabel, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(fullNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 9;
        JScrollPane scrollPane = new JScrollPane(addressArea);
        formPanel.add(scrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Populate the fields with user data.
     */
    private void populateFields() {
        usernameField.setText(user.getUsername());
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        addressArea.setText(user.getAddress());
    }
    
    /**
     * Set up action listeners for the buttons.
     */
    private void setupListeners() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });
    }
    
    /**
     * Save the user profile changes.
     */
    private void saveProfile() {
        // Validate and update fields
        if (!validateFields()) {
            return;
        }
        
        // Update password if new password is provided
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        
        if (!newPassword.isEmpty()) {
            // Verify current password
            User authenticatedUser = authController.authenticateUser(user.getUsername(), currentPassword);
            if (authenticatedUser == null) {
                JOptionPane.showMessageDialog(this,
                        "Current password is incorrect",
                        "Password Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update the password
            user.setPassword(newPassword);
        }
        
        // Update other fields
        user.setFullName(fullNameField.getText().trim());
        user.setEmail(emailField.getText().trim());
        user.setPhone(phoneField.getText().trim());
        user.setAddress(addressArea.getText().trim());
        
        // Save to database
        if (authController.updateUserProfile(user)) {
            JOptionPane.showMessageDialog(this,
                    "Profile updated successfully",
                    "Profile Updated", JOptionPane.INFORMATION_MESSAGE);
            profileUpdated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update profile. Please try again.",
                    "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate the input fields.
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateFields() {
        // Check full name
        if (fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Full name cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Email cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid email format",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check password if changing
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (!newPassword.isEmpty()) {
            // Check current password
            if (currentPasswordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this,
                        "Current password is required to set a new password",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Check password match
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                        "New password and confirm password do not match",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Check if profile was updated.
     * @return true if profile was updated, false otherwise
     */
    public boolean isProfileUpdated() {
        return profileUpdated;
    }
    
    /**
     * Get the updated user.
     * @return The updated User object
     */
    public User getUpdatedUser() {
        return user;
    }
} 