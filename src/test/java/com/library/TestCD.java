package com.library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class TestCD {

    @Test
    public void testCDProperties() {
        LocalDate date = LocalDate.now();
        CD cd = new CD("My CD", false, date);

        assertEquals("My CD", cd.getTitle());
        assertFalse(cd.isBorrowed());
        assertEquals(date, cd.getDueDate());
    }

    @Test
    public void testSetters() {
        CD cd = new CD("Old", false, LocalDate.now());

        cd.setTitle("New");
        assertEquals("New", cd.getTitle());

        cd.setBorrowed(true);
        assertTrue(cd.isBorrowed());

        LocalDate newDate = LocalDate.now().plusDays(7);
        cd.setDueDate(newDate);
        assertEquals(newDate, cd.getDueDate());
    }
}
