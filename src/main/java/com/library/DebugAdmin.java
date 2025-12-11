package com.library;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DebugAdmin {

    // Private constructor
    private DebugAdmin() {
        throw new IllegalStateException("Utility class");
    }
    private static final Logger LOGGER = Logger.getLogger(DebugAdmin.class.getName());


    private static final String ADMIN_FILE = "admins.txt";
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // ❗ استبدال System.out بـ Logger
        LOGGER.info("=== إضافة مدير جديد ===");

        LOGGER.info("أدخل اسم المدير: ");
        String name = scanner.nextLine();

        LOGGER.info("أدخل الإيميل: ");
        String email = scanner.nextLine();

        LOGGER.info("أدخل كلمة المرور: ");
        String password = scanner.nextLine();

        // تشفير
        String hashedPassword = PasswordHasher.hashPassword(password);

        LOGGER.info("=== البيانات المدخلة ===");
        LOGGER.info("الاسم: " + name);
        LOGGER.info("الإيميل: " + email);
        LOGGER.info("كلمة المرور الأصلية: " + password);
        LOGGER.info("كلمة المرور المشفرة: " + hashedPassword);

        // حفظ
        saveAdminToFile(name, email, hashedPassword);

        LOGGER.info("✅ تم حفظ البيانات بنجاح في ملف: " + ADMIN_FILE);

        scanner.close();
    }

    public static void saveAdminToFile(String name, String email, String hashedPassword) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ADMIN_FILE, true))) {

            writer.println(name + "|" + email + "|" + hashedPassword);

            LOGGER.info("تم تخزين البيانات بالتنسيق: "
                    + name + "," + email + "," + hashedPassword);

        } catch (IOException e) {

            // ❗ استبدال System.err و e.printStackTrace بـ logger
            LOGGER.log(Level.SEVERE, "❌ خطأ في حفظ البيانات: " + e.getMessage(), e);
        }
    }

    // private constructor
    static class PasswordHasher {

        private PasswordHasher() {
            throw new IllegalStateException("Utility class");
        }

        public static String hashPassword(String password) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                byte[] array = md.digest(password.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : array) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
                return null;
            }
        }

        public static boolean verifyPassword(String inputPassword, String storedHash) {
            String hashedInput = hashPassword(inputPassword);
            return hashedInput != null && hashedInput.equals(storedHash);
        }
    }
}
