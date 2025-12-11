package com.library;

import java.io.*;
import java.util.Properties;

/**
 * تحميل إعدادات البريد الإلكتروني
 */
public class EmailConfig {
    private static Properties properties = new Properties();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream input = new FileInputStream("email_config.properties")) {
            properties.load(input);
            System.out.println("✅ Email configuration loaded");
        } catch (IOException e) {
            System.err.println("❌ Error loading email config: " + e.getMessage());
            System.out.println("💡 Creating default config file...");
            createDefaultConfig();
        }
    }

    private static void createDefaultConfig() {
        try (OutputStream output = new FileOutputStream("email_config.properties")) {
            properties.setProperty("library.email", "YOUR_EMAIL_HERE@gmail.com");
            properties.setProperty("library.app.password", "YOUR_APP_PASSWORD_HERE");
            properties.setProperty("library.name", "Library Management System");
            properties.setProperty("smtp.host", "smtp.gmail.com");
            properties.setProperty("smtp.port", "587");

            properties.store(output, "Email Configuration");
            System.out.println("📁 Created email_config.properties");
            System.out.println("⚠️  Please edit the file with your email details!");
        } catch (IOException e) {
            System.err.println("Error creating config: " + e.getMessage());
        }
    }

    public static String getEmail() {
        return properties.getProperty("library.email");
    }

    public static String getAppPassword() {
        return properties.getProperty("library.app.password");
    }

    public static String getLibraryName() {
        return properties.getProperty("library.name", "Library Management System");
    }

    public static boolean isConfigured() {
        String email = getEmail();
        String password = getAppPassword();
        return email != null && !email.contains("YOUR_EMAIL_HERE") &&
                password != null && !password.contains("YOUR_APP_PASSWORD_HERE");
    }
}