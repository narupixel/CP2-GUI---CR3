package gui;

import dataLoader.LoadEmployeeData;
import models.EmployeeProfile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * EmployeeListGUI.java
 * 
 * This class implements a form that displays employee information in a JTable
 * as specified in the change request. The table shows the exact fields required:
 * Employee Number, Last Name, First Name, SSS Number, PhilHealth Number, TIN,
 * and Pag-IBIG Number for all employees in the system.
 * 
 * Key Features:
 * - Displays all employees in a sortable JTable with the exact required fields
 * - Provides employee detail view functionality
 * - Includes new employee creation capability
 * - Implements real-time data refresh after modifications
 * - Uses proper table formatting for professional appearance
 * 
 * Required Fields (as per change request):
 * 1. Employee Number
 * 2. Last Name
 * 3. First Name
 * 4. SSS Number
 * 5. PhilHealth Number
 * 6. TIN
 * 7. Pag-IBIG Number
 * 
 * @author Payroll System Team
 * @version 1.0
 */
public class EmployeeListGUI extends JFrame {
    
    /**
     * Table components for displaying employee data in the required format.
     */
    private JTable employeeTable;           // Main table displaying employee information
    private DefaultTableModel tableModel;  // Table model for managing employee data
    private List<EmployeeProfile> employees; // List of all employees loaded from the system
    
    /**
     * Control buttons for user interaction with the employee data.
     */
    private JButton viewEmployeeButton;     // Button to view detailed employee information
    private JButton newEmployeeButton;      // Button to create a new employee record
    private JButton refreshButton;          // Button to refresh the employee list
    
    /**
     * File path to the employee data source for loading and saving operations.
     */
    private final String employeeFile = "src/main/resources/Employee Details.tsv";

    /**
     * Constructor that initializes the Employee List GUI with all required components.
     * This method sets up the main window, creates the JTable with the exact fields
     * specified in the change request, loads employee data, and configures all
     * user interface elements for optimal user experience.
     */
    public EmployeeListGUI() {
        // Configure the main window properties for optimal display
        setTitle("Employee Management - All Employees");
        setSize(900, 600); // Wider to accommodate all required columns
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null); // Center the window on screen
        setLayout(new BorderLayout());
        
        // Create and configure all GUI components
        createTablePanel();
        createButtonPanel();
        
        // Load and display employee data in the table
        loadEmployeeData();
        
        // Set initial UI state for proper user interaction
        updateButtonStates();
    }

    /**
     * Creates the table panel containing the JTable with exactly the fields
     * specified in the change request. The table displays Employee Number,
     * Last Name, First Name, SSS Number, PhilHealth Number, TIN, and 
     * Pag-IBIG Number for all employees in a professional, sortable format.
     */
    private void createTablePanel() {
        // Define column headers exactly as specified in the change request
        String[] columnNames = {
            "Employee Number",   // Field 1: Employee Number
            "Last Name",         // Field 2: Last Name  
            "First Name",        // Field 3: First Name
            "SSS Number",        // Field 4: SSS Number
            "PhilHealth Number", // Field 5: PhilHealth Number
            "TIN",               // Field 6: TIN
            "Pag-IBIG Number"    // Field 7: Pag-IBIG Number
        };
        
        // Create table model with the specified columns and no initial data
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only for data integrity
            }
        };
        
        // Create the JTable with the configured model
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(25); // Comfortable row height for readability
        employeeTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        
        // Enable table sorting for user convenience
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);
        
        // Configure column widths for optimal display of each field
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Employee Number
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Last Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(120); // First Name
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(120); // SSS Number
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(130); // PhilHealth Number
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(120); // TIN
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(130); // Pag-IBIG Number
        
        // Add selection listener to enable/disable buttons based on selection
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // Create scroll pane for the table to handle large employee lists
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employee List - All Employees"));
        
        // Add the table panel to the center of the main window
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the button panel containing action buttons for employee management.
     * The panel includes buttons for viewing employee details, creating new employees,
     * and refreshing the employee list to reflect any external changes.
     */
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create View Employee button for accessing detailed employee information
        viewEmployeeButton = new JButton("View Employee");
        viewEmployeeButton.setPreferredSize(new Dimension(130, 30));
        viewEmployeeButton.setToolTipText("View detailed information for the selected employee");
        viewEmployeeButton.addActionListener(e -> viewSelectedEmployee());
        
        // Create New Employee button for adding new employees to the system
        newEmployeeButton = new JButton("New Employee");
        newEmployeeButton.setPreferredSize(new Dimension(130, 30));
        newEmployeeButton.setToolTipText("Add a new employee to the system");
        newEmployeeButton.addActionListener(e -> createNewEmployee());
        
        // Create Refresh button for updating the employee list
        refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.setToolTipText("Refresh the employee list to show recent changes");
        refreshButton.addActionListener(e -> refreshEmployeeList());
        
        // Add all buttons to the button panel
        buttonPanel.add(viewEmployeeButton);
        buttonPanel.add(newEmployeeButton);
        buttonPanel.add(refreshButton);
        
        // Add the button panel to the bottom of the main window
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Loads employee data from the TSV file and populates the JTable with
     * exactly the fields specified in the change request. This method reads
     * all employee records and displays only the required fields in the table.
     */
    private void loadEmployeeData() {
        try {
            // Load all employee records from the data file
            employees = LoadEmployeeData.loadFromFile(employeeFile);
            
            // Clear existing table data before loading new data
            tableModel.setRowCount(0);
            
            // Populate table with employee data showing only the required fields
            for (EmployeeProfile employee : employees) {
                Object[] rowData = {
                    employee.getEmployeeNumber(),    // Field 1: Employee Number
                    employee.getLastName(),          // Field 2: Last Name
                    employee.getFirstName(),         // Field 3: First Name
                    employee.getSssNumber(),         // Field 4: SSS Number
                    employee.getPhilhealthNumber(),  // Field 5: PhilHealth Number
                    employee.getTinNumber(),         // Field 6: TIN
                    employee.getPagibigNumber()      // Field 7: Pag-IBIG Number
                };
                tableModel.addRow(rowData);
            }
            
            // Update the window title to show the current employee count
            setTitle("Employee Management - All Employees (" + employees.size() + " employees)");
            
        } catch (Exception e) {
            // Handle any errors during data loading
            JOptionPane.showMessageDialog(this,
                "Error loading employee data: " + e.getMessage(),
                "Data Loading Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens the detailed view for the currently selected employee.
     * This method retrieves the selected employee from the table and
     * opens the EmployeeDetailGUI to display comprehensive information.
     */
    private void viewSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Convert view row to model row in case of sorting
            int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
            EmployeeProfile selectedEmployee = employees.get(modelRow);
            
            // Open the employee detail window with both required parameters
            SwingUtilities.invokeLater(() -> {
                EmployeeDetailGUI detailGUI = new EmployeeDetailGUI(selectedEmployee, this);
                detailGUI.setVisible(true);
            });
        }
    }

    /**
     * Opens the new employee creation dialog.
     * This method launches the NewEmployeeGUI to allow users to add
     * new employees to the system with all required information.
     */
    private void createNewEmployee() {
        SwingUtilities.invokeLater(() -> {
            NewEmployeeGUI newEmployeeGUI = new NewEmployeeGUI(this);
            newEmployeeGUI.setVisible(true);
        });
    }

    /**
     * Refreshes the employee list by reloading data from the file.
     * This method is called after employee modifications to ensure
     * the table displays the most current employee information.
     */
    public void refreshEmployeeList() {
        loadEmployeeData();
        updateButtonStates();
        
        // Provide user feedback that the refresh was completed
        JOptionPane.showMessageDialog(this,
            "Employee list refreshed successfully.",
            "Refresh Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the enabled state of buttons based on current table selection.
     * This method ensures that the View Employee button is only enabled
     * when an employee is selected in the table.
     */
    private void updateButtonStates() {
        boolean hasSelection = employeeTable.getSelectedRow() >= 0;
        viewEmployeeButton.setEnabled(hasSelection);
        // New Employee and Refresh buttons are always enabled
    }

    /**
     * Gets the currently selected employee from the table.
     * This method is used by other components that need access to
     * the selected employee information.
     * 
     * @return The selected EmployeeProfile, or null if no selection
     */
    public EmployeeProfile getSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
            return employees.get(modelRow);
        }
        return null;
    }
}

