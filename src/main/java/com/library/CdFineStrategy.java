package com.library;

public class CdFineStrategy implements FineStrategy {

    @Override
    public int calculateFine(int overdueDays) {
        if (overdueDays <= 0) {
            return 0;
        }
        return overdueDays * 20; // 20 NIS per overdue day
    }
}
