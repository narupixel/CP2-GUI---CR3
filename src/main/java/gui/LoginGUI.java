package gui;

import auth.UserAuthManager;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Login interface for the MotorPH Employee Management System.
 * Provides authentication functionality before allowing access to the system.
 */
public class LoginGUI extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JLabel statusLabel;
    
    private final UserAuthManager authManager;
    
    /**
     * Constructor that initializes the login interface.
     */
    public LoginGUI() {
        authManager = UserAuthManager.getInstance();
        
        // Configure window properties
        setTitle("MotorPH Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Initialize components
        initializeComponents();
        
        // Set size and center on screen
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    
    /**
     * Initializes the components of the login interface.
     */
    private void initializeComponents() {
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header/logo panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Set initial focus to username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
    
    /**
     * Creates the header panel with logo and title.
     *
     * @return The configured header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("MotorPH Employee System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Creates the form panel with input fields.
     *
     * @return The configured form panel
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);
        formPanel.add(new JLabel("Username:"), gbc);
        
        usernameField = new JTextField(15);
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocusInWindow();
                }
            }
        });
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(usernameField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Password:"), gbc);
        
        passwordField = new JPasswordField(15);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        });
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        
        // Status label for displaying errors/messages
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 5, 0);
        formPanel.add(statusLabel, gbc);
        
        return formPanel;
    }
    
    /**
     * Creates the button panel with login and forgot password buttons.
     *
     * @return The configured button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(this::handleForgotPassword);
        buttonPanel.add(forgotPasswordButton);
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> attemptLogin());
        buttonPanel.add(loginButton);
        
        return buttonPanel;
    }
    
    /**
     * Handles the forgot password action.
     *
     * @param e The action event
     */
    private void handleForgotPassword(ActionEvent e) {
        String username = JOptionPane.showInputDialog(this,
                "Enter your username to reset your password:",
                "Password Reset",
                JOptionPane.QUESTION_MESSAGE);
        
        if (username == null || username.trim().isEmpty()) {
            return;
        }
        
        User user = authManager.findUserByUsername(username.trim());
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Username not found. Please try again.",
                    "Reset Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String token = authManager.generatePasswordResetToken(username);
        
        // In a real application, you would send this token via email
        // For this demo, we'll just show it directly
        JPasswordField newPassword = new JPasswordField();
        JPasswordField confirmPassword = new JPasswordField();
        
        JPanel resetPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        resetPanel.add(new JLabel("A password reset token has been generated."));
        resetPanel.add(new JLabel("Enter your new password:"));
        resetPanel.add(newPassword);
        resetPanel.add(new JLabel("Confirm new password:"));
        resetPanel.add(confirmPassword);
        
        int result = JOptionPane.showConfirmDialog(this, resetPanel, 
                "Password Reset", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String newPasswordText = new String(newPassword.getPassword());
            String confirmPasswordText = new String(confirmPassword.getPassword());
            
            if (newPasswordText.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "Password must be at least 6 characters.",
                        "Password Too Short",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!newPasswordText.equals(confirmPasswordText)) {
                JOptionPane.showMessageDialog(this,
                        "Passwords do not match.",
                        "Password Mismatch",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean success = authManager.resetPassword(token, newPasswordText);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Password has been reset successfully.",
                        "Password Reset",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to reset password. Please try again.",
                        "Reset Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Attempts to login with the provided credentials.
     */
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password are required");
            return;
        }
        
        if (authManager.isAccountLocked(username)) {
            long minutes = authManager.getLockoutRemainingMinutes(username);
            statusLabel.setText("Account is locked. Try again in " + minutes + " minutes");
            return;
        }
        
        User authenticatedUser = authManager.authenticate(username, password);
        
        if (authenticatedUser != null) {
            // Authentication successful
            JOptionPane.showMessageDialog(this,
                    "Login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Open main menu and pass authenticated user for session tracking
            openMainMenu(authenticatedUser);
        } else {
            // Authentication failed
            statusLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
    }
    
    /**
     * Opens the main menu after successful authentication.
     *
     * @param user The authenticated user
     */
    private void openMainMenu(User user) {
        SwingUtilities.invokeLater(() -> {
            // Create and show the main menu, passing the authenticated user
            MainMenuGUI mainMenu = new MainMenuGUI(user);
            mainMenu.setVisible(true);
            
            // Close the login window
            setVisible(false);
            dispose();
        });
    }
    
    /**
     * The main entry point for the application.
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        try {
            // Set the look and feel to the system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}