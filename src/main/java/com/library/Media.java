/*
package com.library;

import java.time.LocalDate;

public abstract class Media {
    protected String title;
    protected boolean borrowed = false;
    protected LocalDate dueDate;
    protected String borrowerName;

    public Media(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
    public boolean isBorrowed() { return borrowed; }
    public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public abstract void borrow(String userName); // كل نوع يحدد مدة الاستعارة
    public abstract String getType(); // لتوضيح النوع في العرض
}
*/

package com.library;

import java.time.LocalDate;

public abstract class Media {
    protected String title;
    protected boolean borrowed = false;
    protected LocalDate dueDate;
    protected String borrowerName;

    public Media(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
    public boolean isBorrowed() { return borrowed; }
    public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public abstract void borrow(String userName); // كل نوع يحدد مدة الاستعارة
    public abstract String getType(); // لتوضيح النوع في العرض
}
