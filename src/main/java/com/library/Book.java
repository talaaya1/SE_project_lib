/*
package com.library;

import java.time.LocalDate;

public class Book extends Media {
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        super(title);
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }

    @Override
    public void borrow(String userName) {
        borrowed = true;
        borrowerName = userName;
        dueDate = LocalDate.now().plusDays(28); // كتب 28 يوم
    }

    @Override
    public String getType() { return "Book"; }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") " +
                (borrowed ? "Borrowed by " + borrowerName + ", due: " + dueDate : "Available");
    }
}
*/

package com.library;

import java.time.LocalDate;

public class Book extends Media {
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        super(title);
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }

    @Override
    public void borrow(String userName) {
        borrowed = true;
        borrowerName = userName;
        dueDate = LocalDate.now().plusDays(28); // كتب 28 يوم
    }

    @Override
    public String getType() { return "Book"; }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") " +
                (borrowed ? "Borrowed by " + borrowerName + ", due: " + dueDate : "Available");
    }
}



