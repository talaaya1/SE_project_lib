package com.library;

/**
 * Strategy interface for fine calculation.
 * Follows Strategy Design Pattern.
 *
 * @author Library Team
 * @version 2.0
 */
public interface FineStrategy {
    /**
     * Calculates fine for overdue days.
     *
     * @param overdueDays Number of overdue days
     * @return Fine amount in NIS
     */
    double calculateFine(int overdueDays);
}