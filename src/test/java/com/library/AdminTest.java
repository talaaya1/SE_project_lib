
package com.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminTest {

    @Test
    void testLoginSuccess() {
        Admin admin = new Admin("admin", "1234");
        boolean result = admin.login("admin", "1234");
        assertTrue(result, "Login should succeed with valid credentials");
        assertTrue(admin.isLoggedIn(), "Admin should be logged in after valid login");
    }

    @Test
    void testLoginFail_BothWrong() {
        Admin admin = new Admin("admin", "1234");
        boolean result = admin.login("wrongUser", "wrongPass");
        assertFalse(result, "Login should fail with both username and password wrong");
        assertFalse(admin.isLoggedIn(), "Admin should not be logged in after invalid login");
    }

    @Test
    void testLoginFail_WrongPasswordOnly() {
        Admin admin = new Admin("admin", "1234");
        boolean result = admin.login("admin", "wrong");
        assertFalse(result, "Login should fail if only the password is wrong");
        assertFalse(admin.isLoggedIn(), "Admin should not be logged in if password is incorrect");
    }

    @Test
    void testLoginFail_WrongUsernameOnly() {
        Admin admin = new Admin("admin", "1234");
        boolean result = admin.login("wrong", "1234");
        assertFalse(result, "Login should fail if only the username is wrong");
        assertFalse(admin.isLoggedIn(), "Admin should not be logged in if username is incorrect");
    }

    @Test
    void testLogout() {
        Admin admin = new Admin("admin", "1234");
        admin.login("admin", "1234");
        admin.logout();
        assertFalse(admin.isLoggedIn(), "Admin should be logged out after calling logout()");
    }
}
