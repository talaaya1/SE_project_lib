package com.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLogout {

    @Test
    public void testAdminActionsAllCases() {
        SessionManager sessionManager = new SessionManager();
        Admin admin = new Admin("admin1", "pass123");
        sessionManager.regAdmin(admin);

        // --------- الحالة 1: admin مسجل دخول ---------
        System.out.println("=== Scenario 1: Admin logged in ===");
        sessionManager.login("admin1", "pass123");
        assertTrue(sessionManager.isLoggedIn(), "Admin should be logged in");

        sessionManager.performAdminAction("Check Library"); // ينجح
        sessionManager.addBook("Java Programming");          // ينجح

        sessionManager.logout();
        assertFalse(sessionManager.isLoggedIn(), "Admin should be logged out");

        assertThrows(IllegalStateException.class, () -> sessionManager.performAdminAction("Check Library"));
        assertThrows(IllegalStateException.class, () -> sessionManager.addBook("Python Programming"));

        // --------- الحالة 2: admin مش مسجل دخول ---------
        System.out.println("\n=== Scenario 2: Admin not logged in ===");

        sessionManager.logout(); // يطبع: "No admin is currently logged in"
        assertFalse(sessionManager.isLoggedIn(), "Admin should still be logged out");

        assertThrows(IllegalStateException.class, () -> sessionManager.performAdminAction("Check Library"));
        assertThrows(IllegalStateException.class, () -> sessionManager.addBook("Java Programming"));
    }
}
