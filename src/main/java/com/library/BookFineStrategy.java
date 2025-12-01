package com.library;

public class BookFineStrategy implements FineStrategy{

    @Override
    public int calculateFine(int overdueDays) {
        if ((overdueDays>0)){
            return 10;
        }
        else {
            return 0;
        }
    }
}
