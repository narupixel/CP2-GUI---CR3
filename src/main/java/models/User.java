package models;

/**
 * Represents a user account in the system.
 * Contains authentication information and links to employee data.
 */
public class User {
    private String username;
    private String passwordHash;
    private String salt;
    private String employeeNumber;
    private String email;
    private String role;
    
    /**
     * Default constructor for serialization/deserialization.
     */
    public User() {
    }
    
    /**
     * Creates a new user with the specified credentials and information.
     */
    public User(String username, String passwordHash, String salt, String employeeNumber, String email, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.employeeNumber = employeeNumber;
        this.email = email;
        this.role = role;
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getSalt() {
        return salt;
    }
    
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", employeeNumber='" + employeeNumber + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               '}';
    }
}