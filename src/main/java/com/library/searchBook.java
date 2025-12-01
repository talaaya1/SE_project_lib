package com.library;

import java.util.ArrayList;

public class searchBook {
    private final ArrayList<Book>books = new ArrayList<>();

    public void addBook(Book book){
        books.add((book));
    }

    public ArrayList<Book> search(String query){
        ArrayList<Book> results = new ArrayList<>();
        String lowerQ=query.toLowerCase();
            for(Book book : books){
                if(book.getTitle().equalsIgnoreCase(query)||
                book.getAuthor().equalsIgnoreCase(query) || book.getIsbn().equalsIgnoreCase(query)){
                    results.add(book);
                }
        }
        return results;
    }


}
