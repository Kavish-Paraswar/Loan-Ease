package loanapproval.ui;

import loanapproval.controller.AuthenticationController;
import loanapproval.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Login panel for authentication in the Loan Approval System.
 */
public class LoginPanel extends JPanel {
    private MainFrame parent;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    private JButton registerButton;
    
    private AuthenticationController authController;
    
    /**
     * Create a new LoginPanel.
     * @param parent The parent MainFrame
     */
    public LoginPanel(MainFrame parent) {
        this.parent = parent;
        this.authController = new AuthenticationController();
        
        initComponents();
        layoutComponents();
        setupListeners();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(41, 128, 185), // Start color - nice blue
            0, getHeight(), new Color(44, 62, 80) // End color - dark blue/gray
        );
        
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.dispose();
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Custom font
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        
        // Create text fields with custom styling
        usernameField = new JTextField(20);
        usernameField.setFont(fieldFont);
        usernameField.setMargin(new Insets(8, 10, 8, 10));
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(fieldFont);
        passwordField.setMargin(new Insets(8, 10, 8, 10));
        
        // Create styled combo box
        String[] userTypes = {"User", "Officer"};
        userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setFont(fieldFont);
        
        // Create styled buttons
        loginButton = createStyledButton("Login", buttonFont, new Color(41, 128, 185));
        registerButton = createStyledButton("Register", buttonFont, new Color(39, 174, 96));
    }
    
    /**
     * Create a styled button with rounded corners and hover effect
     */
    private JButton createStyledButton(String text, Font font, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                
                g2.setColor(Color.WHITE);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        
        return button;
    }
    
    /**
     * Layout the UI components.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Create logo panel
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Make this panel transparent to show the gradient background
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 0));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBorder(new EmptyBorder(30, 0, 20, 0));
        
        // Add styled logo
        JLabel logoLabel = new JLabel("Loan Approval System");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        
        // Add a description label
        JLabel descLabel = new JLabel("Secure, Fast, and Reliable Loan Processing");
        descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        descLabel.setForeground(new Color(236, 240, 241));
        
        // Add logo components to logo panel
        JPanel logoTextPanel = new JPanel(new BorderLayout());
        logoTextPanel.setOpaque(false);
        logoTextPanel.add(logoLabel, BorderLayout.CENTER);
        logoTextPanel.add(descLabel, BorderLayout.SOUTH);
        logoPanel.add(logoTextPanel);
        
        // Create a card-like white panel for the login form
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        formCard.setOpaque(false);
        formCard.setBorder(new EmptyBorder(25, 30, 25, 30));
        formCard.setLayout(new GridBagLayout());
        
        // Style labels
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JLabel userTypeLabel = new JLabel("Login as");
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        usernameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        userTypeLabel.setFont(labelFont);
        
        // Create form layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Add components with proper spacing
        gbc.gridx = 0;
        gbc.gridy = 0;
        formCard.add(usernameLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formCard.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formCard.add(passwordLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formCard.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formCard.add(userTypeLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        formCard.add(userTypeCombo, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        formCard.add(buttonPanel, gbc);
        
        // Create a container panel for the form card to position it
        JPanel formCardContainer = new JPanel(new GridBagLayout());
        formCardContainer.setOpaque(false);
        formCardContainer.add(formCard, new GridBagConstraints());
        
        // Add components to the main panel
        add(logoPanel, BorderLayout.NORTH);
        add(formCardContainer, BorderLayout.CENTER);
    }
    
    /**
     * Set up action listeners for the buttons.
     */
    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationDialog();
            }
        });
    }
    
    /**
     * Perform login authentication.
     */
    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        boolean isOfficer = userTypeCombo.getSelectedIndex() == 1;
        
        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Username and password cannot be empty", "Login Error");
            return;
        }
        
        User user = authController.authenticateUser(username, password);
        
        if (user == null) {
            showErrorMessage("Invalid username or password", "Authentication Failed");
            return;
        }
        
        // Check if user type matches the selected type
        boolean isUserOfficer = user.getUserType() == User.UserType.OFFICER;
        
        if (isOfficer != isUserOfficer) {
            showErrorMessage("Login type does not match user type", "Authentication Failed");
            return;
        }
        
        // Login successful
        parent.setLoggedInUser(user);
        
        if (isUserOfficer) {
            parent.showOfficerDashboard();
        } else {
            parent.showUserDashboard();
        }
    }
    
    /**
     * Show error message with styled dialog
     */
    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show the registration dialog.
     */
    private void showRegistrationDialog() {
        // Only allow user registration, not officer
        if (userTypeCombo.getSelectedIndex() == 1) {
            showErrorMessage("Officer registration is not allowed", "Registration Error");
            return;
        }
        
        RegisterDialog dialog = new RegisterDialog(parent);
        dialog.setVisible(true);
        
        // If registration was successful, pre-fill the username
        if (dialog.isRegistrationSuccessful()) {
            usernameField.setText(dialog.getUsername());
            passwordField.setText("");
            JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now login.",
                    "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
} 