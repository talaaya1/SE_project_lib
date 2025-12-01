package com.library;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

public class TestFineCalculator {

    @Test
    public void testBookFine() {
        // إنشاء استراتيجية الكتاب
        FineStrategy bookStrategy = new BookFineStrategy();
        FineCalculator calc = new FineCalculator(bookStrategy);

        // حساب الغرامة لكتاب متأخر 5 أيام
        int fine = calc.calculateFine(5);
        assertEquals(10, fine, "Book fine should be 10 NIS for overdue book");

        // حساب الغرامة لكتاب بدون تأخير
        fine = calc.calculateFine(0);
        assertEquals(0, fine, "Book fine should be 0 NIS if not overdue");
    }

    @Test
    public void testCdFine() {
        // إنشاء استراتيجية الـ CD
        FineStrategy cdStrategy = new CdFineStrategy();
        FineCalculator calc = new FineCalculator(cdStrategy);

        // حساب الغرامة لCD متأخر 3 أيام
        int fine = calc.calculateFine(3);
        assertEquals(60, fine, "CD fine should be 20 NIS per overdue day");

        // حساب الغرامة لCD بدون تأخير
        fine = calc.calculateFine(0);
        assertEquals(0, fine, "CD fine should be 0 NIS if not overdue");
    }

}
