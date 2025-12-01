package com.library;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class TestOverdue {

    @Test
    public void testOverdue(){
        Book book = new Book("Java","Tala","123");
        book.setBorrowed(true);
        book.setDueDate(LocalDate.now().minusDays(1));
        Overdue detector = new Overdue();

        assertTrue(detector.isOverdue(book),"متأخر");

        book.setDueDate(LocalDate.now().plusDays(1));
        assertFalse(detector.isOverdue(book),"غير متأخر");
    }
}
