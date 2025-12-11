package com.library;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {

    private static final Logger logger = Logger.getLogger(User.class.getName());

    private String name;
    private String email;
    private String passwordHash;
    private List<Media> borrowedMedia;
    private double totalFines;

    // ====================== Constructors ======================
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.passwordHash = PasswordHasher.hashPassword(password);
        this.borrowedMedia = new ArrayList<>();
        this.totalFines = 0.0;
    }

    private User(String name, String email, String passwordHash, double fines) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.borrowedMedia = new ArrayList<>();
        this.totalFines = fines;
    }

    // ====================== Password Methods ======================
    public boolean verifyPassword(String password) {
        return PasswordHasher.verifyPassword(password, this.passwordHash);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            this.passwordHash = PasswordHasher.hashPassword(newPassword);
            logger.info("Password changed successfully for user: " + email);
            return true;
        }
        logger.warning("Failed password change attempt for user: " + email);
        return false;
    }

    // ====================== Getters ======================
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Media> getBorrowedMedia() {
        return new ArrayList<>(borrowedMedia);
    }

    public double getTotalFines() {
        return totalFines;
    }

    // ====================== Fine Management ======================
    public void setTotalFines(double fines) {
        this.totalFines = fines;
        logger.info("Total fines updated for " + email + ": " + fines + " NIS");
    }

    public void addFine(double amount) {
        this.totalFines += amount;
        logger.info("Added fine for " + email + ": " + amount + " NIS | Total: " + totalFines);
    }

    public boolean payFine(double amount) {
        if (amount > 0 && amount <= totalFines) {
            totalFines -= amount;
            logger.info("Payment made by " + email + ": " + amount + " NIS | Remaining: " + totalFines);
            return true;
        }
        logger.warning("Invalid fine payment attempt by " + email + ": " + amount + " NIS | Total fines: " + totalFines);
        return false;
    }

    // ====================== Borrowing Methods ======================
    public void borrowMediaRecord(Media media) {
        borrowedMedia.add(media);
        logger.info(email + " borrowed: " + media.getTitle());
    }

    public void returnMediaRecord(Media media) {
        if (borrowedMedia.remove(media)) {
            logger.info(email + " returned: " + media.getTitle());
        } else {
            logger.warning(email + " tried to return unborrowed media: " + media.getTitle());
        }
    }

    public boolean canBorrow() {
        boolean result = totalFines <= 0 && borrowedMedia.size() < 5;
        logger.fine(email + " canBorrow check: " + result);
        return result;
    }

    // ====================== File Handling ======================
    public String toFileString() {
        return String.join(",", name, email, passwordHash, String.valueOf(totalFines));
    }

    public static User fromFileString(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                return new User(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error parsing user line: " + line, e);
        }
        return null;
    }


}
