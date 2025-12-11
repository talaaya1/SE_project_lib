package com.library;

import java.time.LocalDate;
//1
public class Book extends Media {

    private String authorName;
    private String bookIsbn;

    // ------------ Constructors ------------
    public Book(String title, String authorName, String bookIsbn) {
        super(title);
        this.authorName = authorName;
        this.bookIsbn = bookIsbn;
    }

    public Book(String title, String authorName, String bookIsbn, int copies) {
        super(title, copies);
        this.authorName = authorName;
        this.bookIsbn = bookIsbn;
    }

    // ------------ Getters ------------
    public String getAuthorName() { return authorName; }
    public String getBookIsbn() { return bookIsbn; }

    public String availabilityStatus() {
        return getAvailableCopies() > 0 ? "In Stock" : "Out of Stock";
    }

    // ------------ Overrides from Media ------------
    @Override
    public String getType() {
        return "Book";
    }

    @Override
    public int getBorrowDays() {
        return 28;
    }

    @Override
    public boolean borrow(String userEmail) {
        if (getAvailableCopies() <= 0) return false;

        decreaseAvailableCopies();
        setBorrower(userEmail);
        setBorrowDate(LocalDate.now());
        setDueDate(LocalDate.now().plusDays(getBorrowDays()));
        return true;
    }

    @Override
    public void returned() {
        increaseAvailableCopies();
        clearBorrowInfo();
    }

    // ------------ Helpers ------------
    private void setBorrower(String email) {
        this.borrowed = true;
        this.borrowerEmail = email;
    }

    private void clearBorrowInfo() {
        this.borrowed = false;
        this.borrowerEmail = null;
        this.borrowDate = null;
        this.dueDate = null;
    }

    // ------------ toString ------------
    @Override
    public String toString() {
        String status = borrowed ? "(Borrowed by " + borrowerEmail + ", due: " + dueDate + ")"
                : "(Available)";
        return String.format("Book: %s by %s (ISBN: %s) - %d copies %s",
                title, authorName, bookIsbn, getAvailableCopies(), status);
    }
}
