package auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for secure password hashing and verification.
 * Uses SHA-256 with salting for secure password storage.
 */
public class PasswordHasher {
    
    /**
     * Generates a random salt for password hashing.
     *
     * @return A Base64-encoded string representation of the salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hashes a password using SHA-256 with the provided salt.
     *
     * @param password The plaintext password to hash
     * @param salt The salt to use in the hashing process
     * @return A Base64-encoded string representation of the hashed password
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verifies if a provided password matches a stored hash.
     *
     * @param password The password to verify
     * @param storedHash The stored hash to compare against
     * @param storedSalt The salt used in the original hash
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash, String storedSalt) {
        String newHash = hashPassword(password, storedSalt);
        return newHash.equals(storedHash);
    }
}