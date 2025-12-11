package com.library;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * خدمة البريد الإلكتروني باستخدام Jakarta Mail وSMTP
 */
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    private final String host = "smtp.gmail.com";
    private final int port = 587;
    private final String username;  // بريدك الإلكتروني
    private final String password;  // App Password

    private final Session session;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
        this.session = createSession();
    }

    // ====================== Logger Helpers ======================
    private void logInfo(String msg) { logger.info(msg); }
    private void logWarning(String msg) { logger.warning(msg); }
    private void logSevere(String msg, Exception e) { logger.log(Level.SEVERE, msg, e); }

    // ====================== إنشاء الجلسة ======================
    private Session createSession() {
        if (username == null || password == null ||
                username.contains("your-email") || password.contains("your-password")) {
            logWarning("Please configure email settings first!");
            return null;
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host);

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    // ====================== إرسال بريد نصي ======================
    public boolean sendEmail(String toEmail, String subject, String body) {
        if (session == null) return false;
        return sendMessage(toEmail, subject, body, false);
    }

    // ====================== إرسال بريد HTML ======================
    public boolean sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        if (session == null) return false;
        return sendMessage(toEmail, subject, htmlBody, true);
    }

    // ====================== إرسال البريد ======================
    private boolean sendMessage(String toEmail, String subject, String content, boolean isHtml) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            if (isHtml) message.setContent(content, "text/html; charset=utf-8");
            else message.setText(content);

            Transport.send(message);
            logInfo("✅ Email sent successfully to: " + toEmail);
            logEmail(toEmail, subject, true, null);
            return true;
        } catch (MessagingException e) {
            logSevere("❌ Failed to send email to: " + toEmail, e);
            logEmail(toEmail, subject, false, e.getMessage());
            return false;
        }
    }

    // ====================== تسجيل محاولات الإرسال ======================
    private void logEmail(String toEmail, String subject, boolean success, String error) {
        try (java.io.FileWriter fw = new java.io.FileWriter("email_log.txt", true)) {
            String status = success ? "SUCCESS" : "FAILED: " + error;
            fw.write(java.time.LocalDateTime.now() + " | " +
                    toEmail + " | " + subject + " | " + status + "\n");
        } catch (java.io.IOException e) {
            logSevere("Error logging email", e);
        }
    }
}
