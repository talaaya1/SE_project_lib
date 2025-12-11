package com.library;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class DebugAdminTest {

    private static final String ADMIN_FILE = "admins.txt";

    @Test
    void testPasswordHasherNormalFlow() {
        String password = "123456";
        String hash = DebugAdmin.PasswordHasher.hashPassword(password);

        assertNotNull(hash);
        assertTrue(DebugAdmin.PasswordHasher.verifyPassword(password, hash));
        assertFalse(DebugAdmin.PasswordHasher.verifyPassword("wrong", hash));
    }

   /* @Test
    void testPasswordHasherThrowsException() throws Exception {
        // استخدام Mockito لمحاكاة فشل MessageDigest.getInstance
        try (MockedStatic<MessageDigest> mdMock = Mockito.mockStatic(MessageDigest.class)) {
            mdMock.when(() -> MessageDigest.getInstance("MD5"))
                    .thenThrow(new NoSuchAlgorithmException("Simulated"));

            RuntimeException ex = assertThrows(RuntimeException.class, () ->
                    DebugAdmin.PasswordHasher.hashPassword("test"));

            assertTrue(ex.getMessage().contains("MD5 Algorithm not available"));
        }
    }*/

    @Test
    void testSaveAdminToFileNormalFlow() throws Exception {
        // حذف الملف إذا كان موجود
        File file = new File(ADMIN_FILE);
        if (file.exists()) file.delete();

        String name = "Admin";
        String email = "admin@test.com";
        String password = "pass123";
        String hash = DebugAdmin.PasswordHasher.hashPassword(password);

        DebugAdmin.saveAdminToFile(name, email, hash);

        assertTrue(file.exists());

        String content = new String(Files.readAllBytes(Paths.get(ADMIN_FILE)));
        assertTrue(content.contains(name));
        assertTrue(content.contains(email));
        assertTrue(content.contains(hash));
    }

    @Test
    void testSaveAdminToFileIOException() throws Exception {
        File file = new File(ADMIN_FILE);
        file.createNewFile();
        file.setReadOnly(); // اجبار IOException

        String hash = DebugAdmin.PasswordHasher.hashPassword("pass123");

        // يجب ألا يطرح استثناء خارجياً لأن الكود يسجل الخطأ فقط
        assertDoesNotThrow(() ->
                DebugAdmin.saveAdminToFile("Admin", "email@test.com", hash));

        file.setWritable(true); // إعادة الكتابة
    }

    @Test
    void testFullFlowIntegration() throws Exception {
        String name = "Tester";
        String email = "tester@example.com";
        String password = "secret";
        String hash = DebugAdmin.PasswordHasher.hashPassword(password);

        DebugAdmin.saveAdminToFile(name, email, hash);

        String content = new String(Files.readAllBytes(Paths.get(ADMIN_FILE)));
        assertTrue(content.contains(name));
        assertTrue(content.contains(email));
        assertTrue(content.contains(hash));

        assertTrue(DebugAdmin.PasswordHasher.verifyPassword(password, hash));
        assertFalse(DebugAdmin.PasswordHasher.verifyPassword("wrong", hash));
    }
}
