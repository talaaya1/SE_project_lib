package com.library;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void BookTest() {
        Book book = new Book("Java Basics", "John Doe", "1234567890", 2);

        // --- قيم ابتدائية ---
        assertEquals("Java Basics", book.getTitle());
        assertEquals("John Doe", book.getAuthorName());
        assertEquals("1234567890", book.getBookIsbn());
        assertEquals(2, book.getAvailableCopies());
        assertEquals("In Stock", book.availabilityStatus()); // تم تعديل القيمة
        assertFalse(book.isBorrowed());

        // --- إعارة نسخة ---
        assertTrue(book.borrow("user1@test.com"));
        assertEquals(1, book.getAvailableCopies());
        assertTrue(book.isBorrowed());
        assertEquals("user1@test.com", book.getBorrowerEmail());

        // --- إعارة النسخة الثانية ---
        assertTrue(book.borrow("user2@test.com"));
        assertEquals(0, book.getAvailableCopies());
        assertTrue(book.isBorrowed());
        assertEquals("user2@test.com", book.getBorrowerEmail());
        assertEquals("Out of Stock", book.availabilityStatus()); // تم تعديل القيمة

        // --- محاولة إعارة عندما لا توجد نسخ ---
        assertFalse(book.borrow("user3@test.com"));

        // --- التأكد من overdue ---
        book.setDueDate(LocalDate.now().minusDays(5));
        assertTrue(book.isOverdue());
        assertEquals(5, book.getOverdueDays());

        // --- إرجاع نسخة ---
        book.returned();
        assertEquals(1, book.getAvailableCopies());
        assertFalse(book.isBorrowed());
        assertNull(book.getBorrowerEmail());
        assertEquals("In Stock", book.availabilityStatus()); // تم تعديل القيمة

        // --- toString يغطي كل الحقول ---
        String str = book.toString();
        assertTrue(str.contains("Java Basics"));
        assertTrue(str.contains("John Doe"));
        assertTrue(str.contains("1234567890"));
    }
}
