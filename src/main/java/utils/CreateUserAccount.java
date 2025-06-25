package utils;

import auth.UserAuthManager;

public class CreateUserAccount {
    public static void main(String[] args) {
        UserAuthManager authManager = UserAuthManager.getInstance();
        
        // Parameters: username, password, employeeNumber, email, role
        boolean success = authManager.addUser(
            "garcia",           // username (typically first or last name)
            "password123",      // initial password (user can reset later)
            "10001",            // employee number
            "garcia@motorph.com", // email
            "USER"              // role
        );
        
        if (success) {
            System.out.println("User account created successfully!");
        } else {
            System.out.println("Failed to create user account. Username might already exist.");
        }
    }
}