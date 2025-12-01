package com.library;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TestUserManager {

    @Test
    void testUnregisterUser_AllCases() {
        UserManager userManager = new UserManager();
        Admin admin = new Admin("admin", "password");

        // -------- Case 1: نجاح العملية، User بدون كتب ----------
        User user1 = new User("Alice", "alice@example.com", new ArrayList<>());
        userManager.addUser(user1);
        assertDoesNotThrow(() -> userManager.unregisterUser(admin, user1));
        assertFalse(userManager.getUsers().contains(user1));

        // -------- Case 2: User بدون كتب ولكن لديه unpaid fines ----------
        User user2 = new User("Bob", "bob@example.com", new ArrayList<>());
        user2.setUnpaidFines(10.0);
        userManager.addUser(user2);
        Exception ex1 = assertThrows(IllegalArgumentException.class, () -> userManager.unregisterUser(admin, user2));
        assertEquals("User has unpaid fines and cannot be unregistered.", ex1.getMessage());

        // -------- Case 3: User لديه كتب مستعارة ----------
        ArrayList<Book> books = new ArrayList<>();
        Book borrowedBook = new Book("Book 1", "Author A", "ISBN001");
        borrowedBook.setBorrowed(true);
        books.add(borrowedBook);
        User user3 = new User("Charlie", "charlie@example.com", books);
        userManager.addUser(user3);
        Exception ex2 = assertThrows(IllegalArgumentException.class, () -> userManager.unregisterUser(admin, user3));
        assertEquals("User has active borrowed books and cannot be unregistered.", ex2.getMessage());

        // -------- Case 4: Admin null ----------
        User user4 = new User("Dave", "dave@example.com", new ArrayList<>());
        userManager.addUser(user4);
        Exception ex3 = assertThrows(IllegalArgumentException.class, () -> userManager.unregisterUser(null, user4));
        assertEquals("Only admins can unregister users.", ex3.getMessage());

        // -------- Case 5: User لديه كتب ولكن none borrowed ----------
        ArrayList<Book> books2 = new ArrayList<>();
        books2.add(new Book("Book 2", "Author B", "ISBN002")); // not borrowed
        User user5 = new User("Eve", "eve@example.com", books2);
        userManager.addUser(user5);
        assertDoesNotThrow(() -> userManager.unregisterUser(admin, user5));
        assertFalse(userManager.getUsers().contains(user5));
    }
}
