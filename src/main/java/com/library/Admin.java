package com.library;
import java.util.logging.Logger;
//done
//
public class Admin {
    private static final Logger LOGGER = Logger.getLogger(Admin.class.getName());

    private String username;
    private String email;
    private String passwordHash;
    private boolean loggedIn;

    public Admin(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.passwordHash = PasswordHasher.hashPassword(password);
        this.loggedIn = false;
    }

    public Admin(String username, String email, String passwordHash, boolean loggedIn) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.loggedIn = loggedIn;
    }

    public boolean login(String email, String password) {
        if (this.email.equals(email) && PasswordHasher.verifyPassword(password, this.passwordHash)) {
            this.loggedIn = true;
            return true;
        }
        return false;
    }

    public void logout() {
        this.loggedIn = false;
    }

    public String getUsername() { return username; }

    public String getEmail() { return email; }
    public boolean isLoggedIn() { return loggedIn; }

    public String toFileString() {
        return username + "," + email + "," + passwordHash + "," + loggedIn;
    }

    public static Admin fromFileString(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String username = parts[0];
                String email = parts[1];
                String passwordHash = parts[2];
                boolean loggedIn = Boolean.parseBoolean(parts[3]);

                return new Admin(username, email, passwordHash, loggedIn);
            }
        } catch (Exception e) {
            LOGGER.severe("Error parsing admin: " + e.getMessage());
        }
        return null;
    }
    public String getPassword() {
        return passwordHash;
    }


    public String getName() {
        return username;
    }
}