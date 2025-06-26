package gui;

import auth.UserAuthManager;
import models.User;

import javax.swing.*;
import java.awt.*;
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
        
        // Username field - increase the width of the label area
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setPreferredSize(new Dimension(80, 20)); // Set fixed width for the label
        formPanel.add(usernameLabel, gbc);
        
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
        
        // Password field - also with fixed width label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setPreferredSize(new Dimension(80, 20)); // Set fixed width for the label
        formPanel.add(passwordLabel, gbc);
        
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
     * Creates the button panel with login button.
     *
     * @return The configured button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(80, 30)); // Set a fixed size for the button
        loginButton.addActionListener(e -> attemptLogin());
        buttonPanel.add(loginButton);
        
        return buttonPanel;
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