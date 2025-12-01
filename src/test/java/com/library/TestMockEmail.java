package com.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class TestMockEmail {

    @Test
    public void testSendEmailRecordsMessages() {
        MockEmailService mock = new MockEmailService();

        // نحاكي إرسال رسالتين
        mock.sendEmail("user1@example.com", "You have 2 overdue books.");
        mock.sendEmail("user2@example.com", "You have 1 overdue book.");

        List<String> messages = mock.getSentMessages();

        // نتأكد إنو تم تسجيلهم
        assertEquals(2, messages.size(), "يجب أن تكون هناك رسالتان مسجلتان");

        assertTrue(messages.get(0).contains("user1@example.com"));
        assertTrue(messages.get(1).contains("user2@example.com"));
    }
}
