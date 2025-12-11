package com.library;
// donee
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookFineStrategyTest {

    private final BookFineStrategy fineStrategy = new BookFineStrategy();

    @Test
    void testNoOverdueDays() {
        assertEquals(0.0, fineStrategy.calculateFine(0), "Fine should be 0 for 0 overdue days");
    }

    @Test
    void testNegativeOverdueDays() {
        assertEquals(0.0, fineStrategy.calculateFine(-5), "Fine should be 0 for negative overdue days");
    }

    @Test
    void testOneOverdueDay() {
        assertEquals(10.0, fineStrategy.calculateFine(1), "Fine should be 10 for 1 overdue day");
    }

    @Test
    void testMultipleOverdueDays() {
        assertEquals(50.0, fineStrategy.calculateFine(5), "Fine should be 50 for 5 overdue days");
        assertEquals(100.0, fineStrategy.calculateFine(10), "Fine should be 100 for 10 overdue days");
    }
}
