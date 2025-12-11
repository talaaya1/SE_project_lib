package com.library;
// done
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CDFineStrategyTest {

    @Test
    void testCalculateFine() {
        CDFineStrategy strategy = new CDFineStrategy();

        // 0 يوم تأخير → الغرامة = 0
        assertEquals(0.0, strategy.calculateFine(0), 0.001);

        // أيام سلبية → الغرامة = 0
        assertEquals(0.0, strategy.calculateFine(-5), 0.001);

        // يوم واحد → الغرامة = 20
        assertEquals(20.0, strategy.calculateFine(1), 0.001);

        // 5 أيام → الغرامة = 100
        assertEquals(100.0, strategy.calculateFine(5), 0.001);
    }
}
