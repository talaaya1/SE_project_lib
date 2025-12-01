package com.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAdmin {
    @Test
    void testAdminGetters() {
        Admin admin = new Admin("admin1", "pass123");
        assertEquals("admin1", admin.getUsername());
        assertEquals("pass123", admin.getPassword());
    }
}
