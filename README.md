# MotorPH Employee Management System

## Overview
The MotorPH Employee Management System is a comprehensive desktop application for managing employee records and payroll information. It provides an intuitive graphical interface for viewing, searching, creating, updating, and deleting employee records.

## Features
- **Main Menu**: Simple navigation with two primary options:
  - View Specific Employee
  - View All Employees
- **Employee Search**: Quickly find an employee by their employee number
- **Employee Records Management**: View a complete list of all employees
- **Employee Information Editing**: Update employee details such as:
  - Personal information
  - Government ID numbers (SSS, PhilHealth, TIN, Pag-IBIG)
- **Real-time Validation**: Input validation and immediate feedback for form fields
- **Responsive UI**: Clean, professional interface with proper spacing and layout

## Getting Started
1. Launch the application using the `MainMenuGUI` class as the entry point
2. From the main menu, choose one of the following options:
   - **View Specific Employee**: Search for an employee by their employee number
   - **View All Employees**: See the complete employee table with editing capabilities

## System Requirements
- Java Runtime Environment (JRE) 11 or higher
- Windows, macOS, or Linux operating system
- Minimum 4GB RAM
- Screen resolution of 1280x720 or higher (recommended)

## Usage Guide

### View Specific Employee
1. Click "View Specific Employee" from the main menu
2. Enter the employee number in the search field
3. Click "Search" or press Enter
4. If found, the employee's details will be displayed

### View and Manage All Employees
1. Click "View All Employees" from the main menu
2. Browse the employee table to view all records
3. Select an employee to view or edit their details
4. Use the buttons at the top for common actions:
   - Search & View: Find an employee by number
   - View Details: See complete employee information
   - New Employee: Add a new employee record
5. Use the buttons in the editing panel to:
   - Update Employee: Save changes to employee information
   - Delete Employee: Remove an employee record

## Data Format
Employee data is stored in TSV (Tab-Separated Values) format, allowing for easy imports and exports with spreadsheet applications.

## Development
This application is built using:
- Java Swing for the user interface
- Object-oriented design principles
- MVC architecture pattern

## Version History
- v1.0: Initial release with basic employee management functionality
- v1.1: Added main menu and improved search functionality
- v1.2: Enhanced UI with consistent button sizing and improved validation

## Authors
MotorPH Payroll System Team