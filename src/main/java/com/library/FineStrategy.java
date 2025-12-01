package com.library;

public interface FineStrategy {

    /**
     * Calculates the fine amount for an overdue item.
     * @param overdueDays number of days overdue
     * @return fine amount in NIS
     */

    int calculateFine(int overdueDays);

}
