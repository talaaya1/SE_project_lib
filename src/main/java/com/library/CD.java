/*
package com.library;


import java.time.LocalDate;

public class CD extends Media {
    private String artist;

    public CD(String title, String artist) {
        super(title);
        this.artist = artist;
    }

    public String getArtist() { return artist; }

    @Override
    public void borrow(String userName) {
        borrowed = true;
        borrowerName = userName;
        dueDate = LocalDate.now().plusDays(7); // CDs لمدة 7 أيام
    }

    @Override
    public String getType() { return "CD"; }

    @Override
    public String toString() {
        return title + " by " + artist + " (CD) " +
                (borrowed ? "Borrowed by " + borrowerName + ", due: " + dueDate : "Available");
    }
}
*/

package com.library;

import java.time.LocalDate;

public class CD extends Media {
    private String artist;

    public CD(String title, String artist) {
        super(title);
        this.artist = artist;
    }

    public String getArtist() { return artist; }

    @Override
    public void borrow(String userName) {
        borrowed = true;
        borrowerName = userName;
        dueDate = LocalDate.now().plusDays(7); // CDs لمدة 7 أيام
    }

    @Override
    public String getType() { return "CD"; }

    @Override
    public String toString() {
        return title + " by " + artist + " (CD) " +
                (borrowed ? "Borrowed by " + borrowerName + ", due: " + dueDate : "Available");
    }
}
