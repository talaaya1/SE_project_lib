/* package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FakeEmailManager extends EmailManager {

    private final List<String> log = new ArrayList<>();

    public FakeEmailManager() {
        super("dummy@library.com", "dummyPassword"); // لتجاوز constructor
    }

    public List<String> getLog() {
        return log;
    }

    public void clearLog() {
        log.clear();
    }

    @Override
    public boolean sendEmail(String to, String subject, String body) {
        log.add("Email sent to: " + to + " | Subject: " + subject);
        return true;
    }

    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlBody) {
        log.add("HTML Email sent to: " + to + " | Subject: " + subject);
        return true;
    }

    @Override
    public void sendWelcomeEmail(String userEmail, String name) {
        log.add("Welcome email sent to: " + userEmail);
    }

    @Override
    public void sendBorrowConfirmation(String userEmail, String name, Media media, LocalDate dueDate) {
        log.add("Borrow confirmation sent to: " + userEmail + " for " + media.getTitle());
    }

    @Override
    public void sendOverdueReminder(String userEmail, String name, List<Media> overdueItems) {
        log.add("Overdue reminder sent to: " + userEmail);
    }

    @Override
    public void sendPaymentReceipt(User user, double amount) {
        log.add("Payment receipt sent to: " + user.getName());
    }

    @Override
    public void sendFineNotification(User user, double fineAmount) {
        log.add("Fine notification sent to: " + user.getName());
    }

    @Override
    public void sendOverdueRemindersToAll(Library library) {
        for (User u : library.getAllUsers()) {
            log.add("Overdue reminder sent to: " + u.getName());
        }
    }
}
*/

