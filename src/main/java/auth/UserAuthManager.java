package auth;

import models.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Manages user authentication, including login, password reset, and user data storage.
 */
public class UserAuthManager {
    
    private static final String USER_DATA_FILE = "src/main/resources/UserAccounts.csv";
    private static final String HEADER = "username,passwordHash,salt,employeeNumber,email,role";
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_MINUTES = 15;
    
    private List<User> users;
    private Map<String, Integer> failedLoginAttempts = new HashMap<>();
    private Map<String, LocalDateTime> accountLockouts = new HashMap<>();
    private Map<String, String> passwordResetTokens = new HashMap<>();
    
    private static UserAuthManager instance;
    
    /**
     * Gets the singleton instance of UserAuthManager.
     *
     * @return The UserAuthManager instance
     */
    public static UserAuthManager getInstance() {
        if (instance == null) {
            instance = new UserAuthManager();
        }
        return instance;
    }
    
    /**
     * Private constructor for singleton pattern.
     * Loads user data from file on initialization.
     */
    private UserAuthManager() {
        loadUsers();
        
        // If no users exist, create a default admin user
        if (users.isEmpty()) {
            createDefaultAdmin();
        }
    }
    
    /**
     * Creates a default admin user if no users exist.
     * This ensures there's always at least one way to access the system.
     */
    private void createDefaultAdmin() {
        String username = "admin";
        String password = "admin123";  // Default password (user should change this)
        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hashPassword(password, salt);
        
        User adminUser = new User(username, hash, salt, "ADMIN", "admin@motorph.com", "ADMIN");
        users.add(adminUser);
        saveUsers();
    }
    
    /**
     * Loads user data from the CSV file.
     */
    private void loadUsers() {
        users = new ArrayList<>();
        File file = new File(USER_DATA_FILE);
        
        // If the file doesn't exist, create it with headers
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8.name())) {
                    writer.println(HEADER);
                }
            } catch (IOException e) {
                System.err.println("Error creating user data file: " + e.getMessage());
            }
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip the header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                if (!line.trim().isEmpty()) {
                    String[] data = line.split(",");
                    if (data.length >= 6) {
                        User user = new User(
                                data[0],            // username
                                data[1],            // passwordHash
                                data[2],            // salt
                                data[3],            // employeeNumber
                                data[4],            // email
                                data[5]             // role
                        );
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }
    
    /**
     * Saves user data to the CSV file.
     */
    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new File(USER_DATA_FILE), StandardCharsets.UTF_8.name())) {
            // Write header
            writer.println(HEADER);
            
            // Write user data
            for (User user : users) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getUsername()).append(",")
                  .append(user.getPasswordHash()).append(",")
                  .append(user.getSalt()).append(",")
                  .append(user.getEmployeeNumber()).append(",")
                  .append(user.getEmail()).append(",")
                  .append(user.getRole());
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }
    
    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username to authenticate
     * @param password The password to verify
     * @return The authenticated User object if successful, null otherwise
     */
    public User authenticate(String username, String password) {
        // Check if account is locked
        if (isAccountLocked(username)) {
            return null;
        }
        
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                boolean isAuthenticated = PasswordHasher.verifyPassword(
                        password, user.getPasswordHash(), user.getSalt());
                
                if (isAuthenticated) {
                    // Reset failed attempts on successful login
                    failedLoginAttempts.remove(username);
                    return user;
                } else {
                    // Increment failed attempts
                    incrementFailedAttempts(username);
                    return null;
                }
            }
        }
        
        // Username not found
        return null;
    }
    
    /**
     * Increments the failed login attempts for a user and locks the account if threshold is reached.
     *
     * @param username The username to track
     */
    private void incrementFailedAttempts(String username) {
        int attempts = failedLoginAttempts.getOrDefault(username, 0) + 1;
        failedLoginAttempts.put(username, attempts);
        
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            accountLockouts.put(username, LocalDateTime.now());
            failedLoginAttempts.remove(username); // Reset counter
        }
    }
    
    /**
     * Checks if an account is currently locked due to too many failed attempts.
     *
     * @param username The username to check
     * @return true if the account is locked, false otherwise
     */
    public boolean isAccountLocked(String username) {
        if (!accountLockouts.containsKey(username)) {
            return false;
        }
        
        LocalDateTime lockTime = accountLockouts.get(username);
        LocalDateTime now = LocalDateTime.now();
        
        if (ChronoUnit.MINUTES.between(lockTime, now) >= LOCKOUT_MINUTES) {
            // Lockout period has expired
            accountLockouts.remove(username);
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the remaining lockout time in minutes.
     *
     * @param username The username to check
     * @return The number of minutes remaining in the lockout, or 0 if not locked
     */
    public long getLockoutRemainingMinutes(String username) {
        if (!isAccountLocked(username)) {
            return 0;
        }
        
        LocalDateTime lockTime = accountLockouts.get(username);
        LocalDateTime now = LocalDateTime.now();
        return LOCKOUT_MINUTES - ChronoUnit.MINUTES.between(lockTime, now);
    }
    
    /**
     * Generates a password reset token for a user.
     *
     * @param username The username requesting password reset
     * @return The reset token, or null if user not found
     */
    public String generatePasswordResetToken(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            return null;
        }
        
        // Generate a random token
        String token = UUID.randomUUID().toString();
        passwordResetTokens.put(token, username);
        
        // In a real application, you would send this token to the user's email
        
        return token;
    }
    
    /**
     * Resets a user's password using a valid reset token.
     *
     * @param token The password reset token
     * @param newPassword The new password to set
     * @return true if password was reset successfully, false otherwise
     */
    public boolean resetPassword(String token, String newPassword) {
        String username = passwordResetTokens.get(token);
        if (username == null) {
            return false;
        }
        
        User user = findUserByUsername(username);
        if (user == null) {
            return false;
        }
        
        // Generate new salt and hash for the new password
        String newSalt = PasswordHasher.generateSalt();
        String newHash = PasswordHasher.hashPassword(newPassword, newSalt);
        
        user.setSalt(newSalt);
        user.setPasswordHash(newHash);
        
        // Remove the used token
        passwordResetTokens.remove(token);
        
        // Save changes to file
        saveUsers();
        
        return true;
    }
    
    /**
     * Adds a new user to the system.
     *
     * @param username The username for the new user
     * @param password The plaintext password (will be hashed)
     * @param employeeNumber The employee number to link to
     * @param email The user's email address
     * @param role The user's role (ADMIN, USER, etc.)
     * @return true if user was added successfully, false otherwise
     */
    public boolean addUser(String username, String password, String employeeNumber, String email, String role) {
        // Check if username already exists
        if (findUserByUsername(username) != null) {
            return false;
        }
        
        // Generate salt and hash password
        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hashPassword(password, salt);
        
        User newUser = new User(username, hash, salt, employeeNumber, email, role);
        users.add(newUser);
        saveUsers();
        
        return true;
    }
    
    /**
     * Finds a user by their username.
     *
     * @param username The username to search for
     * @return The User object if found, null otherwise
     */
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Gets all users in the system.
     *
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
