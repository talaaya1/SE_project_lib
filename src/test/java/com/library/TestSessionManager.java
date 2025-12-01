package com.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestSessionManager {


    @Test
    void testAdminLoginLogoutAndActions() {
        SessionManager sessionManager = new SessionManager();
        Admin admin1 = new Admin("admin1", "pass123");
        sessionManager.regAdmin(admin1);

        // -------- Login ناجح --------
        assertTrue(sessionManager.login("admin1", "pass123"));
        assertTrue(sessionManager.isLoggedIn());
        assertEquals(admin1, sessionManager.getLoggedInAdmin());

        // -------- Actions أثناء تسجيل الدخول --------
        assertDoesNotThrow(() -> sessionManager.performAdminAction("Check Library"));
        assertDoesNotThrow(() -> sessionManager.addBook("Java Programming"));

        // -------- Logout --------
        sessionManager.logout();
        assertFalse(sessionManager.isLoggedIn());
        assertNull(sessionManager.getLoggedInAdmin());

        // -------- Actions بعد Logout --------
        assertThrows(IllegalStateException.class, () -> sessionManager.performAdminAction("Check Library"));
        assertThrows(IllegalStateException.class, () -> sessionManager.addBook("Python Programming"));

        // -------- Login خاطئ --------
        assertFalse(sessionManager.login("wrongUser", "wrongPass"));
        assertFalse(sessionManager.isLoggedIn());
    }

    @Test
    void testLogoutWithNoAdminLoggedIn() {
        SessionManager sessionManager = new SessionManager();

        // لا يوجد admin مسجل دخول
        assertFalse(sessionManager.isLoggedIn());

        // هنا يتم تنفيذ الـ else داخل logout()
        sessionManager.logout(); // Coverage على السطر "No admin is currently logged in"
    }

}
