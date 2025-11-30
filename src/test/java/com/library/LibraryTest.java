package com.library;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    // ---------------- Admin Tests ----------------
    @Test
    void testAddBook() {
        Library library = new Library(true);
        Admin admin = new Admin("admin", "1234");
        admin.login("admin", "1234");

        Book book = new Book("1984", "George Orwell", "1234567890");
        library.addBook(admin, book);

        List<Book> books = library.getMediaList().stream()
                .filter(m -> m instanceof Book)
                .map(m -> (Book)m)
                .collect(Collectors.toList());

        assertEquals(1, books.size());
        assertEquals("1984", books.get(0).getTitle());
        assertEquals("George Orwell", books.get(0).getAuthor());
        assertEquals("1234567890", books.get(0).getIsbn());
    }

    @Test
    void testAddCD() {
        Library library = new Library(true);
        Admin admin = new Admin("admin", "1234");
        admin.login("admin", "1234");

        CD cd = new CD("Best Hits", "Artist X");
        library.addCD(admin, cd);

        List<CD> cds = library.getMediaList().stream()
                .filter(m -> m instanceof CD)
                .map(m -> (CD)m)
                .collect(Collectors.toList());

        assertEquals(1, cds.size());
        assertEquals("Best Hits", cds.get(0).getTitle());
        assertEquals("Artist X", cds.get(0).getArtist());
    }

    @Test
    void testSearchMedia() {
        Library library = new Library(true);
        Admin admin = new Admin("admin", "1234");
        admin.login("admin", "1234");

        Book book = new Book("1984", "George Orwell", "1234567890");
        CD cd = new CD("Best Hits", "Artist X");

        library.addBook(admin, book);
        library.addCD(admin, cd);

        // بحث عن الكتاب بالعنوان
        List<Media> resultBook = library.searchMedia("1984");
        assertEquals(1, resultBook.size());
        assertEquals(book, resultBook.get(0));

        // بحث عن الكتاب بالمؤلف
        List<Media> resultAuthor = library.searchMedia("George");
        assertEquals(1, resultAuthor.size());
        assertEquals(book, resultAuthor.get(0));

        // بحث عن CD بالفنان
        List<Media> resultArtist = library.searchMedia("Artist X");
        assertEquals(1, resultArtist.size());
        assertEquals(cd, resultArtist.get(0));

        // بحث عن شيء غير موجود
        List<Media> resultNone = library.searchMedia("Unknown");
        assertEquals(0, resultNone.size());
    }

    // ---------------- User Tests ----------------
    @Test
    void testBorrowAndReturnMedia() {
        Library library = new Library(true);
        Admin admin = new Admin("admin", "1234");
        admin.login("admin", "1234");

        User user = new User("Alice");
        library.addUser(user);

        Book book = new Book("1984", "George Orwell", "1234567890");
        CD cd = new CD("Best Hits", "Artist X");
        library.addBook(admin, book);
        library.addCD(admin, cd);

        // استعارة ناجحة
        assertTrue(library.borrowMedia(user, book));
        assertTrue(library.borrowMedia(user, cd));
        assertTrue(book.isBorrowed());
        assertTrue(cd.isBorrowed());

        // إرجاع بدون غرامة
        library.returnMedia(user, book);
        library.returnMedia(user, cd);
        assertFalse(book.isBorrowed());
        assertFalse(cd.isBorrowed());
        assertEquals(0.0, user.getTotalFines());

        // استعارة وإرجاع مع غرامة
        library.borrowMedia(user, book);
        book.setDueDate(LocalDate.now().minusDays(5)); // تأخير 5 أيام
        library.returnMedia(user, book);
        assertEquals(5.0, user.getTotalFines());

        // دفع جزء من الغرامة
        user.payFine(2.0);
        assertEquals(3.0, user.getTotalFines());

        // دفع الباقي
        user.payFine(3.0);
        assertEquals(0.0, user.getTotalFines());
    }

    @Test
    void testBorrowMediaWithFineOrOverdue() {
        Library library = new Library(true);
        Admin admin = new Admin("admin", "1234");
        admin.login("admin", "1234");

        User user = new User("Bob");
        library.addUser(user);

        Book book1 = new Book("Book One", "Author A", "1111");
        CD cd1 = new CD("CD One", "Artist A");
        library.addBook(admin, book1);
        library.addCD(admin, cd1);

        // وجود غرامة يمنع الاستعارة
        user.addFine(10.0);
        assertFalse(library.borrowMedia(user, book1));
        assertFalse(library.borrowMedia(user, cd1));

        // دفع الغرامة يسمح بالاستعارة
        user.payFine(10.0);
        assertTrue(library.borrowMedia(user, book1));
        assertTrue(library.borrowMedia(user, cd1));

        // استعارة وسائط متأخرة غير مسموح بها
        book1.setDueDate(LocalDate.now().minusDays(3));
        assertFalse(library.borrowMedia(user, book1));
    }

    @Test
    void testAddUserAndGetUserByName() {
        Library library = new Library(true);

        User u1 = new User("Charlie");
        library.addUser(u1);

        assertEquals(u1, library.getUserByName("Charlie"));
        assertNull(library.getUserByName("NonExisting"));
    }
}
