package com.library;

import java.time.LocalDate;

public class Overdue {

    public boolean isOverdue(Book book){
        if(!book.isBorrowed() || book.getDueDate()==null){
            return false;  // يعني مش متأخر
        }

        LocalDate today = LocalDate.now();
        return today.isAfter(book.getDueDate()); // متأخر
    }
}
