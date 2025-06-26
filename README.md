# MotorPH Employee Management System

## Overview
The MotorPH Employee Management System is a secure desktop application for managing employee records and payroll information. It provides an intuitive graphical interface for viewing, searching, creating, updating, and deleting employee records, all protected by a secure login system.

## Features
- **Secure Authentication**:
  - User account system with username/password login
  - Password hashing and salting for security
  - Account lockout after multiple failed attempts
  - Password reset functionality
- **Main Menu**: Navigation system with two primary options:
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

### Running the Application
1. Launch the application using the `LoginGUI` class as the entry point
2. Login with the default admin credentials:
   - Username: `admin`
   - Password: `admin123`
3. From the main menu, choose your desired action

### Creating User Accounts
User accounts are stored in `UserAccounts.csv` and can be created in two ways:

1. **For Administrators**:
   - Login with admin credentials
   - Use the main menu to access user management

2. **Using the Utility Class**:
   - Run the `CreateUserAccount` utility class to add users programmatically
   - This is useful for initial setup or batch user creation

## System Requirements
- Java Runtime Environment (JRE) 11 or higher
- Windows, macOS, or Linux operating system
- Minimum 4GB RAM
- Screen resolution of 1280x720 or higher (recommended)

## Security Features
- Passwords are stored using SHA-256 hashing with unique salts for each user
- Salts and hashed passwords are encoded in Base64 format in the UserAccounts.csv file
- Account lockout mechanism triggers after 5 failed login attempts (15-minute lockout period)
- Password reset functionality with secure token generation
- Session tracking for authenticated users throughout the application

## Usage Guide

### Login
1. Enter your username and password
2. Click "Login" or press Enter
3. If you forgot your password, use the "Forgot Password" button

### View Specific Employee
1. Select "View Specific Employee" from the main menu
2. Enter the employee number in the search field
3. Click "Search" or press Enter
4. If found, the employee's details will be displayed

### View and Manage All Employees
1. Select "View All Employees" from the main menu
2. Browse the employee table to view all records
3. Select an employee to view or edit their details
4. Use the action buttons for common tasks

## Data Structure
- Employee data is stored in TSV (Tab-Separated Values) format
- User accounts are stored in CSV format with secure password hashing
- Each user account is linked to an employee number for tracking

## Development
This application is built using:
- Java Swing for the user interface
- Object-oriented design principles
- MVC architecture pattern
- Industry-standard security practices

## Version History
- v1.0: Initial release with basic employee management functionality
- v1.1: Added main menu and improved search functionality
- v1.2: Enhanced UI with consistent button sizing and improved validation
- v2.0: Implemented secure login system with user accounts

## Authors
MotorPH Payroll System Team