package com.library;
//done
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserBasicFunctions() {
        // إنشاء مستخدم جديد
        User user = new User("John Doe", "john@test.com", "password123");

        // التحقق من القيم الابتدائية
        assertEquals("John Doe", user.getName());
        assertEquals("john@test.com", user.getEmail());
        assertTrue(user.verifyPassword("password123"));
        assertFalse(user.verifyPassword("wrongpassword"));
        assertEquals(0.0, user.getTotalFines());
        assertTrue(user.canBorrow());

        // تغيير كلمة المرور
        assertTrue(user.changePassword("password123", "newpass"));
        assertTrue(user.verifyPassword("newpass"));
        assertFalse(user.verifyPassword("password123"));
        assertFalse(user.changePassword("wrong", "abc"));

        // إضافة ودفع الغرامات
        user.addFine(50);
        assertEquals(50, user.getTotalFines());
        assertFalse(user.canBorrow()); // لا يمكن الإعارة مع غرامات
        assertTrue(user.payFine(20));
        assertEquals(30, user.getTotalFines());
        assertFalse(user.payFine(100)); // محاولة دفع أكثر من الغرامة الحالية
        user.payFine(30);
        assertEquals(0, user.getTotalFines());
        assertTrue(user.canBorrow());

        // تسجيل الإعارة والإرجاع
        TestMedia media = new TestMedia("Book1", 1);
        user.borrowMediaRecord(media);
        assertEquals(1, user.getBorrowedMedia().size());
        user.returnMediaRecord(media);
        assertEquals(0, user.getBorrowedMedia().size());

        // اختبار toFileString و fromFileString
        String line = user.toFileString();
        User loaded = User.fromFileString(line);
        assertNotNull(loaded);
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getEmail(), loaded.getEmail());
        assertEquals(user.getTotalFines(), loaded.getTotalFines());
    }

    // فئة Media بسيطة للاختبار
    static class TestMedia extends Media {
        public TestMedia(String title, int copies) { super(title, copies); }

        @Override
        public String getType() { return "TestMedia"; }

        @Override
        public int getBorrowDays() { return 7; }

        @Override
        public boolean borrow(String userEmail) {
            if (availableCopies > 0) {
                decreaseAvailableCopies();
                this.borrowerEmail = userEmail;
                this.borrowDate = LocalDate.now();
                this.dueDate = borrowDate.plusDays(getBorrowDays());
                return true;
            }
            return false;
        }

        @Override
        public void returned() { increaseAvailableCopies(); }
    }
}
