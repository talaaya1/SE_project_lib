package com.library;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setup() {
        // استخدم قيم وهمية لتجاوز فحص "your-email"
        emailService = new EmailService("test@example.com", "password123");
    }

    @Test
    void testSendEmailSuccess() throws Exception {
        // محاكاة Transport.send
        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class))).thenAnswer(invocation -> null);

            boolean result = emailService.sendEmail("receiver@test.com", "Test Subject", "Hello Text");
            assertTrue(result);

            // تحقق من إنشاء ملف اللوج
            File logFile = new File("email_log.txt");
            assertTrue(logFile.exists());
            String content = new String(Files.readAllBytes(logFile.toPath()));
            assertTrue(content.contains("receiver@test.com"));
            assertTrue(content.contains("Test Subject"));
        }
    }

    @Test
    void testSendHtmlEmailSuccess() throws Exception {
        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class))).thenAnswer(invocation -> null);

            boolean result = emailService.sendHtmlEmail("receiver@test.com", "HTML Subject", "<b>Hello HTML</b>");
            assertTrue(result);

            String content = new String(Files.readAllBytes(new File("email_log.txt").toPath()));
            assertTrue(content.contains("HTML Subject"));
        }
    }

    @Test
    void testSendEmailFailure() throws Exception {
        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenThrow(new MessagingException("Simulated failure"));

            boolean result = emailService.sendEmail("fail@test.com", "Fail Subject", "Body");
            assertFalse(result);

            String content = new String(Files.readAllBytes(new File("email_log.txt").toPath()));
            assertTrue(content.contains("Fail Subject"));
            assertTrue(content.contains("FAILED"));
        }
    }

    @Test
    void testSessionNullDueToBadConfig() {
        EmailService badService = new EmailService("your-email@gmail.com", "your-password");
        assertFalse(badService.sendEmail("a@test.com", "Subject", "Body"));
        assertFalse(badService.sendHtmlEmail("a@test.com", "Subject", "<b>Body</b>"));
    }
}
