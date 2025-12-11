package com.library;

import java.time.LocalDate;

public abstract class Media {
    protected String title;
    protected boolean borrowed = false;
    protected LocalDate borrowDate;
    protected LocalDate dueDate;
    protected String borrowerEmail;
    protected int totalCopies;
    protected int availableCopies;

    // تعديل الكونستركتور إلى protected
    protected Media(String title) {
        this.title = title;
        this.totalCopies = 1;
        this.availableCopies = 1;
    }

    protected Media(String title, int totalCopies) {
        this.title = title;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getTitle() { return title; }
    public boolean isBorrowed() { return borrowed; }
    public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getBorrowerEmail() { return borrowerEmail; }
    public void setBorrowerEmail(String borrowerEmail) { this.borrowerEmail = borrowerEmail; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public abstract String getType();
    public abstract int getBorrowDays();
    public abstract boolean borrow(String userEmail);
    public abstract void returned();

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public long getOverdueDays() {
        if (!isOverdue()) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    // ==== تعديل عدد النسخ ====
    public void increaseAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            if (availableCopies == totalCopies) {
                borrowed = false;
                borrowerEmail = null;
                borrowDate = null;
                dueDate = null;
            }
        }
    }

    public void decreaseAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
            if (availableCopies == 0) {
                borrowed = true;
            }
        }
    }
}
