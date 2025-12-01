package com.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestBook {

    @Test
    void testBookGetters() {
        Book book = new Book("Java Programming", "Author A", "ISBN001");

        // استدعاء كل getters
        assertEquals("Java Programming", book.getTitle());
        assertEquals("Author A", book.getAuthor());
        assertEquals("ISBN001", book.getIsbn());
    }
}
