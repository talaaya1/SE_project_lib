package com.library;

import java.time.LocalDate;

public class CD {
    private String title;
    private boolean isBorrowed;
    private LocalDate dueDate;


    public CD(String title, boolean isBorrowed, LocalDate dueDate) {
        this.title = title;
        this.isBorrowed = isBorrowed;
        this.dueDate=dueDate;
    }

    public void setBorrowed(boolean isBorrowed){
        this.isBorrowed=isBorrowed;
    }
    public boolean isBorrowed(){ return isBorrowed;}

    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle(){return title;}

    public void setDueDate(LocalDate dueDate){
        this.dueDate=dueDate;
    }
    public LocalDate getDueDate(){return dueDate;}


}
