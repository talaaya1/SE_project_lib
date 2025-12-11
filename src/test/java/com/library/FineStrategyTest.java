package com.library;
// done
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FineStrategyTest {

    @Test
    void testBookFine() {
        FineStrategy bookFine = new BookFineStrategy();
        assertEquals(0.0, bookFine.calculateFine(0));
        assertEquals(10.0, bookFine.calculateFine(1));
        assertEquals(50.0, bookFine.calculateFine(5));
    }

    @Test
    void testCDFine() {
        FineStrategy cdFine = new CDFineStrategy();
        assertEquals(0.0, cdFine.calculateFine(0));
        assertEquals(20.0, cdFine.calculateFine(1));
        assertEquals(100.0, cdFine.calculateFine(5));
    }
}
