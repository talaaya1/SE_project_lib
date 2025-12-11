package com.library;

import java.time.LocalDate;

public class CD extends Media {
    private String artist;
    private String cdId;

    // ------------ Constructors ------------
    public CD(String title, String artist, String cdId) {
        super(title);
        this.artist = artist;
        this.cdId = cdId;
    }

    public CD(String title, String artist, String cdId, int availableCopies) {
        super(title, availableCopies); // نستخدم availableCopies مباشرة
        this.artist = artist;
        this.cdId = cdId;
    }

    // ------------ Getters ------------
    public String getArtist() { return artist; }
    public String getCdId() { return cdId; }

    // ------------ Media overrides ------------
    @Override
    public String getType() {
        return "CD";
    }

    @Override
    public int getBorrowDays() {
        return 7;
    }

    // ===== Borrow & Return Methods =====
    @Override
    public boolean borrow(String userEmail) {
        if (availableCopies <= 0) return false;
        availableCopies--; // تقليل النسخ عند الاستعارة
        borrowed = availableCopies == 0;
        borrowerEmail = userEmail;
        borrowDate = LocalDate.now();
        dueDate = LocalDate.now().plusDays(getBorrowDays());
        return true;
    }

    @Override
    public void returned() {
        availableCopies++; // زيادة النسخ عند الإرجاع
        borrowed = false;
        borrowerEmail = null;
        borrowDate = null;
        dueDate = null;
    }

    // ------------ toString ------------
    @Override
    public String toString() {
        return "CD: " + title + " by " + artist + " (ID: " + cdId + ") - " +
                availableCopies + " copies " +
                (borrowed ? "(Borrowed by " + borrowerEmail + ", due: " + dueDate + ")" : "(Available)");
    }


}
