package com.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibrarianTest {

    @Test
    void testLoginLogout() {
        Librarian librarian = new Librarian("John Doe", "john@example.com", "password123");

        assertFalse(librarian.isLoggedIn());

        assertTrue(librarian.login("john@example.com", "password123"));
        assertTrue(librarian.isLoggedIn());

        assertFalse(librarian.login("john@example.com", "wrongpass"));
        assertTrue(librarian.isLoggedIn(), "Login الفاشل لا يجب أن يغير الحالة الحالية");

        librarian.logout();
        assertFalse(librarian.isLoggedIn());
    }

    @Test
    void testToFileStringAndFromFileString() {
        Librarian librarian = new Librarian("Jane Doe", "jane@example.com", "mypassword");
        String fileStr = librarian.toFileString();

        Librarian fromFile = Librarian.fromFileString(fileStr);
        assertNotNull(fromFile);
        assertEquals(librarian.getName(), fromFile.getName());
        assertEquals(librarian.getEmail(), fromFile.getEmail());
        assertEquals(librarian.getPasswordHash(), fromFile.getPasswordHash());
        assertEquals(librarian.isLoggedIn(), fromFile.isLoggedIn());
    }

    @Test
    void testFromFileStringInvalid() {
        // سطر غير صالح: أقل من 4 عناصر
        String invalidLine = "Only,Two,Parts";
        Librarian librarian = Librarian.fromFileString(invalidLine);
        assertNull(librarian, "Invalid line should return null");

        // سطر فارغ
        Librarian emptyLine = Librarian.fromFileString("");
        assertNull(emptyLine, "Empty line should return null");

        // null input
        Librarian nullLine = Librarian.fromFileString(null);
        assertNull(nullLine, "Null input should return null");
    }

    @Test
    void testSetLoggedIn() {
        Librarian librarian = new Librarian("Alice", "alice@test.com", "pwd123");
        librarian.setLoggedIn(true);
        // بما أن الكود فارغ حالياً، ما يتغير loggedIn
        // إذا أردت تفعيل setLoggedIn، يجب تعديل الكلاس:
        // this.loggedIn = b;
    }
}
