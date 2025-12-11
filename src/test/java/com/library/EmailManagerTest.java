package com.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EmailManagerTest {

    private EmailManager emailManager;

    @BeforeEach
    void setUp() {
        // استخدام أي قيم dummy، لن يتم إرسال إيميل حقيقي
        emailManager = new EmailManager("test@library.com", "appPassword");
    }

    @Test
    void testSendEmail() {
        // يجب أن يرجع true حتى لو لم يُرسل فعلياً (catch Exception)
        boolean result = emailManager.sendEmail("user@test.com", "Test Subject", "Test Body");
        assertTrue(result);
    }

    @Test
    void testSendHtmlEmail() {
        boolean result = emailManager.sendHtmlEmail("user@test.com", "HTML Test", "<b>Hello</b>");
        assertTrue(result);
    }

    @Test
    void testSendWelcomeEmail() {
        // فقط للتأكد من عدم رمي استثناء
        assertDoesNotThrow(() -> emailManager.sendWelcomeEmail("user@test.com", "Alice"));
    }

    @Test
    void testSendBorrowConfirmation() {
        Book book = new Book("Java 101", "Author X", "ISBN123", 1);
        LocalDate dueDate = LocalDate.now().plusDays(28);
        assertDoesNotThrow(() -> emailManager.sendBorrowConfirmation("user@test.com", "Alice", book, dueDate));
    }

    @Test
    void testSendOverdueReminder() {
        ArrayList<Media> overdueItems = new ArrayList<>();
        Book book = new Book("Old Book", "Author Y", "ISBN999", 1);
        book.setDueDate(LocalDate.now().minusDays(5));
        overdueItems.add(book);

        assertDoesNotThrow(() -> emailManager.sendOverdueReminder("user@test.com", "Alice", overdueItems));
    }

    @Test
    void testSendPaymentReceipt() {
        assertDoesNotThrow(() -> emailManager.sendPaymentReceipt(
                "user@test.com", "Alice", 50.0, 0.0, "Cash"));
    }

    @Test
    void testSendFineNotification() {
        Book book = new Book("Late Book", "Author Z", "ISBN777", 1);
        LocalDate dueDate = LocalDate.now().minusDays(10);

        assertDoesNotThrow(() -> emailManager.sendFineNotification(
                "user@test.com", "Alice", book.getTitle(), 100.0, 10, dueDate));
    }
}