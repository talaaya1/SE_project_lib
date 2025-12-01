package com.library;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {

    private final String name;
    private final String email;
    private final ArrayList<Book> borrowBooks;
    private double unpaidFines;




    public User(String name, String email, ArrayList<Book> borrowBooks) {
        this.name = name;
        this.email = email;
        this.borrowBooks = borrowBooks;
    }

    public String getEmail(){
        return email;
    }
    public ArrayList<Book> getBorrowBooks(){
        return borrowBooks;
    }

    public void borrowBook(Book book){
        borrowBooks.add(book);
        book.setBorrowed(true);
        book.setDueDate(LocalDate.now().plusDays(28));
    }

    public double getUnpaidFines(){
        return unpaidFines;
    }
    public void setUnpaidFines(double unpaidFines){
        this.unpaidFines=unpaidFines;
    }

}
