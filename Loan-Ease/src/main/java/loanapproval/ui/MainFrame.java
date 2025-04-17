package loanapproval.ui;

import loanapproval.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main application window that contains all panels.
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private UserDashboardPanel userDashboardPanel;
    private OfficerDashboardPanel officerDashboardPanel;
    
    private User loggedInUser;
    private JMenuBar menuBar;
    
    /**
     * Create a new MainFrame.
     */
    public MainFrame() {
        setTitle("Loan Approval System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Get screen dimensions to maximize window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.width * 0.95); // 95% of screen width
        int height = (int)(screenSize.height * 0.9); // 90% of screen height
        
        setSize(width, height);
        setLocationRelativeTo(null); // Center on screen
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        
        // Set app icon if available
        try {
            // You would need to add an icon file to your resources
            // ImageIcon icon = new ImageIcon(getClass().getResource("/images/app_icon.png"));
            // setIconImage(icon.getImage());
        } catch (Exception e) {
            // Icon loading failed, continue without an icon
        }
        
        initComponents();
        setupMenuBar();
        
        // Set a nice looking UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Initialize panels
        loginPanel = new LoginPanel(this);
        userDashboardPanel = new UserDashboardPanel(this);
        officerDashboardPanel = new OfficerDashboardPanel(this);
        
        // Add panels to card layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(userDashboardPanel, "USER_DASHBOARD");
        mainPanel.add(officerDashboardPanel, "OFFICER_DASHBOARD");
        
        // Set initial panel
        cardLayout.show(mainPanel, "LOGIN");
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Set up the application menu bar.
     */
    private void setupMenuBar() {
        // Create a more modern menu bar with better visibility
        menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Use gradient for better appearance
                GradientPaint gradient = new GradientPaint(0, 0, new Color(41, 128, 185), 
                        0, getHeight(), new Color(25, 118, 210));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Set properties for better visibility
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width, 40));
        
        // File menu - with improved visibility
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.WHITE);
        fileMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fileMenu.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        fileMenu.setOpaque(false);
        
        JMenuItem logoutItem = createMenuItem("Logout", "/images/logout.png");
        JMenuItem exitItem = createMenuItem("Exit", "/images/exit.png");
        
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help menu - with improved visibility
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(Color.WHITE);
        helpMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        helpMenu.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        helpMenu.setOpaque(false);
        
        JMenuItem aboutItem = createMenuItem("About", "/images/about.png");
        
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });
        
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar with more spacing
        menuBar.add(Box.createHorizontalStrut(10)); // Add some spacing
        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalStrut(10)); // Add spacing between menus
        menuBar.add(helpMenu);
        
        // Initially hide the menu bar (only show it after login)
        setJMenuBar(null);
    }
    
    /**
     * Create a styled menu item with improved styling
     */
    private JMenuItem createMenuItem(String text, String iconPath) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        item.setBackground(Color.WHITE);
        
        // Add padding to menu items
        item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        try {
            // You would need to add icon files to your resources
            // ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            // item.setIcon(icon);
        } catch (Exception e) {
            // Icon loading failed, continue without an icon
        }
        
        return item;
    }
    
    /**
     * Show the login panel.
     */
    public void showLoginPanel() {
        // Hide menu bar
        setJMenuBar(null);
        
        // Clear logged in user
        loggedInUser = null;
        
        // Show login panel
        cardLayout.show(mainPanel, "LOGIN");
        
        // Reset dashboard panels
        userDashboardPanel.resetPanel();
        officerDashboardPanel.resetPanel();
    }
    
    /**
     * Show the user dashboard panel.
     */
    public void showUserDashboard() {
        // Show menu bar
        setJMenuBar(menuBar);
        
        // Refresh dashboard data
        userDashboardPanel.refreshData();
        
        // Show user dashboard panel
        cardLayout.show(mainPanel, "USER_DASHBOARD");
    }
    
    /**
     * Show the officer dashboard panel.
     */
    public void showOfficerDashboard() {
        // Show menu bar
        setJMenuBar(menuBar);
        
        // Refresh dashboard data
        officerDashboardPanel.refreshData();
        
        // Show officer dashboard panel
        cardLayout.show(mainPanel, "OFFICER_DASHBOARD");
    }
    
    /**
     * Log out the current user.
     */
    public void logout() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            showLoginPanel();
        }
    }
    
    /**
     * Set the logged in user.
     * @param user The logged in user
     */
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
    
    /**
     * Get the logged in user.
     * @return The logged in user
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
    
    /**
     * Show the About dialog.
     */
    private void showAboutDialog() {
        // Create a custom about dialog with styling
        JDialog aboutDialog = new JDialog(this, "About Loan Approval System", true);
        aboutDialog.setSize(400, 250);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setResizable(false);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Loan Approval System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Version
        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JTextArea descArea = new JTextArea(
                "A Java Swing application for loan application and approval process. " +
                "This system allows users to apply for loans and officers to review applications."
        );
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setBackground(panel.getBackground());
        descArea.setMargin(new Insets(10, 10, 10, 10));
        
        // OK button
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.addActionListener(e -> aboutDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        
        // Add components to panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(versionLabel, BorderLayout.CENTER);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(descArea, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.add(panel);
        aboutDialog.setVisible(true);
    }
} 