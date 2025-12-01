package com.library;

import java.time.LocalDate;

public class FineCalculator {
    private FineStrategy fineStrategy;

    public FineCalculator(FineStrategy fineStrategy) {
        this.fineStrategy = fineStrategy;
    }

    public int calculateFine(int overdueDays) {
        return fineStrategy.calculateFine(overdueDays);
    }

}
