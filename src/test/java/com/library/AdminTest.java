package com.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private Admin admin;

    @BeforeEach
    void setUp() {
        // إنشاء admin جديد قبل كل اختبار
        admin = new Admin("testAdmin", "admin@test.com", "password123");
    }

    @Test
    void testLoginSuccess() {
        // تسجيل الدخول باستخدام بيانات صحيحة
        boolean result = admin.login("admin@test.com", "password123");
        assertTrue(result, "Admin should be able to login with correct credentials");
        assertTrue(admin.isLoggedIn(), "Admin loggedIn flag should be true after successful login");
    }

    @Test
    void testLoginFailWrongPassword() {
        // تجربة تسجيل الدخول بكلمة مرور خاطئة
        boolean result = admin.login("admin@test.com", "wrongPassword");
        assertFalse(result, "Admin should not login with wrong password");
        assertFalse(admin.isLoggedIn(), "loggedIn flag should remain false on failed login");
    }

    @Test
    void testLoginFailWrongEmail() {
        // تجربة تسجيل الدخول بإيميل خاطئ
        boolean result = admin.login("wrong@test.com", "password123");
        assertFalse(result, "Admin should not login with wrong email");
        assertFalse(admin.isLoggedIn(), "loggedIn flag should remain false on failed login");
    }

    @Test
    void testLogout() {
        admin.login("admin@test.com", "password123");
        admin.logout();
        assertFalse(admin.isLoggedIn(), "Admin should be logged out after logout()");
    }

    @Test
    void testToFileStringAndFromFileString() {
        String fileString = admin.toFileString();
        Admin parsedAdmin = Admin.fromFileString(fileString);

        assertNotNull(parsedAdmin, "Parsed admin should not be null");
        assertEquals(admin.getUsername(), parsedAdmin.getUsername(), "Username should match");
        assertEquals(admin.getEmail(), parsedAdmin.getEmail(), "Email should match");
        assertEquals(admin.isLoggedIn(), parsedAdmin.isLoggedIn(), "LoggedIn flag should match");
        // كلمة السر مش مطابقة نصياً بسبب الهاش، لكن يمكن اختبار تسجيل الدخول
        assertTrue(parsedAdmin.login("admin@test.com", "password123"), "Parsed admin should login successfully with original password");
    }

    @Test
    void testFromFileStringInvalid() {
        Admin invalidAdmin = Admin.fromFileString("invalid,line");
        assertNull(invalidAdmin, "Parsing an invalid line should return null");
    }
}
