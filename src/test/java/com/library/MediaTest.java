package com.library;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MediaTest {

    // ==== Subclass بسيطة للاختبار ====
    static class TestMedia2 extends Media {
        public TestMedia2(String title) { super(title); }
        public TestMedia2(String title, int totalCopies) { super(title, totalCopies); }

        @Override
        public String getType() { return "TestMedia"; }

        @Override
        public int getBorrowDays() { return 7; }

        @Override
        public boolean borrow(String userEmail) {
            if (availableCopies > 0) {
                decreaseAvailableCopies();
                this.borrowerEmail = userEmail;
                this.borrowDate = LocalDate.now();
                this.dueDate = borrowDate.plusDays(getBorrowDays());
                this.borrowed = true;
                return true;
            }
            return false;
        }

        @Override
        public void returned() {
            increaseAvailableCopies();
            if (availableCopies == totalCopies) {
                borrowed = false;
                borrowerEmail = null;
                borrowDate = null;
                dueDate = null;
            }
        }
    }

    // ==== التست ====
    @Test
    void testMediaBasic() {
        TestMedia2 media = new TestMedia2("My Book", 2);

        // التأكد من القيم الابتدائية
        assertEquals("My Book", media.getTitle());
        assertEquals(2, media.getAvailableCopies());
        assertFalse(media.isBorrowed());

        // تجربة الإعارة
        assertTrue(media.borrow("user@test.com"));
        assertEquals(1, media.getAvailableCopies());
        assertTrue(media.isBorrowed());
        assertEquals("user@test.com", media.getBorrowerEmail());

        // تجربة الإرجاع
        media.returned();
        assertEquals(2, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
        assertNull(media.getBorrowerEmail());

        // تجربة التأخير
        media.borrow("user@test.com");
        media.setDueDate(LocalDate.now().minusDays(3));
        assertTrue(media.isOverdue());
        assertEquals(3, media.getOverdueDays());
    }

    // ===== NEW TESTS FOR 100% COVERAGE =====

    @Test
    void testMediaConstructorWithTitleOnly() {
        TestMedia2 media = new TestMedia2("Single Copy Book");

        assertEquals("Single Copy Book", media.getTitle());
        assertEquals(1, media.getTotalCopies());
        assertEquals(1, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
        assertEquals("TestMedia", media.getType());
        assertEquals(7, media.getBorrowDays());
    }

    @Test
    void testMediaConstructorWithCopies() {
        TestMedia2 media = new TestMedia2("Multi Copy Book", 5);

        assertEquals("Multi Copy Book", media.getTitle());
        assertEquals(5, media.getTotalCopies());
        assertEquals(5, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
    }

    @Test
    void testAllSettersAndGetters() {
        TestMedia2 media = new TestMedia2("Test Book", 3);

        // Test setters
        LocalDate borrowDate = LocalDate.of(2024, 1, 15);
        LocalDate dueDate = LocalDate.of(2024, 1, 22);

        media.setBorrowed(true);
        media.setBorrowDate(borrowDate);
        media.setDueDate(dueDate);
        media.setBorrowerEmail("test@email.com");

        // Test getters
        assertTrue(media.isBorrowed());
        assertEquals(borrowDate, media.getBorrowDate());
        assertEquals(dueDate, media.getDueDate());
        assertEquals("test@email.com", media.getBorrowerEmail());
        assertEquals(3, media.getTotalCopies());
        assertEquals(3, media.getAvailableCopies());
    }

    @Test
    void testIncreaseAvailableCopies() {
        TestMedia2 media = new TestMedia2("Test Book", 3);

        // Borrow one copy
        media.borrow("user@test.com");
        assertEquals(2, media.getAvailableCopies());
        assertTrue(media.isBorrowed());

        // Increase available copies manually
        media.increaseAvailableCopies();
        assertEquals(3, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
        assertNull(media.getBorrowerEmail());
        assertNull(media.getBorrowDate());
        assertNull(media.getDueDate());

        // Try to increase when already at max
        media.increaseAvailableCopies();
        assertEquals(3, media.getAvailableCopies()); // Should not increase
    }

    @Test
    void testDecreaseAvailableCopies() {
        TestMedia2 media = new TestMedia2("Test Book", 3);

        assertEquals(3, media.getAvailableCopies());
        assertFalse(media.isBorrowed());

        // Decrease available copies
        media.decreaseAvailableCopies();
        assertEquals(2, media.getAvailableCopies());
        assertFalse(media.isBorrowed()); // Still has copies available

        // Decrease until no copies
        media.decreaseAvailableCopies();
        media.decreaseAvailableCopies();
        assertEquals(0, media.getAvailableCopies());
        assertTrue(media.isBorrowed()); // No copies left

        // Try to decrease when already at 0
        media.decreaseAvailableCopies();
        assertEquals(0, media.getAvailableCopies()); // Should not decrease below 0
    }

    @Test
    void testIncreaseAvailableCopiesWithPartialReturn() throws Exception {
        TestMedia2 media = new TestMedia2("Test Book", 5);

        // Borrow 3 copies using reflection to simulate multiple borrows
        media.borrow("user1@test.com");
        media.borrow("user2@test.com");
        media.borrow("user3@test.com");

        // At this point, borrowed should be true because we have multiple borrowers
        // but our simple implementation sets borrowed = true on any borrow

        assertEquals(2, media.getAvailableCopies());
        assertTrue(media.isBorrowed());

        // Return one copy
        media.increaseAvailableCopies();
        assertEquals(3, media.getAvailableCopies());
        assertTrue(media.isBorrowed()); // Still borrowed because not all copies returned
    }

    @Test
    void testIsOverdue() {
        TestMedia2 media = new TestMedia2("Test Book", 1);

        // Not overdue when not borrowed
        assertFalse(media.isOverdue());

        // Borrow it
        media.borrow("user@test.com");

        // Not overdue when due date is in future
        media.setDueDate(LocalDate.now().plusDays(5));
        assertFalse(media.isOverdue());

        // Overdue when due date is in past
        media.setDueDate(LocalDate.now().minusDays(1));
        assertTrue(media.isOverdue());

        // Exactly today is not overdue
        media.setDueDate(LocalDate.now());
        assertFalse(media.isOverdue());

        // Null due date
        media.setDueDate(null);
        assertFalse(media.isOverdue());
    }

    @Test
    void testGetOverdueDays() {
        TestMedia2 media = new TestMedia2("Test Book", 1);

        // Not borrowed, no overdue days
        assertEquals(0, media.getOverdueDays());

        // Borrowed but not overdue
        media.borrow("user@test.com");
        media.setDueDate(LocalDate.now().plusDays(5));
        assertEquals(0, media.getOverdueDays());

        // Overdue by 3 days
        media.setDueDate(LocalDate.now().minusDays(3));
        assertEquals(3, media.getOverdueDays());

        // Overdue by 1 day
        media.setDueDate(LocalDate.now().minusDays(1));
        assertEquals(1, media.getOverdueDays());

        // Overdue by 30 days
        media.setDueDate(LocalDate.now().minusDays(30));
        assertEquals(30, media.getOverdueDays());
    }

    @Test
    void testBorrowWhenNoCopiesAvailable() {
        TestMedia2 media = new TestMedia2("Test Book", 1);

        // Borrow the only copy
        assertTrue(media.borrow("user1@test.com"));
        assertEquals(0, media.getAvailableCopies());
        assertTrue(media.isBorrowed());

        // Try to borrow when no copies available
        assertFalse(media.borrow("user2@test.com"));
        assertEquals(0, media.getAvailableCopies());

        // Return and try again
        media.returned();
        assertEquals(1, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
        assertTrue(media.borrow("user2@test.com"));
    }

    @Test
    void testMultipleBorrowsAndReturns() {
        TestMedia2 media = new TestMedia2("Test Book", 3);

        // Borrow multiple times
        assertTrue(media.borrow("user1@test.com"));
        assertEquals(2, media.getAvailableCopies());

        assertTrue(media.borrow("user2@test.com"));
        assertEquals(1, media.getAvailableCopies());

        assertTrue(media.borrow("user3@test.com"));
        assertEquals(0, media.getAvailableCopies());
        assertTrue(media.isBorrowed());

        // Try to borrow when no copies
        assertFalse(media.borrow("user4@test.com"));

        // Return one copy
        media.returned();
        assertEquals(1, media.getAvailableCopies());
        assertTrue(media.isBorrowed()); // Still borrowed

        // Return all copies
        media.returned();
        media.returned();
        assertEquals(3, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
        assertNull(media.getBorrowerEmail());
    }

    @Test
    void testSetAndGetMethodsEdgeCases() {
        TestMedia2 media = new TestMedia2("Test Book", 2);

        // Test null values
        media.setBorrowerEmail(null);
        assertNull(media.getBorrowerEmail());

        media.setBorrowDate(null);
        assertNull(media.getBorrowDate());

        media.setDueDate(null);
        assertNull(media.getDueDate());

        // Test empty string
        media.setBorrowerEmail("");
        assertEquals("", media.getBorrowerEmail());

        // Test with special characters
        media.setBorrowerEmail("user.name+test@domain.co.uk");
        assertEquals("user.name+test@domain.co.uk", media.getBorrowerEmail());
    }

    @Test
    void testAbstractMethodsImplementation() {
        TestMedia2 media = new TestMedia2("Test Book", 1);

        assertEquals("TestMedia", media.getType());
        assertEquals(7, media.getBorrowDays());

        // Test borrow and return abstract methods
        assertTrue(media.borrow("test@email.com"));
        assertEquals(0, media.getAvailableCopies());

        media.returned();
        assertEquals(1, media.getAvailableCopies());
    }

    @Test
    void testIncreaseAndDecreaseBoundaryConditions() {
        TestMedia2 media = new TestMedia2("Test Book", 0);

        // Edge case: media with 0 total copies
        assertEquals(0, media.getTotalCopies());
        assertEquals(0, media.getAvailableCopies());

        // Should not decrease below 0
        media.decreaseAvailableCopies();
        assertEquals(0, media.getAvailableCopies());

        // Should not increase above totalCopies
        media.increaseAvailableCopies();
        assertEquals(0, media.getAvailableCopies());
    }

    @Test
    void testBorrowSetsCorrectDates() {
        TestMedia2 media = new TestMedia2("Test Book", 2);

        LocalDate beforeBorrow = LocalDate.now();
        assertTrue(media.borrow("user@test.com"));
        LocalDate afterBorrow = LocalDate.now();

        // Check borrow date is today
        assertTrue(media.getBorrowDate().isEqual(beforeBorrow) ||
                media.getBorrowDate().isEqual(afterBorrow));

        // Check due date is correct
        LocalDate expectedDueDate = media.getBorrowDate().plusDays(media.getBorrowDays());
        assertEquals(expectedDueDate, media.getDueDate());
    }

    @Test
    void testReturnResetsFieldsWhenAllCopiesReturned() {
        TestMedia2 media = new TestMedia2("Test Book", 2);

        // Borrow both copies
        media.borrow("user1@test.com");
        media.borrow("user2@test.com");

        assertEquals(0, media.getAvailableCopies());
        assertTrue(media.isBorrowed());
        assertNotNull(media.getBorrowerEmail()); // Last borrower email

        // Return first copy
        media.returned();
        assertEquals(1, media.getAvailableCopies());
        assertTrue(media.isBorrowed()); // Still borrowed

        // Return second copy (all copies returned)
        media.returned();
        assertEquals(2, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
        assertNull(media.getBorrowerEmail());
        assertNull(media.getBorrowDate());
        assertNull(media.getDueDate());
    }

    @Test
    void testMediaEqualityAndToString() {
        // Note: Media doesn't override equals/hashCode, but we can test other behaviors
        TestMedia2 media1 = new TestMedia2("Book A", 2);
        TestMedia2 media2 = new TestMedia2("Book B", 3);

        // They should be different objects
        assertNotEquals(media1, media2);

        // Test borrow behavior doesn't affect other media
        media1.borrow("user@test.com");
        assertEquals(1, media1.getAvailableCopies());
        assertEquals(3, media2.getAvailableCopies()); // Unchanged
    }

    @Test
    void testZeroOverdueDaysWhenNotOverdue() {
        TestMedia2 media = new TestMedia2("Test Book", 1);

        // Not borrowed
        assertEquals(0, media.getOverdueDays());

        // Borrowed but due in future
        media.borrow("user@test.com");
        media.setDueDate(LocalDate.now().plusDays(10));
        assertEquals(0, media.getOverdueDays());

        // Due today
        media.setDueDate(LocalDate.now());
        assertEquals(0, media.getOverdueDays());
    }

    @Test
    void testIncreaseCopiesResetsBorrowedStatus() throws Exception {
        TestMedia2 media = new TestMedia2("Test Book", 3);

        // Borrow all copies
        media.borrow("user1@test.com");
        media.borrow("user2@test.com");
        media.borrow("user3@test.com");

        assertEquals(0, media.getAvailableCopies());
        assertTrue(media.isBorrowed());

        // Increase copies back to total
        media.increaseAvailableCopies();
        media.increaseAvailableCopies();
        media.increaseAvailableCopies();

        assertEquals(3, media.getAvailableCopies());
        assertFalse(media.isBorrowed());
        assertNull(media.getBorrowerEmail());
        assertNull(media.getBorrowDate());
        assertNull(media.getDueDate());
    }

    @Test
    void testDecreaseCopiesSetsBorrowedStatus() {
        TestMedia2 media = new TestMedia2("Test Book", 3);

        assertFalse(media.isBorrowed());

        // Decrease to 1 copy
        media.decreaseAvailableCopies();
        media.decreaseAvailableCopies();

        assertEquals(1, media.getAvailableCopies());
        assertFalse(media.isBorrowed()); // Still has copies

        // Decrease to 0 copies
        media.decreaseAvailableCopies();

        assertEquals(0, media.getAvailableCopies());
        assertTrue(media.isBorrowed()); // No copies left
    }
}