package loanapproval.ui;

import loanapproval.controller.AuthenticationController;
import loanapproval.model.User;
import loanapproval.model.User.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for new user registration.
 */
public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JButton registerButton;
    private JButton cancelButton;
    
    private AuthenticationController authController;
    private boolean registrationSuccessful = false;
    private String username = "";
    
    /**
     * Create a new RegisterDialog.
     * @param parent The parent frame
     */
    public RegisterDialog(JFrame parent) {
        super(parent, "User Registration", true);
        this.authController = new AuthenticationController();
        
        initComponents();
        layoutComponents();
        setupListeners();
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        fullNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        
        registerButton = new JButton("Register");
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
        
        // Username row
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);
        
        // Password row
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);
        
        // Confirm Password row
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(confirmPasswordField, gbc);
        
        // Full Name row
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Full Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(fullNameField, gbc);
        
        // Email row
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(emailField, gbc);
        
        // Phone row
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(phoneField, gbc);
        
        // Address row
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        JScrollPane scrollPane = new JScrollPane(addressArea);
        formPanel.add(scrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Set up action listeners for the buttons.
     */
    private void setupListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Register a new user.
     */
    private void registerUser() {
        // Validate input fields
        if (!validateFields()) {
            return;
        }
        
        // Create new user
        User user = new User();
        user.setUsername(usernameField.getText().trim());
        user.setPassword(new String(passwordField.getPassword()));
        user.setFullName(fullNameField.getText().trim());
        user.setEmail(emailField.getText().trim());
        user.setPhone(phoneField.getText().trim());
        user.setAddress(addressArea.getText().trim());
        user.setUserType(UserType.USER); // Only users can register
        
        // Try to register the user
        if (authController.registerUser(user)) {
            username = user.getUsername();
            registrationSuccessful = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Username may already exist.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate the input fields.
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateFields() {
        // Check for empty fields
        if (usernameField.getText().trim().isEmpty() ||
                passwordField.getPassword().length == 0 ||
                confirmPasswordField.getPassword().length == 0 ||
                fullNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (Username, Password, Full Name, Email)",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check password match
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate email format
        String email = emailField.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid email format",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if registration was successful.
     * @return true if registration was successful, false otherwise
     */
    public boolean isRegistrationSuccessful() {
        return registrationSuccessful;
    }
    
    /**
     * Get the username of the registered user.
     * @return the username
     */
    public String getUsername() {
        return username;
    }
} 