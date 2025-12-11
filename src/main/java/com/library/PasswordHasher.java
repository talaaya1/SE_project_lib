package com.library;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordHasher {
    private static final Logger logger = Logger.getLogger(PasswordHasher.class.getName());
    private static final int SALT_LENGTH = 16;

    // ===== Public Methods =====
    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);
            return encodeSaltedHash(password, salt);
        } catch (Exception e) {
            logger.log(Level.WARNING, "SHA-256 unavailable, using fallback", e);
            return simpleHash(password);
        }
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        try {
            byte[] combined = Base64.getDecoder().decode(storedHash);
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            byte[] hashedInput = hashWithSalt(inputPassword, salt);
            byte[] storedPassword = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, SALT_LENGTH, storedPassword, 0, storedPassword.length);
            return MessageDigest.isEqual(hashedInput, storedPassword);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error verifying password, using fallback", e);
            return simpleHash(inputPassword).equals(storedHash);
        }
    }

    // ===== Private Helpers =====
    private static byte[] hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        return md.digest(password.getBytes());
    }

    private static String encodeSaltedHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        byte[] hashed = hashWithSalt(password, salt);
        byte[] combined = new byte[salt.length + hashed.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hashed, 0, combined, salt.length, hashed.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static String simpleHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.WARNING, "Fallback MD5 unavailable, returning plain password", e);
            return password;
        }
    }

    // ===== Main Test =====

}
