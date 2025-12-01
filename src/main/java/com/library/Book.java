package com.library;

import java.time.LocalDate;

public class Book {
    private final String title;
    private final String author;
    private final String isbn;
    private boolean borrowed;
    private LocalDate dueDate;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.borrowed=false;
        this.dueDate=null;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getIsbn(){
        return isbn;
    }

    public boolean isBorrowed(){ return borrowed;}
    public void setBorrowed(boolean borrowed){this.borrowed=borrowed;}

    public LocalDate getDueDate(){return dueDate;}
    public void setDueDate(LocalDate dueDate){this.dueDate=dueDate;}

}
