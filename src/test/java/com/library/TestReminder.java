package com.library;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TestReminder {

    @Test
    public void testSendReminders() {
        Overdue overdue = new Overdue();
        MockEmailService mockEmail = new MockEmailService();
        ReminderService reminderService = new ReminderService(overdue, mockEmail);

        ArrayList<Book> books= new ArrayList<>();
        User user = new User("Tala", "tala@example.com",books);

        Book book1 = new Book("Java", "Author1", "111");
        user.borrowBook(book1);
        book1.setDueDate(LocalDate.now().minusDays(5)); // متأخر

        Book book2 = new Book("Python", "Author2", "222");
        user.borrowBook(book2);
        book2.setDueDate(LocalDate.now().plusDays(2)); // مش متأخر

        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        System.out.println("Borrowed books: " + user.getBorrowBooks().size());
        for (Book b : user.getBorrowBooks()) {
            System.out.println(b.getTitle() + " overdue? " + overdue.isOverdue(b));
        }

        reminderService.sendReminders(users);

        assertEquals(1, mockEmail.getSentMessages().size(), "يجب إرسال رسالة واحدة فقط");
        assertTrue(mockEmail.getSentMessages().get(0).contains("1 overdue book(s)."),
                "الرسالة يجب أن تحتوي عدد الكتب المتأخرة");

    }
}
