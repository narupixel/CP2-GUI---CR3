package gui;

import javax.swing.*;
import java.awt.*;

/**
 * MainMenuGUI.java
 * 
 * This class implements the main menu for the MotorPH Employee Management System.
 * It provides options to search for a specific employee or view all employees.
 * 
 * @author MotorPH Payroll System Team
 * @version 1.0
 */
public class MainMenuGUI extends JFrame {
    
    private JButton viewSpecificEmployeeButton;
    private JButton viewAllEmployeesButton;
    
    /**
     * Constructor that initializes the main menu GUI with buttons for
     * navigating to different parts of the application.
     */
    public MainMenuGUI() {
        // Set up the main window
        setTitle("MotorPH Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Initialize components
        initializeComponents();
        
        // Configure window properties
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Initializes the components of the main menu.
     */
    private void initializeComponents() {
        // Create a header panel with logo and title
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create main menu panel with buttons
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the header panel with the company logo and application title.
     * 
     * @return JPanel containing the header elements
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("MotorPH Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        return headerPanel;
    }
    
    /**
     * Creates the main menu panel with option buttons.
     * 
     * @return JPanel containing the menu options
     */
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        menuPanel.setBackground(Color.WHITE);
        
        // Create buttons with consistent size
        Dimension buttonSize = new Dimension(300, 50);
        
        viewSpecificEmployeeButton = new JButton("View Specific Employee");
        viewSpecificEmployeeButton.setMaximumSize(buttonSize);
        viewSpecificEmployeeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewSpecificEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewSpecificEmployeeButton.setToolTipText("Search for and view a specific employee's information");
        
        viewAllEmployeesButton = new JButton("View All Employees");
        viewAllEmployeesButton.setMaximumSize(buttonSize);
        viewAllEmployeesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAllEmployeesButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewAllEmployeesButton.setToolTipText("View and manage all employee records");
        
        // Add action listeners
        viewSpecificEmployeeButton.addActionListener(e -> openEmployeeSearch());
        viewAllEmployeesButton.addActionListener(e -> openAllEmployees());
        
        // Add spacing between components
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(viewSpecificEmployeeButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        menuPanel.add(viewAllEmployeesButton);
        menuPanel.add(Box.createVerticalGlue());
        
        return menuPanel;
    }
    
    /**
     * Opens a dialog for employee search, reusing the search functionality
     * from PayrollGUI.
     */
    private void openEmployeeSearch() {
        // Create a simple dialog with a search field
        JDialog searchDialog = new JDialog(this, "Search Employee", true);
        searchDialog.setLayout(new BorderLayout());
        searchDialog.setSize(400, 150);
        searchDialog.setLocationRelativeTo(this);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel searchLabel = new JLabel("Employee Number:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton cancelButton = new JButton("Cancel");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(cancelButton);
        
        // When search button is clicked, create a PayrollGUI instance and use its search function
        searchButton.addActionListener(e -> {
            String employeeNumber = searchField.getText().trim();
            if (!employeeNumber.isEmpty()) {
                searchDialog.dispose();
                // Create a temporary PayrollGUI (not visible) to use its search functionality
                PayrollGUI payrollGUI = new PayrollGUI();
                // Set the search field with the employee number
                payrollGUI.searchAndViewEmployee(employeeNumber);
            } else {
                JOptionPane.showMessageDialog(searchDialog, 
                    "Please enter an employee number.", 
                    "Input Required", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> searchDialog.dispose());
        
        // Allow Enter key to trigger search
        searchField.addActionListener(e -> searchButton.doClick());
        
        searchDialog.add(searchPanel, BorderLayout.CENTER);
        searchDialog.setVisible(true);
    }
    
    /**
     * Opens the all employees window showing the full employee table.
     */
    private void openAllEmployees() {
        PayrollGUI allEmployeesGUI = new PayrollGUI();
        allEmployeesGUI.setVisible(true);
    }
    
    /**
     * The main entry point for the application.
     * Creates and displays the main menu GUI.
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuGUI mainMenu = new MainMenuGUI();
            mainMenu.setVisible(true);
        });
    }
}