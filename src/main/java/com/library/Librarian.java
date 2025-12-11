package com.library;
import java.util.logging.Logger;

public class Librarian {
    private static final Logger LOGGER = Logger.getLogger(Librarian.class.getName());

    private String name;
    private String email;
    private String passwordHash;
    private boolean loggedIn;

    // Constructor للباسوورد العادي (يتم تشفيره)
    public Librarian(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.passwordHash = PasswordHasher.hashPassword(password);
        this.loggedIn = false;
    }

    // Constructor للقراءة من الملف (يأخذ هاش جاهز)
    public Librarian(String name, String email, String passwordHash, boolean loggedIn) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.loggedIn = loggedIn;
    }

    public boolean login(String email, String password) {
        if (this.email.equals(email) && PasswordHasher.verifyPassword(password, this.passwordHash)) {
            this.loggedIn = true;  // ← هون بنعدل loggedIn لـtrue
            return true;
        }
        return false;
    }

    public void logout() {
        this.loggedIn = false;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isLoggedIn() { return loggedIn; }
    public String getPasswordHash() { return passwordHash; }

    public String toFileString() {
        return name + "," + email + "," + passwordHash + "," + loggedIn;
    }

    public static Librarian fromFileString(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String name = parts[0];
                String email = parts[1];
                String passwordHash = parts[2];
                boolean loggedIn = Boolean.parseBoolean(parts[3]);

                return new Librarian(name, email, passwordHash, loggedIn);
            }
        } catch (Exception e) {
            LOGGER.severe("Error parsing librarian: " + e.getMessage());
        }
        return null;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }


}