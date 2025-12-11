package com.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.library.PasswordHasher;

class PasswordHasherTest {

    @Test
    void testHashAndVerifyCorrectPassword() {
        String password = "123456";

        // هاش كلمة المرور (SHA-256 مع Salt)
        String hashed = PasswordHasher.hashPassword(password);

        // تحقق من كلمة المرور الصحيحة
        assertTrue(PasswordHasher.verifyPassword(password, hashed), "Password should verify correctly");
    }

    @Test
    void testVerifyWrongPassword() {
        String password = "123456";
        String hashed = PasswordHasher.hashPassword(password);

        // تحقق من كلمة المرور الغلط
        assertFalse(PasswordHasher.verifyPassword("wrongPassword", hashed), "Wrong password should not verify");
    }

    @Test
    void testSimpleHashConsistency() {
        String password = "mypassword";
        String simpleHashed1 = PasswordHasher.simpleHash(password); // بدون salt
        String simpleHashed2 = PasswordHasher.simpleHash(password);

        assertEquals(simpleHashed1, simpleHashed2, "simpleHash should be consistent");
    }

    @Test
    void testSimpleHashDirectVerification() {
        String password = "mypassword";
        String simpleHashed = PasswordHasher.simpleHash(password);

        assertEquals(simpleHashed, PasswordHasher.simpleHash(password), "Direct verification of simpleHash should succeed");
        assertNotEquals(simpleHashed, PasswordHasher.simpleHash("wrongPassword"), "Wrong password should not match simpleHash");
    }
}
