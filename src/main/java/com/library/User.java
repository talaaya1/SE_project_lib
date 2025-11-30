/*
package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private double fineBalance = 0.0;

    // قائمة عامة لكل الوسائط المستعارة (Books + CDs)
    private List<com.library.Media> borrowedMedia = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public double getFineBalance() { return fineBalance; }

    // إضافة غرامة
    public void addFine(double amount) {
        if (amount > 0) this.fineBalance += amount;
    }

    // دفع الغرامة
    public void payFine(double amount) {
        if (amount > 0) {
            this.fineBalance -= amount;
            if (this.fineBalance < 0) this.fineBalance = 0;
        }
    }

    // التحقق إذا يمكن استعارة وسائط
    public boolean canBorrow() {
        return fineBalance == 0.0;
    }

    // استرجاع قائمة الوسائط المستعارة
    public List<com.library.Media> getBorrowedMedia() { return borrowedMedia; }

    // تسجيل استعارة أي وسائط
    public void borrowMediaRecord(com.library.Media m) {
        if (m != null) borrowedMedia.add(m);
    }

    // تسجيل إعادة أي وسائط
    public void returnMediaRecord(com.library.Media m) {
        borrowedMedia.remove(m);
    }

    // تحقق من وجود وسائط متأخرة
    public boolean hasOverdueMedia() {
        LocalDate today = LocalDate.now();
        for (com.library.Media m : borrowedMedia) {
            if (m.getDueDate() != null && m.getDueDate().isBefore(today)) {
                return true;
            }
        }
        return false;
    }

    // إجمالي الغرامات لكل الوسائط المستعارة
    public double getTotalFines() {
        LocalDate today = LocalDate.now();
        double total = 0.0;

        for (Media m : borrowedMedia) {
            if (m.isBorrowed() && m.getBorrowerName().equalsIgnoreCase(this.name)) {
                if (m.getDueDate() != null && today.isAfter(m.getDueDate())) {
                    long daysLate = today.toEpochDay() - m.getDueDate().toEpochDay();
                    total += daysLate * 1.0; // 1 وحدة نقدية لكل يوم تأخير
                }
            }
        }

        return total + fineBalance; // مع أي غرامات موجودة مسبقًا
    }

    // مساعدة: تحقق من وجود أي وسائط مستعارة
    public boolean hasBorrowedMedia() {
        return !borrowedMedia.isEmpty();
    }
}
*/

package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private double fineBalance = 0.0; // كل الغرامات هنا

    // قائمة عامة لكل الوسائط المستعارة (Books + CDs)
    private List<com.library.Media> borrowedMedia = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    // إرجاع الغرامة الحالية بعد أي دفع
    public double getTotalFines() {
        return fineBalance;
    }

    // إضافة غرامة جديدة
    public void addFine(double amount) {
        if (amount > 0) this.fineBalance += amount;
    }

    // دفع الغرامة
    public void payFine(double amount) {
        if (amount > 0) {
            this.fineBalance -= amount;
            if (this.fineBalance < 0) this.fineBalance = 0;
        }
    }

    // التحقق إذا يمكن استعارة وسائط
    public boolean canBorrow() {
        return fineBalance == 0.0 && !hasOverdueMedia();
    }

    // استرجاع قائمة الوسائط المستعارة
    public List<com.library.Media> getBorrowedMedia() { return borrowedMedia; }

    // تسجيل استعارة أي وسائط
    public void borrowMediaRecord(com.library.Media m) {
        if (m != null) borrowedMedia.add(m);
    }

    // تسجيل إعادة أي وسائط
    public void returnMediaRecord(com.library.Media m) {
        borrowedMedia.remove(m);
    }

    // تحقق من وجود وسائط متأخرة
    public boolean hasOverdueMedia() {
        LocalDate today = LocalDate.now();
        for (com.library.Media m : borrowedMedia) {
            if (m.getDueDate() != null && m.getDueDate().isBefore(today)) {
                return true;
            }
        }
        return false;
    }

    // مساعدة: تحقق من وجود أي وسائط مستعارة
    public boolean hasBorrowedMedia() {
        return !borrowedMedia.isEmpty();
    }
}
