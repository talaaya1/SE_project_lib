package com.library;

/**
 * Fine strategy for books (10 NIS per day).
 * Implements Strategy Pattern for fine calculation.
 *
 * @author Library Team
 * @version 2.0
 */
public class BookFineStrategy implements FineStrategy {
    private static final double FINE_PER_DAY = 10.0;

    @Override
    public double calculateFine(int overdueDays) {
        if (overdueDays <= 0) return 0.0;
        return overdueDays * FINE_PER_DAY;
    }
}