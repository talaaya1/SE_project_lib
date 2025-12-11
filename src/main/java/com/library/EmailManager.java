package com.library;

import java.time.LocalDate;
import java.util.List;

public class
EmailManager {
    private final EmailService emailService;

    public EmailManager(String email, String appPassword) {
        this.emailService = new EmailService(email, appPassword);
    }



    // ====================== method الأساسية الناقصة ======================
    /**
     * إرسال بريد نصي عادي
     */
    public boolean sendEmail(String toEmail, String subject, String body) {
        emailService.sendEmail(toEmail, subject, body);
        return true;

    }

    /**
     * إرسال بريد HTML
     */
    public boolean sendHtmlEmail(String toEmail, String subject, String htmlBody) {

        emailService.sendHtmlEmail(toEmail, subject, htmlBody);
        return true;

    }

    // باقي الـmethods الموجودة عندك...
    public void sendWelcomeEmail(String toEmail, String userName) {
        String subject = "🎉 Welcome to Library System, " + userName + "!";
        String body =
                "Dear " + userName + ",\n\n" +
                        "Welcome to Library Management System!\n\n" +
                        "Your account has been created successfully.\n" +
                        "Account Details:\n" +
                        "- Username: " + userName + "\n" +
                        "- Email: " + toEmail + "\n" +
                        "- Registration Date: " + LocalDate.now() + "\n\n" +
                        "You can now:\n" +
                        "📚 Borrow books (28 days, 10 NIS/day fine)\n" +
                        "💿 Borrow CDs (7 days, 20 NIS/day fine)\n" +
                        "🔍 Search the library catalog\n" +
                        "💰 Pay fines online\n\n" +
                        "Need help? Contact library@support.com\n\n" +
                        "Best regards,\n" +
                        "Library Management Team\n" +
                        "-----------------------------\n" +
                        "This is an automated message.";

        sendEmail(toEmail, subject, body);
    }

    public void sendBorrowConfirmation(String toEmail, String userName,
                                       Media media, LocalDate dueDate) {
        String itemType = media instanceof Book ? "Book" : "CD";
        int borrowDays = media.getBorrowDays();
        double fineRate = media instanceof Book ? 10.0 : 20.0;

        String subject = "📚 Borrowing Confirmation: " + media.getTitle();
        String body =
                "Dear " + userName + ",\n\n" +
                        "Your borrowing request has been processed.\n\n" +
                        "📋 BORROWING DETAILS:\n" +
                        "Item: " + media.getTitle() + "\n" +
                        "Type: " + itemType + "\n" +
                        "Borrow Date: " + LocalDate.now() + "\n" +
                        "Due Date: " + dueDate + "\n" +
                        "Borrow Period: " + borrowDays + " days\n\n" +
                        "⚠️  FINE INFORMATION:\n" +
                        "Daily Fine Rate: " + fineRate + " NIS per day\n" +
                        "Fine Starts: After " + dueDate + "\n\n" +
                        "💡 TIPS:\n" +
                        "- Return before due date to avoid fines\n" +
                        "- You can view your borrowed items in your account\n" +
                        "- Contact us for any issues\n\n" +
                        "Best regards,\n" +
                        "Library Management Team";

        sendEmail(toEmail, subject, body);
    }

    public void sendOverdueReminder(String userEmail, String userName,
                                    List<Media> overdueItems) {
        if (overdueItems.isEmpty()) {
            System.out.println("⚠️  No overdue items for " + userName);
            return;
        }

        String subject = "📚 Library Overdue Items Reminder";

        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(userName).append(",\n\n");
        message.append("This is a friendly reminder that you have overdue library items.\n\n");
        message.append("Overdue Items:\n");
        message.append("===============\n");

        double totalFines = 0.0;
        for (Media item : overdueItems) {
            long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                    item.getDueDate(), LocalDate.now());
            double fine = item instanceof Book ?
                    overdueDays * 10.0 : overdueDays * 20.0;
            totalFines += fine;

            message.append("• ").append(item.getTitle())
                    .append(" (").append(item.getType()).append(")\n")
                    .append("  Due Date: ").append(item.getDueDate()).append("\n")
                    .append("  Days Overdue: ").append(overdueDays).append("\n")
                    .append("  Fine: ").append(fine).append(" NIS\n\n");
        }

        message.append("Total Fine: ").append(totalFines).append(" NIS\n\n");
        message.append("Please return these items as soon as possible to avoid additional fines.\n");
        message.append("You can pay your fines at the library front desk.\n\n");
        message.append("Best regards,\nLibrary Management System");

        boolean sent = sendEmail(userEmail, subject, message.toString());


        System.out.println("📧 Overdue reminder sent to: " + userName);

    }





    public void sendPaymentReceipt(String toEmail, String userName,
                                   double amountPaid, double remainingBalance,
                                   String paymentMethod) {
        String subject = "✅ Payment Received: " + amountPaid + " NIS";
        String body =
                "Dear " + userName + ",\n\n" +
                        "Thank you for your payment!\n\n" +
                        "💰 PAYMENT RECEIPT:\n" +
                        "Payment Date: " + LocalDate.now() + "\n" +
                        "Amount Paid: " + amountPaid + " NIS\n" +
                        "Payment Method: " + paymentMethod + "\n" +
                        "Remaining Balance: " + remainingBalance + " NIS\n" +
                        "Receipt ID: RCP-" + System.currentTimeMillis() % 100000 + "\n\n" +
                        "📝 Next Steps:\n" +
                        "- Keep this email as your receipt\n" +
                        "- Your borrowing privileges are restored\n" +
                        "- Visit the library for any assistance\n\n" +
                        "Best regards,\n" +
                        "Library Management Team";

        boolean sent = sendEmail(toEmail, subject, body);
        if (sent) {
            System.out.println("✅ Payment receipt sent to: " + userName);
        }
    }

    public void sendFineNotification(String toEmail, String userName,
                                     String itemTitle, double fineAmount,
                                     long overdueDays, LocalDate dueDate) {
        String subject = "⚠️ Overdue Fine: " + fineAmount + " NIS for " + itemTitle;

        String htmlBody =
                "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd;'>" +
                        "<h2 style='color: #d9534f;'>⚠️ Overdue Item Fine</h2>" +
                        "<p>Dear <strong>" + userName + "</strong>,</p>" +
                        "<p>You have been charged a fine for an overdue item.</p>" +
                        "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                        "<h3>📋 Fine Details:</h3>" +
                        "<table style='width: 100%;'>" +
                        "<tr><td><strong>Item:</strong></td><td>" + itemTitle + "</td></tr>" +
                        "<tr><td><strong>Due Date:</strong></td><td>" + dueDate + "</td></tr>" +
                        "<tr><td><strong>Overdue Days:</strong></td><td>" + overdueDays + " days</td></tr>" +
                        "<tr><td><strong>Fine Amount:</strong></td><td><span style='color: red; font-weight: bold;'>" +
                        fineAmount + " NIS</span></td></tr>" +
                        "</table>" +
                        "</div>" +
                        "<p>Please pay your fine to continue borrowing items.</p>" +
                        "<p>You can pay through the library system or visit the library.</p>" +
                        "<hr>" +
                        "<p style='color: #666; font-size: 12px;'>" +
                        "This is an automated message from Library Management System.<br>" +
                        "Please do not reply to this email." +
                        "</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        boolean sent = sendHtmlEmail(toEmail, subject, htmlBody);

        System.out.println("✅ Fine notification sent to: " + userName);

    }

}